package com.heyi.framework.mongo.springdata.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtil {
	public static Pageable convertMongoPage(int pageNo, int pageSize) {
		if (pageNo > 0)
			pageNo--;
		return new PageRequest(pageNo, pageSize);
	}

	public static Pageable convertMongoPage(int pageNo, int pageSize, Sort sort) {
		if (pageNo > 0)
			pageNo--;
		return new PageRequest(pageNo, pageSize, sort);
	}
}
