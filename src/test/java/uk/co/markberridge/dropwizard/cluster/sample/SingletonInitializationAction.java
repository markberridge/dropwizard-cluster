package uk.co.markberridge.dropwizard.cluster.sample;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingletonInitializationAction implements Runnable{

    private static Logger log = LoggerFactory.getLogger(SingletonInitializationAction.class);
    private ScheduledExecutorService scheduledExecutorService;

    public SingletonInitializationAction(ScheduledExecutorService scheduledExecutorService){
        this.scheduledExecutorService = scheduledExecutorService;
    }
    
    @Override
    public void run() {
        // Create a scheduled executor service to log that I am the singleton
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                log.info("########## Now I am the master");
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    
}
