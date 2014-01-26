package com.google.gwt.sample.stockwatcher.server;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class ServeServlet extends HttpServlet {
    private BlobstoreService blob= BlobstoreServiceFactory.getBlobstoreService();

public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException {
		System.out.println("Serve reached.");
        BlobKey blobKey = new BlobKey(req.getParameter("blobkey"));
        String imagekey = req.getParameter("imagekey");

        blob.serve(blobKey, res);
    }
}

