package org.yaroslaavl.communicationservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.yaroslaavl.communicationservice.service.SecurityContextService;

@Slf4j
@Service
public class SecurityContextServiceImpl implements SecurityContextService {

    public static final String SUB = "sub";
    public static final String FULL_NAME = "name";

    @Override
    public String getAuthenticatedUserInfo(String type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwt) {
            return jwt.getTokenAttributes().get(type).toString();
        }

        log.warn("Authentication is not JwtAuthenticationToken or it has no sub");
        return null;
    }
}
