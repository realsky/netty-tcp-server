package com.kl.nts.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutorService implements ITaskExecutorService {
    private final ExecutorService executorService;

    public TaskExecutorService(int nThreads) {
        executorService = Executors.newFixedThreadPool(nThreads);
    }

    public void execute(Runnable command) {
        executorService.execute(command);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
