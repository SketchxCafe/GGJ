package com.google.gwt.sample.stockwatcher.client;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class UIDrawings extends LayoutPanel{

	public UIDrawings(){
		this.add(new Label("Hallo"));
		
		
		StockWatcher.imageService.getRecentlyUploaded(new AsyncCallback<LinkedList<ImageBlob>>(){
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Loading old pictures failed.");
				com.google.gwt.user.client.Window.alert("Loading old pictures failed.");
			}

			@Override
			public void onSuccess(LinkedList<ImageBlob> result) {
				System.out.println("Yay! S. from RU.");
				for (ImageBlob current: result)
				{
					com.google.gwt.user.client.ui.Image myImage = new com.google.gwt.user.client.ui.Image(current.servingUrl);
					//myImage.setSize("100%", "100%");
					RootPanel.get().add(myImage);
				}
			}
		});
	}
}
