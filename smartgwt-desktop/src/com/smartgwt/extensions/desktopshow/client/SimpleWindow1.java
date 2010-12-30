package com.smartgwt.extensions.desktopshow.client;

import com.smartgwt.extensions.desktop.client.DesktopWindow;

public class SimpleWindow1 extends DesktopWindow {
	public SimpleWindow1() {
		super();
		setHeaderIcon( "[SKIN]/headerIcons/arrow_down.png");
		setTitle( "Simple Window 1");
		setWidth( 400);
		setHeight( 600);
	}
}
