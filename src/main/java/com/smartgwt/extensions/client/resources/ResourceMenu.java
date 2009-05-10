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
package com.smartgwt.extensions.client.resources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.extensions.client.filemanager.File.Field;
import com.smartgwt.extensions.client.htmleditor.DocViewer;
import com.smartgwt.extensions.client.util.ListOrderer;
import com.smartgwt.extensions.client.util.SaveListener;
import com.smartgwt.extensions.client.util.UI;
import com.smartgwt.extensions.client.util.Util;
import com.smartgwt.extensions.client.util.ds.ProjectDataSource;
import com.smartgwt.extensions.client.util.ds.ResponseListener;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class ResourceMenu extends Canvas implements SaveListener, com.smartgwt.client.widgets.form.fields.events.ClickHandler {
	private SectionStack sectionStack;
	private Resource selectedResource;
	private ResourceEditor resEdit;
	private ProjectDataSource dataSource;
	private List<Record> masterList;
	private VLayout layout;
	private String resLib;
	private ListOrderer listOrderer;
	private List<String> labelOrder;
	
	public ResourceMenu(ProjectDataSource dataSource, String resLib) {
		this(dataSource, new Menu(),resLib);
	}
	
	public ResourceMenu(ProjectDataSource dataSource, Menu parentMenu, String resLib)  {
		this.dataSource = dataSource;
		this.resLib = resLib;
		setBackgroundColor(UI.getBackgroundColor());
		setWidth100();
		setHeight("98%");
		setShowEdges(true);
		setEdgeSize(3);
		setEdgeOffset(3);
		layout = UI.getVLayout();
		setBorder("1px solid #CCC");
		setSectionStack();
        layout.setContextMenu(getMenu(parentMenu));
        addChild(layout);
        Resource.setFields(dataSource);
        fetchData();
	}
	
	public void edit() {
		if (resEdit == null) {
			resEdit = new ResourceEditor(resLib);
			resEdit.setSaveListener(this);
		}
		resEdit.setResource(selectedResource);
		UI.showNearClick(resEdit,0,0);
	}
	
	public void onSave() {
		ListGridRecord rec = new ListGridRecord();
		selectedResource.update(rec);
		if (selectedResource.getId().length() > 0) {
			dataSource.setResponseListener(new ResponseListener() {
				public void onResponse(DSResponse resp, DSRequest req, Object data) {
					String id = selectedResource.getId();
					for (Record rec: masterList)
						if (rec.getAttribute(Field._id.toString()).equals(id))
							selectedResource.update(rec);
					updateMenu();
				}
			});
			dataSource.updateData(rec);
		} else {
			dataSource.setResponseListener(new ResponseListener() {
				public void onResponse(DSResponse resp, DSRequest req, Object data) {
					Record[] rec = resp.getData();
					if (rec.length > 0)
						masterList.add(rec[0]);
					updateMenu();
				}
			});
			dataSource.addData(rec);
		}
	}
	
	private void setSectionStack() {
		if (sectionStack != null) {
			layout.removeMember(sectionStack);
			sectionStack.destroy();
		}
		sectionStack = new SectionStack();  
        sectionStack.setWidth100();  
        sectionStack.setHeight100();        
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);  
        layout.addMember(sectionStack);		
	}
	
	private void fetchData() {
		dataSource.setResponseListener(new ResponseListener() {
			public void onResponse(DSResponse resp, DSRequest req, Object data) {
				String order = XMLTools.selectString(data,"//resources/@order");
				if (order != null && order.length() > 0)
					labelOrder = Util.getStringList(order);
				masterList = new ArrayList<Record>();
				for (Record rec: resp.getData())
					masterList.add(rec);
				updateMenu();
			}
		});
		dataSource.fetchData();
	}
	
	public void onClick(ClickEvent e) {
		String url = e.getItem().getAttribute(Resource.Field.url.toString());
		if (!url.startsWith("http"))
			url = "/thinkspace/download"+resLib+url;
		DocViewer viewer = new DocViewer(getTitle(),url);
		UI.showNearClick(viewer,50,0);
	}
	
	private void updateMenu() {
		setSectionStack();
		LinkedHashMap<String,List<Resource>> map = new LinkedHashMap<String,List<Resource>>();
		for (Record rec: masterList) {
			String labels = rec.getAttribute(Resource.Field.labels.toString());
			for (String label: Util.getStringList(labels)) {
				List<Resource> resList = map.get(label);
				if (resList == null) {
					resList = new ArrayList<Resource>();
					map.put(label,resList);
				}
				Resource resource = new Resource(rec);
				resource.addClickHandler(this);
				resList.add(resource);
			}
		}
		if (labelOrder == null)
			labelOrder = new ArrayList<String>();
		// add new labels
	    for (String label: map.keySet())
	    	if (!hasLabel(label))
	    		labelOrder.add(label);
	    // remove old labels
	    for (String label: labelOrder)
	    	if (!map.containsKey(label))
	    		labelOrder.remove(label);
		for (String label: labelOrder) {
			DynamicForm form = addSection(label);
			List<Resource> list = map.get(label);
			Resource[] resArray = new Resource[list.size()];
			list.toArray(resArray);
			form.setFields(resArray);
		}		
	}
	
	private boolean hasLabel(String label) {
    	for (String l: labelOrder)
    		if (label.equalsIgnoreCase(l))
    			return true;
    	return false;
	}
	
	private DynamicForm addSection(String label) {
		SectionStackSection section = new SectionStackSection("HTML Flow");
		section.setTitle(label);
		section.setExpanded(false);  
		section.setCanCollapse(true);
		
		DynamicForm form = new DynamicForm();
		form.setNumCols(1);
		form.setPadding(5);
		section.addItem(form);
		sectionStack.addSection(section);
		return form;
	}
	
	private Menu getMenu(Menu parentMenu) {
		final Menu menu = new Menu();  
		menu.setWidth(50);  
		final MenuItem addRes = new MenuItem("Add Resource","[SKINIMG]/actions/add.png");
		addRes.addClickHandler(new ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				selectedResource = new Resource("","New Title","http://","");
				edit();
			}
		});
		final MenuItem editRes = new MenuItem("Edit Resource","[SKINIMG]/actions/edit.png");
		editRes.addClickHandler(new ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				edit();
			}
		});
		final MenuItem delRes = new MenuItem("Delete Resource","[SKINIMG]/actions/remove.png");
		delRes.addClickHandler(new ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				SC.confirm("Are you sure you want to delete?", new BooleanCallback(){
					public void execute(Boolean value) {
						if (value != null && value) {
							dataSource.setResponseListener(new ResponseListener() {
								public void onResponse(DSResponse resp, DSRequest req, Object data) {
									for (Iterator<Record> it = masterList.iterator(); it.hasNext();) {
										Record rec = it.next();
										if (rec.getAttribute(Field._id.toString()).equals(selectedResource.getId()))
											it.remove();												
										updateMenu();
									}
								}
							});
							dataSource.removeData(selectedResource.getRecord());
						}
					}
				});					
			}
		});
		final MenuItem orderRes = new MenuItem("Configure Resources","[SKINIMG]/actions/configure.png");
		orderRes.addClickHandler(new ClickHandler(){
			public void onClick(MenuItemClickEvent e) {
				if (listOrderer == null)
					listOrderer = new ListOrderer("Labels");
				listOrderer.setList(labelOrder);
				UI.showNearClick(listOrderer, 0, 0);
				listOrderer.setSaveListener(new SaveListener() {
					public void onSave() {
						labelOrder = listOrderer.getList();
						String order = Util.getCSVString(labelOrder);
						dataSource.addParameter("type", "resorder");
						dataSource.addParameter("order", order);
						ListGridRecord rec = new ListGridRecord();
						dataSource.setResponseListener(new ResponseListener(){
							public void onResponse(DSResponse resp, DSRequest req, Object data) {
								dataSource.addParameter("type","content-res");
								updateMenu();
								
							}
						});
						dataSource.updateData(rec);
					}
				});
			}
		});
		MenuItemIfFunction menuFunction = new MenuItemIfFunction() {
			public boolean execute(Canvas canvas, Menu menu, MenuItem item) {
				selectedResource = null;
				Canvas c = EventHandler.getTarget();
				if (c != null && c instanceof DynamicForm) {
					int top = EventHandler.getY()-c.getAbsoluteTop();
					for (FormItem formItem:((DynamicForm)c).getFields()) {
						Resource r = (Resource)formItem;
						if (top > r.getTop()) {
							selectedResource = r;
							break;
						}
					}
				}
				return selectedResource != null;
			}
		};
		
		delRes.setEnableIfCondition(menuFunction);
		editRes.setEnableIfCondition(menuFunction);
        
		menu.setData(addRes, editRes, delRes, orderRes);
		if (parentMenu.getItems().length > 0) {
			menu.addData(new MenuItemSeparator());
		}
		for (MenuItem item : parentMenu.getItems()) {
			menu.addData(item);
		}
		return menu;
	}
}
