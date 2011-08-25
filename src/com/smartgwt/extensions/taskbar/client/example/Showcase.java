package com.smartgwt.extensions.taskbar.client.example;

import com.google.gwt.core.client.EntryPoint;
//import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.extensions.taskbar.client.AutoFittingTaskBar;
import com.smartgwt.extensions.taskbar.client.AutoHidingTaskBar;
import com.smartgwt.extensions.taskbar.client.MultiRowTaskBar;
import com.smartgwt.extensions.taskbar.client.TaskBar;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Showcase implements EntryPoint {

	private static final int taskBarWidth = 640;
	private static final int vstackCanvasesWidth = 680;
	
	public void onModuleLoad() {

		Canvas canvas = new Canvas();
		canvas.setAlign(Alignment.CENTER);

		VStack verticalStack = new VStack();
//		verticalStack.setBorder("1px dashed black");
		
		canvas.addChild(verticalStack);
//Title
		Canvas titleCanvas = new Canvas();
		titleCanvas.setWidth(vstackCanvasesWidth);
		titleCanvas.setHeight(40);

		verticalStack.addMember(titleCanvas);

		
		Label titleLabel = new Label("<b style='color:black; font-size:26px;'>TaskBar Class Showcase</b>");
		titleLabel.setAlign(Alignment.CENTER);
		titleLabel.setWidth100();
		titleLabel.setHeight100();
		titleLabel.setLeft(0);
		titleLabel.setTop(0);
		
		titleCanvas.addChild(titleLabel);
		
//TaskBar		
		
		Canvas taskBarCanvas = new Canvas();
		taskBarCanvas.setWidth(vstackCanvasesWidth);
		taskBarCanvas.setBackgroundColor("#ffe6ba");
		taskBarCanvas.setBorder("1px dashed gray");
		
		final TaskBar taskbar = new TaskBar(10, 10, taskBarWidth, 25);
		taskBarCanvas.addChild(taskbar);
		
		IButton btnAddWindow = new IButton("Add Window");

		Label tbtitleLabel = new Label("<b style='color:red;'>Basic Taskbar</b>");
		taskBarCanvas.addChild(tbtitleLabel);
		tbtitleLabel.setLeft(10);
		tbtitleLabel.setTop(10);
		tbtitleLabel.setHeight(25);
		tbtitleLabel.setWidth(150);
		tbtitleLabel.setBorder("1px dotted red");
		tbtitleLabel.setAlign(Alignment.CENTER);
		
		
		btnAddWindow.setLeft(tbtitleLabel.getRight() + 10);
		btnAddWindow.setTop(tbtitleLabel.getTop());
		
		
		taskBarCanvas.addChild(btnAddWindow);
		
		btnAddWindow.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				taskbar.registerWindow(TaskBarWindowFactory.get().createWindow());
			}
		});
		

		taskbar.setTop(btnAddWindow.getBottom() + 30 );

		
		
		IButton btnDoubleHeight = new IButton("Height*2 ");
		btnDoubleHeight.setTop(30);
		btnDoubleHeight.setLeft(410);
		
		taskBarCanvas.addChild(btnDoubleHeight);

		btnDoubleHeight.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				taskbar.setHeight(2 * taskbar.getHeight() );
			}
		});
		
		IButton btnHalfHeight = new IButton("Height/2");
		btnHalfHeight.setTop(30);
		btnHalfHeight.setLeft(510);
		
		
		taskBarCanvas.addChild(btnHalfHeight);
		
		btnHalfHeight.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				taskbar.setHeight( taskbar.getHeight()/2 );
			}
		});
		
		
		IButton btnDoubleWidth = new IButton("Width*2");
		btnDoubleWidth.setTop(10);
		btnDoubleWidth.setLeft(410);
		
		
		taskBarCanvas.addChild(btnDoubleWidth);
		
		btnDoubleWidth.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				taskbar.setWidth(2 * taskbar.getWidth() );
			}
		});
		
		IButton btnHalfWidth = new IButton("Width/2");
		btnHalfWidth.setTop(10);
		btnHalfWidth.setLeft(510);
		
		
		taskBarCanvas.addChild(btnHalfWidth);
		
		btnHalfWidth.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				taskbar.setWidth( taskbar.getWidth()/2 );
			}
		});				
		
		
		
		verticalStack.addMember(taskBarCanvas);

