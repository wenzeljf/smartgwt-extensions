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

import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.extensions.fileuploader.client.filemanager.File.Field;
import com.smartgwt.extensions.util.client.SaveListener;
import com.smartgwt.extensions.util.client.UI;
import com.smartgwt.extensions.util.client.Util;
import com.smartgwt.extensions.util.client.ds.ProjectDataSource;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class Notes extends Canvas implements SaveListener {
	private TreeGrid treeGrid;
	private TreeGridField labelField;
	private ProjectDataSource dataSource;
	private NotesEditor notesEditor;
	private NoteEditor noteEditor;
	private String resPath;
	private String stylesPath;
	private boolean refreshTypes = true;
	
	public Notes(ProjectDataSource dataSource, String resPath, String stylesPath) {
		this(dataSource, new Menu(),resPath,stylesPath);
	}
	
	public Notes(ProjectDataSource dataSource, Menu parentMenu, String resPath, String stylesPath) {
		this.dataSource = dataSource;
		this.resPath = resPath;
		this.stylesPath = stylesPath;
		treeGrid = new TreeGrid();
		treeGrid.setBodyBackgroundColor(UI.getBackgroundColor());
		treeGrid.setWidth100();  
		treeGrid.setHeight100();  
		treeGrid.setShowEdges(true);
		treeGrid.setEdgeSize(3);
		treeGrid.setEdgeOffset(3);
		treeGrid.setCanDragRecordsOut(true);  
		treeGrid.setCanAcceptDroppedRecords(true);
		treeGrid.setCanAcceptDrop(true);
		treeGrid.setCanReparentNodes(true);
		treeGrid.setCanDropOnLeaves(true);
		treeGrid.setAlternateRecordStyles(true);
		treeGrid.setCanEdit(false);
		treeGrid.setWrapCells(true);
		treeGrid.setFixedRecordHeights(false);
		treeGrid.setDragDataAction(DragDataAction.MOVE);
        treeGrid.setCustomIconProperty("icon");
        treeGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
        	public void onCellDoubleClick(CellDoubleClickEvent e) {
				ListGridRecord rec = treeGrid.getSelectedRecord();
				Note note = new Note(rec);
				editNote(note);
        	}
        });
		labelField = new TreeGridField(Note.Field.label.toString(), "My Notes");
		labelField.setCanSort(false);
		labelField.setShowHover(true);
		labelField.setHoverCustomizer(new HoverCustomizer() {
			public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
				return record.getAttribute("comment");
			}
		});
		treeGrid.setFields(new TreeGridField[]{labelField});
		treeGrid.setContextMenu(getMenu(parentMenu));
        Note.setFields(dataSource);
        treeGrid.setDataSource(dataSource);
        treeGrid.setAutoFetchData(true);
        treeGrid.setHoverWidth(200);
        treeGrid.setHoverWrap(true);
        treeGrid.setLeaveScrollbarGap(false);
 		addChild(treeGrid);
	}
	
	private Menu getMenu(Menu parentMenu) {
		final Menu menu = new Menu();  
		final MenuItem addNote = new MenuItem("Add Note","[SKINIMG]/actions/add.png");
		addNote.addClickHandler(new ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				addNote("New Note","", "");
			}
		});
		final MenuItem addSubFolder = new MenuItem("Add Folder","[SKINIMG]/FileBrowser/createNewFolder.png");
		addSubFolder.addClickHandler(new ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				addFolder("New Folder", null);
			}
		});
		final MenuItem edit = new MenuItem("Edit Note","[SKINIMG]/actions/edit.png");
		edit.addClickHandler(new ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				ListGridRecord rec = treeGrid.getSelectedRecord();
				Note note = new Note(rec);
				editNote(note);
			}
		});
		final MenuItem delete = new MenuItem("Delete Note/Folder","[SKINIMG]/actions/remove.png");
		delete.addClickHandler(new ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				SC.confirm("Are you sure you want to delete?", new BooleanCallback(){
					public void execute(Boolean value) {
						if (value != null)
							treeGrid.removeSelectedData();
					}
				});					
			}
		});
		// TODO: only show for configureNotes permission
		final MenuItem configure = new MenuItem("Configure Notes","[SKINIMG]/actions/configure.png");
		configure.addClickHandler(new ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				if (notesEditor == null)
					notesEditor = new NotesEditor();
				notesEditor.setDataSource(dataSource);
				refreshTypes = true;
				UI.showNearClick(notesEditor, -100, 0);
			}
		});
		final MenuItemIfFunction menuFunction = new MenuItemIfFunction() {
			public boolean execute(Canvas canvas, Menu menu, MenuItem item) {
        		ListGridRecord record = treeGrid.getSelectedRecord();
        		if (record == null) return false;
        		boolean isFolder = record.getAttributeAsBoolean(Note.Field.isFolder.toString());
        		if (item == addSubFolder)
        			return isFolder;
        		else 
        			return true;
			}
		};
		
		delete.setEnableIfCondition(menuFunction);
		edit.setEnableIfCondition(menuFunction);
		addSubFolder.setEnableIfCondition(menuFunction);
        menu.setData(addNote, edit, addSubFolder, delete, configure);
        if (parentMenu.getItems().length > 0) {
        	menu.addData(new MenuItemSeparator());
        }
        for (MenuItem item : parentMenu.getItems()) {
        	menu.addData(item);
        }
        return menu;
	}
	
	public void addNote(String label,String type, String comment) {
		label = Util.convertToRelative(label,"/thinkspace/download"+resPath);
		Note newNote = new Note("", getParentId(), type, label, "", false);
		editNote(newNote);
	}

	public void addFolder(String label, String type ) {
		Note newNote = new Note("", getParentId(), type, label, "", true);
		editNote(newNote);
	}
	
	public void onSave() {
		ListGridRecord rec = new ListGridRecord();
		noteEditor.getNote().update(rec);
		if (rec.getAttribute(Note.Field._id.toString()).equals(""))
			dataSource.addData(rec);
		else
			dataSource.updateData(rec);
	}
	
	private void editNote(Note note) {
		if (noteEditor == null) {
			noteEditor = new NoteEditor(resPath,stylesPath);
			noteEditor.setSaveListener(this);
		}
		if (refreshTypes) {
			noteEditor.refreshTypes(dataSource);
			refreshTypes = false;
		}
		noteEditor.setNote(note);			
		UI.showNearClick(noteEditor,0 ,0);
	}

	private String getParentId() {
		String parentId = "2";
		ListGridRecord note = treeGrid.getSelectedRecord();
		if (note != null) {
			parentId = note.getAttribute(Field._id.toString());
			Tree tree = treeGrid.getData();
			TreeNode parent = tree.findById(parentId);
			tree.openFolder(parent);
		}
		return parentId;
	}
	
}
