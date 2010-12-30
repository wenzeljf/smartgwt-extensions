package com.smartgwt.extensions.desktop.client;

import com.smartgwt.client.data.Record;

public class Shortcut extends Record {
    private DesktopWindow window;

    public Shortcut() {
    }

    public Shortcut(DesktopWindow window) {
        setWindow( window);
    }

    public String getTitle() {
        return window.getTitle();
    }

    public String getIcon() {
        return window.getHeaderIcon();
    }

    public void setWindow(DesktopWindow window) {
        this.window = window;
        setAttribute( "title", window.getTitle());
        setAttribute( "icon", window.getShortcutIcon());
    }

    public DesktopWindow getWindow() {
        return window;
    }

}
