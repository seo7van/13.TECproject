package com.tecProject.tec.controller;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tecProject.tec.domain.User;
import com.tecProject.tec.service.UserService;

@RestController
@RequestMapping("/user")
@ResponseBody
public class UserController {

    @Autowired
    private UserService userService;

    // 기존 메서드
    @GetMapping
    public String mainP() {
        // 세션에서 아이디 확인
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 세션에서 userType 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        return "Main Controller: " + username + " " + role;
    }

    // 새로운 프로필 API
    @GetMapping("/profile/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable("username") String username) {
        try {
            User user = userService.findUserByUsername(username);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
    
    @PutMapping("/profile/update")
    public ResponseEntity<?> updateUserProfile(@RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Update failed: " + e.getMessage());
        }
    }
    
    @PutMapping("/changePassword/{username}")
    public ResponseEntity<?> changePassword(@PathVariable("username") String username, @RequestBody Map<String, String> passwords) {
        try {
            String currentPassword = passwords.get("currentPassword");
            String newPassword = passwords.get("newPassword");

            boolean isPasswordChanged = userService.changePassword(username, currentPassword, newPassword);

            if (isPasswordChanged) {
                return ResponseEntity.ok("Password changed successfully.");
            } else {
                return ResponseEntity.status(400).body("Current password is incorrect.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error changing password: " + e.getMessage());
        }
    }
    
    
    
    
}
