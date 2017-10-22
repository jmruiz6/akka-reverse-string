package com.akkademy.messages;

import java.io.Serializable;

public class ReverseString implements Serializable{
    public final String stringToReverse;

    public ReverseString(String stringToReverse) {
        this.stringToReverse = stringToReverse;
    }
}
