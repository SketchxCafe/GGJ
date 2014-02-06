package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class PlayerInfo implements Serializable {

	private static final long serialVersionUID = 4924047758898465078L;
	public String nickName;
	public String logoutURL;
}
