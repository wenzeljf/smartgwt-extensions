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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.extensions.util.client.UI;
import com.smartgwt.extensions.util.client.ds.ProjectDataSource;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class ItemEditor extends Window {
	private ListGrid itemGrid;
	private List<Item> items;
	
	public ItemEditor(ProjectDataSource dataSource) {
		setTitle("Item Editor");
		setWidth(500);
		setHeight(450);
		setCanDragReposition(true);
		setCanDragResize(true);
		setIsModal(true);
		
		itemGrid = UI.getListGrid();
		itemGrid.setHeight("95%");
		itemGrid.setShowHeaderMenuButton(true);
		itemGrid.setEditEvent(ListGridEditEvent.CLICK);
		itemGrid.setListEndEditAction(RowEndEditAction.NEXT);
		itemGrid.setLeaveScrollbarGap(false);
		itemGrid.setDataSource(dataSource);
		itemGrid.setCanEdit(true);
		itemGrid.setRowEndEditAction(RowEndEditAction.DONE);
		setFields();
		VStack stack = UI.getVStack();
		stack.setBackgroundColor(UI.getBackgroundColor());
		stack.addMember(itemGrid);
		addItem(stack);
	}
	
	public void setItems(final List<Item> items) {
		this.items = items;
		itemGrid.fetchData(null,new DSCallback() {
			public void execute(DSResponse response, java.lang.Object rawData, DSRequest request) {
				final List<Item> deletes = new ArrayList<Item>();
				final List<Item> adds = new ArrayList<Item>();
				for (Record rec: response.getData()) {
					String name = rec.getAttribute(Item.Field.name.toString());
					if (name != null) {
						boolean found = false;
						for (Item ans: items)
							if (ans.getName().equals(name)) {
								found = true;
								break;
							}
						if (!found) deletes.add(new Item(rec));
					}
				}
				for (Item ans: items) {
					boolean found = false;
					for (Record rec: response.getData()) {
						String name = rec.getAttribute(Item.Field.name.toString());
						if (ans.getName().equals(name)) {
							found = true;
							break;
						}
					}
					if (!found) adds.add(new Item(ans));
				}
				for (Record rec: deletes)
					itemGrid.removeData(rec);
				for (Record rec: adds)
					itemGrid.addData(rec);
			}
		});
	}
	
	private void setFields() {
		ListGridField nameField = new ListGridField(Item.Field.name.toString(), "Item Name");
		nameField.setDefaultValue("Name");
		nameField.setType(ListGridFieldType.TEXT);
		nameField.setRequired(true);
		nameField.setCanEdit(false);
		ListGridField typeField = new ListGridField(Item.Field.type.toString(), "Type");
		typeField.setDefaultValue("text");
		typeField.setType(ListGridFieldType.TEXT);
		typeField.setRequired(true);
		typeField.setHidden(true);
		typeField.setCanEdit(false);
		final ListGridField correctField = new ListGridField(Item.Field.value.toString(), "Correct Answer");
		correctField.setDefaultValue("");
		correctField.setType(ListGridFieldType.TEXT);
		correctField.setRequired(true);
		itemGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent e) {
	        	String name = e.getRecord().getAttribute(Item.Field.name.toString());
	        	correctField.setValueMap((Map<String,String>)null);
 	        	for (Item item: items)
	        		if (item.getName().equals(name) && item.hasMap()) {
	        			correctField.setValueMap(item.getMap());
	        			break;
	        	}
			}
		});
		ListGridField judgingField = new ListGridField(Item.Field.judging.toString(), "Judging");
		judgingField.setDefaultValue("0");
		judgingField.setType(ListGridFieldType.TEXT);
		judgingField.setRequired(true);
		ListGridField hintField = new ListGridField(Item.Field.hint.toString(), "Hint");
		hintField.setDefaultValue("");
		hintField.setType(ListGridFieldType.TEXT);
		hintField.setRequired(true);
		itemGrid.setFields(nameField,typeField,correctField,judgingField,hintField);

	}
}
