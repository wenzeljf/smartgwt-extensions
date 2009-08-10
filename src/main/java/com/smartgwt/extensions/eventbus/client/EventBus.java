package com.smartgwt.extensions.eventbus.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A very simple EventBus for loosely coupled event driven coupling between
 * components. There is no buffering and events are lost if no listeners are
 * registered when event is published. Events are relayed to Listeners in the
 * order that they were added.
 * 
 * @author farrukh@wellfleetsoftware.com , mihai.ile@gmail.com
 */
public class EventBus {
	
	static Map<String, List<Subscription>> topicSubscribersMap = new HashMap<String, List<Subscription>>();

	/**
	 * Publish an Object to a topic if the topic exists
	 * 
	 * @param topic
	 *            the topic being published to
	 * @param o
	 *            the event object being published
	 */
	@SuppressWarnings("unchecked")
	public static void publish(String topic, Object o) {
		List<Subscription> topicSubscribers = topicSubscribersMap.get(topic);
		if (topicSubscribers != null) {
			ArrayList<Subscription> tmpList = new ArrayList<Subscription>();
			for (Subscription subscription : topicSubscribers) {
				tmpList.add(subscription);
			}
			try { // do not propagate errors that occurred at the receptors back to senders
				for (Subscription subscriber : tmpList) {
					TopicSubscriber listener = subscriber.getListener();
					listener.onEvent(subscriber, o);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks if a given topic exists on the {@link EventBus} and contains at
	 * least one subscriber
	 * 
	 * @param topic
	 *            the name of the topic on the {@link EventBus} being checked
	 * @return <code>true</code> if topic exists and contains at least one
	 *         subscriber, <code>false</code> otherwise
	 */
	public static boolean topicExists(String topic) {
		List<Subscription> topicSubscribers = topicSubscribersMap.get(topic);
		return topicSubscribers != null && !topicSubscribers.isEmpty();
	}

	/**
	 * Creates a subscription to a given topic, if the topic doesen't exists a
	 * new one will be created
	 * 
	 * @param topic
	 *            the topic for the subscription
	 * @param listener
	 *            the listener to notify when receiving messages
	 * @return a {@link Subscription} that represents this subscription, to be
	 *         used later by {@link #unsubscribe(Subscription)}
	 */
	public static Subscription subscribe(String topic, TopicSubscriber<?> listener) {
		Subscription subscription = new Subscription(topic, listener);
		List<Subscription> topicSubscribers = topicSubscribersMap.get(topic);
		if (topicSubscribers == null) {
			topicSubscribers = new ArrayList<Subscription>();
			topicSubscribersMap.put(topic, topicSubscribers);
		}
		topicSubscribers.add(subscription);
		return subscription;
	}

	/**
	 * Removes a subscription to the topic indicated by it if topic still exists
	 * (if this subscription is the last one in the topic, the topic itself will
	 * be removed from the {@link EventBus})
	 * 
	 * @param subscription
	 *            the subscription to be removed
	 */
	public static void unsubscribe(Subscription subscription) {
		String topic = subscription.getTopic();
		List<Subscription> topicSubscribers = topicSubscribersMap.get(topic);
		if (topicSubscribers != null) {
			topicSubscribers.remove(subscription);
			if (topicSubscribers.isEmpty()) {
				topicSubscribersMap.remove(topic);
			}
		}
	}
}
