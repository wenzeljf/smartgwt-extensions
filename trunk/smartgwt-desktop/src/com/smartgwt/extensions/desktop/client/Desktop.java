package com.smartgwt.extensions.desktop.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Orientation;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

/**
 * A desktop represents a desktop like application which contains a task bar,
 * start menu, and shortcuts.
 * 
 * <p/>
 * Rather than adding content directly to the root panel, content should be
 * wrapped in windows. Windows can be opened via shortcuts and the start menu.
 */
public class Desktop {

    protected TaskBar taskBar = new TaskBar(this);
    protected WindowListener listener;
    protected VLayout viewport;
    protected TileGrid desktop;
    protected DesktopWindow activeWindow;
    protected List<Shortcut> shortcuts;
    protected List<DesktopWindow> windows;

    public Desktop() {
        shortcuts = new ArrayList<Shortcut>();
        windows = new ArrayList<DesktopWindow>();

        listener = new WindowListener(this);

        viewport = new VLayout(0);
        viewport.setMargin(0);
        viewport.setWidth100();
        viewport.setHeight100();
        viewport.setBackgroundColor( "transparent");

        desktop = new TileGrid();
        desktop.setShowEdges(false);
        DetailViewerField title = new DetailViewerField("title");
        DetailViewerField icon = new DetailViewerField("icon");
        icon.setType("image");
        icon.setImageSize(48);
        desktop.setFields(icon, title);
        desktop.setTileHeight(70);
        desktop.setTileWidth(60);

        desktop.setOrientation(Orientation.VERTICAL);
        desktop.setHeight("*");
        desktop.addRecordClickHandler(new RecordClickHandler() {
            @Override
            public void onRecordClick(RecordClickEvent event) {
                // open window;
                final Record record = event.getRecord();
                if (record == null) return;
                DesktopWindow window = ((Shortcut) record).getWindow();

                activeWindow(window);
            }
        });

        viewport.addMember(desktop);
        viewport.addMember(taskBar);

        final RootPanel rootPanel = RootPanel.get();
        rootPanel.setHeight("100%");
        rootPanel.add(viewport, -1, -1);
    }

    public void activeWindow(DesktopWindow win) {
        if (win == null)
            return;
        if (!getWindows().contains(win)) {
            addWindow(win);
        }

        if (activeWindow == win)
            return; // do nothing.
        if (activeWindow != null) {
            addPreviousActiveWindow(activeWindow);
        }
        activeWindow = win;
        if (win.isVisible())
            win.bringToFront();
        else
            win.show();
        taskBar.markActive(win);
    }

    Stack<DesktopWindow> activeWindows = new Stack<DesktopWindow>();

    private void addPreviousActiveWindow(DesktopWindow win) {
        while (true) { // remove all
            if (!activeWindows.remove(win))
                break;
        }
        activeWindows.push(win);
    }

    public DesktopWindow getPreviousActiveWindow(DesktopWindow win) {
        while (true) { // remove all
            if (!activeWindows.remove(win))
                break;
        }
        if (activeWindows.size() == 0)
            return null;
        return activeWindows.pop();
    }

    public void hideWindow(DesktopWindow win) {
        if (win == activeWindow)
            activeWindow = null;
        win.hide();
        taskBar.markDeactive(win);
        // active previous window.
        activeWindow(getPreviousActiveWindow(win));
    }

    /**
     * Adds a shortcut to the desktop.
     * 
     * @param shortcut
     *            the shortcut to add
     */
    public void addShortcut(Shortcut shortcut) {
        shortcuts.add(shortcut);
        RecordList recordList = desktop.getRecordList();
        if (recordList == null) {
            recordList = new RecordList();
            desktop.setData(recordList);
        }
        recordList.add(shortcut);
    }

    /**
     * Adds a window to the desktop.
     * 
     * @param window
     *            the window to add
     */
    public void addWindow(DesktopWindow window) {
        if (windows.add(window)) {
            viewport.addChild(window);
            window.addCloseClickHandler(listener);
            window.addMinimizeClickHandler(listener);
            window.addMouseDownHandler(listener);
            taskBar.addTaskButton(window);
        }
    }

    /**
     * Returns the container of the "desktop", which is the viewport minus the
     * task bar.
     * 
     * @return the desktop layout container
     */
    public TileGrid getDesktop() {
        return desktop;
    }

    /**
     * Returns a list of the desktop's shortcuts.
     * 
     * @return the shortcuts
     */
    public List<Shortcut> getShortcuts() {
        return shortcuts;
    }

    /**
     * Returns the desktop's task bar.
     * 
     * @return the task bar
     */
    public TaskBar getTaskBar() {
        return taskBar;
    }

    /**
     * Returns a list of the desktop's windows.
     * 
     * @return the windows
     */
    public List<DesktopWindow> getWindows() {
        return windows;
    }

    /**
     * Removes a shortcut from the desktop.
     * 
     * @param shortcut
     *            the shortcut to remove
     */
    public void removeShortcut(Shortcut shortcut) {
        shortcuts.remove(shortcut);
        desktop.getRecordList().remove(shortcut);
    }

    /**
     * Removes a window from the desktop.
     * 
     * @param window
     *            the window to remove
     */
    public void removeWindow(DesktopWindow window) {
        hideWindow(window);
        if (windows.remove(window)) {
            taskBar.removeTaskButton(window.getID());
        }
    }
}
