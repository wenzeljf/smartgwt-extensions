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

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.extensions.client.util.ds.ProjectDataSource;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class Resource extends LinkItem {
	public static enum Field {_id, title, url, labels };
	
	public static void setFields(ProjectDataSource dataSource) {
		DataSourceTextField id = new DataSourceTextField(Field._id.toString(), "ID");
		id.setValueXPath("@"+Field._id.toString());
		id.setPrimaryKey(true);
		id.setRequired(true);
		DataSourceTextField title = new DataSourceTextField(Field.title.toString(), "Title");
		title.setRequired(true);
		DataSourceTextField url = new DataSourceTextField(Field.url.toString(), "Url");  
		url.setRequired(true);
		DataSourceTextField labels = new DataSourceTextField(Field.labels.toString(), "Labels");  
		labels.setRequired(true);
		dataSource.setFields(id,title,url,labels);
	}
	
	public Resource(Record rec) {
		this(rec.getAttribute(Field._id.toString()),
			rec.getAttribute(Field.title.toString()),
			rec.getAttribute(Field.url.toString()),
			rec.getAttribute(Field.labels.toString()));
	}
	
	public Resource(String id, String title, String url, String labels) {
		setId(id);
		setTitle(title);
		setLinkTitle(title);
		setUrl(url);
		setLabels(labels);
		setTarget("javascript");
		setShowTitle(false);
	}

	public void update(Record rec) {
		rec.setAttribute(Field._id.toString(), getId());
		rec.setAttribute(Field.title.toString(), getTitle());
		rec.setAttribute(Field.url.toString(), getUrl());
		rec.setAttribute(Field.labels.toString(), getLabels());
	}
	
	public Record getRecord() {
		Record rec = new Record();
		update(rec);
		return rec;
	}
	
	public String getId() {
		return getAttribute(Field._id.toString());
	}

	public void setId(String id) {
		setAttribute(Field._id.toString(),id);
	}

	public String getTitle() {
		return getAttribute(Field.title.toString());
	}

	public void setTitle(String title) {
		setAttribute(Field.title.toString(),title);
	}

	public String getUrl() {
		return getAttribute(Field.url.toString());
	}

	public void setUrl(String url) {
		setAttribute(Field.url.toString(),url);
	}

	public String getLabels() {
		return getAttribute(Field.labels.toString());
	}

	public void setLabels(String labels) {
		setAttribute(Field.labels.toString(),labels);
	}
}
