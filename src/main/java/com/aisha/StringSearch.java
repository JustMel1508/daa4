package com.aisha;

import java.util.List;

public interface StringSearch {

    List<Integer> search(String text, String pattern);

    long getOperations();

    void resetOperations();
}