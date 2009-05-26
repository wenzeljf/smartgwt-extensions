package com.smartgwt.extensions.client.fileuploader;

import com.google.gwt.user.client.ui.FormPanel;

public class FileUploadingForm extends FormPanel {
	
	public FileUploadingForm(String url) {
		setWidth("1");
		setHeight("1");
		setVisible(false);

		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);
		setAction(url);
	}
	
}
