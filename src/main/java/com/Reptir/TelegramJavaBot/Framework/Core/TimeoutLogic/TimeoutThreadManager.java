package com.Reptir.TelegramJavaBot.Framework.Core.TimeoutLogic;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimeoutThreadManager {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final TimeoutService timeoutService;
    public long delaySecondsChecking;
    private volatile boolean active = false;

    ScheduledFuture<?> functionHandler;

    public TimeoutThreadManager(TimeoutService timeoutService, long delaySecondsChecking) {
        this.timeoutService = timeoutService;
        this.delaySecondsChecking = delaySecondsChecking;
    }

    private class ScheduledFunction implements Runnable {
        @Override
        public void run() {
            if (active) {
                timeoutService.checkTimeout();
            }
        }
    }

    // нестрогие функции
    public void pauseChecking() {
        active = false;
    }
    public void resumeChecking() {
        active = true;
    }


    // терминаторные функции
    public void startChecking() {
        if (functionHandler == null || functionHandler.isCancelled() || functionHandler.isDone()) {
            functionHandler = scheduler.scheduleWithFixedDelay(new ScheduledFunction(), 0, delaySecondsChecking, TimeUnit.SECONDS);
            active = true;
        }
    }
    public void stopChecking() {
        active = false;
        if (functionHandler != null) functionHandler.cancel(true);
        scheduler.shutdownNow();  // теперь пул мертв
    }
}
