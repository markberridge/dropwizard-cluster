package uk.co.markberridge.dropwizard.cluster;

import io.dropwizard.Configuration;
import io.dropwizard.validation.PortRange;

public class ClusterConfiguration extends Configuration {

    @PortRange
    private int clusterPort;

    public int getClusterPort() {
        return clusterPort;
    }

    public void setClusterPort(int clusterPort) {
        this.clusterPort = clusterPort;
    }
}
