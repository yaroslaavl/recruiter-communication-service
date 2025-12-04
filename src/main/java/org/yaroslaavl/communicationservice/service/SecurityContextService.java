package org.yaroslaavl.communicationservice.service;

@FunctionalInterface
public interface SecurityContextService {

    String getAuthenticatedUserInfo(String type);
}
