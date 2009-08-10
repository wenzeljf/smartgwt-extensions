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

package com.smartgwt.extensions.fileuploader.client;

import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.WidgetCanvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.extensions.fileuploader.client.event.BeforeFileAddEvent;
import com.smartgwt.extensions.fileuploader.client.event.BeforeFileAddHandler;
import com.smartgwt.extensions.fileuploader.client.event.FileAddEvent;
import com.smartgwt.extensions.fileuploader.client.event.FileAddHandler;
import com.smartgwt.extensions.fileuploader.client.event.FileQueueResetEvent;
import com.smartgwt.extensions.fileuploader.client.event.FileQueueResetHandler;
import com.smartgwt.extensions.fileuploader.client.event.FileRemoveEvent;
import com.smartgwt.extensions.fileuploader.client.event.FileRemoveHandler;
import com.smartgwt.extensions.fileuploader.client.event.FileSelectedEvent;
import com.smartgwt.extensions.fileuploader.client.event.FileSelectedHandler;
import com.smartgwt.extensions.fileuploader.client.event.FileUploadFailedEvent;
import com.smartgwt.extensions.fileuploader.client.event.FileUploadFailedHandler;
import com.smartgwt.extensions.fileuploader.client.event.FileUploadStartEvent;
import com.smartgwt.extensions.fileuploader.client.event.FileUploadStartHandler;
import com.smartgwt.extensions.fileuploader.client.event.FileUploadSuccessEvent;
import com.smartgwt.extensions.fileuploader.client.event.FileUploadSuccessHandler;
import com.smartgwt.extensions.fileuploader.client.event.UploadCompletedEvent;
import com.smartgwt.extensions.fileuploader.client.event.UploadCompletedHandler;
import com.smartgwt.extensions.fileuploader.client.event.UploadStartEvent;
import com.smartgwt.extensions.fileuploader.client.event.UploadStartHandler;
import com.smartgwt.extensions.fileuploader.client.event.UploadStopEvent;
import com.smartgwt.extensions.fileuploader.client.event.UploadStopHandler;

/**
 * A window that allow user to upload files to remote server.
 * 
 * @author anthony.yuan@gmail.com
 */
public class FileUploader extends Window implements FileUploaderConstants {

	private static int NEXT_ID = 1;

	private String url;
	private String postVarName = "file";
	private Dictionary messageDictionary;

	private ArrayList<FileNameFilter> fileFilters;

	private FileUploadInput lastNewFileInput;
	private Progressbar progressbar;
	private FileGrid fileGrid;
	private Button addFileDummyButton;
	private Button uploadFileButton;
	private Button stopFileButton;
	private Button resetFileButton;
	private FlowPanel newFileInputContainer;
	private FileUploadingForm formFileUpload;
	private int uploadFileNum = 0;
	private FileRecord uploadingFileRecord;

	/* - Utility functions -------------------------------------------------- */
	/**
	 * Create a FileUploader and show it.
	 * 
	 * @param title
	 *            title of the dialog
	 * @param uiLang
	 *            language code for UI and message. e.g. 'en', 'fr', etc.
	 * @param afterUploadedCallback
	 * @return generated FileUploader
	 */
	public static FileUploader popup(final String title, final String uiLang) {
		FileUploader dialogBox = new FileUploader(title, uiLang);
		dialogBox.setIsModal(true);
		dialogBox.setShowModalMask(true);
		dialogBox.setCanDragResize(true);

		dialogBox.setAutoCenter(true);
		dialogBox.show();

		return dialogBox;
	}

	/**
	 * Create a new Window.
	 * 
	 * @param title
	 *            the title
	 * @param uiLang
	 *            language code for UI and message
	 */
	public FileUploader(String title, String uiLang) {
		assert uiLang != null;
		setTitle(title);
		setAutoSize(true);
		setCanDragResize(true);

		this.messageDictionary = FileUploaderUtils.getDictionary(uiLang);

		create(MIN_WIDTH, MIN_HEIGHT);
	}

