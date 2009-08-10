package com.smartgwt.extensions.modal.client.example;


import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.extensions.modal.client.ModalWindow;

public class ModalExample implements EntryPoint {

    public void onModuleLoad() {
		
		Canvas preview1 = new Canvas();
		preview1.setShowResizeBar(true);
		preview1.setBorder("1px solid #000");
		preview1.addChild(getDummyControls());
		ModalWindow modalWindow1 = new ModalWindow(preview1);
		modalWindow1.show(true);

		Canvas preview2 = new Canvas();
		preview2.setShowResizeBar(true);
		preview2.setBorder("1px solid #000");
		preview2.addChild(getDummyControls());
		ModalWindow modalWindow2 = new ModalWindow(preview2);
		modalWindow2.show("text only...", false);
		
		Canvas preview3 = new Canvas();
		preview3.setShowResizeBar(true);
		preview3.setBorder("1px solid #000");
		preview3.addChild(getDummyControls());
		ModalWindow modalWindow3 = new ModalWindow(preview3);
		modalWindow3.show("loading...", true);

		HLayout main = new HLayout();
		main.addMember(preview1);
		main.addMember(preview2);
		main.addMember(preview3);
		main.draw();

    }

	private Canvas getDummyControls() {
		DynamicForm form = new DynamicForm();  

		TextItem usernameItem = new TextItem();  
		usernameItem.setTitle("Username");  
		usernameItem.setRequired(true);  
		usernameItem.setDefaultValue("bob");  

		TextItem emailItem = new TextItem();  
		emailItem.setTitle("Email");  
		emailItem.setRequired(true);  
		emailItem.setDefaultValue("bob@isomorphic.com");  

		TextItem passwordItem = new TextItem();  
		passwordItem.setTitle("Password");  
		passwordItem.setRequired(true);  
		passwordItem.setType("password");  

		TextItem password2Item = new TextItem();  
		password2Item.setTitle("Again");  
		password2Item.setRequired(true);  
		password2Item.setType("password");  

		form.setFields(new FormItem[] {usernameItem, emailItem, passwordItem, password2Item});  
		return form;
	}

}
