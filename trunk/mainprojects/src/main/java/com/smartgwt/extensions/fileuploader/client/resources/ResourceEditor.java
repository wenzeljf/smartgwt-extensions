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
package com.smartgwt.extensions.fileuploader.client.resources;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.extensions.fileuploader.client.filemanager.FileSelector;
import com.smartgwt.extensions.fileuploader.client.filemanager.Library;
import com.smartgwt.extensions.util.client.SaveListener;
import com.smartgwt.extensions.util.client.SelectionListener;
import com.smartgwt.extensions.util.client.UI;
import com.smartgwt.extensions.util.client.ds.ProjectDataSource;

/**
 * 
 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class ResourceEditor extends Window {
	private Resource resource;
	private TextItem title;
	private TextItem url;
	private TextItem labels;
	private SaveListener listener;

	public ResourceEditor(final String libPath) {
		setTitle("Configure Resources");
		setWidth(400);
		setHeight(200);
		setIsModal(true);
		setCanDragResize(true);
		Label label = new Label("Enter a resource title, url or filename, and the labels you want to list the resource under.");
		label.setHeight(20);
		label.setWrap(true);
		final DynamicForm form = UI.getForm(3);
		form.setHeight(100);
		LengthRangeValidator valLen = new LengthRangeValidator();
		valLen.setClientOnly(true);
		valLen.setMin(1);
		valLen.setMax(100);
		valLen.setErrorMessage("Please enter a value");

		title = new TextItem("title");
		title.setTitle("Title");
		title.setValidators(valLen);

		url = new TextItem("url");
		url.setTitle("Url");
		url.setDefaultValue("http://");
		url.setValidators(valLen);
		url.setStartRow(true);
		labels = new TextItem("labels");
		labels.setTitle("Labels");
		labels.setDefaultValue("");
		labels.setValidators(valLen);
		labels.setStartRow(true);
		ButtonItem files = new ButtonItem();
		files.setShowTitle(false);
		files.setIcon("[SKINIMG]/FileBrowser/file.png");
		files.setTitle("File");
		files.setStartRow(false);
		form.setFields(title,url,files,labels);
		files.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent e) {
				ProjectDataSource pds = new ProjectDataSource("ThinkSpace","ThinkSpace","library.xq");
				final FileSelector selector = new FileSelector(pds,libPath,Library.Mode.SELECT_UPLOAD);
				selector.setSelectionListener(new SelectionListener() {
					public void onSelection(String path) {
						url.setValue(path);
						UI.hide(selector);
					}
				});
				UI.showNearClick(selector,0,0);
			}
		});
		IButton save = new IButton("Save");
		final ResourceEditor menu = this;
		save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(ClickEvent e) {
				if (form.validate()) {
					String stitle = title.getDisplayValue();
					String surl = url.getDisplayValue();
					String slabels = labels.getDisplayValue();
					resource.setTitle(stitle);
					resource.setUrl(surl);
					resource.setLabels(slabels);
					if (listener != null)
						listener.onSave();
					UI.hide(menu);
				}
			}
		});
		VStack stack = new VStack();
		stack.setBackgroundColor(UI.getBackgroundColor());
		stack.setDefaultLayoutAlign(Alignment.CENTER);
		stack.setPadding(5);
		stack.addMember(label);
		stack.addMember(form);
		stack.addMember(save);
		addItem(stack);
	}

	public void setSaveListener(SaveListener listener) {
		this.listener = listener;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
		title.setValue(resource.getTitle());
		url.setValue(resource.getUrl());
		labels.setValue(resource.getLabels());
	}
}
