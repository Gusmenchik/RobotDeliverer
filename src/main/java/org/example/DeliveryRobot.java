package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DeliveryRobot {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        int numThreads = 1000;

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countR = countRightTurns(route);
                updateSizeToFreq(countR);
                System.out.println("Thread " + Thread.currentThread().getId() + ": Count of 'R' = " + countR);
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        printFrequencyMap();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countRightTurns(String route) {
        int count = 0;
        for (int i = 0; i < route.length(); i++) {
            if (route.charAt(i) == 'R') {
                count++;
            }
        }
        return count;
    }

    public static void updateSizeToFreq(int countR) {
        synchronized (sizeToFreq) {
            sizeToFreq.merge(countR, 1, Integer::sum);
        }
    }

    public static void printFrequencyMap() {
        System.out.println("Frequency Map:");
        int mostFrequentCount = 0;
        int mostFrequentCountOccurrences = 0;

        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            int count = entry.getKey();
            int occurrences = entry.getValue();

            if (occurrences > mostFrequentCountOccurrences) {
                mostFrequentCount = count;
                mostFrequentCountOccurrences = occurrences;
            }

            System.out.println("- " + count + " (" + occurrences + " times)");
        }

        System.out.println("Most frequent count: " + mostFrequentCount + " (occurred " + mostFrequentCountOccurrences + " times)");
    }
}
