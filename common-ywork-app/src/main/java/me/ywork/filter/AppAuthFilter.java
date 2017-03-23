package me.ywork.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.kafka.http.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.heyi.framework.session.ltpatoken.LtpaToken;

/**
 * 
 * @author sulta
 *
 */
public class AppAuthFilter extends AbstractAppFilter {
	private static final Logger logger = LoggerFactory
			.getLogger(AppAuthFilter.class);
	private static final String CORPCOOKIENAME = "LWEQYTOKEN";


	
	private SessionManager sessionManager;
	
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void delegateFilter(ServletRequest servletRequest,
			ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {

		/**
		 * 客户端cookie
		 */
		String DINGCOOKIENAME  = CORPCOOKIENAME;
		
		MockHttpServletRequest request = (MockHttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		logger.debug("Filter Requested URL：" + request.getRequestURL());
		String corpid = request.getParameter("corpid");
		
		Cookie[] cookies = request.getCookies();
		String token = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if ((DINGCOOKIENAME).equalsIgnoreCase(cookies[i].getName())) {
					token = cookies[i].getValue();
				}
			}
		}
		String sessionId = null;
		if(!isNull(token)){
			try {
				LtpaToken ltpatoken = new LtpaToken(token);
				if(ltpatoken.isValid()){
					JSONObject mutiCorpUserInfo = JSONObject.parseObject(ltpatoken.getUser());
					
					String cid = null;
					
					if (corpid == null) { //url上没有带corpid的.从token中取默认corpid
						cid = mutiCorpUserInfo.getString("c");
					}else{
						if(mutiCorpUserInfo.containsKey(corpid)){
							cid = corpid;
						}
					}
					
					if(!StringUtils.isEmpty(cid)){
						Boolean isAdmin = false;
						String uid = null;
						String un = null; //username
						String av = null; //avatar
						
						
						JSONObject userInfo = mutiCorpUserInfo.getJSONObject(cid);
						if (userInfo != null) { //新版
							
							uid = userInfo.getString("u");
							
							if (userInfo.containsKey("un")) {
								un = userInfo.getString("un");
							}
							
							if (userInfo.containsKey("av")) {
								av = userInfo.getString("av");
							}
							
							if (userInfo.containsKey("a")) {
								isAdmin = userInfo.getBoolean("a");
							}
							
						}else{
							isAdmin = mutiCorpUserInfo.getBoolean("a");
							uid = mutiCorpUserInfo.getString("u");
							un = mutiCorpUserInfo.getString("un");
						}
						
						if(!StringUtils.isEmpty(uid)){
							sessionId = cid.concat(uid);
							request.setParameter("_curCorpId", cid);
							request.setParameter("_curUserId", uid);
							if(!StringUtils.isEmpty(un)){
								request.setParameter("_curUserName", un);
							}
							if(!StringUtils.isEmpty(av)){
								request.setParameter("_avatar", av);
							}
							request.setAttribute("_curUserIsAdmin", isAdmin);
						}
					}else{
						logger.error("token 无效: {},cookie:{}", token,JSONObject.toJSONString(mutiCorpUserInfo));
					}
				}else{
					logger.error("token 无效: {}", token);
				}
			} catch (Exception e) {
				logger.error("解析token时出错",e);
			}
		}
		
		if(isNull(sessionId)){
			if(token!=null){
				removeCookie(DINGCOOKIENAME, response);
			}
			logger.error("Filter sessionId为空。");
			setJsonResponse(response, "{\"auth\":\"0\"}");
			return;
		}

		if(sessionManager!=null){
			HttpSession session = sessionManager.getSession(sessionId);
			if(session!=null){
				request.setSession(session);
			}
		}
		
		chain.doFilter(request, response);
	}
	
	private void setJsonResponse(HttpServletResponse response,String words) throws IOException {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			writer.append(words);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void removeCookie(String cookieName, HttpServletResponse response) {
		Cookie c = new Cookie(cookieName, "123");
		c.setPath("/");
		c.setMaxAge(-1);
		response.addCookie(c);
	}
	
	@Override
	public void destroy() {

	}

	static boolean isNull(String str) {
		return StringUtils.isEmpty(str) || "null".equals(str.trim());
	}
}
