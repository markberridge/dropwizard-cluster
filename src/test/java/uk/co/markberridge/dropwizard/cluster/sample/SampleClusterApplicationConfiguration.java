package uk.co.markberridge.dropwizard.cluster.sample;

import io.dropwizard.Configuration;
import uk.co.markberridge.dropwizard.cluster.ClusterConfiguration;

public class SampleClusterApplicationConfiguration extends Configuration implements ClusterConfiguration {

    private String clusterConfig;

    @Override
    public String getClusterConfig() {
        return clusterConfig;
    }
}
