package com.sample.ratelimiter.aop;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class RedisKeyForRateLimit implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String apiName;
	
	private Map<String,Object> clientIdentifiers;

	public RedisKeyForRateLimit(String apiName) {
		this.setApiName(apiName);
		this.clientIdentifiers = new TreeMap<>();
	}
	
	public void addClientIdentifier(String identifierName, Object o) {
		clientIdentifiers.put(identifierName,o);
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public Map<String,Object> getClientIdentifiers() {
		return clientIdentifiers;
	}

	public void setClientIdentifiers(Map<String,Object> clientIdentifiers) {
		this.clientIdentifiers = clientIdentifiers;
	}
	
	
	
}
