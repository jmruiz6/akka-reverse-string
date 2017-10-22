package com.akkademy.reverse.api;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.akkademy.messages.ReverseString;
import com.akkademy.reverse.actor.actor.StringReverser;

import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;

public class ReverseStringService {

    private ActorSystem system = ActorSystem.create("ReverseStringSystem");
    private ActorRef actor = system.actorOf(Props.create(StringReverser.class), "AkkademyDbActor");

    public CompletionStage reverseString(String textToReverse) {
        return toJava(ask(actor, new ReverseString(textToReverse), 5000));
    }

    public void terminate() {
        system.terminate();
    }

}
