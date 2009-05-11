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

package com.smartgwt.extensions.client.fileuploader.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.extensions.client.fileuploader.FileUploader;

/**
 * Fired when file upload error occurred.
 * 
 * @author anthony.yuan@gmail.com
 */
public class FileUploadFailedEvent extends GwtEvent<FileUploadFailedHandler> {

	/**
	 * Handler type.
	 */
	private static Type<FileUploadFailedHandler> TYPE;

	public static Type<FileUploadFailedHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<FileUploadFailedHandler>();
		}
		return TYPE;
	}

	private FileUploader fileUploader;
	private String filename;
	private JSONValue jsonData;

	/**
	 * Constructor.
	 * 
	 * @param fileUploader
	 *            the FileUploader instance
	 * @param filename
	 *            the name of the file to queue
	 * @param jsonData
	 *            JSONValue builded from JSON-data returned from upload handler
	 *            response. Use the jsonObject.get(key) to get the data from the
	 *            JSon object. Might be null, if there is no response or
	 *            incorrect response from server side.
	 */
	public FileUploadFailedEvent(FileUploader fileUploader, String filename,
			JSONValue jsonData) {
		this.fileUploader = fileUploader;
		this.filename = filename;
		this.jsonData = jsonData;
	}

	/**
	 * @return the name of the file to queue
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @return the FileUploader instance
	 */
	public FileUploader getFileUploader() {
		return fileUploader;
	}

	/**
	 * @return JSONValue builded from JSON-data returned from upload handler
	 *         response. Use the jsonObject.get(key) to get the data from the
	 *         JSon object. Might be null, if there is no response or incorrect
	 *         response from server side.
	 */
	public JSONValue getJsonData() {
		return jsonData;
	}

	@Override
	protected void dispatch(FileUploadFailedHandler handler) {
		handler.onCompleted(this);
	}

	@Override
	public GwtEvent.Type<FileUploadFailedHandler> getAssociatedType() {
		return getType();
	}

}
