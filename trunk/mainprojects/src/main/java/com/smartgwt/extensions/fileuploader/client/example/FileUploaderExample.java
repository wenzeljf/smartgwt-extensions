/*
 * SmartGWT Extensions Copyright (c) 2009, Hang (Anthony) Yuan.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.smartgwt.extensions.fileuploader.client.example;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.util.KeyCallback;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.extensions.fileuploader.client.ExtensionFilter;
import com.smartgwt.extensions.fileuploader.client.FileUploader;
import com.smartgwt.extensions.fileuploader.client.FileUploaderUtils;
import com.smartgwt.extensions.fileuploader.client.event.BeforeFileAddEvent;
import com.smartgwt.extensions.fileuploader.client.event.BeforeFileAddHandler;
import com.smartgwt.extensions.fileuploader.client.event.UploadCompletedEvent;
import com.smartgwt.extensions.fileuploader.client.event.UploadCompletedHandler;

public class FileUploaderExample implements EntryPoint {

	public void onModuleLoad() {
		// add shortcut key to display SC's debug console.
		if (!GWT.isScript()) {
			KeyIdentifier debugKey = new KeyIdentifier();
			debugKey.setCtrlKey(true);
			debugKey.setKeyName("1");

			Page.registerKey(debugKey, new KeyCallback() {
				public void execute(String keyName) {
					SC.showConsole();
				}
			});
		}

		final Canvas canvasMain = new Canvas();

		IButton button = new IButton("FileUploader");
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final FileUploader dialog = FileUploader.popup("File Uploader",
						"en");
				dialog.setUrl(GWT.getModuleBaseURL()
						+ "uploadFile?parentType=folder&id=1");
				dialog.setPostVarName("file");
				dialog.setWidth(602);
				dialog.setHeight(300);
				dialog.addUploadCompletedHandler(new UploadCompletedHandler() {
					public void onCompleted(UploadCompletedEvent event) {
						SC.say(FileUploaderUtils.getDictionary("en").get(
								FileUploader.MSG_UPLOADING_FINISHED));
					}
				});
				dialog.addBeforeFileAddHandler(new BeforeFileAddHandler() {
					public void onAdd(BeforeFileAddEvent event) {
						// SC.say("Adding file: " + event.getFilename());
						// event.cancel();
					}
				});
				dialog.addCloseClickHandler(new CloseClickHandler() {
					public void onCloseClick(CloseClientEvent event) {
						// SC.say("Dialog is closing");
						dialog.hide();
					}
				});
				dialog.addFileNameFilter(new ExtensionFilter(".bmp", ".gif",
						".jpeg", ".jpg", ".pdf", ".png"));
			}
		});

		canvasMain.addChild(button);
		canvasMain.draw();
	}

}
