package me.ywork.page;

import java.beans.PropertyEditorSupport;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;


/**
 * @author fivest
 */
public class PageableArgumentResolver implements WebArgumentResolver {
	private static Logger logger = LoggerFactory.getLogger(PageableArgumentResolver.class);

	private static final Pageable DEFAULT_PAGE_REQUEST = new PageRequest(0, 10, 0);
	private static final String DEFAULT_PREFIX = "page";
	private static final String DEFAULT_SEPARATOR = ".";

	private Pageable fallbackPagable = DEFAULT_PAGE_REQUEST;
	private String prefix = DEFAULT_PREFIX;
	private String separator = DEFAULT_SEPARATOR;

	public void setFallbackPagable(Pageable fallbackPagable) {
		this.fallbackPagable = null == fallbackPagable ? DEFAULT_PAGE_REQUEST
				: fallbackPagable;
	}

	public void setPrefix(String prefix) {
		this.prefix = null == prefix ? DEFAULT_PREFIX : prefix;
	}

	public void setSeparator(String separator) {
		this.separator = null == separator ? DEFAULT_SEPARATOR : separator;
	}

	public Object resolveArgument(MethodParameter methodParameter,
			NativeWebRequest webRequest) {

		if (methodParameter.getParameterType().equals(Pageable.class)) {

			assertPageableUniqueness(methodParameter);

			Pageable request = getDefaultFromAnnotationOrFallback(methodParameter);
			ServletRequest servletRequest = (ServletRequest) webRequest
					.getNativeRequest();
			PropertyValues propertyValues = new ServletRequestParameterPropertyValues(
					servletRequest, getPrefix(methodParameter), separator);

			DataBinder binder = new ServletRequestDataBinder(request);

			binder.initDirectFieldAccess();
			/*???binder.registerCustomEditor(Sort.class, new SortPropertyEditor(
					"sort.dir", propertyValues));*/
			binder.bind(propertyValues);

			Integer page = getConvertedParameterNumber(webRequest, "pageNo");// request.getPageNumber()
			if (page == null || page == 0) {
				page = 1;
			}
			Integer totalCount = getConvertedParameterNumber(webRequest, "totalCount");
			if (totalCount == null) {
				totalCount = 0;
			}
			
			Integer pageSize = getConvertedParameterNumber(webRequest, "pageSize");
			if (pageSize == null) {
				pageSize = request.getPageSize();
			}
			
			if (page > 0) {
				request = new PageRequest(page, pageSize, totalCount.longValue());
			}

			return request;
		}

		return UNRESOLVED;
	}

	private Pageable getDefaultFromAnnotationOrFallback(
			MethodParameter methodParameter) {

		// search for PageableDefaults annotation
		for (Annotation annotation : methodParameter.getParameterAnnotations()) {
			if (annotation instanceof PageableDefaults) {
				return getDefaultPageRequestFrom((PageableDefaults) annotation);
			}
		}

		// Construct request with fallback request to ensure sensible
		// default values. Create fresh copy as Spring will manipulate the
		// instance under the covers
		return new PageRequest(fallbackPagable.getPageNo(), fallbackPagable
				.getPageSize()/*???, fallbackPagable.getSort()*/, 0);
	}

	private static Pageable getDefaultPageRequestFrom(PageableDefaults defaults) {

		// +1 is because we substract 1 later
		int defaultPageNumber = defaults.pageNo(); //??? + 1;
		int defaultPageSize = defaults.value();

		/*???if (defaults.sort().length == 0) {
			return new PageRequest(defaultPageNumber, defaultPageSize);
		}*/

		return new PageRequest(defaultPageNumber, defaultPageSize/*???, defaults
				.sortDir(), defaults.sort()*/,defaults.totalCount());
	}

	private String getPrefix(MethodParameter parameter) {

		for (Annotation annotation : parameter.getParameterAnnotations()) {
			if (annotation instanceof Qualifier) {
				return new StringBuilder(((Qualifier) annotation).value())
						.append("_").append(prefix).toString();
			}
		}

		return prefix;
	}

	private void assertPageableUniqueness(MethodParameter parameter) {

		Method method = parameter.getMethod();

		if (containsMoreThanOnePageableParameter(method)) {
			Annotation[][] annotations = method.getParameterAnnotations();
			assertQualifiersFor(method.getParameterTypes(), annotations);
		}
	}

	private boolean containsMoreThanOnePageableParameter(Method method) {

		boolean pageableFound = false;

		for (Class<?> type : method.getParameterTypes()) {

			if (pageableFound && type.equals(Pageable.class)) {
				return true;
			}

			if (type.equals(Pageable.class)) {
				pageableFound = true;
			}
		}

		return false;
	}

	private void assertQualifiersFor(Class<?>[] parameterTypes,
			Annotation[][] annotations) {

		Set<String> values = new HashSet<String>();

		for (int i = 0; i < annotations.length; i++) {

			if (Pageable.class.equals(parameterTypes[i])) {

				Qualifier qualifier = findAnnotation(annotations[i]);

				if (null == qualifier) {
					throw new IllegalStateException(
							"Ambiguous Pageable arguments in handler method. If you use multiple parameters of type Pageable you need to qualify them with @Qualifier");
				}

				if (values.contains(qualifier.value())) {
					throw new IllegalStateException(
							"Values of the user Qualifiers must be unique!");
				}

				values.add(qualifier.value());
			}
		}
	}

	private Qualifier findAnnotation(Annotation[] annotations) {

		for (Annotation annotation : annotations) {
			if (annotation instanceof Qualifier) {
				return (Qualifier) annotation;
			}
		}

		return null;
	}

	private static class SortPropertyEditor extends PropertyEditorSupport {

		private final String orderProperty;
		private final PropertyValues values;

		public SortPropertyEditor(String orderProperty, PropertyValues values) {

			this.orderProperty = orderProperty;
			this.values = values;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
		 */
		@Override
		public void setAsText(String text) {

			PropertyValue rawOrder = values.getPropertyValue(orderProperty);
			/*???Sort.Direction order = null == rawOrder ? Sort.Direction.ASC
					: Sort.Direction.fromString(rawOrder.getValue().toString());

			setValue(new Sort(order, text));*/
		}
	}

	// ////////
	private Integer getConvertedParameterNumber(NativeWebRequest request,
			String name) {
		if (!StringUtils.hasText(name)) {
			throw new IllegalArgumentException("getConvertedParameterNumber - parameter name is null or empty.");
		}
		
		Integer number = null;
		String sNumber = request.getParameter(name);
		if (StringUtils.hasText(sNumber)) {
			try {
				number = Integer.parseInt(sNumber);
			} catch (NumberFormatException e) {
				throw new RuntimeException(e);
			}
		} else {
			logger.warn("cannot find the request parameter with name {}", name);
		}
		
		return number;
	}
}
