package me.ywork.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 使用排除法url-pattern的Filter
 * @author sulta
 *
 */
public abstract class AbstractAppFilter implements Filter{
	
	private static final Log logger = LogFactory.getLog(AbstractAppFilter.class);
	
	private static final String EXTENSION_MAPPING_PATTERN = "*.";

	private static final String PATH_MAPPING_PATTERN = "/*";

	private List<String> excludeUrlPatterns = Collections.emptyList();
	
	private final List<String> exactMatches = new ArrayList<String>();

	private final List<String> startsWithMatches = new ArrayList<String>();

	private final List<String> endsWithMatches = new ArrayList<String>();
	

	public List<String> getExcludeUrlPatterns() {
		return Collections.unmodifiableList(this.excludeUrlPatterns);
	}

	public void setExcludeUrlPatterns(List<String> excludeUrlPatterns) {
		this.excludeUrlPatterns = new ArrayList<String>(excludeUrlPatterns);
		
		this.endsWithMatches.clear();
		this.startsWithMatches.clear();
		this.exactMatches.clear();
		
		for (String urlPattern : excludeUrlPatterns) {
			if(urlPattern.startsWith(EXTENSION_MAPPING_PATTERN)) {
				this.endsWithMatches.add(urlPattern.substring(1, urlPattern.length()));
			} else if(urlPattern.equals(PATH_MAPPING_PATTERN)) {
				this.startsWithMatches.add("");
			}
			else if (urlPattern.endsWith(PATH_MAPPING_PATTERN)) {
				this.startsWithMatches.add(urlPattern.substring(0, urlPattern.length() - 1));
				this.exactMatches.add(urlPattern.substring(0, urlPattern.length() - 2));
			} else {
				if("".equals(urlPattern)) {
					urlPattern = "/";
				}
				this.exactMatches.add(urlPattern);
			}
		}
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestPath = httpRequest.getPathInfo();

		if(matches(requestPath)) { //排除
			filterChain.doFilter(request, response);
		} else {
			delegateFilter(httpRequest, response, filterChain);
		}
	}
	
	
	private boolean matches(String requestPath) {
		for(String pattern : this.exactMatches) {
			if(pattern.equals(requestPath)) {
				return true;
			}
		}
		if(!requestPath.startsWith("/")) {
			return false;
		}
		for(String pattern : this.endsWithMatches) {
			if(requestPath.endsWith(pattern)) {
				return true;
			}
		}
		for(String pattern : this.startsWithMatches) {
			if(requestPath.startsWith(pattern)) {
				return true;
			}
		}
		return false;
	}
	
	public abstract void delegateFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException;
}
