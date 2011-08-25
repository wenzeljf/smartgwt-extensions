package com.smartgwt.extensions.taskbar.client.example;

import com.smartgwt.extensions.taskbar.client.TaskBarWindow;

public class TaskBarWindowFactory {
	private static TaskBarWindowFactory factory = null;
	private static int counter = 0;
	private static final byte WINDOW_TYPES = 3;
	
	private TaskBarWindowFactory(){

	}
	
	public static TaskBarWindowFactory get(){
		if (factory==null)
			factory = new TaskBarWindowFactory();
		
		return factory;
	}

	
	public TaskBarWindow createWindow(){
		byte whichType = (byte) Math.floor( Math.random() * WINDOW_TYPES );
		switch(whichType){
			case 0:
				return new InfoWin("InfoWin # "+ ++counter);
			case 1:
				return new DataWin("DataWin # "+ ++counter);
			case 2:
				return new TrialWin("TrialWin # "+ ++counter);
			default :
				return null;
		}
	}
	
}
