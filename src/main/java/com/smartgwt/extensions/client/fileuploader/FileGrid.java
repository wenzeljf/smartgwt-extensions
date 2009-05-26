package com.smartgwt.extensions.client.fileuploader;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class FileGrid extends ListGrid implements FileUploaderConstants {

	public FileGrid(final Dictionary messageDictionary) {

		setAlternateRecordStyles(true);
		setEmptyCellValue("-");
		setShowAllRecords(true);
		setSelectionType(SelectionStyle.MULTIPLE);

		setCanHover(true);

		HoverCustomizer rowTip = new HoverCustomizer() {
			public String hoverHTML(Object value, ListGridRecord record,
					int rowNum, int colNum) {
				if (colNum == 0) {
					return null;
				}

				FileRecord fileRecord = new FileRecord(record.getJsObj());
				HTML tip;
				if (colNum == 1) {
					tip = new HTML(fileRecord.getFileName());
				} else {
					tip = new HTML(fileRecord.getNote());
				}
				tip.addStyleName(CSS_FILE_NAME_TIP);

				HTML tipWrapper = new HTML();
				tipWrapper.getElement().appendChild(tip.getElement());
				return tipWrapper.getHTML();
			}
		};

		ListGridField fileStateField = new ListGridField(
				FileRecord.RECORD_FIELD_FILE_STATE, messageDictionary
						.get(LABEL_STATUS_CODE), 60);
		fileStateField.setAlign(Alignment.CENTER);
		fileStateField.setType(ListGridFieldType.IMAGE);
		fileStateField.setImageURLPrefix(IMG_STATUS_PREFIX);
		fileStateField.setImageURLSuffix(IMG_STATUS_SUFFIX);
		fileStateField.setHoverCustomizer(rowTip);

		ListGridField fileNameField = new ListGridField(
				FileRecord.RECORD_FIELD_FILE_NAME, messageDictionary
						.get(LABEL_FILE_NAME));
		fileNameField.setHoverCustomizer(rowTip);

		ListGridField noteField = new ListGridField(
				FileRecord.RECORD_FIELD_NOTE,
				messageDictionary.get(LABEL_NOTE), 200);
		noteField.setHoverCustomizer(rowTip);

		setFields(fileStateField, fileNameField, noteField);
	}
	
	public FileRecord[] getFileRecords() {
		return convertToFileRecords(getRecords());
	}

	public FileRecord[] getSelection() {
		return convertToFileRecords(super.getSelection());
	}

	private FileRecord[] convertToFileRecords(ListGridRecord[] records) {
		if (records == null) {

		}
		FileRecord[] fileRecords = new FileRecord[records.length];
		for (int i = 0; i < records.length; i++) {
			fileRecords[i] = new FileRecord(records[i].getJsObj());
		}
		return fileRecords;
	}

}
