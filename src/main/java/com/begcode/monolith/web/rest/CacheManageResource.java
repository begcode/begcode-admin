package com.begcode.monolith.web.rest;

import com.begcode.monolith.service.CacheManageService;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CacheManageResource {

    private final CacheManageService cacheManageService;

    public CacheManageResource(CacheManageService cacheManageService) {
        this.cacheManageService = cacheManageService;
    }

    @GetMapping("/cache-manage")
    public ResponseEntity<Collection<String>> getAllCacheNames() {
        return ResponseEntity.ok().body(this.cacheManageService.getAllCacheNames());
    }

    @DeleteMapping("/cache-manage/{cacheName}")
    public ResponseEntity<Collection<String>> clearByCacheName(@PathVariable String cacheName) {
        this.cacheManageService.clearByCacheName(cacheName);
        return ResponseEntity.ok().build();
    }
}
