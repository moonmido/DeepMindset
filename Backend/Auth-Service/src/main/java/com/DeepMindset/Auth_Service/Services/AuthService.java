package com.DeepMindset.Auth_Service.Services;

import com.DeepMindset.Auth_Service.Models.MyUser;
import com.DeepMindset.Auth_Service.MyExceptions.SmallPasswordException;
import com.DeepMindset.Auth_Service.MyExceptions.UserAlreadyExistException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Value("${keycloak.realm}")
    private String realm;

    @Autowired
    private Keycloak keycloak;


    public boolean IsUserExist(String username){
        if(username==null || username.isEmpty()) throw new IllegalArgumentException();
        try {
            UserRepresentation userRepresentation = keycloak.realm(realm).users().searchByUsername(username, true).get(0);
            return true;
        }catch (Exception e){
            return false;
        }

    }


    public void RemoveUser(String userId){
        if(userId==null) throw new IllegalArgumentException();
        UsersResource usersResource = GetAllUsers();
        usersResource.get(userId).remove();
        log.info("User Deleted");
    }

    public void ChangePassword(String username,String password){
        if(username==null || password==null) throw new IllegalArgumentException();
        if(!IsUserExist(username)) throw new NotFoundException();
        UsersResource allClients = GetAllUsers();
        String id = allClients.searchByUsername(username, true).getFirst().getId();
        UserResource userResource = allClients.get(id);
        userResource.executeActionsEmail(List.of("UPDATE_PASSWORD"));
        log.info("Mail Sent, Update your Password");
    }
    private void SendEmailVerification(String userId){
        if(userId==null) throw new IllegalArgumentException();
        UsersResource allClients = GetAllUsers();
        allClients.get(userId).sendVerifyEmail();
    }

    public void LogoutUser(String userId){
        if(userId==null) throw new IllegalArgumentException();
        UsersResource usersResource = GetAllUsers();
        usersResource.get(userId).logout();
        log.info("User with id="+userId+" Logout !");
    }



    public void CreateUser(MyUser myUser){
        if(myUser==null) throw new IllegalArgumentException();
        if(IsUserExist(myUser.username())) throw new UserAlreadyExistException();
        if(myUser.password().length()<8) throw new SmallPasswordException();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(myUser.username());
        userRepresentation.setUsername(myUser.username());
        userRepresentation.setFirstName(myUser.firstname());
        userRepresentation.setLastName(myUser.lastname());
        userRepresentation.setEnabled(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(myUser.password());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);

        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        UsersResource usersResource = GetAllUsers();
        Response response = usersResource.create(userRepresentation);
        if(response.getStatus()==201){
            log.info("User Created");
        }else{
            log.error("Unexpected Registration Problem");
        }

    }


    public UsersResource GetAllUsers(){
        return keycloak.realm(realm)
                .users();
    }



}
