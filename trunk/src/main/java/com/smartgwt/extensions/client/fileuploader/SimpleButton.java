package com.smartgwt.extensions.client.fileuploader;

import com.smartgwt.client.widgets.Button;

public class SimpleButton extends Button {
	
	public SimpleButton(String title, String icon) {
		super(title);

		setIcon(icon);
		setShowRollOver(false);
		setShowDownIcon(false);
		setShowDisabledIcon(false);
	}

}
