package com.app.quantity.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name = "HISTORY-SERVICE",
    configuration = FeignClientConfig.class  // ✅ ADD CONFIGURATION
)
public interface HistoryServiceClient {
    
    @PostMapping("/internal/history/save")
    void saveHistory(@RequestBody SaveHistoryRequest request,
                     @RequestHeader("X-User-Id") Long userId,
                     @RequestHeader("X-User-Email") String userEmail);
}