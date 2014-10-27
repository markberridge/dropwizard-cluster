package uk.co.markberridge.dropwizard.cluster;

import com.codahale.metrics.health.HealthCheck;

public class SingletonHealthCheck extends HealthCheck {

    private boolean master = false;

    @Override
    protected Result check() throws Exception {
        return Result.healthy(master ? "I AM THE MASTER" : "passive");
    }

    public void setMaster(boolean master) {
        this.master = master;
    }
}
