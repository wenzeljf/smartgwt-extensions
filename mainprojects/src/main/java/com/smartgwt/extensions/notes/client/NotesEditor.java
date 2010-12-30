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
package com.smartgwt.extensions.notes.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.PickTreeItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorEnterEvent;
import com.smartgwt.client.widgets.grid.events.EditorEnterHandler;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.extensions.util.client.UI;
import com.smartgwt.extensions.util.client.ds.ProjectDataSource;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class NotesEditor extends Window {
	private ListGrid folders;
	private ListGrid types;

	public NotesEditor() {
		setTitle("Configure Notes");
		setIsModal(true);
		setWidth(400);
		setHeight(300);
		setCanDragResize(true);

      folders = getListGrid(getFoldersMenu());

      types = getListGrid(getTypesMenu());

      ListGridField type = new ListGridField("typename","Type");
		ListGridField icon = new ListGridField("icon","Icon");
		icon.addEditorEnterHandler(new EditorEnterHandler() {
			public void onEditorEnter(EditorEnterEvent e) {
				SC.say("enter");

			}
		});
		icon.setType(ListGridFieldType.IMAGE);
		icon.setAlign(Alignment.CENTER);
		icon.setWidth(32);
		PickTreeItem iconEditor = new PickTreeItem("iconeditor");
		iconEditor.setLoadDataOnDemand(true);
		ProjectDataSource pds = new ProjectDataSource("ThinkSpace","ThinkSpace","library.xq");
		iconEditor.setDataSource(pds);
		icon.setEditorType(iconEditor);
		types.setFields(type,icon);
		VStack stack = UI.getVStack();
		stack.setMembersMargin(10);
		stack.setBackgroundColor(UI.getBackgroundColor());
		stack.setDefaultLayoutAlign(Alignment.CENTER);
		stack.setPadding(5);
		stack.addMember(folders);
		stack.addMember(types);
		addItem(stack);
	}

   private ListGrid getListGrid(final Menu menu) {
      final ListGrid listGrid = UI.getListGrid();
      listGrid.setLeaveScrollbarGap(false);
      listGrid.setCanReorderRecords(true);
      listGrid.setCanEdit(true);
      listGrid.setCanRemoveRecords(false);
      listGrid.setContextMenu(menu);
      return listGrid;
   }

   public List<String> getFolders() {
		List<String> list = new ArrayList<String>();
		for(ListGridRecord rec: folders.getRecords())
			list.add(rec.getAttribute("name"));
		return list;
	}

	public void setDataSource(ProjectDataSource dataSource) {
		ProjectDataSource folderDS = Note.getFolderDataSource(dataSource);
		folders.setDataSource(folderDS);
		folders.fetchData();
		ProjectDataSource typesDS = Note.getTypesDataSource(dataSource);
		types.setDataSource(typesDS);
		types.fetchData();
	}

	public HashMap<String,String> getTypes() {
		ListGridRecord[] recs = types.getRecords();
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		for (ListGridRecord rec: recs)
			map.put(rec.getAttribute("name"), rec.getAttribute("icon"));
		return map;
	}

	private Menu getTypesMenu() {
		Menu menu = new Menu();
		menu.setWidth(50);
		final MenuItem add = new MenuItem("Add Type","[SKINIMG]/actions/add.png");
		add.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				types.startEditingNew();
			}
		});
		final MenuItem delete = new MenuItem("Delete Type","[SKINIMG]/actions/remove.png");
		delete.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				ListGridRecord rec = types.getSelectedRecord();
				SC.confirm("Delete "+rec.getAttribute("name")+"?", new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value)
							types.removeSelectedData();
					}
				});
			}
		});
		menu.setItems(add,delete);
		return menu;
	}

	private Menu getFoldersMenu() {
		Menu menu = new Menu();
		menu.setWidth(50);
		final MenuItem add = new MenuItem("Add Folder","[SKINIMG]/actions/add.png");
		add.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				folders.startEditingNew();
			}
		});
		final MenuItem delete = new MenuItem("Delete Folder","[SKINIMG]/actions/remove.png");
		delete.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				ListGridRecord rec = folders.getSelectedRecord();
				SC.confirm("Delete "+rec.getAttribute("name")+"?", new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value)
							folders.removeSelectedData();
					}
				});
			}
		});
		menu.setItems(add,delete);
		return menu;
	}
}
