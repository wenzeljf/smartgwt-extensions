package com.smartgwt.extensions.client.fileuploader;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FileUpload;
import com.smartgwt.client.util.SC;
import com.smartgwt.extensions.client.fileuploader.event.FileSelectedEvent;
import com.smartgwt.extensions.client.fileuploader.event.FileSelectedHandler;

public class FileUploadInput extends FileUpload {
	private static final String CSS_LAST_FILE_INPUT = "fu-lastFileInput";

	private FileRecord fileRecord = new FileRecord();
	
	public FileUploadInput(String postVarName) {
		// TODO 00 change the mock button image based on mouse events
		// sinkEvents(Event.ONBLUR | Event.ONCHANGE | Event.ONMOUSEOVER
		// | Event.ONMOUSEOUT | Event.ONMOUSEMOVE);
		sinkEvents(Event.ONCHANGE);
		setName(postVarName);
		((InputElement) getElement().cast()).setSize(1);
		// TODO 00 move and resize the input instead of CSS.
		addStyleName(CSS_LAST_FILE_INPUT);

		DOM.setStyleAttribute(getElement(), "-moz-opacity", "50");
		DOM.setStyleAttribute(getElement(), "cursor", "pointer");
		DOM.setStyleAttribute(getElement(), "filter", "alpha(opacity=50)");
		DOM.setStyleAttribute(getElement(), "font-size", "23px");
		DOM.setStyleAttribute(getElement(), "opacity", "50");
		DOM.setStyleAttribute(getElement(), "position", "absolute");
		DOM.setStyleAttribute(getElement(), "width", "100px");
		DOM.setStyleAttribute(getElement(), "z-index", "3000000");

		if (SC.isIE()) {
			DOM.setStyleAttribute(getElement(), "left", "-55px");
			DOM.setStyleAttribute(getElement(), "top", "-55px");
		} else {
			DOM.setStyleAttribute(getElement(), "left", "-24px");
			DOM.setStyleAttribute(getElement(), "top", "-16px");
		}
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
		if (fileName == null || fileName.trim().length() == 0
				|| fileName.equals(fileRecord.getFileName())) {
			return;
		}

		fireEvent(new FileSelectedEvent(FileUploadInput.this, fileName));
	}

	public FileRecord getFileRecord() {
		return fileRecord;
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
