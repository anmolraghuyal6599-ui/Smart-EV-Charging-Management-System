
package service;

import java.util.HashMap;
import java.util.Map;

// Manages background threads for EV charging operations
public class ThreadManager {

    // Stores active threads using their task IDs
    private final Map<String, Thread> activeThreads = new HashMap<>();

    // Starts a new background task
    public void startTask(String taskId, Runnable task) {

        if (taskId == null || taskId.trim().isEmpty() || task == null) {
            throw new IllegalArgumentException("Invalid task ID or task.");
        }

        synchronized (activeThreads) {

            Thread existingThread = activeThreads.get(taskId);

            if (existingThread != null && existingThread.isAlive()) {
                System.out.println("Task is already running: " + taskId);
                return;
            }

            Thread worker = new Thread(() -> {

                System.out.println("[THREAD STARTED] Task: " + taskId);

                try {
                    task.run();
                } finally {

                    synchronized (activeThreads) {
                        activeThreads.remove(taskId);
                    }

                    System.out.println("[THREAD FINISHED] Task: " + taskId);
                }
            });

            worker.setName("EV-Thread-" + taskId);

            activeThreads.put(taskId, worker);
            worker.start();
        }
    }

    // Stops a particular background task
    public void stopTask(String taskId) {

        synchronized (activeThreads) {

            Thread worker = activeThreads.get(taskId);

            if (worker != null && worker.isAlive()) {
                worker.interrupt();
                System.out.println("[THREAD STOP REQUESTED] " + taskId);
            } else {
                System.out.println("No active thread found for: " + taskId);
            }
        }
    }

    // Checks whether a particular task is running
    public boolean isTaskRunning(String taskId) {

        synchronized (activeThreads) {
            Thread worker = activeThreads.get(taskId);
            return worker != null && worker.isAlive();
        }
    }

    // Returns the number of active background tasks
    public int getActiveTaskCount() {

        synchronized (activeThreads) {
            return activeThreads.size();
        }
    }

    // Stops all active background tasks
    public void stopAllTasks() {

        synchronized (activeThreads) {

            for (Thread worker : activeThreads.values()) {
                if (worker.isAlive()) {
                    worker.interrupt();
                }
            }
        }

        System.out.println("[THREAD MANAGER] Stop requested for all tasks.");
    }
}

