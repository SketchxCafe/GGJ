package com.google.gwt.sample.stockwatcher.client;

import java.util.LinkedList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.sample.stockwatcher.shared.MyStringUtil;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

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
					FlowPanel myFP = new FlowPanel();

					com.google.gwt.user.client.ui.Image myImage = new com.google.gwt.user.client.ui.Image(current.servingUrl);
					//myImage.setSize("100%", "100%");
					//RootPanel.get().add(myImage);
					//myFP.add(myImage);

					if (current.getWord()!=null)
					{
						UIDraw a = new UIDraw(current.getWord());
						a.fp.add(myImage);
						//Label myLabel = new Label(current.getWord());
						//RootPanel.get().add(myLabel);
						//a.fp.add(myLabel);
						a.addStuff();
						//a.fp.setWidth("500px");
						RootPanel.get().add(a.fp);
					}
					//System.out.println("Loaded an image of " + current.getWord() +".");
				}
			}
		});
	}

	private class UIDraw{
		String [] options;
		FlowPanel fp;
		int correctNum = (int) (Math.random()*5);

		UIDraw(String correct){
			fp = new FlowPanel();
			options = new String[5];
			options[0] = correct;
			for (int i = 1; i < options.length; i++)
			{
				if (i!= correctNum)
					options[i] = MyStringUtil.getRandomString();
				else
					options[i] = correct;
			}
		}

		public void addStuff(){
			//			TextArea myTA = new TextArea();
			//			Button blah = new Button("Check!");
			//			blah.addClickHandler(new ClickHandler() {
			//				
			//				@Override
			//				public void onClick(ClickEvent event) {
			//					// TODO Auto-generated method stub
			//					
			//				}
			//			});

			for (int i = 0; i < options.length; i++)
			{
				if (i == correctNum)
				{
					Button a = new Button(options[i]);
					fp.add(a);
					a.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							((Button) event.getSource()).setText("Right!");
							//							final DialogBox dialogBox = new DialogBox();
							//							dialogBox.setAnimationEnabled(true);
							//							dialogBox.setText("Blah");
							//							dialogBox.setTitle("Right!!");
							//							dialogBox.show();
						}
					});
				}
				else
				{
					Button a = new Button(options[i]);
					fp.add(a);
					a.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							((Button) event.getSource()).setText("Wrong!");
							//							final DialogBox dialogBox = new DialogBox();
							//							dialogBox.setText("Blah");
							//							dialogBox.setTitle("Wrong!!");
							//							dialogBox.center();
							//							//closeButton.setFocus(true);
							//							dialogBox.show();
						}
					});
				}
			}
		}
	}
}
