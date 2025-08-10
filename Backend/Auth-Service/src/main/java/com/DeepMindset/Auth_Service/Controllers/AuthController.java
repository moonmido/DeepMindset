package com.DeepMindset.Auth_Service.Controllers;

import com.DeepMindset.Auth_Service.Models.MyUser;
import com.DeepMindset.Auth_Service.MyExceptions.SmallPasswordException;
import com.DeepMindset.Auth_Service.MyExceptions.UserAlreadyExistException;
import com.DeepMindset.Auth_Service.Services.AuthService;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/create")
    public ResponseEntity<?> CreateUser(@RequestBody MyUser myUser){

        try {
            authService.CreateUser(myUser);
            return ResponseEntity.ok("User created");
        }catch (IllegalArgumentException i){
return ResponseEntity.badRequest().body("Empty user");
        }catch (UserAlreadyExistException a){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User Already created");
        }catch (SmallPasswordException s){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("password lenght must be bigger than 8 ");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error ");
        }
    }

    @PutMapping("/update-password/{username}")
    public ResponseEntity<?> UpdatePassword(@PathVariable String username , @RequestParam String Ppassword){
        try {
authService.ChangePassword(username,Ppassword);
return ResponseEntity.ok("Check your email to update your password");
        }catch (IllegalArgumentException i){
            return ResponseEntity.badRequest().body("Empty userId or Ppassword");
        }catch (NotFoundException n){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error ");
        }
    }




    @DeleteMapping("/delete-account/{id}")
    public ResponseEntity<?> RemoveAccount(@PathVariable String id){
        try {
            authService.RemoveUser(id);
            return ResponseEntity.ok("Account Deleted");
        }catch (IllegalArgumentException i){
            return ResponseEntity.badRequest().body("Empty userId");
        }        catch (NotFoundException n) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error ");
        }
    }

@PutMapping("/logout/{id}")
    public ResponseEntity<?> LogoutUser(@PathVariable String id){
        try {
            authService.LogoutUser(id);
            return ResponseEntity.ok("Account Logout");
        }catch (IllegalArgumentException i){
            return ResponseEntity.badRequest().body("Empty userId");
        }
}


    }
