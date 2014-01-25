package com.google.gwt.sample.stockwatcher.client;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.sample.stockwatcher.client.ImageService;
import com.google.gwt.sample.stockwatcher.client.ImageServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.sample.stockwatcher.shared.FieldVerifier;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockWatcher implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	FormPanel form = new FormPanel();

	private final ImageServiceAsync imageService = GWT.create(ImageService.class);
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// Drawing Canvas
		DrawingArea canvas = new DrawingArea(400, 400);
		RootPanel.get().add(canvas);
		
		/*
		final Circle circle = new Circle(0, 0, 10);
		canvas.add(circle);
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				circle.setX(event.getX());
				circle.setY(event.getY());
			}
		});
		*/
		
		
		VerticalPanel UploadPanel = new VerticalPanel();
		UploadPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		//UI_MenuPanel.add(UploadPanel);
		UploadPanel.setWidth("315px");

		Label lblUploadACute = new Label("Upload a cute Tree Picture!");
		lblUploadACute.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		UploadPanel.add(lblUploadACute);

		final FileUpload fileUpload = new FileUpload();
		fileUpload.setName("fileUpload");
		UploadPanel.add(fileUpload);
		form = new FormPanel();
		form.setWidget(UploadPanel);
		//form.setEncoding(FormPanel.ENCODING_MULTIPART);
		//form.setMethod(FormPanel.METHOD_POST);
		startNewBlobstoreSession();
		fileUpload.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				System.out.println(fileUpload.getFilename());
				com.google.gwt.user.client.Window.alert(("Yay! You chose a file! Magic-magic-magic!!!!\n"+
						fileUpload.getFilename()));
				//startNewBlobstoreSession();
				form.submit();
				
				
			}});
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event){
				form.reset();
				startNewBlobstoreSession();
				System.out.println("The event results are " + event.getResults());
				com.google.gwt.user.client.Window.alert("The event results are " + event.getResults());
				String key = event.getResults();
				//key = "ahFjcHNjMzEwdGVhbXRyZWVtaXIPCxIJSW1hZ2VCbG9iGGMM";	//Hardcoded test
				imageService.get(key, new AsyncCallback<ImageBlob>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("Naaaaayy!!! =(");
						com.google.gwt.user.client.Window.alert("Naaaaaayyy!! =(");
					}

					@Override
					public void onSuccess(ImageBlob result) {
						System.out.println(result==null);
						System.out.println("new " + result.servingUrl + ", yay!!!");
						com.google.gwt.user.client.Window.alert("new " + result.servingUrl + ", yay!!!");
						//pictureListTab.addImageFirst(result.servingUrl);
						//UIPictureSingle uip = new UIPictureSingle(result.servingUrl);
						form.reset();
						//uip.imageblob = result;
						//uip.image.setUrl(result.getServingUrl());
					}
					
				});
			}
		});
		
		//form.add(UploadPanel);
		RootPanel.get().add(form);
		
		
		
		
		
		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
	
	
	
	@UiHandler("uploadButton")
	void onSubmit(ClickEvent e) {
		form.submit();
	}

	private void startNewBlobstoreSession() {
		imageService.getBlobstoreUploadUrl(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				form.setAction(result);
				form.setEncoding(FormPanel.ENCODING_MULTIPART);
				form.setMethod(FormPanel.METHOD_POST);
				System.out.println(result + " was successful.");
			//	uploadButton.setText("Upload");
			//	uploadButton.setEnabled(true);

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("Starting Blob session failed =(");
			}
		});
	}
}
