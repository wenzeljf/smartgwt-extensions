package com.smartgwt.extensions.eventbus.client;

/**
 * Class that represents a subscription to a given topic with the given listener
 * 
 * @author mihai.ile@gmail.com
 * 
 */
public class Subscription {

	private final String topic;
	private final TopicSubscriber<?> listener;

	/**
	 * Creates a new subscription reference given the topic and it's listener
	 * 
	 * @param topic
	 *            the topic for the subscription
	 * @param listener
	 *            the listener associated to it
	 */
	protected Subscription(String topic, TopicSubscriber<?> listener) {
		this.topic = topic;
		this.listener = listener;
	}

	/**
	 * Returns the topic for the subscription
	 * 
	 * @return a {@link String} containing the topic for the subscription
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Returns the listener associated with the subscription
	 * 
	 * @return a {@link TopicSubscriber} containing the listener for the
	 *         subscription
	 */
	public TopicSubscriber<?> getListener() {
		return listener;
	}

}
