package com.smartgwt.extensions.showcase.client;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class Showcase implements EntryPoint {

	private static final LinkedHashMap<String, String> EXAMPLES = new LinkedHashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("", "");
			put("containers.ContainersExample/ContainersExample", "Containers");
			//put("eventbus.EventbusExample/EventbusExample", "Eventbus");
			//put("fileuploader.FileUploaderExample/FileUploaderExample", "FileUploader");
			//put("gwtrpcds.GwtRpcDsExample/GwtRpcDsExample", "GwtRpcDs");
			//put("htmleditor.HTMLEditorExample/HTMLEditorExample", "HTMLEditor");
			put("hwc.HWC/HWC", "'Hello World!' Creator");
			put("modal.ModalExample/ModalExample", "Modal Window");
			//put("notes.NotesExample/NotesExample", "Notes");
		}
	};

	public void onModuleLoad() {

		new DynamicForm() {
			{
				setItems(new ComboBoxItem("comboExamples", "Extension") {
					{
						setCanSubmit(false);
						setType("select");
						setValueMap(EXAMPLES);
						setValue("");
						addChangedHandler(new ChangedHandler() {
							public void onChanged(ChangedEvent event) {
								String v = event.getItem().getValue()
										.toString();
								v = "../com.smartgwt.extensions." + v + ".html";
								Window.Location.assign(v);
							}
						});
					}
				});
			}
		}.draw();
	}

}
