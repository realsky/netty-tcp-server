package com.kl.nts.service;

public class ServiceLocator {
    private static ITaskExecutorService taskExecutorService = new TaskExecutorService(2);

    public static ITaskExecutorService getTaskExecutorService() {
        return taskExecutorService;
    }
}
