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
import com.smartgwt.extensions.client.fileuploader.FileUploader;

/**
 * Fired when file queue is reseted.
 * 
 * @author anthony.yuan@gmail.com
 */
public class FileQueueResetEvent extends GwtEvent<FileQueueResetHandler> {

    /**
     * Handler type.
     */
    private static Type<FileQueueResetHandler> TYPE;
    
    public static Type<FileQueueResetHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<FileQueueResetHandler>();
        }
        return TYPE;
    }
    
    private FileUploader fileUploader;
    
    public FileQueueResetEvent(FileUploader fileUploader) {
		this.fileUploader = fileUploader;
    }

    public FileUploader getFileUploader() {
        return fileUploader;
    }

    @Override
    protected void dispatch(FileQueueResetHandler handler) {
        handler.onCompleted(this);
    }

    @Override
    public GwtEvent.Type<FileQueueResetHandler> getAssociatedType() {
        return getType();
    }

}
