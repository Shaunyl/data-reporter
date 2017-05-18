package it.shaunyl.eventbus;

import javax.annotation.Nonnull;

/**
 * *********************************************************************************************************************
 *
 * A simple event bus for a local publish/subscribe facility.
 *
 * @author Fabrizio Giudici
 * @version $Id: EventBus.java 4159 2012-12-10 11:31:11Z $
 *
 *********************************************************************************************************************
 */
public interface EventBus {

    /**
     * *****************************************************************************************************************
     *
     * Publishes the given event. The topic is the class of the event.
     *
     * @param event the event
     * @param <Topic> topic event.
     *
     *****************************************************************************************************************
     */
    public <Topic> void publish(@Nonnull Topic event);

    /**
     * *****************************************************************************************************************
     *
     * Publishes the given event and topic. Passing an explicit topic can be
     * useful when dealing with a hierarchy of events (so, perhaps a subclass is
     * passed but the topic is the root of the hierarchy).
     *
     * @param topic the topic
     * @param event the event
     * @param <Topic> topic event.
     *
     *****************************************************************************************************************
     */
    public <Topic> void publish(@Nonnull Class<Topic> topic, @Nonnull Topic event);

    /**
     * *****************************************************************************************************************
     *
     * Subscribes an {@link EventBusListener} to a topic.
     *
     * @param topic the topic
     * @param listener the listener
     * @param <Topic> topic event.
     *
     *****************************************************************************************************************
     */
    public <Topic> void subscribe(@Nonnull Class<Topic> topic, @Nonnull EventBusListener<Topic> listener);

    /**
     * Un-subscribe the bus to some listeners.
     * @param listener list of listeners to which the bus is un-subscribed to.
     */
    public void unsubscribe(EventBusListener<?> listener);
}
