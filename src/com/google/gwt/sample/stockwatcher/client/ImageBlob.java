package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;


@SuppressWarnings("serial")
@PersistenceCapable
public class ImageBlob implements Serializable{
	public static final String SERVING_URL = "servingUrl";
	public static final String OWNER_ID = "ownerId";
	public static final String WORD = "word";
	String word;
	String key;
	String servingUrl;
	String ownerId;
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getServingUrl() {
		return servingUrl;
	}

	public void setServingUrl(String servingUrl) {
		this.servingUrl = servingUrl;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}


}
