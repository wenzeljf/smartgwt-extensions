package com.smartgwt.extensions.taskbar.client;

import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.extensions.taskbar.client.events.Event;

public class AutoHidingTaskBar extends TaskBar {

	public AutoHidingTaskBar(int left, int top, int width, int height) {
		super(left, top, width, height);
		hide();							//Initially hides the taskbar (since it's empty) 
	}
	
	/**
	 * Updates taskbar's visibility according to the last changes occurred
	 */
	@Override
	protected void refreshTaskBar(Event refreshTaskBarEvent) {

		if (!this.isVisible() && !tasks.isEmpty())
			this.animateShow(AnimationEffect.FADE,
					refreshTaskBarEvent.setExternalOnCompletionNotification(),
					mainEventsChain.scaleAnimationTime(ANIMATE_FADE_TIME));
		else if (this.isVisible() && tasks.isEmpty())
			this.animateHide(AnimationEffect.FADE,
					refreshTaskBarEvent.setExternalOnCompletionNotification(),
					mainEventsChain.scaleAnimationTime(ANIMATE_FADE_TIME));

	}

}
