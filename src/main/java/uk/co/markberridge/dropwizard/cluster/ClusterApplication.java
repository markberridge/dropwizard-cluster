package uk.co.markberridge.dropwizard.cluster;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.markberridge.dropwizard.cluster.resource.PingResource;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.contrib.pattern.ClusterSingletonManager;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Main dropwizard service
 */
public class ClusterApplication extends Application<ClusterConfiguration> {

    private static final Logger log = LoggerFactory.getLogger(ClusterApplication.class);

    public static void main(String... args) throws Exception {
        if (args.length == 0) {
            String configFileName = new OverrideConfig("cluster.yml").getName();
            new ClusterApplication().run(new String[] { "server", configFileName });
        } else {
            new ClusterApplication().run(args);
        }
    }

    @Override
    public String getName() {
        return "cluster";
    }

    @Override
    public void initialize(Bootstrap<ClusterConfiguration> bootstrap) {
        //
    }

    @Override
    public void run(ClusterConfiguration config, Environment environment) throws Exception {

        log.info("Starting {} on cluster port {}", getClass().getSimpleName(), config.getClusterPort());

        // Override the configuration of the port
        Config c = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + config.getClusterPort()).withFallback(
                ConfigFactory.load());

        // Create an Akka system
        ActorSystem system = ActorSystem.create("ClusterSystem", c);

        // Create an actor that handles cluster domain events
        // system.actorOf(Props.create(ClusterListener.class),
        // "clusterListener");
        system.actorOf(ClusterSingletonManager.defaultProps(Props.create(ClusterSingleton.class), "ClusterSingleton",
                PoisonPill.getInstance(), null));

        // Resources
        environment.jersey().register(new PingResource());

        // Health Checks
        environment.healthChecks().register("healthy", new AlwaysHealthyHealthCheck());
    }
}