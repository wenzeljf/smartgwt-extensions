package com.smartgwt.extensions.taskbar.client.example;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.extensions.taskbar.client.TaskBarWindow;

public class DataWin extends TaskBarWindow {

	Label lblDatawin ;
	
	public DataWin(String title ) {
		super(title);
		resizeTo(320, 200);
		
		lblDatawin = new Label("");
		addItem(lblDatawin);

		this.hide();
	}


	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
		if (lblDatawin!=null)
			lblDatawin.setTitle("<b style='color:red'>"+title+"</b>");		
	}
}
