package uk.co.markberridge.dropwizard.cluster;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ClusterSingletonBundle implements ConfiguredBundle<ClusterConfiguration> {

    private String name;
    private ManagedAkkaCluster managedAkkaCluster;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        this.name = bootstrap.getApplication().getName();
    }

    @Override
    public void run(ClusterConfiguration config, Environment environment) throws Exception {

        SingletonHealthCheck singletonHealthCheck = new SingletonHealthCheck();
        environment.healthChecks().register("singleton", singletonHealthCheck);

        this.managedAkkaCluster = new ManagedAkkaCluster(name, config.getClusterConfig(), singletonHealthCheck);
        environment.lifecycle().manage(managedAkkaCluster);
    }

    public void initializeSingletonAction(Runnable action) {
        this.managedAkkaCluster.intialiseSingleton(action);
    }

}
