package com.smartgwt.extensions.desktop.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Displays the start menu button followed by a list of open windows.
 */
public class TaskBar extends HLayout {

    protected TasksButtonsPanel tbPanel;
    private Desktop desktop;
    private ToolStrip quickStartBar = new ToolStrip();

    public TaskBar(Desktop desktop) {
        this.desktop = desktop;
        tbPanel = new TasksButtonsPanel();
        setHeight(30);
        addMember(tbPanel);
        tbPanel.setStyleName( "ToolStrip x-task-button-panel");
        quickStartBar.setAlign( Alignment.RIGHT);
        quickStartBar.addSeparator();
        addMember(quickStartBar);
        quickStartBar.setStyleName( "ToolStrip x-quickstart-bar");
    }

    public ToolStrip getQuickStartBar() {
        return quickStartBar;
    }

    /**
     * Adds a button.
     * 
     * @param win
     *            the window
     * @return the new task button
     */
    public TaskButton addTaskButton(DesktopWindow win) {
        return tbPanel.addButton(win);
    }

    /**
     * Returns the bar's buttons.
     * 
     * @return the buttons
     */
    public List<TaskButton> getButtons() {
        return tbPanel.getItems();
    }

    /**
     * Removes a button.
     * 
     * @param id
     *            the button to remove
     */
    public void removeTaskButton(String id) {
        tbPanel.removeButton(id);
    }

    /**
     * Sets the active button.
     * 
     * @param btn
     *            the button
     */
    public void setActiveButton(TaskButton btn) {
        tbPanel.setActiveButton(btn);
    }

    class TaskButton extends ToolStripButton implements ClickHandler {

        private DesktopWindow win;

        TaskButton(DesktopWindow win) {
            this.win = win;
            setActionType(SelectionType.RADIO);
            setRadioGroup("TASK_BUTTON");
            setTitle(win.getTitle());
            setIcon(win.getHeaderIcon());
            addClickHandler(this);
        }

        @Override
        public void onClick(ClickEvent event) {
            if (win.isVisible() && win == desktop.activeWindow) {
                desktop.hideWindow(win);
            } else {
                desktop.activeWindow(win);
            }
        }
    }

    class TasksButtonsPanel extends ToolStrip {
        private Map<String, TaskButton> items;

        TasksButtonsPanel() {
            items = new HashMap<String, TaskButton>();
            setWidth("100%");
        }

        public TaskButton addButton(DesktopWindow win) {
            TaskButton btn = new TaskButton(win);

            items.put(win.getID(), btn);
            addButton(btn);
            setActiveButton(btn);
            return btn;
        }

        public List<TaskButton> getItems() {
            return new ArrayList<TaskButton>(items.values());
        }

        public void removeButton(String wid) {
            removeMember(items.get(wid));
            items.remove(wid);
        }

        public void setActiveButton(TaskButton btn) {
            btn.setSelected(true);
        }
    }

    public void markActive(DesktopWindow win) {
        tbPanel.items.get(win.getID()).setSelected(true);
    }

    public void markDeactive(DesktopWindow win) {
        TaskButton taskButton = tbPanel.items.get(win.getID());
        if ( taskButton == null) return;
		taskButton.setSelected(false);
    }
}
