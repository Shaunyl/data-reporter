package it.shaunyl.eventbus;

import javax.annotation.Nonnull;

/**
 * *********************************************************************************************************************
 *
 * A listener to receive notifications from an {@link EventBus}.
 *
 * @author Fabrizio Giudici
 * @param <Topic> a listener of topic.
 * @version $Id: EventBusListener.java 4159 2012-12-10 11:31:11Z $
 *
 *********************************************************************************************************************
 */
public interface EventBusListener<Topic> {

    /**
     * *****************************************************************************************************************
     *
     * Notifies the reception of the given event.
     *
     * @param event the event
     *
     *****************************************************************************************************************
     */
    public void notify(@Nonnull Topic event);
}
