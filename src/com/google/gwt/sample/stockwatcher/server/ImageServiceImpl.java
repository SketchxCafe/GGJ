package com.google.gwt.sample.stockwatcher.server;
import java.util.LinkedList;

import com.google.gwt.sample.stockwatcher.client.ImageBlob;
import com.google.gwt.sample.stockwatcher.client.ImageService;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ImageServiceImpl extends RemoteServiceServlet implements ImageService{
	BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	
	@Override
	public String getBlobstoreUploadUrl() {
		System.out.println("Getting the upload url.");
		return blobstoreService.createUploadUrl("/upload");
		//return blobstoreService.createUploadUrl("/images/");
	}

	@Override
	public ImageBlob get(String key){
		System.out.println("Somewhere in Impl...");
		imageDAO idao = new imageDAO();
		System.out.println("The key is " + key + ", and DAO is " + idao + ".");
		ImageBlob image = idao.get(key);
		return image;
	}
	@Override
	public void deleteImage(String key) {
		// TODO Auto-generated method stub
		System.out.println("Somewhere where I am not supposed to be - 2.");
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		imageDAO idao = new imageDAO();
		ImageBlob image = idao.get(key);
		if(image.getOwnerId().equals(user.getUserId())) {
			idao.delete(key);
		}
	}
	
	@Override
	public LinkedList<ImageBlob> getRecentlyUploaded() {
		imageDAO dao = new imageDAO();
		LinkedList<ImageBlob> images = dao.getRecent(); 
		return images;
	}
}
