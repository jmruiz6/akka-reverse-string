package com.akkademy.reverse.actor;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import com.akkademy.messages.ReverseString;
import com.akkademy.reverse.actor.actor.StringReverser;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import scala.concurrent.Future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;


public class StringReverserTest {

    static ActorSystem actorSystem = ActorSystem.create("ReverseStringSystem");
    TestActorRef actorRef = TestActorRef.create(actorSystem, Props.create(StringReverser.class));

    public CompletableFuture<String> sendReverseMessage(Object message) {
        Future sFuture = ask(actorRef, message, 1000);
        final CompletableFuture<String> jFuture = (CompletableFuture<String>) toJava(sFuture);
        return jFuture;
    }

    @Test
    public void itShouldReplyReversedString() throws Exception {
        assert(sendReverseMessage(new ReverseString("probanding")).get(1000, TimeUnit.MILLISECONDS).equals("gnidnaborp"));
    }

    @Test (expected = ExecutionException.class)
    public void shouldReplyToUnknownMessageWithFailure() throws Exception {
        sendReverseMessage("Unknown").get(1000, TimeUnit.MILLISECONDS);
    }
}
