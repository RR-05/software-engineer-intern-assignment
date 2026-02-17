package com.assignment.engine;

import java.util.concurrent.BlockingQueue;

/**
 * The Producer generates 1,000 tasks and places them into the queue.
 */
public class TaskProducer implements Runnable {
    private final BlockingQueue<Task> queue;

    public TaskProducer(BlockingQueue<Task> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 1000; i++) {
            try {
                // Creating a new task and pushing it to the queue
                Task task = new Task(i, "Payload-Data-" + i);
                queue.put(task); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("✅ Producer: All 1,000 tasks have been queued.");
    }
}