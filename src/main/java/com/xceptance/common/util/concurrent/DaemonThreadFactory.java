package com.xceptance.common.util.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;

import com.xceptance.common.util.Getter;

/**
 * A {@link ThreadFactory} implementation that creates threads with the daemon flag set.
 */
public class DaemonThreadFactory implements ThreadFactory
{
    /**
     * The default thread factory which does the hard work.
     */
    private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

    /**
     * The number of threads created by this factory so far.
     */
    private final AtomicInteger count = new AtomicInteger();

    /**
     * Default name generator if none is set
     */
    private static final IntFunction<String> DEFAULT_NAME_GENERATOR = i -> "Thread-" + i;
    
    /**
     * The thread name's prefix getter.
     */
    private final IntFunction<String> nameGenerator;

    /**
     * Priority to set
     */
    private final int priority;
    
    /**
     * Constructor.
     */
    public DaemonThreadFactory()
    {
        this(DEFAULT_NAME_GENERATOR, Thread.NORM_PRIORITY);
    }

    /**
     * Constructor.
     * 
     * @param nameGenerator
     *            a name generator that is given an int and it shall return a name
     * @param priority the priority for the threads to create
     */
    public DaemonThreadFactory(final IntFunction<String> nameGenerator, final int priority)
    {
        this.nameGenerator = nameGenerator == null ? DEFAULT_NAME_GENERATOR : nameGenerator;
        this.priority = priority;
    }

    /**
     * Constructor.
     * 
     * @param getter
     *            the thread name's prefix getter
     */
    public DaemonThreadFactory(final IntFunction<String> nameGenerator)
    {
        this(nameGenerator, Thread.NORM_PRIORITY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Thread newThread(final Runnable runnable)
    {
        final Thread thread = defaultThreadFactory.newThread(runnable);
        thread.setDaemon(true);
        thread.setPriority(priority);

        thread.setName(nameGenerator.apply(count.getAndIncrement()));

        return thread;
    }
}
