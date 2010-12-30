package com.smartgwt.extensions.desktopshow.client;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.extensions.desktop.client.Desktop;
import com.smartgwt.extensions.desktop.client.DesktopWindow;
import com.smartgwt.extensions.desktop.client.Shortcut;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DesktopShowcase implements EntryPoint {
	Desktop desktop;
	public void onModuleLoad() {
		desktop = new Desktop();
		DesktopWindow win1 = new SimpleWindow1();
		DesktopWindow win2 = new SimpleWindow2();
		DesktopWindow win3 = new SimpleWindow3();
		DesktopWindow win4 = new SimpleWindow4();
		desktop.addShortcut( new Shortcut( win1));
		desktop.addShortcut( new Shortcut( win2));
		desktop.addShortcut( new Shortcut( win3));
		desktop.addShortcut( new Shortcut( win4));
		
		ToolStrip quickBar = desktop.getTaskBar().getQuickStartBar();
		quickBar.addButton( createQuickStartButton( win1));
		quickBar.addButton( createQuickStartButton( win2));
		quickBar.addButton( createQuickStartButton( win3));
		quickBar.addButton( createQuickStartButton( win4));
		quickBar.addSeparator();
		TextItem searchItem = new TextItem();
		searchItem.setShowTitle( false);
		searchItem.setIcons( new PickerIcon( PickerIcon.SEARCH));
		quickBar.addFormItem( searchItem);
		quickBar.addSeparator();
		ToolStripButton about = new ToolStripButton();
		about.setIcon( "[SKIN]/headerIcons/help.png");
		quickBar.addButton( about);
		quickBar.addSeparator();
		
		desktop.activeWindow( win1);
		desktop.activeWindow( win2);
		desktop.activeWindow( win3);
	}
	private ToolStripButton createQuickStartButton(final DesktopWindow window) {
        ToolStripButton btn = new ToolStripButton();
        btn.setIcon( window.getHeaderIcon());
        btn.setTooltip( window.getTitle());
        btn.addClickHandler( new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                desktop.activeWindow( window);
            }
        });
        return btn;
	}
}
