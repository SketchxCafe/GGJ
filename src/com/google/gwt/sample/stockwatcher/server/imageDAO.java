package com.google.gwt.sample.stockwatcher.server;
import java.util.LinkedList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.sample.stockwatcher.client.ImageBlob;

public class imageDAO {
	DatastoreService datastore;

	public imageDAO() {
		System.out.println("Image DAO created.");
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	public ImageBlob get(String encodedKey) {
		System.out.println("Encoded key is " + encodedKey);
		Key key = KeyFactory.stringToKey(encodedKey);
		try {
			Entity result = datastore.get(key);
			ImageBlob image = fromEntity(result);
			image.setKey(encodedKey);
			return image;
		} catch (EntityNotFoundException e) {
			System.out.println("Entry not found, dude!");
			return null;
		}

	}
	
	private ImageBlob fromEntity(Entity result) {
		System.out.println("fromEntity does its thing.");
		ImageBlob image = new ImageBlob();
		
		image.setServingUrl((String) result.getProperty(ImageBlob.SERVING_URL));
		image.setWord((String) result.getProperty(ImageBlob.WORD));
		System.out.println("fromEntity says that the surl is " + (String) result.getProperty(ImageBlob.SERVING_URL));
		//image.setOwnerId((String) result.getProperty(ImageBlob.OWNER_ID));

		if (image.getKey() == null) {
			String encodedKey = KeyFactory.keyToString(result.getKey());
			image.setKey(encodedKey);
		}

		return image;
	}
	public void delete(String encodedKey) {
		Key key = KeyFactory.stringToKey(encodedKey);
		datastore.delete(key);
	}


	public LinkedList<ImageBlob> getRecent() {
		Query query = new Query("ImageBlob");
		//query.addSort(UploadedImage.CREATED_AT, SortDirection.DESCENDING);
		//FetchOptions options = FetchOptions.Builder.withLimit(25);

		LinkedList<ImageBlob> results = new LinkedList<ImageBlob>();
		//for (Entity result : datastore.prepare(query).asIterable(options)) {
		for (Entity result : datastore.prepare(query).asIterable()) {
			ImageBlob image = fromEntity(result);
			results.add(image);
		}
		return results;
	}
	

}
