package com.akkademy.reverse.api;

import org.junit.Test;
import scala.concurrent.Future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;

public class ReverseStringServiceTest {

    private ReverseStringService client = new ReverseStringService();
    private List<String> stringsToReverse = Arrays.asList("abc", "123", "def", "456");

    public CompletableFuture<String> callReverseString(String message) {
        final CompletableFuture<String> jFuture = (CompletableFuture<String>) client.reverseString(message);
        return jFuture;
    }

    @Test
    public void itShouldReverseString() throws Exception {
        String result = (String) callReverseString("otra prueba").get();
        assert("abeurp arto".equals(result));
        client.terminate();
    }

    @Test
    public void itShouldReverseListOfStrings() throws Exception {
        List<CompletableFuture<String>> futures = stringsToReverse.stream()
                .map(this::callReverseString)
                .collect(Collectors.toList());
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        CompletableFuture<List<String>> allDone = allDoneFuture.thenApply(v ->
                futures.stream().
                        map(future -> future.join()).
                        collect(Collectors.toList())
        );
        List<String> receivedResponses = allDone.get();
        assert(Arrays.asList("cba", "321", "fed", "654").equals(receivedResponses));
    }
}
