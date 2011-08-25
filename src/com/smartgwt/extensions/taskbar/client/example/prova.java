package com.smartgwt.extensions.taskbar.client.example;

import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.extensions.taskbar.client.events.Event;
import com.smartgwt.extensions.taskbar.client.events.EventChain;

public class prova {
	EventChain ec = new EventChain();

	
	public prova(){
		Event e1 = ec.addNewEvent(  );
		e1.setEventBody(new AnimationCallback() {
			
			@Override
			public void execute(boolean earlyFinish) {
				alert("Output 1");
			}
		});
		
		Event e2 = ec.addNewEvent(  );
		e2.setEventBody(new AnimationCallback() {
			
			@Override
			public void execute(boolean earlyFinish) {
				alert("Output 2");
			}
		});
		
		Event e3 = ec.addNewEvent(  );
		e3.setEventBody(new AnimationCallback() {
			
			@Override
			public void execute(boolean earlyFinish) {
				alert("Output 3");
			}
		});
		
//		ec.print();
		ec.execute(false);

	}
	 
	public static native void alert(String msg) /*-{
	  	alert(msg);
	 }-*/;
	
	
	

}
