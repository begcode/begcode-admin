package com.begcode.monolith.service;

import java.util.Collection;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheManageService {

    private final CacheManager cacheManager;

    public CacheManageService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Collection<String> getAllCacheNames() {
        return this.cacheManager.getCacheNames();
    }

    public void clearByCacheName(String name) {
        Cache cache = this.cacheManager.getCache(name);
        if (cache != null) {
            cache.clear();
        }
    }
}
