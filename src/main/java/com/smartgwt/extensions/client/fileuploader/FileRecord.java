package com.smartgwt.extensions.client.fileuploader;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class FileRecord extends ListGridRecord {
	public static final String RD_FIELD_FILE_ID = "fileId";
	public static final String RD_FIELD_FILE_NAME = "fileName";
	public static final String RD_FIELD_FOLDER_ID = "folderId";
	public static final String RD_FIELD_STATUS_CODE = "statusCode";
	public static final String RD_FIELD_NOTE = "note";

	public String getFileId() {
		return getAttribute(RD_FIELD_FILE_ID);
	}

	public void setFileId(String fileId) {
		setAttribute(RD_FIELD_FILE_ID, fileId);
	}

	public String getFileName() {
		return getAttribute(RD_FIELD_FILE_NAME);
	}

	public void setFileName(String fileName) {
		setAttribute(RD_FIELD_FILE_NAME, fileName);
	}

	public String getStatusCode() {
		return getAttribute(RD_FIELD_STATUS_CODE);
	}

	public void setStatusCode(String statusCode) {
		setAttribute(RD_FIELD_STATUS_CODE, statusCode);
	}

	public void setStatusCode(FileState statusCode) {
		if (statusCode != null) {
			setAttribute(RD_FIELD_STATUS_CODE, statusCode.toString());
		} else {
			setAttribute(RD_FIELD_STATUS_CODE, statusCode);
		}
	}

	public String getNote() {
		return getAttribute(RD_FIELD_NOTE);
	}

	public void setNote(String note) {
		setAttribute(RD_FIELD_NOTE, note);
	}

}
