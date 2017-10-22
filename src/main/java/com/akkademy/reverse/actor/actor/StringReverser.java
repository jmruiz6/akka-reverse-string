package com.akkademy.reverse.actor.actor;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akkademy.messages.ReverseString;

public class StringReverser extends AbstractActor {

    protected final LoggingAdapter log = Logging.getLogger(context().system(), this);

    private String reverseString(String stringToReverse) {
        return new StringBuilder(stringToReverse).reverse().toString();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ReverseString.class, message -> {
                    log.info("Received reverse request – value:{}", message.stringToReverse);
                    getSender().tell(reverseString(message.stringToReverse), getSelf());
                })
                .matchAny(o -> sender().tell(new Status.Failure(new Exception("Mensaje no identificado")), self()))
                .build();
    }
}
