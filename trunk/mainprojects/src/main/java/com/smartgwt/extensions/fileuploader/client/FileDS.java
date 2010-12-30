package com.smartgwt.extensions.fileuploader.client;

import com.google.gwt.i18n.client.Dictionary;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceEnumField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class FileDS extends DataSource implements FileUploaderConstants {

	public FileDS(final Dictionary messageDictionary) {

		DataSourceIntegerField pkField = new DataSourceIntegerField(
				FileRecord.RECORD_FIELD_FILE_ID, "ID");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);

		DataSourceTextField fileNameField = new DataSourceTextField(
				FileRecord.RECORD_FIELD_FILE_NAME, LABEL_FILE_NAME);
		fileNameField.setRequired(true);

		DataSourceIntegerField folderIdField = new DataSourceIntegerField(
				FileRecord.RECORD_FIELD_FOLDER_ID, "Folder Id");
		folderIdField.setHidden(true);
		folderIdField.setForeignKey(FileRecord.RECORD_FIELD_FOLDER_ID);

		DataSourceEnumField statusCodeField = new DataSourceEnumField(
				FileRecord.RECORD_FIELD_FILE_STATE, messageDictionary
						.get(LABEL_STATUS_CODE));
		statusCodeField.setRequired(true);

		DataSourceTextField noteField = new DataSourceTextField(
				FileRecord.RECORD_FIELD_NOTE, LABEL_NOTE);

		setFields(pkField, fileNameField, folderIdField,
				statusCodeField, noteField);
	}

}
