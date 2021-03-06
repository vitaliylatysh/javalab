package com.epam.cdp.hw2.cacheservice;

import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.epam.cdp.hw2.utils.Constants.EXPIRE_AFTER_ACCESS;

public class CacheServiceJava extends AbstractCacheService implements ICacheService {

    private static final Logger logger = Logger.getLogger(CacheServiceJava.class);

    private ScheduledExecutorService scheduledExecutorService;
    private Map<String, CacheEntry> values;

    CacheServiceJava() {
        values = new ConcurrentHashMap<>();
        scheduledExecutorService = Executors.newScheduledThreadPool(1);

        Runnable removeTask = () -> {
            Map.Entry<String, CacheEntry> entryToRemove = values.entrySet().stream()
                    .min(Comparator.comparing(entry -> entry.getValue().getCounter()))
                    .get();
            values.remove(entryToRemove.getKey());
            this.addEvictionToStats();
            logger.info("Removed: " + entryToRemove.getValue());
        };

        scheduledExecutorService.scheduleAtFixedRate(removeTask, EXPIRE_AFTER_ACCESS, EXPIRE_AFTER_ACCESS, TimeUnit.SECONDS);
    }

    @Override
    public String get(String entryKey) {
        CacheEntry cacheEntry = values.get(entryKey);

        if (cacheEntry == null) {
            return null;
        }

        cacheEntry.incrementCounter();
        return cacheEntry.getValue();
    }

    @Override
    public boolean put(String entryKey, String entryValue) {

        if (entryKey == null || entryValue == null) {
            return false;
        }

        CacheEntry cacheEntry = values.get(entryKey);

        if (cacheEntry == null) {
            CacheEntry newCacheEntry = new CacheEntry(entryValue);
            newCacheEntry.incrementCounter();

            long startPutTime = System.currentTimeMillis();
            values.put(entryKey, newCacheEntry);
            long finishPutTime = System.currentTimeMillis();

            long timeToPut = finishPutTime - startPutTime;
            this.getAllPutTimes().add(timeToPut);
            logger.info("Added: " + newCacheEntry + " in " + timeToPut + " ms.");
            return true;
        }

        logger.info("Already in cache: " + cacheEntry);
        cacheEntry.incrementCounter();

        return false;
    }

}
