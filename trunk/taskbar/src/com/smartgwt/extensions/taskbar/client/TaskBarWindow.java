package com.smartgwt.extensions.taskbar.client;

import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;

/**
 * Generic SmartGWT Window that can be added to a TaskBar 
 * @author Marcello La Rocca (marcellolarocca@gmail.com) 
 *
 */
public abstract class TaskBarWindow extends Window {
	
	private int oldLeft = 0;
	private int oldTop = 0; 
	private int oldWidth = 0;
	private int oldHeight = 0;

	/**
	 * Reference to the Task connected to this Window 
	 */
	private Task task = null;

	/**
	 * Reference to the TaskBar who owns the Window
	 */
	private TaskBar taskBar = null;
	
    private static final int ANIMATION_TIME = 500;
    
//    private static final String WARNING_TEXT_REGISTRATION = "Impossibile aggiungere la finestra";
    
//    private static final String WARNING_TEXT_TASKBAR = "Errore: impossibile ripristinare la finestra";
	
    /**
     * @param title The Window title
     */
	public TaskBarWindow(String title ) {
		final TaskBarWindow thisWindow = this;
		this.setTitle(title);

        this.addMinimizeClickHandler(new MinimizeClickHandler() {
            public void onMinimizeClick(MinimizeClickEvent event) {
            	//Can't just minimize the window: control goes to the connected task
            	event.cancel();
            	task.minimizeTask();
            	minimizeWindow();              
            }
        } );
        
        this.addCloseClickHandler( new CloseClickHandler() {
			
			@Override
			public void onCloseClick(CloseClientEvent event) {
				//Must remove the window from the TaskBar to whom it's registered, if any
				if (taskBar!=null){
					taskBar.removeWindow(thisWindow);			
				}
				thisWindow.destroy();			
			}
		});
		
	}
	
	/**
	 * Steps in when a TaskBarWindow needs to be minimized	
	 */
	public void minimizeWindow(){
        oldLeft = getLeft();
        oldTop = getTop();
        oldWidth = getWidth();
        oldHeight = getHeight();
        Task task = getTask();

        
        this.animateMove(task.getAbsoluteLeft(), task.getAbsoluteTop(), 

        		new AnimationCallback() {
					
					@Override
					public void execute(boolean earlyFinish) {
						//This is crucial because the test in Task.onDoubleClick is on window.getMinimized();
						minimize();
					}
				}
			
        		, ANIMATION_TIME );
        
        this.animateResize(task.getWidth(), task.getHeight());
        this.animateHide(AnimationEffect.FADE, null, ANIMATION_TIME);
	}

	/**
	 * Steps in when a TaskBarWindow needs to be restored
	 */
	public void restoreWindow(){
		//Need to restore because it's been minimized before
		restore();
		this.animateMove( oldLeft, oldTop, null, ANIMATION_TIME );
		this.animateResize(oldWidth, oldHeight );
		this.animateShow(AnimationEffect.FADE, null, ANIMATION_TIME);
	
		this.restore();
	}
	

	/**
	/**
	 * Sets the connections between this Window, the TaskBar which owns it and the Task that minimize/restore the Window 
	 * 
	 * @param ownerTaskBar 	The TaskBar to whom this Window is added
	 * @param connectedTask	The corresponding Task inside the TaskBar
	 */
	public void addToTaskBar(TaskBar ownerTaskBar, Task connectedTask){
		taskBar = ownerTaskBar;
		task = connectedTask;		
	}

	/**
	 * 
	 * @return the Task connected to this Window
	 */
	public Task getTask(){
		return task;
	}

}
