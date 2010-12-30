package com.smartgwt.extensions.eventbus.client;

/**
 * Default callback that is notified when an event occurs
 * 
 * @author farrukh@wellfleetsoftware.com , mihai.ile@gmail.com
 */
public interface TopicSubscriber<T> {

	/**
	 * Listener that gets notified of an event on a topic.
	 * 
	 * @param subscription
	 *            the subscription that generated the event
	 * @param event
	 *            the event object
	 */
	public void onEvent(Subscription subscription, T event);
}