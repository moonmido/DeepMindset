package com.DeepMindset.Auth_Service.Configurations;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

@Value("${keycloak.server.url}")
    private String server_url;
@Value("${keycloak.realm}")
    private String realm;
@Value("${keycloak.client.id}")
    private String client_id;
@Value("${keycloak.client.secret}")
    private String client_secret;


@Bean
    public Keycloak keycloak(){
    return KeycloakBuilder.builder()
            .clientId(client_id)
            .serverUrl(server_url)
            .realm(realm)
            .grantType("client_credentials")
            .clientSecret(client_secret)
            .build();
}


}
