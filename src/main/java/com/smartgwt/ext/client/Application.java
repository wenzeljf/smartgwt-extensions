package com.smartgwt.ext.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.KeyCallback;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Application implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
        KeyIdentifier debugKey = new KeyIdentifier();
        debugKey.setCtrlKey(true);
        debugKey.setKeyName("D");

        Page.registerKey(debugKey, new KeyCallback() {
            public void execute(String keyName) {
                SC.showConsole();
            }
        });

        VLayout vLayout = new VLayout(5);
        vLayout.setWidth100();
        DynamicForm form = new DynamicForm();

        form = new DynamicForm();
        form.setNumCols(2);

        DateItem dateItem1 = new DateItem();
        dateItem1.setName("dateItem1");
        dateItem1.setTitle("Date 1");
        dateItem1.setRequired(true);
        dateItem1.setTitleOrientation(TitleOrientation.TOP);
        dateItem1.setWidth("*");
        dateItem1.setColSpan(2);
        dateItem1.setUseTextField(true);
        dateItem1.setTextAlign(Alignment.LEFT);
        dateItem1.setDisplayFormat(DateDisplayFormat.TOLOCALESTRING);

        DateItem dateItem2 = new DateItem();
        dateItem2.setName("dateItem2");
        dateItem2.setTitle("Date 2");
        dateItem2.setRequired(true);
        dateItem2.setTitleOrientation(TitleOrientation.TOP);
        dateItem2.setWidth("*");
        dateItem2.setColSpan(2);
        dateItem2.setUseTextField(true);
        dateItem2.setTextAlign(Alignment.LEFT);
        dateItem2.setDisplayFormat(DateDisplayFormat.TOLOCALESTRING);

        form.setFields(new FormItem[]{dateItem1, dateItem2});

        vLayout.addMember(form);
        
        RootPanel.get().add(vLayout);
	}    
}
