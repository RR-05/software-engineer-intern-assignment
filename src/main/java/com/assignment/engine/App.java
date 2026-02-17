package com.assignment.engine;

import java.util.concurrent.*;

public class App {
    public static void main(String[] args) throws InterruptedException {
        // The "Conveyor Belt" that holds our 1,000 tasks
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>(100);

        // 1. Create the Producer (makes 1,000 tasks)
        Thread producerThread = new Thread(new TaskProducer(queue));

        // 2. Create a "Worker Pool" of 10 threads to process tasks in parallel
        ExecutorService workerPool = Executors.newFixedThreadPool(10);

        System.out.println("🚀 Starting Fan-Out Engine...");
        long startTime = System.currentTimeMillis();

        // Start the producer
        producerThread.start();

        // Start 10 workers to start grabbing tasks
        for (int i = 0; i < 10; i++) {
            workerPool.execute(new TaskWorker(queue));
        }

        // Wait for producer to finish making all 1,000 tasks
        producerThread.join();

        // Monitor the queue until it's empty
        while (!queue.isEmpty()) {
            Thread.sleep(100); 
        }

        long endTime = System.currentTimeMillis();
        System.out.println("🏁 All tasks finished in: " + (endTime - startTime) + "ms");
        
        // Shut down the workers
        workerPool.shutdownNow();
    }
}