//HIDING TASKBAR
		Canvas ahtaskBarCanvas = new Canvas();
		ahtaskBarCanvas.setWidth(vstackCanvasesWidth);
		ahtaskBarCanvas.setBackgroundColor("#fffeba");
		ahtaskBarCanvas.setBorder("1px dashed gray");

		final AutoHidingTaskBar ahtaskbar = new AutoHidingTaskBar(10, 10, taskBarWidth, 40);
		ahtaskBarCanvas.addChild(ahtaskbar);
		
		IButton ahbtnAddWindow = new IButton("Add Window");

		Label ahtitleLabel = new Label("<b style='color:red;'>Auto Hiding Taskbar</b>");
		ahtaskBarCanvas.addChild(ahtitleLabel);
		ahtitleLabel.setLeft(10);
		ahtitleLabel.setTop(10);
		ahtitleLabel.setHeight(25);
		ahtitleLabel.setWidth(150);
		ahtitleLabel.setBorder("1px dotted red");
		ahtitleLabel.setAlign(Alignment.CENTER);

		
		ahbtnAddWindow.setLeft(ahtitleLabel.getRight() + 10);
		ahbtnAddWindow.setTop(ahtitleLabel.getTop());
		
		
		ahtaskBarCanvas.addChild(ahbtnAddWindow);
		
		ahbtnAddWindow.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ahtaskbar.registerWindow(TaskBarWindowFactory.get().createWindow());
			}
		});
		
		ahtaskbar.setTop(ahbtnAddWindow.getBottom() + 20 );
	

		
		
		IButton ahbtnDoubleHeight = new IButton("Height*2 ");
		ahbtnDoubleHeight.setTop(30);
		ahbtnDoubleHeight.setLeft(410);
		
		ahtaskBarCanvas.addChild(ahbtnDoubleHeight);

		ahbtnDoubleHeight.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ahtaskbar.setHeight(2 * ahtaskbar.getHeight() );
			}
		});
		
		IButton ahbtnHalfHeight = new IButton("Height/2");
		ahbtnHalfHeight.setTop(30);
		ahbtnHalfHeight.setLeft(510);
		
		
		ahtaskBarCanvas.addChild(ahbtnHalfHeight);
		
		ahbtnHalfHeight.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ahtaskbar.setHeight( ahtaskbar.getHeight()/2 );
			}
		});
		
		
		IButton ahbtnDoubleWidth = new IButton("Width*2");
		ahbtnDoubleWidth.setTop(10);
		ahbtnDoubleWidth.setLeft(410);
		
		
		ahtaskBarCanvas.addChild(ahbtnDoubleWidth);
		
		ahbtnDoubleWidth.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ahtaskbar.setWidth(2 * ahtaskbar.getWidth() );
			}
		});
		
		IButton ahbtnHalfWidth = new IButton("Width/2");
		ahbtnHalfWidth.setTop(10);
		ahbtnHalfWidth.setLeft(510);
		
		
		ahtaskBarCanvas.addChild(ahbtnHalfWidth);
		
		ahbtnHalfWidth.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ahtaskbar.setWidth( ahtaskbar.getWidth()/2 );
			}
		});				
		
			
		
		
		
		
		verticalStack.addMember(ahtaskBarCanvas);
		
		
