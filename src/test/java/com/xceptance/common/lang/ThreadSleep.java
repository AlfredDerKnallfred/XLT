package com.xceptance.common.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

import com.xceptance.xlt.engine.util.TimerUtils;

/**
 * Class description goes here.
 * 
 * @author René Schwietzke (Xceptance Software Technologies GmbH)
 */
public class ThreadSleep
{
    public static final Random random = new Random();

    private static final int ITERATIONS = 200;

    private static final int SLEEPTIME = 100;

    private static final int THREADS = 200;

    static class SleepThread extends Thread
    {
        private static final Queue<SleepThread> threads = new ConcurrentLinkedQueue<SleepThread>();

        public long endSleepTime = 0;

        private void sleep(final int duration)
        {
            long now = TimerUtils.getTime();

            final SleepThread first = threads.peek();
            if (first != null)
            {
                if (first.endSleepTime >= TimerUtils.getTime())
                {
                    threads.remove(first);
                    LockSupport.unpark(first);
                }
            }

            // retire for a moment
            endSleepTime = TimerUtils.getTime() + duration;
            while (now < endSleepTime)
            {
                LockSupport.parkUntil(endSleepTime);
                now = TimerUtils.getTime();
            }
        }

        @Override
        public void run()
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (final InterruptedException e1)
            {
                e1.printStackTrace();
            }

            final StringBuilder a = new StringBuilder();
            long min = Integer.MAX_VALUE;
            long max = 0;
            final long start = TimerUtils.getTime();
            for (int i = 0; i < ITERATIONS; i++)
            {
                final long s = TimerUtils.getTime();
                sleep(SLEEPTIME);
                final long e = TimerUtils.getTime();

                final long last = e - s;

                if (last < min)
                {
                    min = last;
                }
                if (last > max)
                {
                    max = last;
                }

                final int l = random.nextInt(500) + 1500;
                for (int j = 0; j < l; j++)
                {
                    a.append("a" + "b" + i);
                }
            }
            final long end = TimerUtils.getTime();

            sleep(1000);
            final long runtime = end - start;
            final long avg = runtime / ITERATIONS;
            System.out.println("Runtime " + getName() + ": " + runtime + " / " + avg + " / min: " + min + " / max: " + max);
        }
    }

    public static void main(final String[] args)
    {
        final List<SleepThread> list = new ArrayList<SleepThread>();
        for (int i = 0; i < THREADS; i++)
        {
            final SleepThread t = new ThreadSleep.SleepThread();
            list.add(t);

            t.start();
        }
        System.out.println("Setup done");
        for (int i = 0; i < THREADS; i++)
        {
            final SleepThread t = list.get(i);
            try
            {
                t.join();
            }
            catch (final InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
