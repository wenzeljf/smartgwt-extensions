/**
Copyright (c) <2009> <Pete Boysen>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.smartgwt.extensions.client.filemanager;

import com.smartgwt.client.widgets.Window;
import com.smartgwt.extensions.client.util.SelectionListener;
import com.smartgwt.extensions.client.util.ds.ProjectDataSource;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class FileSelector extends Window {
	private Library library;
	public FileSelector(ProjectDataSource dataSource, String root, Library.Mode mode) {
		setWidth(500);
		setHeight(400);
		setTitle("Select a File");
		setCanDragReposition(true);
		setCanDragResize(true);
		setShowMaximizeButton(true);
		library = new Library(dataSource, root, null, mode);
		addItem(library);
	}

	public void setSelectionListener(SelectionListener listener) {
		library.setSelectionListener(listener);
	}
}
