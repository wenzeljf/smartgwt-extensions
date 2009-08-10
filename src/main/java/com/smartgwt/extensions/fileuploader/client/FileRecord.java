package com.smartgwt.extensions.fileuploader.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class FileRecord extends ListGridRecord implements FileUploaderConstants {

    public FileRecord() {
	}

	public FileRecord(JavaScriptObject jsObj) {
		super(jsObj);
	}

	public String getFileId() {
		return getAttribute(RECORD_FIELD_FILE_ID);
	}

	public void setFileId(String fileId) {
		setAttribute(RECORD_FIELD_FILE_ID, fileId);
	}

	public String getFileName() {
		return getAttribute(RECORD_FIELD_FILE_NAME);
	}

	public void setFileName(String fileName) {
		setAttribute(RECORD_FIELD_FILE_NAME, fileName);
	}

	public FileState getFileState() {
		return (FileState) getAttributeAsObject(RECORD_FIELD_FILE_STATE);
	}

	public void setFileState(FileState fileState) {
		setAttribute(RECORD_FIELD_FILE_STATE, fileState);
	}

	public String getNote() {
		return getAttribute(RECORD_FIELD_NOTE);
	}

	public void setNote(String note) {
		setAttribute(RECORD_FIELD_NOTE, note);
	}

	public FileUploadInput getFileUploadInput() {
		return (FileUploadInput) getAttributeAsObject(RECORD_FIELD_FILE_INPUT);
	}

	public void setFileUploadInput(FileUploadInput input) {
		setAttribute(RECORD_FIELD_FILE_INPUT, input);
	}

}
