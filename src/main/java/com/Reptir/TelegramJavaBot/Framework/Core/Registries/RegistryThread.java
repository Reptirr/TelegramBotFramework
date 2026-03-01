package com.Reptir.TelegramJavaBot.Framework.Core.Registries;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RegistryThread {
    ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    Map<String, Future<?>> threads = new ConcurrentHashMap<>();

    private int nextId = 1;
    private final PriorityQueue<Integer> freeIds = new PriorityQueue<>();

    // логика счетчика айди
    public synchronized String getNewId() {
        if (!freeIds.isEmpty()) {
            return String.valueOf(freeIds.poll());
        }
        return String.valueOf(nextId++);
    }
    public synchronized void releaseId(int id) {
        if (id > 0 && id < nextId) {
            freeIds.offer(id);
        }
    }

    public void createThread(String id, Runnable task) {
        if (id == null || id.isEmpty()) {
            id = String.valueOf(getNewId());
        }

        String finalId = id;
        Runnable wrappedTask = () -> {
            try {
                task.run();
            } finally {
                threads.remove(finalId);
            }
        };

        Future<?> future = executorService.submit(wrappedTask);
        threads.put(id, future);
    }
    public void createThread(Runnable task) {
        String id = String.valueOf(getNewId());

        Runnable wrappedTask = () -> {
            try {
                task.run();
            } finally {
                threads.remove(id);
            }
        };

        Future<?> future = executorService.submit(wrappedTask);
        threads.put(id, future);
    }
    public void cancelThread(String id) {
        Future<?> future = threads.get(id);
        if (future != null) {
            future.cancel(true);
        }
        releaseId(Integer.parseInt(id));
    }

    public void shutdown() {
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

    public Map<String, Future<?>> getThreads() {
        return threads;
    }

}
