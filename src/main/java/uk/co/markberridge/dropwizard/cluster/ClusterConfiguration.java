package uk.co.markberridge.dropwizard.cluster;

import org.hibernate.validator.constraints.NotEmpty;

public interface ClusterConfiguration {

    @NotEmpty
    String getClusterConfig();

}
