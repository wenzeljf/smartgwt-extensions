package com.smartgwt.extensions.taskbar.client.example;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.extensions.taskbar.client.TaskBarWindow;

public class TrialWin extends TaskBarWindow {

	Label lblTrialwin = null;
		
	public TrialWin(String title ) {
		super(title);
		resizeTo(240, 120);
		
		lblTrialwin = new Label("");
		addItem(lblTrialwin);

		this.hide();
	}


	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
		if (lblTrialwin!=null)
			lblTrialwin.setTitle("<i>"+title+"</i>");		
	}
}
