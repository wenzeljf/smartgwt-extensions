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
package com.smartgwt.extensions.client.htmleditor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.extensions.client.util.Util;
import com.smartgwt.extensions.client.util.ds.ProjectDataSource;


/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class Item extends ListGridRecord {
	public static enum Field {_id,name,type,value,judging,hint};
/*	private String _id;
	private String name;
	private String type;
	private String value;
	private int judging;
	private String hint;
*/
	private Map<String,String> map;
	
	public static ProjectDataSource getDataSource() {
		DataSourceField id = new DataSourceField(Field._id.toString(),FieldType.TEXT,"ID");
		id.setValueXPath("@"+Field._id.toString());
		id.setPrimaryKey(true);
		id.setHidden(true);
		id.setRequired(true);
		
		DataSourceField name = new DataSourceField(Field.name.toString(),FieldType.TEXT,"Name");
		name.setValueXPath("@"+Field.name.toString());
		name.setRequired(true);
		
		DataSourceField type = new DataSourceField(Field.type.toString(),FieldType.TEXT,"Type");
		type.setValueXPath("@"+Field.type.toString());
		type.setRequired(true);
		type.setHidden(true);
		
		DataSourceField judging = new DataSourceField(Field.judging.toString(),FieldType.ENUM,"Judging");
		judging.setValueXPath("@"+Field.judging.toString());
		judging.setRequired(true);
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		map.put("0","None");
		map.put("1", "Exact");
		map.put("2", "Ignore case");
		map.put("3", "Regular Expression");
		judging.setValueMap(map);
		
		DataSourceField value = new DataSourceField(Field.value.toString(),FieldType.TEXT,"Correct");
		value.setRequired(true);
		
		DataSourceField hint = new DataSourceField(Field.hint.toString(),FieldType.TEXT,"Hint");
		hint.setRequired(true);
		
		ProjectDataSource ds = new ProjectDataSource("ThinkSpace","ProblemSpace","items.xq");
		ds.setXmlRecordXPath("//item");
		ds.setFields(id,name,type,value,judging,hint);
		return ds;		
	}
	
	public Item() {		
	}
	
	public Item(String id, String name, String type, String value, int judging, String hint) {
		setId(id);
		setName(name);
		setType(type);
		setValue(value);
		setJudging(judging);
		setHint(hint);
	}

	public Item(Record rec) {
		this(rec.getAttribute(Field._id.toString()),
			rec.getAttribute(Field.name.toString()),
			rec.getAttribute(Field.type.toString()),
			rec.getAttribute(Field.value.toString()),
			rec.getAttributeAsInt(Field.judging.toString()),
			rec.getAttribute(Field.hint.toString())
		);
	}
	
	public String getId() {
		return getAttributeAsString(Field._id.toString());
	}

	public void setId(String id) {
		setAttribute(Field._id.toString(),id);
	}

	public String getName() {
		return getAttribute(Field.name.toString());
	}

	public void setName(String name) {
		setAttribute(Field.name.toString(),name);
	}

	public String getType() {
		return getAttribute(Field.type.toString());
	}

	public void setType(String type) {
		setAttribute(Field.type.toString(),type);
	}

	public String getValue() {
		return getAttributeAsString(Field.value.toString());
	}

	public void setValue(String value) {
		setAttribute(Field.value.toString(),value);
	}

	public int getJudging() {
		return getAttributeAsInt(Field.judging.toString());
	}

	public void setJudging(int judging) {
		setAttribute(Field.judging.toString(),judging);
	}

	public String getHint() {
		return getAttributeAsString(Field.hint.toString());
	}
	
	public void setHint(String hint) {
		setAttribute(Field.hint.toString(),hint);
	}
	
	public Map<String,String> getMap() {
		return map;
	}
	
	public void setMap(Map<String,String> map) {
		this.map = map;
	}
	
	public boolean hasMap() {
		return map != null;
	}
	
	public boolean isCorrect(String studentAnswer) {
		int judging = getJudging();
		if (judging == 1) {
			return studentAnswer.equals(getValue());
		} else if (judging == 2)
			return studentAnswer.equalsIgnoreCase(getValue());
		else if (judging == 3)
			return Util.matches(getValue(),studentAnswer);
		else
			return true;
	}
}
