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

import java.util.LinkedHashMap;
import java.util.Map;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.extensions.htmleditor.client.HTMLEditor;
import com.smartgwt.extensions.util.client.SaveListener;
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
public class NoteEditor extends Window {
	private Note note;
	private HTMLEditor editor;
	private SelectItem type;
	private TextAreaItem comment;
	private SaveListener saveListener;
	private ProjectDataSource typeDS;
	private Map<String,String> typeMap;
	private String resPath;

	public NoteEditor(final String resPath, String stylesPath) {
		this.resPath = resPath;
		setTitle("Note Editor");
		setIsModal(true);
		setWidth(600);
		setHeight(500);
		setCanDragResize(true);
		editor = new HTMLEditor("500","250",resPath,stylesPath);
		editor.setToolBar("Basic");
		final DynamicForm form = UI.getForm(3);
		form.setCellPadding(4);
		form.setHeight(100);
		CanvasItem label = new CanvasItem("note");
		label.setTitle("Note");
		label.setCanvas(editor);
		label.setWidth(300);
		label.setHeight(100);
		type = new SelectItem("type");
		type.setTitle("Type");
		type.setVisible(true);
		type.setDefaultToFirstOption(true);
		type.setImageURLPrefix("/thinkspace/download"+resPath);
		comment = new TextAreaItem("comment");
		comment.setTitle("comment");
		comment.setWidth(500);
		comment.setHeight(100);
		comment.setDefaultValue("");
		form.setFields(label,type,comment);
		IButton save = new IButton("Save");
		final NoteEditor notesEditor = this;
		save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(ClickEvent e) {
				if (form.validate()) {
					String text = editor.getHTML();
					note.setLabel(text);
					note.setComment(comment.getDisplayValue());
					if (type.getVisible()) {
						String typeString = type.getDisplayValue();
						note.setIcon(typeMap.get(typeString));
					} else {
						note.setIcon(null);
					}
					if (saveListener != null)
						saveListener.onSave();
					UI.hide(notesEditor);
				}
			}
		});
		VStack stack = UI.getVStack();
		stack.setBackgroundColor(UI.getBackgroundColor());
		stack.setDefaultLayoutAlign(Alignment.CENTER);
		stack.setPadding(5);
		stack.addMember(form);
		stack.addMember(save);
		addItem(stack);
	}

	public Note getNote() {
		return note;
	}

	public void refreshTypes(ProjectDataSource ds) {
		typeDS = Note.getTypesDataSource(ds);
		typeDS.setResponseListener(new ResponseListener() {
			public void onResponse(DSResponse resp, DSRequest req, Object data) {
				LinkedHashMap<String, String> types = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> icons = new LinkedHashMap<String, String>();
				Record[] recs = resp.getData();
				for (int i = 0; i < recs.length; i++) {
					String key = recs[i].getAttribute("typename".toString());
					String icon = recs[i].getAttribute("icon".toString());
					types.put(key, key);
					icons.put(icon, icon);
				}
				type.setValueMap(types);
				type.setValueIcons(icons);
				if (recs.length == 0)
					type.hide();
				else
					type.show();
			}
		});
		typeDS.fetchData();
	}

	public void setNote(Note note) {
		this.note = note;
		String text = note.getLabel();
		editor.setHTML(Util.convertToRelative(text,"/thinkspace/download"+resPath));
		editor.edit();
		comment.setDefaultValue(note.getComment());
	}

	public SaveListener getSaveListener() {
		return saveListener;
	}

	public void setSaveListener(SaveListener saveListener) {
		this.saveListener = saveListener;
	}
}
