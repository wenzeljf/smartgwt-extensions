package com.smartgwt.ext.client.eventbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A very simple EventBus for loosely coupled event driven coupling between components.
 * Currently, there is no event buffering and events are lost if no listeneres are registered when event is published.
 * Events are relayed serially to Listener in order that they were added.
 *
 * @author farrukh@wellfleetsoftware.com
 */
public class EventBus {
    static Map<String, List<TopicSubscriber>> topicSubscribersMap = new HashMap<String, List<TopicSubscriber>>();

    /**
     * Publish an Object to a topic.
     *
     * @param topic the topic being published to
     * @param o the event object being published
     */
    public static void publish(String topic, Object o) {
        List<TopicSubscriber> topicSubscribers = topicSubscribersMap.get(topic);
        if (topicSubscribers != null) {
            for (TopicSubscriber topicSubscriber : topicSubscribers) {
                topicSubscriber.onEvent(topic, o);
            }
        }
    }

    /**
     * Subscribe to a previously created topic.
     *
     * @param topic
     * @param listener
     */
    public static void subscribe(String topic, TopicSubscriber listener) {
        List<TopicSubscriber> topicListeners = topicSubscribersMap.get(topic);
        if (topicListeners == null) {
            topicListeners = new ArrayList<TopicSubscriber>();
            topicSubscribersMap.put(topic, topicListeners);
        }

        topicListeners.add(listener);
    }
}
