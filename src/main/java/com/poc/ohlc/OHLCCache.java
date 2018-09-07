package com.poc.ohlc;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.JsonArray;

import tmp.KrakenPublicRequest;

public enum OHLCCache {

	INSTANCE;
	
	/* ehcache */
	private ConcurrentHashMap<OHLC, JsonArray> cache;
	
	private OHLCCache() {
		cache = new ConcurrentHashMap<>();
	}
	
	public void insertOrUpdate(OHLC key, JsonArray krakenResponse) {
		if(key != null && krakenResponse != null) {
			// {"error":["EAPI:Rate limit exceeded"]}
			if(krakenResponse.size() == 0) {
				System.out.println("[OHLCCache.insertOrUpdate] error " + krakenResponse.toString());
				return;
			}
			cache.put(key, krakenResponse);
		}
	}
	
	public JsonArray get(OHLC key) {
		JsonArray elt = cache.get(key);
		if(elt == null) {
			JsonArray tmp = new KrakenPublicRequest().queryPublic("fetchOHLCV", key.getPair(), key.getGrain());
			cache.put(key, tmp);
			OHLCCacheManager.INSTANCE.start();
			return tmp;
		}
		return elt;
	}
	
	@SuppressWarnings("unchecked")
	public Set<OHLC> getAll() {
		return cache.keySet();
	}
}