package com.smartgwt.extensions.desktop.client;

import com.smartgwt.client.widgets.Window;

public class DesktopWindow extends Window {
    
    public DesktopWindow() {
        //setAnimateMinimize( true);
        setAutoCenter( true);
        setShowMinimizeButton( true);
        setShowMaximizeButton( true);
        setShowCloseButton( true);
        setCanDragResize( true);
        setKeepInParentRect( true);
    }
    
    public String getHeaderIcon() {
        return (String) getAttributeAsMap( "headerIconProperties").get( "src");
    }
    
    public String getShortcutIcon() {
        return getHeaderIcon().replace( "16", "48");
    }
}
