package com.github.ydydwang.http.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.ydydwang.http.bean.Session;

public class SessionCacheManager {
	private static final Object lock = new Object();
	private static final Map<String, Session> map = new HashMap<String, Session>();
	private static final Session defaultSession = new Session();
	private static List<Session> cacheList = new ArrayList<Session>();

	public static Session get(String address) {
		return map.get(address);
	}

	public static Session getOrDefault(String address) {
		return map.getOrDefault(address, defaultSession);
	}

	public static void put(String address, Session session) {
		map.put(address, session);
	}

	public static void remove(String address) {
		if (map.containsKey(address)) {
			map.remove(address);
		}
	}

	public static void addToListThenRemoveFromMap(String address, Session session) {
		synchronized(lock) {
			SessionCacheManager.cacheList.add(session);
		}
		map.remove(address);
	}

	public static List<Session> getAllThenRemoveAll() {
		List<Session> returnList = null;
		List<Session> newList = new ArrayList<Session>();
		synchronized(lock) {
			returnList = SessionCacheManager.cacheList;
			SessionCacheManager.cacheList = newList; 
		}
		return returnList;
	}
}
