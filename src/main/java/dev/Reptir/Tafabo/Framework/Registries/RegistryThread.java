package dev.Reptir.Tafabo.Framework.Registries;

import dev.Reptir.Tafabo.Framework.ThreadLogic.ThreadId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RegistryThread {
    ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    Map<ThreadId, Future<?>> threads = new ConcurrentHashMap<>();

    public ThreadId createThread(Runnable task) {
        ThreadId threadId = new ThreadId();
        Runnable wrappedTask = () -> {
            try {
                task.run();
            } finally {
                threads.remove(threadId);
            }
        };

        Future<?> future = executorService.submit(wrappedTask);
        threads.put(threadId, future);
        return threadId;
    }

    public void cancelThread(ThreadId id) {
        Future<?> future = threads.get(id);
        if (future != null) {
            future.cancel(true);
        }
    }

    public void shutdown() {
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

    public Map<ThreadId, Future<?>> getThreads() {
        return threads;
    }

}
