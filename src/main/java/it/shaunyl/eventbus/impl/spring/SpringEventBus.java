package it.shaunyl.eventbus.impl.spring;

import it.shaunyl.eventbus.EventBus;
import it.shaunyl.eventbus.EventBusListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;

/**
 * *********************************************************************************************************************
 *
 * A simple implementation of {@link EventBus} based on Spring.
 *
 * @author Fabrizio Giudici
 * @version $Id: SpringEventBus.java 4159 2012-12-10 11:31:11Z $
 *
 *********************************************************************************************************************
 */
@ThreadSafe
@Slf4j
public class SpringEventBus implements EventBus {

    private final Map<Class<?>, List<WeakReference<EventBusListener<?>>>> listenersMapByTopic = new HashMap<Class<?>, List<WeakReference<EventBusListener<?>>>>();
    @Getter @Setter
    private TaskExecutor taskExecutor;

    /**
     * *****************************************************************************************************************
     *
     * {@inheritDoc}
     *
     *****************************************************************************************************************
     */
    @Override
    public <Topic> void publish(final @Nonnull Topic event) {
        publish((Class<Topic>) event.getClass(), event);
    }

    /**
     * *****************************************************************************************************************
     *
     * {@inheritDoc}
     *
     *****************************************************************************************************************
     */
    @Override
    public <Topic> void publish(final @Nonnull Class<Topic> topic, final @Nonnull Topic event) {
        log.debug("publish({}, {})", topic, event);
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                publishImpl(topic, event);
            }
        });
    }

    /**
     * *****************************************************************************************************************
     *
     * {@inheritDoc}
     *
     *****************************************************************************************************************
     */
    @Override
    public <Topic> void subscribe(final @Nonnull Class<Topic> topic, final @Nonnull EventBusListener<Topic> listener) {
        log.info("subscribe({}, {})", topic, listener);
        findListenersByTopic(topic).add(new WeakReference<EventBusListener<Topic>>(listener));
    }

    /**
     * *************************************************************************
     *
     *
     ***************************************************************************
     */
    private <Topic> void publishImpl(final @Nonnull Class<Topic> topic, final @Nonnull Topic event) {

        final List<WeakReference<EventBusListener<Topic>>> listeners =
                new ArrayList<WeakReference<EventBusListener<Topic>>>(findListenersByTopic(topic));

        for (final WeakReference<EventBusListener<Topic>> listenerReference : listeners) {
            final EventBusListener<Topic> listener = listenerReference.get();

            if (listener != null) {
                try {
                    listener.notify(event);
                } catch (Throwable t) {
                    log.warn("publish()", t);
                }
            }
        }
    }

    /**
     * *************************************************************************
     *
     *
     ***************************************************************************
     */
    @Nonnull
    private <Topic> List<WeakReference<EventBusListener<Topic>>> findListenersByTopic(final @Nonnull Class<Topic> topic) {

        final List tmp = listenersMapByTopic.get(topic);
        List<WeakReference<EventBusListener<Topic>>> listeners = tmp;

        if (listeners == null) {
            listeners = new ArrayList<WeakReference<EventBusListener<Topic>>>();
            listenersMapByTopic.put(topic, (List) listeners);
        }

        return listeners;
    }

    @Override
    public void unsubscribe(EventBusListener<?> listener) {
        log.info("unsubscribe({})", listener);

        for (final List<WeakReference<EventBusListener<?>>> list : listenersMapByTopic.values()) {
            for (final Iterator<WeakReference<EventBusListener<?>>> i = list.iterator(); i.hasNext();) {
                final WeakReference<?> ref = i.next();

                if ((ref.get() == null) || (ref.get() == listener)) {
                    i.remove();
                }
            }
        }
    }
}
