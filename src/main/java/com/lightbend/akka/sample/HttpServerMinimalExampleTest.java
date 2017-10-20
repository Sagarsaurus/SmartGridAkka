package com.lightbend.akka.sample;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.http.javadsl.marshallers.jackson.Jackson;

import java.util.concurrent.CompletionStage;

public class HttpServerMinimalExampleTest extends AllDirectives {

    static ActorSystem system = ActorSystem.create("routes");

    public static void main(String[] args) throws Exception {
        // boot up server using the route as defined below

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        //In order to access all directives we need an instance where the routes are define.
        HttpServerMinimalExampleTest app = new HttpServerMinimalExampleTest();

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost("localhost", 8080), materializer);

        //NamedCache cache = CacheFactory.getCache("test");

        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read(); // let it run until user presses return

        binding
                .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }

    private Route createRoute() {
        return route(
                path("create", () ->
                        post(() -> parameterList(params ->
                        {
                            return entity(Jackson.unmarshaller(TestPojo.class), testPojo -> {

                                String toPost = "http://10.197.177.230:8080/ignite?cmd=put&cacheName=myCacheName&"+testPojo.generateJsonString();
                                //Set off string of messages for actor
                                final ActorRef cacheActor =
                                        system.actorOf(CacheActor.props());
                                final ActorRef databaseActor =
                                        system.actorOf(DatabaseActor.props());

                                cacheActor.tell(new CacheActor.WhatToCache(toPost), ActorRef.noSender());
                                cacheActor.tell(new CacheActor.Cache(), ActorRef.noSender());
                                databaseActor.tell(new DatabaseActor.WhatToStore(toPost), ActorRef.noSender());
                                databaseActor.tell(new DatabaseActor.Store(), ActorRef.noSender());

                                return complete("<h1>Say hello to create</h1>");
                            }
                            );
                        })
        )),

                path("update", () ->
                        put(() ->
                                complete("<h1>Say hello to update</h1>")))
                );
    }

}
