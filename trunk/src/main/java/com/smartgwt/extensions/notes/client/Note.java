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

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.extensions.util.client.ds.ProjectDataSource;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class Note extends TreeNode {
	public static enum Field {_id,parent,icon,isFolder,label,comment};
	
	public static ProjectDataSource getFolderDataSource(ProjectDataSource dataSource) {
		ProjectDataSource pds = dataSource.copy();
		pds.setXmlRecordXPath("//notes/folders/folder");
		DataSourceField id = new DataSourceField("_id",FieldType.TEXT,"Id",4,true);
		id.setPrimaryKey(true);
		id.setValueXPath("@_id");
		id.setHidden(true);
		DataSourceField folder = new DataSourceField("folder",FieldType.TEXT,"Initial Folder",50,true);
		folder.setValueXPath(".");
		pds.setFields(id,folder);
		return pds;
	}
	
	public static ProjectDataSource getTypesDataSource(ProjectDataSource dataSource) {
		ProjectDataSource pds = dataSource.copy();
		pds.setXmlRecordXPath("//notes/notetypes/notetype");
		DataSourceField id = new DataSourceField("_id",FieldType.TEXT,"Id",4,true);
		id.setPrimaryKey(true);
		id.setValueXPath("@_id");
		id.setHidden(true);
		DataSourceField name = new DataSourceField("typename",FieldType.TEXT,"Note Type",30,true);
		name.setValueXPath("@name");
		DataSourceField icon = new DataSourceField("icon",FieldType.IMAGE,"Icon",50,true);
		icon.setValueXPath("@icon");
		pds.setFields(id,name,icon);
		return pds;
	}

	public static void setFields(ProjectDataSource dataSource) {
		DataSourceTextField id = new DataSourceTextField(Field._id.toString(), "ID");
		id.setValueXPath("@"+Field._id.toString());
		id.setPrimaryKey(true);
		id.setRequired(true);
		DataSourceTextField parent = new DataSourceTextField(Field.parent.toString(), "Parent");
		parent.setValueXPath("@"+Field.parent.toString());
		parent.setForeignKey(dataSource.getID()+"."+Field._id.toString());
		parent.setRootValue("1");
		parent.setRequired(true);
		DataSourceTextField icon = new DataSourceTextField(Field.icon.toString(), "Icon");
		parent.setValueXPath("@"+Field.icon.toString());
		parent.setRequired(true);
		DataSourceBooleanField isFolder = new DataSourceBooleanField(Field.isFolder.toString(), "isFolder");  
		isFolder.setValueXPath("@"+Field.isFolder.toString());
		isFolder.setRequired(true);
		DataSourceTextField label = new DataSourceTextField(Field.label.toString(), "Notes");  
		label.setRequired(true);
		DataSourceTextField comment = new DataSourceTextField(Field.comment.toString(), "Comment");  
		comment.setRequired(true);
		comment.setDetail(true);
		dataSource.setFields(id,parent,icon,isFolder,label,comment);
	}
	
	public Note(String label, String comment) {
		this(null, "2", null, label, comment, false);
	}
	
	public Note(ListGridRecord rec) {
		this(rec.getAttribute(Field._id.toString()),
			rec.getAttribute(Field.parent.toString()),
			rec.getAttribute(Field.icon.toString()),
			rec.getAttribute(Field.label.toString()),
			rec.getAttribute(Field.comment.toString()),
			rec.getAttributeAsBoolean(Field.isFolder.toString()));
	}
	
	public void update(ListGridRecord rec) {
		rec.setAttribute(Field._id.toString(), getId());
		rec.setAttribute(Field.parent.toString(), getParent());
		rec.setAttribute(Field.icon.toString(), getIcon());
		rec.setAttribute(Field.label.toString(), getLabel());
		rec.setAttribute(Field.comment.toString(), getComment());
		rec.setAttribute(Field.isFolder.toString(), getIsFolder());
	}
	
	public Note(String id, String parent, String icon, String label, String comment, Boolean isFolder) {
		setId(id);
		setParent(parent);
		setIcon(icon);
		setLabel(label);
		setComment(comment);
		setIsFolder(isFolder);
	}

	public String getId() {
		return getAttribute(Field._id.toString());
	}

	public void setId(String id) {
		setAttribute(Field._id.toString(),id);
	}

	public String getParent() {
		return getAttribute(Field.parent.toString());
	}

	public void setParent(String parent) {
		setAttribute(Field.parent.toString(),parent);
	}

	public void setIcon(String icon) {
		setAttribute(Field.icon.toString(),icon);
	}

	public String getIcon() {
		return getAttribute(Field.icon.toString());
	}

	public Boolean getIsFolder() {
		return getAttributeAsBoolean(Field.isFolder.toString());
	}

	public void setIsFolder(Boolean isFolder) {
		setAttribute(Field.isFolder.toString(),isFolder);
	}

	public String getLabel() {
		return getAttribute(Field.label.toString());
	}

	public void setLabel(String label) {
		setAttribute(Field.label.toString(),label);
	}

	public String getComment() {
		return getAttribute(Field.comment.toString());
	}

	public void setComment(String comment) {
		setAttribute(Field.comment.toString(),comment);
	}
}
