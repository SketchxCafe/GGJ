package com.google.gwt.sample.stockwatcher.client;


import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.sample.stockwatcher.shared.Player;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockWatcher implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	public static final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	FormPanel form = new FormPanel();
	final Canvas myCanvas = Canvas.createIfSupported();
	DrawListener myMouseHandler = new DrawListener();
	public static final ImageServiceAsync imageService = GWT.create(ImageService.class);
	String currentWord = "";


	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		/**
		 * High-level panel with tabs.
		 */
		final TabLayoutPanel myTLP = new TabLayoutPanel(40, Unit.PX);
		RootLayoutPanel.get().add(myTLP);

		/**
		 * Panel with general information.
		 */
		Panel infoPage = new VerticalPanel();
		HTMLPanel wellcomeText = new HTMLPanel("<H2>Wellcome to Abstractly!</H2>"
				+ "<br>"
				+ "Here you can draw abstract stuff."
				+ "<br>"
				+ "Draw so that only some people guess right and guess on others' drawings to get points!"
				+ "<br>"
				+ "If you guess right, you get 1 point. If none people guessed right (including you), you get a point."
				+ "<br>"
				+ "There are 5 guessers per picture; points are distrubued after the 5th guesser."
				+ "<br>"
				+ "The drawer gets points if 1,2,3 or 4 guessers out of 5 guessed correctly (2,3,2,1 points respectively)."
				+ "<br>"
				+ "<br>"
				+ "Added login - supports google login and might use your email as nickname! You can change the nickname at your tab."
				+ "<br>"
				+ "New features comming up!"
				+ "<br>"
				+ "Currently, only Crome is supported."
				+ "<br>"
				+ "One guess = many words, separated by whitespaces."
				+ "<br>"
				+ "For uploading picture that you have drawn, you first need to download it to your computer. (It is a feature!)");
		infoPage.add(wellcomeText);
		myTLP.add(infoPage, "Start");

		/**
		 * A panel that contains instruments for drawing and uploading images.
		 */
		Panel drawPage = new VerticalPanel();
		myTLP.add(drawPage, "Make a new drawing");


		////Building the Drawing interface

		VerticalPanel drawingPanel = new VerticalPanel();
		drawingPanel.getElement().setId("outline");

		//Text area
		HorizontalPanel text = new HorizontalPanel();
		text.getElement().setId("word");

		Label top = new Label("Show them: ");
		top.getElement().setInnerHTML("<H2>Show them:  </H2>");
		top.getElement().setId("top");
		Label word = new Label("");
		word.getElement().setInnerHTML("<H2>--Loading--</H2>");
		word.getElement().setId("guessWord");

		text.add(top);
		text.add(word);
		drawingPanel.add(text);

		//Draw area
		//VerticalPanel drawAr = new VerticalPanel();
		//drawAr.getElement().setId("drawingSpace");

		myCanvas.setCoordinateSpaceHeight(400);
		myCanvas.setCoordinateSpaceWidth(375);
		myCanvas.setPixelSize(375, 400);

		myCanvas.getElement().setId("coolCanvas");

		//drawAr.add(myCanvas);
		//drawingPanel.add(drawAr);
		drawingPanel.add(myCanvas);

		//Buttons
		HTMLPanel pal = new HTMLPanel(
				"<BODY>"

						+ "<br>"
						+ "<input type='image' src='FinnishButton.png' name='image' onclick='finish()'/>"
						+ "<input type='image' src='B_Pen1px.png' name='image' onclick='penSize(1)'/>"
						+ "<input type='image' src='B_ColourPink.png' name='image' onclick='colorChange(&quot;#FF8478&quot;)'/>"
						+ "<input type='image' src='B_ColourRed.png' name='image' onclick='colorChange(&quot;#FF3753&quot;)'/>"
						+ "<input type='image' src='B_ColourYellow.png' name='image' onclick='colorChange(&quot;#FF9B3B&quot;)'/>"
						+ "<input type='image' src='B_ColourGreen.png' name='image' onclick='colorChange(&quot;#395735&quot;)'/>"
						+ "<input type='image' src='B_ColourBlue.png' name='image' onclick='colorChange(&quot;#014880&quot;)'/>"
						+ "<input type='image' src='B_ColourPurple.png' name='image' onclick='colorChange(&quot;#49306A&quot;)'/>"
						+ "<input type='image' src='B_Eraser.png' name='image' onclick='colorChange(&quot;#FFE58C&quot;)'/>"
						+ "<br>"
						+ "<input type='image' src='IQuitButton.png' name='image' onclick='quit()'/>"
						+ "<input type='image' src='B_Pen3px.png' name='image' onclick='penSize(3)'/>"
						+ "<input type='image' src='B_ColourGrey.png' name='image' onclick='colorChange(&quot;#846E7B&quot;)'/>"
						+ "<input type='image' src='B_ColourBrown.png' name='image' onclick='colorChange(&quot;#8E5D59&quot;)'/>"
						+ "<input type='image' src='B_ColourOrange.png' name='image' onclick='colorChange(&quot;#E35736&quot;)'/>"
						+ "<input type='image' src='B_ColourLime.png' name='image' onclick='colorChange(&quot;#8A8F4D&quot;)'/>"
						+ "<input type='image' src='B_ColourSea.png' name='image' onclick='colorChange(&quot;#157083&quot;)'/>"
						+ "<input type='image' src='B_ColourBlack.png' name='image' onclick='colorChange(&quot;#31252F&quot;)'/>"
						+ "<input type='image' src='B_Trash.png' name='image' onclick='trash()'/>"
						+ "</BODY>");
		pal.getElement().setId("palette");
		drawingPanel.add(pal);


		drawPage.add(drawingPanel);

		Context2d context1 = myCanvas.getContext2d();
		context1.arc(0, 100, 30, 0, 80);
		context1.fill();
		context1.closePath();

		//RootLayoutPanel.get().add(new UIDrawings());

		myCanvas.addMouseMoveHandler(myMouseHandler);
		myCanvas.addMouseDownHandler(myMouseHandler);
		myCanvas.addMouseUpHandler(myMouseHandler);
		myCanvas.addMouseOverHandler(myMouseHandler);
		myCanvas.addMouseOutHandler(myMouseHandler);


		/**
		 * The panel that holds the buttons with actions, related to downloading and uploading.
		 */
		HorizontalPanel myButtonPanel = new HorizontalPanel();
		drawPage.add(myButtonPanel);

		//Adds button for saving images.

		Button mySaveButton = new Button("Save the image.");
		mySaveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String data = myCanvas.toDataUrl("image/png");
				data = data.replaceFirst("image/png", "image/octet-stream");
				Window.open(data, "file", null);
			}
		});
		myButtonPanel.add(mySaveButton);


		//Buttons for uploading

		VerticalPanel UploadPanel = new VerticalPanel();
		UploadPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		UploadPanel.setWidth("315px");

		Label lblUploadACute = new Label("Upload a Picture!");
		lblUploadACute.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		UploadPanel.add(lblUploadACute);

		final TextBox hiddenWord = new TextBox();
		hiddenWord.setVisible(false);
		hiddenWord.setName("wordSend");
		UploadPanel.add(hiddenWord);
		
		final FileUpload fileUpload = new FileUpload();
		fileUpload.setName("fileUpload");
		fileUpload.setEnabled(false);
		UploadPanel.add(fileUpload);
		form = new FormPanel();
		form.setWidget(UploadPanel);
		startNewBlobstoreSession();
		fileUpload.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				System.out.println(fileUpload.getFilename());
				hiddenWord.setText(currentWord);
				com.google.gwt.user.client.Window.alert(("Yay! You chose a file! Magic-magic-magic!!!!\n"+
						fileUpload.getFilename()));

				System.out.println("2Data url: " + myCanvas.toDataUrl("image/png"));
				form.submit();
				//event.cancel
			}});
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event){
				form.reset();
				startNewBlobstoreSession();
				System.out.println("The event results are " + event.getResults());
				com.google.gwt.user.client.Window.alert("The event results are " + event.getResults());
				String key = event.getResults();
				imageService.get(key, new AsyncCallback<ImageBlob>(){

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("Naaaaayy!!! =(");
						com.google.gwt.user.client.Window.alert("Naaaaaayyy!! =(");
					}

					@Override
					public void onSuccess(ImageBlob result) {
						System.out.println(result==null);
						System.out.println("new " + result.servingUrl + ", yay!!!");
						com.google.gwt.user.client.Window.alert("new " + result.servingUrl + ", yay!!!");
						form.reset();
					}

				});
			}
		});

		//form.add(UploadPanel);
		myButtonPanel.add(form);


		//Adds button for randomization.
		Button myRandomButton = new Button("Get another word!");
		myRandomButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				requestRandom();
			}
		});

		myButtonPanel.add(myRandomButton);



		/**
		 * A panel with a gallery of past images.
		 */
		UIDrawings myGallery = new UIDrawings();
		myTLP.add(myGallery,"Galery & Guess");


		///Other initialization stuff
		requestRandom();

		
		//Player/Login page
		greetingService.getPlayer(Window.Location.getHref(), new AsyncCallback<Player>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Player result) {
				fileUpload.setEnabled(true);
				
				if (result!=null){
					VerticalPanel userPage = new VerticalPanel();
					Label username = new Label ("Hello, " + result.getUsername() + "!");
					userPage.add(username);
					final String logoutURL = result.logoutURL;
					Button logoutButton = new Button("Logout");
					userPage.add(logoutButton);
					logoutButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							Window.Location.replace(logoutURL);
						}
					});
					
					//Panel for changing username
					Panel changeNamePanel = new HorizontalPanel();
					changeNamePanel.add(new Label("Change Username: "));
					final TextBox myTB = new TextBox();
					changeNamePanel.add(myTB);
					Button changeNameButton = new Button("Submit!");
					
					changeNameButton.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							greetingService.setUserNickname(myTB.getText(), new AsyncCallback<Void>() {
								public void onSuccess(Void nothing)
								{
									com.google.gwt.user.client.Window.alert("Now you are " +myTB.getText() +"!");
								}
								
								public void onFailure(Throwable caught)
								{
									com.google.gwt.user.client.Window.alert("Somthing bad happened during the process.");
								}
							});
							
						}
					});
					changeNamePanel.add(changeNameButton);
					
					userPage.add(new Label("Your score: " + result.getScore()));
					
					userPage.add(changeNamePanel);
					
					myTLP.add(userPage, result.getUsername());	
				}
				else
				{
					VerticalPanel testPanel = new VerticalPanel();
					Button testButton = new Button("Login");
					testPanel.add(testButton);
					myTLP.add(testPanel, "Login");

					testButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							//Window.Location.replace(URL.encode("/login"));
							//Window.Location.replace(UserServiceFactory.getUserService().createLoginURL(URL.encode("")));
							greetingService.getUserLoginUrl(Window.Location.getHref(), new AsyncCallback<String>() {

								@Override
								public void onSuccess(String result) {
									Window.Location.replace(result);

								}

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub	
								}
							});
						}
					});
				}
			}
		});


		//Rankings Page
		
		greetingService.getPlayerRankings(new AsyncCallback<List<Player>>() {
			
			@Override
			public void onSuccess(List<Player> result) {
				VerticalPanel basicList = new VerticalPanel();
				for (Player current : result)
					basicList.add(new Label(current.getUsername() +"   -   " + current.getScore()));
				myTLP.add(basicList, "Player Rankings");
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
		
//		VerticalPanel testPanel = new VerticalPanel();
//		Button testButton = new Button("Login");
//		testPanel.add(testButton);
//		myTLP.add(testPanel, "Test");
//
//		testButton.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				//Window.Location.replace(URL.encode("/login"));
//				//Window.Location.replace(UserServiceFactory.getUserService().createLoginURL(URL.encode("")));
//				greetingService.getUserLoginUrl(Window.Location.getHref(), new AsyncCallback<String>() {
//
//					@Override
//					public void onSuccess(String result) {
//						Window.Location.replace(result);
//
//					}
//
//					@Override
//					public void onFailure(Throwable caught) {
//						// TODO Auto-generated method stub	
//					}
//				});
//			}
//		});


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
			}

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Starting Blob session failed =(");
			}
		});
	}

	private void requestRandom() {
		greetingService.getRandom(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("The word is..... " + result);
				com.google.gwt.dom.client.Element guessText = Document.get().getElementById("guessWord");
				guessText.setInnerHTML("<H2>" + result + "</H2>");
				currentWord = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Rand Failed");
			}
		});
	}

	private class DrawListener implements MouseDownHandler, MouseOverHandler, MouseOutHandler, MouseUpHandler, MouseMoveHandler
	{
		int x = 0, y = 0;
		boolean mouseDown = false;
		Context2d context1 = myCanvas.getContext2d();

		final int REFRESH_MAX = 4;
		int count = 0;

		@Override
		public void onMouseDown(MouseDownEvent event) {
			mouseDown = true;
			context1.beginPath();
			x = event.getX();
			y = event.getY();
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			context1.beginPath();
			x = event.getX();
			y = event.getY();
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			context1.stroke();
			context1.closePath();
			count = 0;
		}

		@Override
		public void onMouseUp(MouseUpEvent event) {
			mouseDown = false;
			context1.stroke();
			context1.closePath();
			count = 0;
		}

		@Override
		public void onMouseMove(MouseMoveEvent event) {
			if (mouseDown)
			{
				context1.lineTo(x, y);
				context1.lineTo(event.getX(), event.getY());
				context1.stroke();
				context1.closePath();
				context1.beginPath();
				x = event.getX();
				y = event.getY();

				count++;
				if (count >= REFRESH_MAX)
				{
					context1.stroke();
					context1.closePath();
					context1.beginPath();
					x = event.getX();
					y = event.getY();
					count = 0;
				}
			}
		}
	}
}
