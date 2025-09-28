package org.yaroslaavl.communicationservice.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.yaroslaavl.communicationservice.config.FeignConfig;
import org.yaroslaavl.communicationservice.feignClient.dto.ApplicationChatInfo;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "recruiting-service", path = "/api/v1", configuration = FeignConfig.class)
public interface RecruitingFeignClient {

    @GetMapping("/applications/chat-open")
    boolean isOpenedChatting(@RequestParam("applicationId") UUID applicationId);

    @GetMapping("/applications/chat-previews")
    List<ApplicationChatInfo> getPreviewApplications(@RequestParam("applicationIds") Set<UUID> applicationIds);
}