	/* - Getters and Setters ------------------------------------------------ */
	/**
	 * returns the URL where the files are posted
	 * 
	 * @return the String URL where files are posted
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * sets the URL to post the files
	 * 
	 * @param url
	 *            the String URL where files are posted
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * sets the name of the variable to use for the files uploaded to the
	 * server. By default it is "file"
	 * 
	 * @param varname
	 *            name of the variable to use for the files uploaded to the
	 *            server. By default it is "file"
	 */
	public void setPostVarName(String postVarName) {
		this.postVarName = postVarName;
	}

	/* - Listener Handling -------------------------------------------------- */
	public HandlerRegistration addBeforeFileAddHandler(
			BeforeFileAddHandler handler) {
		return doAddHandler(handler, BeforeFileAddEvent.getType());
	}

	public HandlerRegistration addFileAddHandler(FileAddHandler handler) {
		return doAddHandler(handler, FileAddEvent.getType());
	}

	public HandlerRegistration addFileQueueResetHandler(
			FileQueueResetHandler handler) {
		return doAddHandler(handler, FileQueueResetEvent.getType());
	}

	public HandlerRegistration addFileRemoveResetHandler(
			FileRemoveHandler handler) {
		return doAddHandler(handler, FileRemoveEvent.getType());
	}

	public HandlerRegistration addFileUploadFailedResetHandler(
			FileUploadFailedHandler handler) {
		return doAddHandler(handler, FileUploadFailedEvent.getType());
	}

	public HandlerRegistration addFileUploadStartResetHandler(
			FileUploadStartHandler handler) {
		return doAddHandler(handler, FileUploadStartEvent.getType());
	}

	public HandlerRegistration addFileUploadSuccessResetHandler(
			FileUploadSuccessHandler handler) {
		return doAddHandler(handler, FileUploadSuccessEvent.getType());
	}

	public HandlerRegistration addUploadCompletedHandler(
			UploadCompletedHandler handler) {
		return doAddHandler(handler, UploadCompletedEvent.getType());
	}

	public HandlerRegistration addUploadStartHandler(UploadStartHandler handler) {
		return doAddHandler(handler, UploadStartEvent.getType());
	}

	public HandlerRegistration addUploadStopHandler(UploadStopHandler handler) {
		return doAddHandler(handler, UploadStopEvent.getType());
	}

	/**
	 * Add FileNameFilter.
	 * 
	 * @param filter
	 *            the FileNameFilter implementation
	 */
	public void addFileNameFilter(FileNameFilter filter) {
		if (fileFilters == null) {
			fileFilters = new ArrayList<FileNameFilter>();
		}
		fileFilters.add(filter);
	}

	/**
	 * Removes a FileNameFilter.
	 * 
	 * @param filter
	 *            the FileNameFilter implementation to be removed
	 */
	public void removeFileNameFilter(FileNameFilter filter) {
		if (fileFilters != null && fileFilters.contains(filter)) {
			fileFilters.remove(filter);
		}
	}

