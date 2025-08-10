package com.DeepMindset.Auth_Service.Controllers;

import com.DeepMindset.Auth_Service.Services.ClientProfileService;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private ClientProfileService profileService;

    @GetMapping("/accout/{userId}")
    public ResponseEntity<?> GetUserProfile(@PathVariable String userId){
        try {
            return ResponseEntity.ok(profileService.GetClientProfile(userId));
        }catch (IllegalArgumentException i){
            return ResponseEntity.badRequest().body("Empty userId");
        }catch (NotFoundException n){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error ");
        }
    }




}
