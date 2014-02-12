package com.google.gwt.sample.stockwatcher.client;

import java.util.LinkedList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.sample.stockwatcher.shared.MyStringUtil;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UIDrawings extends ScrollPanel{

	//private static UIDrawings instance = null;
	private static VerticalPanel instance = null;
	
	public UIDrawings(){
		super();
		instance = new VerticalPanel();
		this.add(instance);
		instance.add(new Label("Hallo! I take some time to load.."));


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
					//FlowPanel myFP = new FlowPanel();

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
						a.blobKey = current.getKey();
						a.addStuff();
						//a.fp.setWidth("500px");
						if (UIDrawings.instance!=null)
							UIDrawings.instance.add(a.fp);
					}
					//System.out.println("Loaded an image of " + current.getWord() +".");
				}
			}
		});
	}

	private class UIDraw{
		String [] options;
		VerticalPanel fp;
		int correctNum = (int) (Math.random()*5);
		String blobKey;

		UIDraw(String correct){
			fp = new VerticalPanel();
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
			
			/// TextBox with options
			
			final TextBox myTA = new TextBox();
			final Button blah = new Button("Check!");
			blah.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					com.google.gwt.user.client.Window.alert("requesting " + blobKey);
					StockWatcher.greetingService.storeWord(myTA.getText(), blobKey, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							com.google.gwt.user.client.Window.alert("Ops, send had failed." + caught.getStackTrace());
						}

						@Override
						public void onSuccess(String result) {
							//((Button) (event.getSource())).setText(result);
							com.google.gwt.user.client.Window.alert(result);
						}
					});
					
//					if (MyStringUtil.checkForRightString(options[correctNum], myTA.getText()))
//						blah.setText("Check: Right!");
//					else
//						blah.setText("Check: False");
				}
			});
			
			HorizontalPanel buttonsPanel = new HorizontalPanel();
			buttonsPanel.add(myTA);
			buttonsPanel.add(blah);
			fp.add(buttonsPanel);

			///Buttons with options
			
//			for (int i = 0; i < options.length; i++)
//			{
//				if (i == correctNum)
//				{
//					Button a = new Button(options[i]);
//					fp.add(a);
//					a.addClickHandler(new ClickHandler() {
//
//						@Override
//						public void onClick(ClickEvent event) {
//							((Button) event.getSource()).setText("Right!");
//							//							final DialogBox dialogBox = new DialogBox();
//							//							dialogBox.setAnimationEnabled(true);
//							//							dialogBox.setText("Blah");
//							//							dialogBox.setTitle("Right!!");
//							//							dialogBox.show();
//						}
//					});
//				}
//				else
//				{
//					Button a = new Button(options[i]);
//					fp.add(a);
//					a.addClickHandler(new ClickHandler() {
//
//						@Override
//						public void onClick(ClickEvent event) {
//							((Button) event.getSource()).setText("Wrong!");
//							//							final DialogBox dialogBox = new DialogBox();
//							//							dialogBox.setText("Blah");
//							//							dialogBox.setTitle("Wrong!!");
//							//							dialogBox.center();
//							//							//closeButton.setFocus(true);
//							//							dialogBox.show();
//						}
//					});
//				}
//			}
		}
	}
}
