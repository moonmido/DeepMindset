package com.DeepMindset.Auth_Service.Controllers;

import com.DeepMindset.Auth_Service.MyExceptions.RoleNotFoundException;
import com.DeepMindset.Auth_Service.Services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    @PutMapping("/assign/{userId}")
    public ResponseEntity<?> AssignRole(@PathVariable String userId, @RequestParam String roleName){
        try {
            roleService.assignRole(userId,roleName);
            return ResponseEntity.ok("Role Assigned");
        }catch (IllegalArgumentException i){
            return ResponseEntity.badRequest().body("Empty userId or RoleName");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }


    @DeleteMapping("/remove-role/{userId}")
    public ResponseEntity<?> RemoveRole(@PathVariable String userId,@RequestParam String roleName){
        try {
            roleService.deleteRole(userId, roleName);
            return ResponseEntity.ok("Role Removed");
        }catch (IllegalArgumentException i){
            return ResponseEntity.badRequest().body("Empty userId or RoleName");
        }catch (RoleNotFoundException r){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role NOT FOUND");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }


}