//MULTIROW TASKBAR		
		Canvas mrtaskBarCanvas = new Canvas();
		mrtaskBarCanvas.setWidth(vstackCanvasesWidth);
		mrtaskBarCanvas.setBackgroundColor("#ffe6ba");
		mrtaskBarCanvas.setBorder("1px dashed gray");

		final MultiRowTaskBar mrtaskbar = new MultiRowTaskBar(10, 10, taskBarWidth, 25);
		mrtaskBarCanvas.addChild(mrtaskbar);
		
		IButton mrbtnAddWindow = new IButton("Add Window");

		Label mrtitleLabel = new Label("<b style='color:red;'>MultiRow Taskbar</b>");
		mrtaskBarCanvas.addChild(mrtitleLabel);
		mrtitleLabel.setLeft(10);
		mrtitleLabel.setTop(10);
		mrtitleLabel.setHeight(25);
		mrtitleLabel.setWidth(150);
		mrtitleLabel.setBorder("1px dotted red");
		mrtitleLabel.setAlign(Alignment.CENTER);

		
		mrbtnAddWindow.setLeft(mrtitleLabel.getRight() + 10);
		mrbtnAddWindow.setTop(mrtitleLabel.getTop());
		
		
		mrtaskBarCanvas.addChild(mrbtnAddWindow);
		
		mrbtnAddWindow.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				mrtaskbar.registerWindow(TaskBarWindowFactory.get().createWindow());
			}
		});
		

		
		

		IButton mrbtnDoubleWidth = new IButton("Width*2");
		mrbtnDoubleWidth.setTop(10);
		mrbtnDoubleWidth.setLeft(410);
		
		
		mrtaskBarCanvas.addChild(mrbtnDoubleWidth);
		
		mrbtnDoubleWidth.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				mrtaskbar.setWidth(2 * mrtaskbar.getWidth() );
			}
		});
		
		IButton mrbtnHalfWidth = new IButton("Width/2");
		mrbtnHalfWidth.setTop(10);
		mrbtnHalfWidth.setLeft(510);
		
		
		mrtaskBarCanvas.addChild(mrbtnHalfWidth);
		
		mrbtnHalfWidth.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				mrtaskbar.setWidth( mrtaskbar.getWidth()/2 );
			}
		});		

		
		final Slider mrtasksPerRowSlider = new Slider("<b style='color:red'>Tasks per row</b>");  
		mrtasksPerRowSlider.setVertical(false);
		mrtasksPerRowSlider.setLeft( mrbtnDoubleWidth.getLeft() );
		mrtasksPerRowSlider.setTop(mrbtnDoubleWidth.getBottom() );
		mrtasksPerRowSlider.setWidth(mrbtnDoubleWidth.getWidth() + mrbtnHalfWidth.getWidth());
		
		mrtasksPerRowSlider.setMinValue(1);
		mrtasksPerRowSlider.setMaxValue(20);
		mrtasksPerRowSlider.setNumValues(20);
		mrtasksPerRowSlider.setValue(mrtaskbar.getTasksPerRow());
		
		
		mrtaskBarCanvas.addChild(mrtasksPerRowSlider);
		
		mrtasksPerRowSlider.addValueChangedHandler( new ValueChangedHandler() {
			
			@Override
			public void onValueChanged(ValueChangedEvent event) {
				mrtaskbar.setTasksPerRow(new Byte( (byte)mrtasksPerRowSlider.getValue() ) );
			}
		});
		
		mrtaskbar.setTop(mrtasksPerRowSlider.getBottom());
		
		mrtaskBarCanvas.setHeight( taskBarCanvas.getHeight() + (mrtasksPerRowSlider.getHeight() - mrbtnDoubleWidth.getHeight() )  );
		
		verticalStack.addMember(mrtaskBarCanvas);
		
//AUTOFITTING TASKBAR		
		Canvas aftaskBarCanvas = new Canvas();
		aftaskBarCanvas.setWidth(vstackCanvasesWidth);
		
		aftaskBarCanvas.setBackgroundColor("#fffeba");
		aftaskBarCanvas.setBorder("1px dashed gray");
		
		final AutoFittingTaskBar aftaskbar = new AutoFittingTaskBar(10, 10, taskBarWidth, 20);
		aftaskBarCanvas.addChild(aftaskbar);
		
		IButton afbtnAddWindow = new IButton("Add Window to AF");

		Label aftitleLabel = new Label("<b style='color:red;'>Auto Fitting Taskbar</b>");
		aftaskBarCanvas.addChild(aftitleLabel);
		aftitleLabel.setLeft(10);
		aftitleLabel.setTop(10);
		aftitleLabel.setHeight(25);
		aftitleLabel.setWidth(150);
		aftitleLabel.setBorder("1px dotted red");
		aftitleLabel.setAlign(Alignment.CENTER);

		
		
		afbtnAddWindow.setLeft(aftitleLabel.getRight() + 10 );
		afbtnAddWindow.setTop(aftitleLabel.getTop());
		
		
		aftaskBarCanvas.addChild(afbtnAddWindow);
		
		afbtnAddWindow.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				aftaskbar.registerWindow(TaskBarWindowFactory.get().createWindow());
			}
		});
		
		aftaskbar.setTop(afbtnAddWindow.getBottom() + 20 );


		
		
		IButton afbtnDoubleHeight = new IButton("Height*2");
		afbtnDoubleHeight.setTop(10);
		afbtnDoubleHeight.setLeft(410);
		
		aftaskBarCanvas.addChild(afbtnDoubleHeight);

		afbtnDoubleHeight.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				aftaskbar.setHeight(2 * aftaskbar.getHeight() );
			}
		});
		
		IButton afbtnHalfHeight = new IButton("Height/2");
		afbtnHalfHeight.setTop(10);
		afbtnHalfHeight.setLeft(510);
		
		
		aftaskBarCanvas.addChild(afbtnHalfHeight);
		
		afbtnHalfHeight.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				aftaskbar.setHeight( aftaskbar.getHeight()/2 );
			}
		});

		
		
		
		verticalStack.addMember(aftaskBarCanvas);
		
//		RootPanel.get().add(canvas);
		canvas.draw();
		
		
		
		
	}
}
