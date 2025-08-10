package com.DeepMindset.Auth_Service.Services;

import com.DeepMindset.Auth_Service.MyExceptions.RoleNotFoundException;
import jakarta.ws.rs.NotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleService.class);
    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;



    public void assignRole(String userId,String roleName){
        if(userId==null ||roleName==null) throw new IllegalArgumentException();

        RolesResource rolesResource = GetAllRoles();
        RoleRepresentation representation = rolesResource.get(roleName).toRepresentation();
        UsersResource allUsers = getAllUsers();
        allUsers.get(userId)
                .roles()
                .realmLevel()
                .add(List.of(representation));
log.info("Role Assigned to user");
    }

    public void deleteRole(String userId,String roleName){
        if(userId==null || roleName==null) throw new IllegalArgumentException();
if(!isRoleExist(userId,roleName)) throw new RoleNotFoundException();

RoleRepresentation representation = GetAllRoles()
        .get(roleName)
        .toRepresentation();

getAllUsers()
        .get(userId)
        .roles()
        .realmLevel()
        .remove(List.of(representation));
log.info("Role Removed ");

    }



    private boolean isRoleExist(String roleName, String userId) {
        try {
            var roles = getAllUsers()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .listAll();

            for (RoleRepresentation role : roles) {
                if (role.getName().equals(roleName)) {
                    return true;
                }
            }
            return false; // role not found
        } catch (NotFoundException e) {
            log.info("User or role not found");
            return false;
        }
    }


    private UsersResource getAllUsers(){
        return keycloak
                .realm(realm)
                .users();
    }



    public RolesResource GetAllRoles(){
        return keycloak.realm(realm).roles();
    }


}
