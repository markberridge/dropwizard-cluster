package uk.co.markberridge.dropwizard.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;

public class SingletonActor extends UntypedActor {

    private static final Logger log = LoggerFactory.getLogger(SingletonActor.class);

    public void onReceive(Object message) {
        log.info("onReceive() called, message=" + message);
    }
}
