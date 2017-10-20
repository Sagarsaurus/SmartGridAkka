package com.lightbend.akka.sample;

import akka.Done;
import akka.actor.ActorSystem;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.Route;
import java.util.Optional;
import java.util.concurrent.*;

// Server definition
class MinimalHttpApp extends HttpApp {
    @Override
    protected Route routes() {
        return path("hello", () ->
                get(() ->
                        complete("<h1>Say hello to akka-http</h1>")
                )
        );
    }


    public static void main(String[] args) {
        final MinimalHttpApp myServer = new MinimalHttpApp();
        try {
            myServer.startServer("localhost", 8080);
        } catch(Exception e) {

        }
    }
}




