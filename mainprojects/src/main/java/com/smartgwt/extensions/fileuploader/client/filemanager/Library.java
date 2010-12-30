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
package com.smartgwt.extensions.fileuploader.client.filemanager;

import java.util.Map;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;
import com.smartgwt.extensions.fileuploader.client.filemanager.File.Field;
import com.smartgwt.extensions.htmleditor.client.DocEditor;
import com.smartgwt.extensions.htmleditor.client.DocViewer;
import com.smartgwt.extensions.util.client.SelectionListener;
import com.smartgwt.extensions.util.client.UI;
import com.smartgwt.extensions.util.client.Util;
import com.smartgwt.extensions.util.client.ds.ProjectDataSource;
import com.smartgwt.extensions.util.client.ds.ResponseListener;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class Library extends Canvas {
	public static enum Mode {EDIT, SELECT, SELECT_UPLOAD, VIEW }
	private static enum EmbeddableType {txt,html,htm,xml,gif,jpg,css,rtf,mov,wmf,wav,mp3,swf,qt,rm,ra,pdf};
	private Mode mode;
	private TreeGrid treeGrid;
	private Upload upload;
	private String resPath;
	private String stylesPath;
	private SelectionListener selectionListener;
	private ProjectDataSource dataSource;
	
	public Library(ProjectDataSource dataSource, String resPath, String stylesPath, Mode mode) {
		this.dataSource = dataSource;
		this.resPath = resPath;
		this.stylesPath = stylesPath;
		this.mode = mode;
		setDSFields(resPath);
		setWidth100();
		setHeight100();
		treeGrid = new TreeGrid();
		treeGrid.setWidth100();  
		treeGrid.setHeight100();
        treeGrid.setCustomIconProperty("icon");
        treeGrid.setDataSource(dataSource);
		treeGrid.setAutoFetchData(true);
		
        treeGrid.setLeaveScrollbarGap(false);
		treeGrid.setAlternateRecordStyles(true);
		treeGrid.setLoadDataOnDemand(true);
		treeGrid.setDragDataAction(DragDataAction.MOVE);
		treeGrid.setEmptyMessage("The folder is empty.");
		treeGrid.addNodeClickHandler(new NodeClickHandler(){
			public void onNodeClick(NodeClickEvent e) {
				TreeNode node = e.getNode();
				click(node);
			}
		});

 		TreeGridField name = new TreeGridField(Field.name.toString(),"Name");
 		name.setWidth("50%");
 		name.setCanEdit(mode == Mode.EDIT);
 		if (mode == Mode.SELECT || mode == Mode.SELECT_UPLOAD) {
 			name.setCellFormatter(new CellFormatter() {
 				public String format(Object obj,ListGridRecord record,int row, int col) {
 					String value = record.getAttribute(Field.name.toString());
 					if (!record.getAttributeAsBoolean(Field.isFolder.toString())) 
 						return "<span style=\"text-decoration:underline\">"+value+"</span>";
 					else
 						return value;
 				}
 			});
 		}
		TreeGridField modified = new TreeGridField(Field.modified.toString(),"Modified");
		modified.setWidth("30%");
		TreeGridField size = new TreeGridField(Field.size.toString(),"Size");
		size.setCellFormatter(new CellFormatter() {
			public String format(Object obj,ListGridRecord record,int row, int col) {
				String value = record.getAttribute(Field.size.toString());
				if (value == null) value="1000";
				int size = Integer.valueOf(value);
				if (size < 1000)
					return size+"B";
				if (size < 1000000)
					return (size / 1000)+"K";
				else
					return (size / 1000000)+"M";
			}
		});
		size.setAlign(Alignment.RIGHT);
		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		if (mode == Mode.EDIT) {
	   		treeGrid.setCanAcceptDroppedRecords(true);
    		treeGrid.setCanDragRecordsOut(true);
    		treeGrid.setCanReparentNodes(true);
    		treeGrid.setCanReorderRecords(true);
    		treeGrid.setCanDropOnLeaves(true);
			final Menu nodeMenu = new Menu();  
			nodeMenu.setWidth(50);  
			treeGrid.setContextMenu(nodeMenu);
			final MenuItem view = new MenuItem("View","[SKINIMG]/actions/view.png");
			view.addClickHandler(new ClickHandler(){
				public void onClick(MenuItemClickEvent e) {
					showFile();
				}
			});
			final MenuItem addSubFolder = new MenuItem("Add Subfolder","[SKINIMG]/FileBrowser/createNewFolder.png");
			addSubFolder.addClickHandler(new ClickHandler(){
				public void onClick(MenuItemClickEvent e) {
					TreeNode parent = (TreeNode)treeGrid.getSelectedRecord();
					TreeNode node = new TreeNode();
					node.setAttribute(Field.name.toString(),"New Folder");
					node.setAttribute(Field.size.toString(),"1000");
					node.setAttribute(Field.modified.toString(),"");
					node.setAttribute(Field.isFolder.toString(),true);
					String parentPath = getResPath();
					if (parent != null)
						parentPath = parent.getAttribute(Field._id.toString());
					node.setAttribute(Field.parent.toString(), parentPath);
					node.setAttribute(Field._id.toString(), parentPath+"New Folder/");
					ProjectDataSource pds = getDataSource();
					pds.addData(node);
				}
			});
			final MenuItem edit = new MenuItem("Edit","[SKINIMG]/actions/edit.png");
			edit.addClickHandler(new ClickHandler(){
				public void onClick(MenuItemClickEvent e) {
					editFile();
				}
			});
			final MenuItem delete = new MenuItem("Delete","[SKINIMG]/actions/remove.png");
			delete.addClickHandler(new ClickHandler(){
				public void onClick(MenuItemClickEvent e) {
					final TreeNode node = (TreeNode)treeGrid.getSelectedRecord();
					SC.confirm("Are you sure you want to delete "+node.getName()+"?", new BooleanCallback(){
						public void execute(Boolean value) {
							if (value != null && value) {
								treeGrid.removeSelectedData();
							}
						}
					});					
				}
			});
			final MenuItem convertFormat = new MenuItem("Convert","[SKINIMG]/actions/refresh.png");
			convertFormat.addClickHandler(new ClickHandler(){
				public void onClick(MenuItemClickEvent e) {
					SC.say("Not Yet Implemented");
				}
			});
			final MenuItem uploadFile = new MenuItem("Upload","actions/upload.png");
			uploadFile.addClickHandler(new ClickHandler(){
				public void onClick(MenuItemClickEvent e) {
					final TreeNode node = (TreeNode)treeGrid.getSelectedRecord();
					if (node != null)
					upload.setHiddenItem("path",node.getAttribute(Field._id.toString()));
					upload.show();
				}
			});
			
			MenuItemIfFunction menuFunction = new MenuItemIfFunction() {
				public boolean execute(Canvas canvas, Menu menu, MenuItem item) {
	        		ListGridRecord record = treeGrid.getSelectedRecord();
	        		if (record == null) return item == uploadFile;
	        		String path = "";
	        		boolean isFolder = record.getAttributeAsBoolean("isFolder");
	         		if (item == view || item == convertFormat)
	        			return !isFolder;
	        		else if (item == addSubFolder || item == uploadFile)
	        			return isFolder;
	        		else if (item == delete)
	        			return !(path.equals("/"+record.getAttribute("name")));
	        		else
	        			return true;
				}
			};
			
			MenuItemSeparator separator = new MenuItemSeparator();
			
			view.setEnableIfCondition(menuFunction);
			delete.setEnableIfCondition(menuFunction);
			addSubFolder.setEnableIfCondition(menuFunction);
			convertFormat.setEnableIfCondition(menuFunction);
			uploadFile.setEnableIfCondition(menuFunction);
	        nodeMenu.setItems(view,edit,delete,separator,uploadFile,addSubFolder,convertFormat);
	        treeGrid.setFields(new TreeGridField[]{name,modified,size});
		} else {
			treeGrid.setFields(new TreeGridField[]{name});
			treeGrid.setShowHeader(true);
		}
		if (mode == Mode.EDIT || mode == Mode.SELECT_UPLOAD) {
			Map<String,String> map = dataSource.getParams();
			map.put("xq","library.xq");
			map.put("path", resPath);
			upload = new Upload(map, Upload.Mode.CONVERSIONS);
			upload.setBorder("1px solid #cccccc");
			upload.setAction(Util.getDataURL());
			upload.setUploadListener(new UploadListener(){
				public void uploadComplete(String fileName) {
				}
			});
			treeGrid.setShowResizeBar(true);
			treeGrid.setResizeBarTarget("next");
			treeGrid.setBorder("1px solid #cccccc");
			treeGrid.setWidth("80%");
			upload.hide();
			layout.addMember(treeGrid);
			layout.addMember(upload);
		} else
			layout.addMember(treeGrid);
		addChild(layout);
	}

	public String getResPath() {
		return resPath;
	}
	
	public String getStylesPath() {
		return stylesPath;
	}
	
	public ProjectDataSource getDataSource() {
		return dataSource;
	}
	
	public SelectionListener getSelectionListener() {
		return selectionListener;
	}

	public void setSelectionListener(SelectionListener selectionListener) {
		this.selectionListener = selectionListener;
	}

	private void click(TreeNode node) {
		if (mode == Mode.VIEW)
			showFile();
		else if ((mode == Mode.SELECT || mode == Mode.SELECT_UPLOAD) && selectionListener != null) {
			String path = node.getAttribute(Field._id.toString());
			path = path.substring(resPath.length());
			selectionListener.onSelection(path);
		}
	}
	
	private void showFile() {
		TreeNode node = (TreeNode)treeGrid.getSelectedRecord();
		String filePath = node.getAttribute(Field._id.toString());
		DocViewer w = new DocViewer(node.getName(), filePath);
		UI.show(w);
	}
	
	private void editFile() {
		TreeNode node = (TreeNode)treeGrid.getSelectedRecord();
		String filePath = node.getAttribute(Field._id.toString());
		DocEditor w = new DocEditor(node.getName(), filePath, resPath, stylesPath);
		UI.show(w);
	}
	
	private void setDSFields(String root) {
		File.setFields(dataSource,root);
		// in case of file rename, remove the old treenode record
		dataSource.setResponseListener(new ResponseListener() {
			public void onResponse(DSResponse resp, DSRequest req, Object data) {
				if (req.getOperationType() == DSOperationType.FETCH) {
					String file = XMLTools.selectString(data, "//file/@_id");
					if (upload != null && file == null)
						upload.show();
				} else if (req.getOperationType() == DSOperationType.UPDATE) {
					// process rename by deleting old path.  New path will be added
					TreeNode oldnode = (TreeNode)treeGrid.getSelectedRecord();
					if (oldnode != null) 
						treeGrid.removeSelectedData();
				}
			}
		});
	}
}
