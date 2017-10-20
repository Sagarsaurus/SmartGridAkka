package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.lightbend.akka.sample.Printer.Greeting;

//#greeter-messages
public class DatabaseActor extends AbstractActor {
    //#greeter-messages
    static public Props props() {
        return Props.create(DatabaseActor.class, () -> new DatabaseActor());
    }

    //#greeter-messages
    static public class WhatToStore {
        public final String what;

        public WhatToStore(String what) {
            this.what = what;
        }
    }

    static public class Store {
        public Store() {
        }
    }
    //#greeter-messages

    private String toPersist = "";

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WhatToStore.class, wts -> {
                    this.toPersist = wts.what;
                })
                .match(Store.class, x -> {
                    //#greeter-send-message
                    System.out.println(this.toPersist);
                    //#greeter-send-message
                })
                .build();
    }
//#greeter-messages
}
//#greeter-messages
