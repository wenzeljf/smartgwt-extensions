package com.smartgwt.extensions.taskbar.client.example;

import com.smartgwt.extensions.taskbar.client.TaskBarWindow;
import com.smartgwt.client.widgets.Label;

public class InfoWin extends TaskBarWindow {
	Label lblInfowin = null;
	
	public InfoWin(String title ) {
		super(title);
		resizeTo(165, 150);
		
		lblInfowin = new Label("");
		addItem(lblInfowin);

		this.hide();
	}


	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
		if (lblInfowin!=null)
			lblInfowin.setTitle(title);		
	}
	
	
}
