package com.assignment.engine;

import java.util.concurrent.BlockingQueue;

/**
 * The Worker (Consumer) grabs tasks from the queue and "processes" them.
 */
public class TaskWorker implements Runnable {
    private final BlockingQueue<Task> queue;

    public TaskWorker(BlockingQueue<Task> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // take() waits if the queue is empty
                Task task = queue.take();
                
                // Simulate processing the task
                System.out.println(Thread.currentThread().getName() + " is processing Task #" + task.id());
                
                // Small sleep to simulate real work (like sending an email or saving to a DB)
                Thread.sleep(10); 
            }
        } catch (InterruptedException e) {
            // This is how we stop the worker when all tasks are done
            Thread.currentThread().interrupt();
        }
    }
}