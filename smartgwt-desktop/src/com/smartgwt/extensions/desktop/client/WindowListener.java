package com.smartgwt.extensions.desktop.client;

import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.events.MouseDownEvent;
import com.smartgwt.client.widgets.events.MouseDownHandler;

public class WindowListener implements CloseClickHandler, MinimizeClickHandler, MouseDownHandler {
    private Desktop desktop;
    public WindowListener(Desktop desktop) {
        this.desktop = desktop;
    }

    @Override
    public void onMinimizeClick(MinimizeClickEvent event) {
        DesktopWindow window = (DesktopWindow) event.getSource();
        desktop.hideWindow( window);
        event.cancel();
    }

    @Override
    public void onCloseClick(CloseClientEvent event) {
        DesktopWindow window = (DesktopWindow) event.getSource();
        desktop.removeWindow( window);
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        DesktopWindow window = (DesktopWindow) event.getSource();
        desktop.activeWindow( window);
    }
}
