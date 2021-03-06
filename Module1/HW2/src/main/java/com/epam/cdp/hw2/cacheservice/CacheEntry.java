package com.epam.cdp.hw2.cacheservice;

import java.util.Objects;

public class CacheEntry {

    private String value;
    private int counter;

    CacheEntry(String value) {
        this.value = value;
    }

    void incrementCounter() {
        this.counter++;
    }

    int getCounter() {
        return counter;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        CacheEntry that = (CacheEntry) object;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "CacheEntry{" +
                "value='" + value + '\'' +
                '}';
    }

}