	/* - Create controls and layout ----------------------------------------- */
	private void create(final int defaultWidth, final int defaultHeight) {
		setMinHeight(MIN_HEIGHT);
		setMinWidth(MIN_WIDTH);
		progressbar = new Progressbar() {
			{
				setVertical(false);
				setHeight(DEFAULT_HEIGHT_PROCESSING_BAR);
				setLength(defaultWidth);
				setDefaultWidth(defaultWidth);
			}
		};
		addItem(progressbar);

		addItem(createFileGrid(defaultWidth, defaultHeight));

		ToolStrip toolStrip = new ToolStrip() {
			{
				setDefaultWidth(MIN_WIDTH);
				setAutoWidth();
			}
		};

		newFileInputContainer = new FlowPanel();
		newFileInputContainer.addStyleName(CSS_NEW_FILE_INPUT_CONTAINER);
		toolStrip.addMember(newFileInputContainer);

		addFileDummyButton = new SimpleButton(messageDictionary.get(LABEL_ADD),
				ICON_ADD);
		newFileInputContainer.add(addFileDummyButton);

		final Button removeFileButton = new SimpleButton(messageDictionary
				.get(LABEL_REMOVE), ICON_REMOVE);
		removeFileButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeButtonOnClick();
			}
		});
		toolStrip.addMember(removeFileButton);

		resetFileButton = new SimpleButton(messageDictionary.get(LABEL_RESET),
				ICON_RESET);
		resetFileButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				resetButtonOnClick();
			}
		});

		toolStrip.addMember(resetFileButton);

		uploadFileButton = new SimpleButton(
				messageDictionary.get(LABEL_UPLOAD), ICON_START);
		uploadFileButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				uploadButtonOnClick();
			}
		});
		toolStrip.addMember(uploadFileButton);

		stopFileButton = new SimpleButton(messageDictionary.get(LABEL_STOP),
				ICON_STOP);
		stopFileButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				stopButtonOnClick();
			}
		});
		toolStrip.addMember(stopFileButton);

		addItem(toolStrip);

		addDrawHandler(new DrawHandler() {
			public void onDraw(DrawEvent event) {
				addNewFileInput();
				resizeLastNewFileInput();
			}
		});
	}

	private void resizeLastNewFileInput() {
		int x = addFileDummyButton.getLeft();
		int y = addFileDummyButton.getTop();
		// int z = addFileButton.getZIndex();

		Element lastFileInputElement = lastNewFileInput.getElement();

		DOM.setStyleAttribute(lastFileInputElement, "left", "" + (x - 50));
		DOM.setStyleAttribute(lastFileInputElement, "top", "" + (y - 2));
		// DOM.setStyleAttribute(lastFileInputElement, "z-index", "" + (z + 1));
		addFileDummyButton.setZIndex(0);
	}

	private ListGrid createFileGrid(int defaultWidth, int defaultHeight) {
		fileGrid = new FileGrid(messageDictionary);
		fileGrid.setDefaultWidth(defaultWidth);
		fileGrid.setDefaultHeight(defaultHeight - DEFAULT_HEIGHT_PROCESSING_BAR
				- DEFAULT_HEIGHT_TOOL_STRIP);

		FileDS fileDS = new FileDS(messageDictionary);
		fileDS.setClientOnly(true);

		fileGrid.setDataSource(fileDS);
		fileGrid.setAutoFetchData(true);

		return fileGrid;
	}

	private void addNewFileInput() {
		final FileUploadInput newFileInput = new FileUploadInput(postVarName,
				fileFilters, messageDictionary);
		newFileInput.addFileSelectedHandler(new FileSelectedHandler() {
			public void onSelect(FileSelectedEvent event) {
				newFileInputOnSelect(event);
			}
		});
		lastNewFileInput = newFileInput;
		newFileInputContainer.add(lastNewFileInput);
	}

	private void newFileInputOnSelect(FileSelectedEvent event) {
		String fileName = event.getFilename();

		// create FileRecord
		FileRecord fileRecord = new FileRecord();
		fileRecord.setFileId(generateID());
		fileRecord.setFileState(FileState.STATE_QUEUE);
		fileRecord.setFileName(fileName);
		fileRecord.setNote(messageDictionary.get(NOTE_QUEUED));

		// fire BeforeFileAddEvent
		BeforeFileAddEvent newEvent = new BeforeFileAddEvent(FileUploader.this,
				fileName);
		fireEvent(newEvent);
		if (newEvent.isCancelled()) {
			return;
		}

		// move lastFileInput to fileUploadMap
		FileUploadInput fileUploadInput = event.getFileUploadInput();
		fileUploadInput.setVisible(false);
		newFileInputContainer.remove(fileUploadInput);

		// show this file in file grid
		fileGrid.addData(fileRecord);

		// create a new FileUpload
		addNewFileInput();
		resizeLastNewFileInput();

		// fire FileAddEvent
		fireEvent(new FileAddEvent(FileUploader.this, fileName));

	}

	/* - Control Event handling --------------------------------------------- */
	/**
	 * Invoke to start uploading files.
	 */
	private void uploadButtonOnClick() {
		// 1. create a file list for uploading
		if (fileGrid.getTotalRows() == 0) {
			return;
		}
		// 2. count uploading files
		uploadFileNum = 0;
		for (FileRecord fileRecord : fileGrid.getFileRecords()) {
			if (FileState.STATE_QUEUE.equals(fileRecord.getFileState())) {
				uploadFileNum++;
			}
		}
		if (uploadFileNum == 0) {
			return;
		}

		// 3. disable the uploadFileButton first
		uploadFileButton.disable();
		resetFileButton.disable();

		// 4. create a iframe for uploading files
		if (formFileUpload == null) {
			createUploadingForm();
		}

		// 5. initialize progressbar
		progressbar.setPercentDone(0);

		// 6. invoke listeners
		fireEvent(new UploadStartEvent(FileUploader.this));

		// 7. start uploading
		processNextFile();
	}

	/**
	 * Invoke to reset the file list.
	 */
	private void resetButtonOnClick() {
		fileGrid.setRecords(new FileRecord[0]);
		progressbar.setPercentDone(0);

		fireEvent(new FileQueueResetEvent(this));
	}

	/**
	 * Invoke to selected file from list.
	 */
	private void removeButtonOnClick() {
		if (!fileGrid.anySelected()) {
			return;
		}

		for (FileRecord fileRecord : fileGrid.getSelection()) {
			if (!FileState.STATE_PROCESSING.equals(fileRecord.getFileState())) {
				fileGrid.removeData(fileRecord);
				fireEvent(new FileRemoveEvent(FileUploader.this, fileRecord
						.getFileName()));
			}
		}
	}

	/**
	 * Invoke to stop the upload process.
	 */
	private void stopButtonOnClick() {
		// 1. stop current uploading file
		if (uploadingFileRecord != null) {
			uploadingFileRecord.setFileState(FileState.STATE_FAILED);
			uploadingFileRecord.setNote(messageDictionary.get(NOTE_CANCELED));
			fileGrid.updateData(uploadingFileRecord);
			uploadingFileRecord = null;
		}

		// 2. clear fileQueue
		if (fileGrid.getTotalRows() > 0) {
			// update status of file record
			for (FileRecord fileRecord : fileGrid.getSelection()) {
				if (FileState.STATE_PROCESSING
						.equals(fileRecord.getFileState())) {
					fileRecord.setFileState(FileState.STATE_QUEUE);
					fileRecord.setNote(messageDictionary.get(NOTE_QUEUED));
					fileGrid.updateData(fileRecord);
				}
			}
		}

		// 3. remove formFileUpload
		if (formFileUpload != null) {
			if (formFileUpload.getWidget() != null) {
				formFileUpload.remove(formFileUpload.getWidget());
			}
			formFileUpload.removeFromParent();
			formFileUpload = null;
		}

		resetFileButton.enable();
		uploadFileButton.enable();

		fireEvent(new UploadStopEvent(this));
	}

	/* - File upload handling ----------------------------------------------- */
	/**
	 * Try to submit next file in the waiting list.
	 */
	private void processNextFile() {
		uploadingFileRecord = findNextFileRecordForUploading();

		if (uploadingFileRecord == null) {
			// remove the iframe for uploading files
			if (formFileUpload != null) {
				formFileUpload.removeFromParent();
				formFileUpload = null;
			}

			uploadFileButton.enable();
			resetFileButton.enable();

			fireEvent(new UploadCompletedEvent(this));
			return;
		}

		// remove the old file input
		if (formFileUpload.getWidget() != null) {
			formFileUpload.remove(formFileUpload.getWidget());
		}

		// add the next file input
		formFileUpload.add(uploadingFileRecord.getFileUploadInput());

		// update its record
		uploadingFileRecord.setFileState(FileState.STATE_PROCESSING);
		fileGrid.updateData(uploadingFileRecord);

		// submit file
		formFileUpload.submit();
	}

	private FileRecord findNextFileRecordForUploading() {
		if (fileGrid.getTotalRows() == 0) {
			return null;
		}
		for (FileRecord fileRecord : fileGrid.getFileRecords()) {
			if (FileState.STATE_QUEUE.equals(fileRecord.getFileState())) {
				return fileRecord;
			}
		}
		return null;
	}

	private void createUploadingForm() {
		formFileUpload = new FileUploadingForm(url);

		Canvas canvas = new WidgetCanvas(formFileUpload);
		canvas.hide();
		addMember(canvas);

		formFileUpload.addFormHandler(new FormHandler() {
			public void onSubmit(FormSubmitEvent event) {
				if (uploadingFileRecord != null) {
					fireEvent(new FileUploadStartEvent(FileUploader.this,
							uploadingFileRecord.getFileName()));
				}

			}
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				GWT.log("Result: " + event.getResults(), null);
				try {
					JSONValue jsonValue = JSONParser
							.parse(getJSONFromFormResult(event.getResults()));

					parseResult(jsonValue);
				} catch (Exception e) {
					GWT.log("Failed to get response.", e);
					onFailure(messageDictionary.get(MSG_SERVER_SIDE_ERROR),
							null);
				}

				// process next one
				processNextFile();
			}
		});
	}

	/**
	 * Strip off "pre" tag.
	 */
	private String getJSONFromFormResult(String htmlResults) {
		String results = htmlResults.trim();
		if (htmlResults != null && !htmlResults.startsWith("{")) {
			results = results.substring(results.indexOf("{"), results
					.lastIndexOf("}") + 1);
			return results;
		} else {
			return results;
		}
	}

	private void parseResult(JSONValue jsonValue) {
		JSONObject jsonObject = jsonValue.isObject();

		if (jsonObject != null) {
			String testData = "";
			Set<String> keys = jsonObject.keySet();
			for (String key : keys) {
				testData += key + ":" + jsonObject.get(key) + ";";
			}
			GWT.log(testData, null);

			String result = String.valueOf(jsonObject.get(JSON_KEY_SUCCESS));
			if ("true".equals(result)) {
				onSuccess(messageDictionary.get(MSG_SUCCESS), jsonValue);
			} else {
				onFailure(String.valueOf(jsonObject.get(JSON_KEY_REASON)),
						jsonValue);
			}
		} else {
			onFailure(messageDictionary.get(MSG_SERVER_SIDE_ERROR), null);
		}
	}

	private void onSuccess(String message, JSONValue jsonValue) {
		// update record of the current uploading file
		if (uploadingFileRecord != null) {
			uploadingFileRecord.setFileState(FileState.STATE_FINISHED);
			uploadingFileRecord.setNote(message);
			fileGrid.updateData(uploadingFileRecord);
		}

		if (uploadingFileRecord != null) {
			fireEvent(new FileUploadSuccessEvent(FileUploader.this,
					uploadingFileRecord.getFileName(), jsonValue));
		}
	}

	private void onFailure(String message, JSONValue jsonValue) {
		// find the reason
		String reason;
		if ("null".equals(message)) {
			reason = messageDictionary.get(MSG_COULDNT_RETRIEVES_ERVER);
		} else {
			reason = message;
		}
		// update record of the current uploading file
		if (uploadingFileRecord != null) {
			uploadingFileRecord.setFileState(FileState.STATE_FAILED);
			uploadingFileRecord.setNote(reason);
			fileGrid.updateData(uploadingFileRecord);
		}

		if (uploadingFileRecord != null) {
			fireEvent(new FileUploadFailedEvent(FileUploader.this,
					uploadingFileRecord.getFileName(), jsonValue));
		}
	}

	/* - Other -------------------------------------------------------------- */
	/**
	 * @return an unique ID for Record's fineId
	 */
	private String generateID() {
		NEXT_ID++;
		return "" + (NEXT_ID - 1);
	}

}
