package uk.co.markberridge.dropwizard.cluster;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;

public class ClusterSingleton extends UntypedActor {

    private static final Logger log = LoggerFactory.getLogger(ClusterSingleton.class);

    private final Cluster cluster;
    private final ScheduledExecutorService scheduledExecutorService;
    private final SingletonHealthCheck healthCheck;

    public ClusterSingleton(SingletonHealthCheck healthCheck) {

        // notify the health check that I am now the master
        this.healthCheck = healthCheck;
        this.healthCheck.setMaster(true);

        this.cluster = Cluster.get(getContext().system());

        // Create a scheduled executor service to log that I am the singleton
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                ClusterSingleton.log.info("########## Now I am the master");
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void finalize() throws Throwable {
        // terminate the executor service
        scheduledExecutorService.shutdown();
    }

    @Override
    public void preStart() {
        // subscribe to cluster changes
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), MemberEvent.class, UnreachableMember.class);
    }

    @Override
    public void postStop() {
        // un-subscribe on stop
        cluster.unsubscribe(getSelf());
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof MemberUp) {
            MemberUp mUp = (MemberUp) message;
            log.info("########## Member is Up: {}", mUp.member());

        } else if (message instanceof UnreachableMember) {
            UnreachableMember mUnreachable = (UnreachableMember) message;
            log.info("########## Member detected as unreachable: {}", mUnreachable.member());

        } else if (message instanceof MemberRemoved) {
            MemberRemoved mRemoved = (MemberRemoved) message;
            log.info("########## Member is Removed: {}", mRemoved.member());

        } else if (message instanceof MemberEvent) {
            // ignore
        } else {
            unhandled(message);
        }
    }
}
