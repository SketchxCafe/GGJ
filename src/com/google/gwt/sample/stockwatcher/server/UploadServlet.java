package com.google.gwt.sample.stockwatcher.server;


import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.gwt.sample.stockwatcher.client.ImageBlob;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet{
	//private static final Logger log = Logger.getLogger(UploadServlet.class.getName());


	private BlobstoreService blob = BlobstoreServiceFactory.getBlobstoreService();
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		System.out.println("UploadServlet reached.");
		@SuppressWarnings("deprecation")
		Map<String, BlobKey> blobs = blob.getUploadedBlobs(req);
		BlobKey blobKey = blobs.get("fileUpload");
		
		
		if (blobKey == null) {
			System.out.println("Ups, something bad had happened.");
		}else{
			System.out.println("The keystring is "+ blobKey.getKeyString());
			ImagesService imageService = ImagesServiceFactory.getImagesService();
			@SuppressWarnings("deprecation")
			String iUrl = imageService.getServingUrl(blobKey);
			System.out.println("The serving URL is "+ iUrl);
			Entity imageBlob = new Entity("ImageBlob");
			imageBlob.setProperty("blobKey", blobKey);
			imageBlob.setProperty(ImageBlob.SERVING_URL, iUrl);
			imageBlob.setProperty(ImageBlob.WORD, GreetingServiceImpl.currWord);
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			datastore.put(imageBlob);

			String keyString = KeyFactory.keyToString(imageBlob.getKey());
			System.out.println("The keyString is "+ iUrl);
			res.sendRedirect("/upload?uploadedImageKey=" + keyString);  
			System.out.println("Exiting from the Upload servlet.");
		}
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String uploadedImageKey = req.getParameter("uploadedImageKey");
		resp.setHeader("Content-Type", "text/html");
		resp.getWriter().println(uploadedImageKey);
		System.out.println("Upload image key is " + uploadedImageKey);

	}
}
