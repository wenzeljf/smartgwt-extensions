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

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.extensions.util.client.ds.ProjectDataSource;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class File {
	public static enum Field {_id,parent,isFolder,name,modified,size,icon};
	
	public static void setFields(ProjectDataSource dataSource, String root) {
		dataSource.setRecordXPath("//file");
 		DataSourceTextField id = new DataSourceTextField(Field._id.toString(), "ID");
		id.setValueXPath("@"+Field._id.toString());
		id.setPrimaryKey(true);
		id.setRequired(true);
		DataSourceTextField parent = new DataSourceTextField(Field.parent.toString(), "Parent");
		parent.setValueXPath("@"+Field.parent.toString());
		parent.setRequired(true);
		parent.setForeignKey(dataSource.getID()+"."+Field._id.toString());
		parent.setRootValue(root);
		DataSourceBooleanField isFolder = new DataSourceBooleanField(Field.isFolder.toString(), "IsFolder");
		DataSourceTextField name = new DataSourceTextField(Field.name.toString(), "Name");
		name.setValueXPath("@name");
		name.setRequired(true);
		DataSourceDateField modified = new DataSourceDateField(Field.modified.toString(), "Modified");
		DataSourceIntegerField size = new DataSourceIntegerField(Field.size.toString(), "Size");
		DataSourceTextField icon = new DataSourceTextField(Field.icon.toString(), "Icon");
		dataSource.setFields(new DataSourceField[]{id,parent,name,isFolder,modified,icon,size});
	}
}
