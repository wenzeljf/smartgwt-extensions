package com.smartgwt.extensions.fileuploader.client;

import java.util.ArrayList;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FileUpload;
import com.smartgwt.client.util.SC;
import com.smartgwt.extensions.fileuploader.client.event.FileSelectedEvent;
import com.smartgwt.extensions.fileuploader.client.event.FileSelectedHandler;

public class FileUploadInput extends FileUpload
		implements
			FileUploaderConstants {

	private ArrayList<FileNameFilter> fileFilters;
	private Dictionary messageDictionary;

	public FileUploadInput(final String postVarName,
			final ArrayList<FileNameFilter> fileFilters,
			final Dictionary messageDictionary) {
		setName(postVarName);
		this.fileFilters = fileFilters;
		this.messageDictionary = messageDictionary;

		// TODO 01 change the mock button image based on mouse events
		// sinkEvents(Event.ONBLUR | Event.ONCHANGE | Event.ONMOUSEOVER
		// | Event.ONMOUSEOUT | Event.ONMOUSEMOVE);
		sinkEvents(Event.ONCHANGE);
		((InputElement) getElement().cast()).setSize(1);
		addStyleName(CSS_LAST_NEW_FILE_INPUT);
	}

	public void onBrowserEvent(Event event) {
		// if ((event.getTypeInt() & (Event.ONBLUR | Event.ONCHANGE)) == 0)
		// {
		// if ((event.getTypeInt() & Event.ONMOUSEOVER) != 0) {
		// addFileButton.setShowFocused(true);
		// } else if ((event.getTypeInt() & Event.ONMOUSEOUT) != 0) {
		// addFileButton.setShowFocused(false);
		// }
		// return;
		// }
		String fileName = getFilename();
		if (fileName == null || fileName.trim().length() == 0) {
			return;
		}

		if (!validateFileName(fileName)) {
			return;
		}
		
		// set up Record based on selected file

		fireEvent(new FileSelectedEvent(FileUploadInput.this, fileName));
	}

	/**
	 * Run file filters to validate the file name.
	 * 
	 * @param fileName
	 * @return true if it is valid
	 */
	private boolean validateFileName(String fileName) {
		if (fileFilters == null) {
			return true;
		}

		String error = "";
		for (FileNameFilter filter : fileFilters) {
			if (!filter.validate(fileName)) {
				error += "\n\n" + filter.getErrorMessage();
			}
		}
		if (error.length() == 0) {
			return true;
		}

		error = messageDictionary.get(MSG_INVALID_FILE_CHOSEN) + "\n<br/>\n"
				+ error;
		if (error.indexOf("{fileName}") > -1) {
			error = error.replace("{fileName}", fileName);
		}
		SC.say(error);
		return false;
	}

	public HandlerRegistration addFileSelectedHandler(
			FileSelectedHandler handler) {
		return doAddHandler(handler, FileSelectedEvent.getType());
	}

	private HandlerManager manager;

	public void fireEvent(GwtEvent<?> event) {
		if (manager != null) {
			manager.fireEvent(event);
		}
	}

	/**
	 * Adds this handler to the widget.
	 * 
	 * @param <H>
	 *            the type of handler to add
	 * @param type
	 *            the event type
	 * @param handler
	 *            the handler
	 * @return {@link HandlerRegistration} used to remove the handler
	 */
	protected final <H extends EventHandler> HandlerRegistration doAddHandler(
			final H handler, GwtEvent.Type<H> type) {
		return ensureHandlers().addHandler(type, handler);
	}

	/**
	 * Ensures the existence of the handler manager.
	 * 
	 * @return the handler manager
	 */
	HandlerManager ensureHandlers() {
		return manager == null ? manager = new HandlerManager(this) : manager;
	}
}
