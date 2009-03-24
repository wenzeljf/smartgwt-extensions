package com.smartgwt.extensions.client.eventbus;

/**
 *
 * @author farrukh@wellfleetsoftware.com
 */
public interface TopicSubscriber<T> {
    /**
     * Listener that gets notified of an event that transpires on a topic.
     *
     * @param topic the topic on which event occured
     * @param o the event object
     */
    public void onEvent(String topic, T o);
}
