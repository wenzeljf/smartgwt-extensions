package com.smartgwt.extensions.containers.client.example;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.extensions.containers.client.CardLayoutCanvas;

public class ContainersExample implements EntryPoint {

	public void onModuleLoad() {

		final CardLayoutCanvas cards = new CardLayoutCanvas() {
			{
				setWidth100();
				setHeight100();
				addExampleCard("card1", "#ffcccc",
						"<h2>Card 1</h2>This is the first card added to the layout.");
				addExampleCard("card2", "#ccffcc",
						"<h2>Card 2</h2>This is the second card added to the layout.");
				addExampleCard("card3", "#ccccff",
						"<h2>Card 3</h2>This is the third card added to the layout.");
			}

			private void addExampleCard(String key, final String bg, final String content) {
				addCard(key, new Canvas() {
					{
						setBackgroundColor(bg);
						setContents(content);
					}
				});
			}
		};

		new Window() {
			{
				setWidth(400);
				setHeight(300);
				setAutoCenter(true);
				setTitle("CardLayout Example");

				addItem(new VLayout() {
					{
						setWidth100();
						setHeight100();

						addMember(cards);

						addMember(new HLayout() {
							{
								setMargin(5);
								setMembersMargin(5);
								addExampleButton("Show card 1", "card1");
								addExampleButton("Show card 2", "card2");
								addExampleButton("Show card 3", "card3");
								addExampleSpacer();
							}

							private void addExampleButton(final String title, final String key) {
								addExampleSpacer();
								addMember(new Button() {
									{
										setTitle(title);
										addClickHandler(new ClickHandler() {
											public void onClick(ClickEvent event) {
												cards.showCard(key);
											}
										});
									}
								});
							}
							
							private void addExampleSpacer() {
								addMember(new Canvas() {
									{
										setWidth100();
									}
								});
							}
						});
					}
				});
			}
		}.show();
	}

}
