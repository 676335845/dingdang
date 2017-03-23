package com.heyi.utils;

import java.util.UUID;

public class IdGenerator {

	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	public static String newId() {
		long time = System.currentTimeMillis();
		String id = Long.toHexString(time) + uuid();
		return id.replaceAll("-", "").substring(0, 32);
	}
}
