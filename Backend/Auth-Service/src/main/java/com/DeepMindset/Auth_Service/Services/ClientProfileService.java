package com.DeepMindset.Auth_Service.Services;

import com.DeepMindset.Auth_Service.Models.MyProfile;
import com.DeepMindset.Auth_Service.MyExceptions.SmallPasswordException;
import jakarta.ws.rs.NotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientProfileService {

    private static final Logger log = LoggerFactory.getLogger(ClientProfileService.class);
    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public MyProfile GetClientProfile(String userId){
if(userId==null) throw new IllegalArgumentException();
if(!isUserExist(userId)) throw new NotFoundException();

UserRepresentation representation = getAllClients().get(userId).toRepresentation();
        return new MyProfile(representation.getUsername(),representation.getFirstName(),representation.getLastName());
    }

    private UsersResource getAllClients(){
        return keycloak
                .realm(realm)
                .users();
    }

    private boolean isUserExist(String userId) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");

        try {
            UsersResource usersResource = keycloak.realm(realm).users();
            UserRepresentation user = usersResource.get(userId).toRepresentation();
            return true; // if we reach here, user exists (enabled or disabled)
        } catch (NotFoundException e) {
            return false; // user doesn't exist
        }
    }



    public void ResetPassword(String userId,String Ppassword,String Npassword){
        if(userId==null ||Ppassword==null || Npassword==null) throw new IllegalArgumentException();
        if(!isUserExist(userId)) throw new NotFoundException();
        if(Npassword.length()<8) throw new SmallPasswordException();

        UserResource userResource = getAllClients().get(userId);
        if(!userResource.toRepresentation().getCredentials().getFirst().getValue().equals(Ppassword)) throw new SecurityException();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(Npassword);
        userResource.resetPassword(credentialRepresentation);
        log.info("Password Reset Successfully");

    }



}
