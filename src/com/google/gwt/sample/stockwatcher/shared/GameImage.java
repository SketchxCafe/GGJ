package com.google.gwt.sample.stockwatcher.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable="true")
public class GameImage implements Serializable{
	
	private static final long serialVersionUID = -9168641132749193471L;

	public static final int MAX_GUESS = 5;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	Long time;
	
	@Persistent
	String imageBlobKey;
	
	@Persistent
	String word;
	
	@Persistent
	String blobServingUrl;
	
	//@PrimaryKey
	@Persistent
	String key;
	
	@Persistent
	String posterID;
	
	@Persistent
	//LinkedList<String> guessers;
	String guesser0;
	@Persistent
	String guesser1;
	@Persistent
	String guesser2;
	@Persistent
	String guesser3;
	@Persistent
	String guesser4;
	
	@Persistent
	boolean [] guessedRight = new boolean[MAX_GUESS];
	
	@Persistent
	int guessed = 0; 

	public GameImage(){
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getImageBlobKey() {
		return imageBlobKey;
	}

	public void setImageBlobKey(String imageBlobKey) {
		this.imageBlobKey = imageBlobKey;
	}

	public String getBlobServingUrl() {
		return blobServingUrl;
	}

	public void setBlobServingUrl(String blobServingUrl) {
		this.blobServingUrl = blobServingUrl;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPosterID() {
		return posterID;
	}

	public void setPosterID(String posterID) {
		this.posterID = posterID;
	}

	public String [] getGuessers() {
		String toReturn [] = new String[5]; 
		toReturn[0] = guesser0;
		toReturn[1] = guesser1;
		toReturn[2] = guesser2;
		toReturn[3] = guesser3;
		toReturn[4] = guesser4;
		
		return toReturn;
	}
	
	public boolean setNextGuesser(String next)
	{
		if (guesser0 == null)
			guesser0 = next;
		else if (guesser1 == null)
			guesser1 = next;
		else if (guesser2 == null)
			guesser2 = next;
		else if (guesser3 == null)
			guesser3 = next;
		else if (guesser4 == null)
			guesser4 = next;
		else
			return false;
		return true;
	}

//	public void setGuessers(LinkedList<String> guessers) {
//		this.guessers = guessers;
//	}

	public boolean[] getGuessedRight() {
		return guessedRight;
	}

	public void setGuessedRight(boolean[] guessedRight) {
		this.guessedRight = guessedRight;
	}

	public int getGuessed() {
		return guessed;
	}

	public void setGuessed(int guessed) {
		this.guessed = guessed;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
}
