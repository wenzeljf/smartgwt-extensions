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
package com.smartgwt.extensions.htmleditor.client;

import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class DocViewer extends Window {
	public DocViewer(String title, String url) {
		final DocViewer appWindow = this;
		setTitle(title);
		setWidth(800);
		setHeight(600);
		setCanDrag(true);
		setCanDragResize(true);
		setShowMaximizeButton(true);
		setShowMinimizeButton(true);
		addCloseClickHandler(new CloseClickHandler() {  
		    public void onCloseClick(CloseClientEvent event) {  
				appWindow.destroy();
		    }  
		});
		HTMLPane pane = new HTMLPane();
		pane.setWidth100();
		pane.setHeight100();
		pane.setContentsURL(url);  
		pane.setContentsType(ContentsType.PAGE);
		addItem(pane);
	}
}
