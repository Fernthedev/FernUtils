package com.github.fernthedev.fernutils;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LatencyTest {

    @DisplayName("Latency test")
    @Test
    public void testAddCheckLatency() {

        final long[] singleThreadAverageNS = {0};
        final long[] multiThreadAverageNS = {0};

        final long testTimes = 320;

        Settings singleThreadSettings = new Settings();
        Settings multiThreadSettings = new Settings();

        Thread singleThread = new Thread(() -> {
            for (int i = 0; i < testTimes; i++) {
                System.out.println("Doing single thread " + i + " Current average: " + TimeUnit.NANOSECONDS.toMillis(singleThreadAverageNS[0] / ((long) i + 1) ));
                StopWatch stopWatch = StopWatch.createStarted();
                Map<String, List<String>> mapStr = singleThreadSettings.singleThread(false, false);
                stopWatch.stop();
                singleThreadAverageNS[0] += stopWatch.getTime(TimeUnit.NANOSECONDS);
            }

            System.out.println("Average Single Thread: " + TimeUnit.NANOSECONDS.toMillis(singleThreadAverageNS[0] / testTimes));
        });


        Thread multiThread = new Thread(() -> {
            for (int i = 0; i < testTimes; i++) {
                System.out.println("Doing multithread " + i + " Current average: " + TimeUnit.NANOSECONDS.toMillis(multiThreadAverageNS[0] / ((long) i + 1)));
                StopWatch stopWatch = StopWatch.createStarted();
                Map<String, List<String>> mapStr = multiThreadSettings.multiThread(false, false);
                stopWatch.stop();
                multiThreadAverageNS[0] += stopWatch.getTime(TimeUnit.NANOSECONDS);
            }

            System.out.println("Average Multithread Thread: " + TimeUnit.NANOSECONDS.toMillis(multiThreadAverageNS[0] / testTimes));
        });

        singleThread.start();
        multiThread.start();

        try {
            singleThread.join();
            multiThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        System.out.println(String.format("Single Thread: %sms", TimeUnit.NANOSECONDS.toMillis(singleThreadAverageNS[0] / testTimes)));
        System.out.println(String.format("MultiThread: %sms", TimeUnit.NANOSECONDS.toMillis(multiThreadAverageNS[0] / testTimes)));


        Assertions.assertDoesNotThrow(() -> {
            if ((singleThreadAverageNS[0] / testTimes) - (multiThreadAverageNS[0] / testTimes) > multiThreadAverageNS[0] / testTimes)
                throw new IllegalStateException("Either machine is better at single threaded or multithreaded average is slower than single threaded average as a difference of multithreaded average. (s - m > m)");
        });
//        Map<String, List<String>> map = settings.multiThread(true);



//        System.out.println(map + "\n"+ mapStr);
    }

}
