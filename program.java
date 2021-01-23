package CalculateMax.PrimeNumbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class BerSuBall {
    public static int findIndex(int[] arr, int t) throws ExecutionException, InterruptedException {
        if (arr == null) {
            return -1;
        }

        int numberOfThreads = 4;
        return findIndexInParallel(arr, t, numberOfThreads);
    }

    private static int findIndexInParallel(int[] arr, int t, int threadCount) throws ExecutionException, InterruptedException {
        if(threadCount > arr.length) {
            threadCount = arr.length;
        }

        int startIndex = 0;
        int range = (int) Math.ceil(arr.length / (double) threadCount);
        int endIndex = range;

        // step 1: create threads using Executor FrameWork
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {

            RangeFinder rangeFinder = new RangeFinder(Arrays.copyOfRange(arr, startIndex, endIndex), startIndex, t);

            // Step 3: submit the task to thread pool
            Future<Integer> submit = executorService.submit(rangeFinder);
            startIndex = startIndex + range;
            int newEndIndex = endIndex + range;
            endIndex = Math.min(newEndIndex, arr.length);
            futures.add(submit);
        }

        // Evaluating the test result
        for (Future<Integer> future : futures) {
            int index = (int) future.get();
            if(index != -1) {
                executorService.shutdownNow();
                return index;
            }
        }
        executorService.shutdown();
        return -1;
    }

    static class RangeFinder implements Callable<Integer> {
        private final int[] arr;
        private final int startIndex;
        private final int t;

        RangeFinder(int[] arr, int startIndex, int t) {
            this.arr = arr;
            this.startIndex = startIndex;
            this.t = t;
        }


        @Override
        public Integer call() {
            long start = System.nanoTime();
            for (int i = 0; i < this.arr.length; i++) {
                if (arr[i] > t) {
                    return startIndex + i;
                }
            }
            long end = System.nanoTime();
            long elapsedTime = end - start;
            double seconds = (double)elapsedTime / 1_000_000_000.0;
            System.out.println("Thread : "+Thread.currentThread().getId() + "  "+seconds);
            return -1;
        }
    }

    // Driver Code - Finds the biggest number of in the array after the constant N
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] my_array = {5, 4, 6, 1, 3, 2, 7, 8, 9, 10};

        int i = findIndex(my_array, 7);
        // find the index of 5
        System.out.println("hello");
        System.out.println("Index position of 5 is: "
                + my_array[i]);
    }
}

