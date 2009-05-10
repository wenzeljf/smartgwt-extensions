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
package com.smartgwt.extensions.client.util;

import java.util.ArrayList;
import java.util.List;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class ListOrderer extends Window {
	private ListGrid orderer;
	private SaveListener listener;

	public ListOrderer(String title) {
		setWidth(200);
		setHeight(200);
		setTitle(title);
		setBackgroundColor(UI.getBackgroundColor());
		Label label = new Label("Reorder by dragging.");
		label.setHeight(10);
		orderer = new ListGrid();
		orderer.setCanReorderRecords(true);
        orderer.setLeaveScrollbarGap(false);
        orderer.setShowHeader(false);
		ListGridField name = new ListGridField("name");
		orderer.setFields(name);
		IButton save = new IButton("Save");
		final ListOrderer listOrderer = this;
		save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(ClickEvent e) {
				if (listener != null)
					listener.onSave();
				UI.hide(listOrderer);
			}
		});
		VStack stack = new VStack();
		stack.setPadding(5);
		stack.setDefaultLayoutAlign(Alignment.CENTER);
		stack.setMembersMargin(10);
		stack.addMember(label);
		stack.addMember(orderer);
		stack.addMember(save);
		addItem(stack);
	}

	public void setList(List<String> list) {
		ListGridRecord[] recs = new ListGridRecord[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ListGridRecord rec = new ListGridRecord();
			rec.setAttribute("name",list.get(i));
			recs[i] = rec;
		}
		orderer.setData(recs);
	}
	
	public List<String> getList() {
		ListGridRecord[] recs = orderer.getRecords();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < recs.length; i++) 
			list.add(recs[i].getAttribute("name"));
		return list;
	}

	public void setSaveListener(SaveListener listener) {
		this.listener = listener;
	}

}
