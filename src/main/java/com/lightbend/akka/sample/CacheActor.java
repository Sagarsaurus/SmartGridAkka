package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import com.lightbend.akka.sample.Printer.Greeting;
import scala.concurrent.ExecutionContextExecutor;

//#greeter-messages
public class CacheActor extends AbstractActor {
    final Http http = Http.get(context().system());
    final ExecutionContextExecutor dispatcher = context().dispatcher();
    final Materializer materializer = ActorMaterializer.create(context());
    //#greeter-messages
    static public Props props() {
        return Props.create(CacheActor.class, () -> new CacheActor());
    }

    //#greeter-messages
    static public class WhatToCache {
        public final String what;

        public WhatToCache(String what) {
            this.what = what;
        }
    }

    static public class Cache {
        public Cache() {
        }
    }
    //#greeter-messages

    private String toPersist = "";

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WhatToCache.class, wtc -> {
                    this.toPersist = wtc.what;
                })
                .match(Cache.class, x -> {
                    //#greeter-send-message
                    System.out.println(this.toPersist);
                    Http.get(context().system())
                            .singleRequest(HttpRequest.create(this.toPersist), materializer);
                    //#greeter-send-message
                })
                .build();
    }
//#greeter-messages
}
//#greeter-messages
