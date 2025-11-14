package com.example.backend.controller;


import com.example.backend.config.CurrentUserUtil;
import com.example.backend.dto.*;
import com.example.backend.model.ConnectionRequest;
import com.example.backend.model.Profile;
import com.example.backend.model.User;
import com.example.backend.repo.ProfileRepo;
import com.example.backend.repo.UserRepo;
import com.example.backend.service.PdfService;
import com.example.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PdfService pdfService;
    private final ProfileRepo profileRepo;

    private final CurrentUserUtil currentUserUtil;
    private final UserRepo userRepo;

    @GetMapping
    public String getHome(){
        return "hi_User";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistryDto registryDto){
        return ResponseEntity.ok(userService.register(registryDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto){
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @PostMapping("/update_profile_picture")
    public ResponseEntity<String> updateProfilePicture(@RequestParam("file")MultipartFile file,
                                                       @RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(userService.updateProfilePicture(file,authHeader));
    }

    @PostMapping("/user_update")
    public ResponseEntity<String> updatedUserProfile(@RequestHeader("Authorization") String authHeader,
                                                     @Valid @RequestBody UpdateUserDto newUserData){
        String message = userService.updateUserProfile(authHeader, newUserData);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/get_user_and_profile")
    public ResponseEntity<UserProfileResponse> getUserAndProfile(@RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(userService.getUserAndProfile(authHeader));
    }

    @PostMapping("/update_profile_data")
    public ResponseEntity<String> updateProfileData(@RequestHeader("Authorization") String authHeader,
                                                    @Valid @RequestBody ProfileDto profileDto){
        return ResponseEntity.ok(userService.updateProfileData(authHeader,profileDto));

    }

    @GetMapping("/get_profile_based_on_username")
    public ResponseEntity<UserProfileResponse> getUserProfileAndUserBasedOnUsername(@RequestParam("username") String username){
        return ResponseEntity.ok(userService.getUserProfileAndUserBasedOnUsername(username));
    }

    @GetMapping("/get_all_users")
    public ResponseEntity<List<UserResponseWithProfile>> getAllUserProfile(){
        return ResponseEntity.ok(userService.getAllUserProfile());
    }

//    Isme USer id Chahiye
    @GetMapping("/{userId}/download_resume")
    public ResponseEntity<byte[]> downloadProfile(@PathVariable("userId") String userId,
            @RequestHeader("Authorization") String authHeader){
        try {
            // 1. Get Current User
            User currUser = currentUserUtil.getCurrentUser(authHeader);
            User user=userRepo.findById(userId).orElse(null);
            if(user==null){throw new IllegalArgumentException("User Not Exist");}
            Profile profile = profileRepo.findByUserId(userId);

            // 2. Generate PDF
            byte[] pdfBytes = pdfService.generateResumePdf(user, profile);

            // 3. Return as download
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/{connectionId}/send_connection_request")
    public ResponseEntity<String> sendConnectionRequest(@RequestHeader("Authorization") String authHeader,
                                                        @PathVariable("connectionId") String connectionId){
        return ResponseEntity.ok(userService.sendConnectionRequest(authHeader,connectionId));

    }

    @GetMapping("/get_connection_request")
    public ResponseEntity<List<ConnectionRequest>> getMyConnectionRequest(@RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(userService.getMyConnectionRequest(authHeader));
    }

    @GetMapping("/what_are_my_connections")
    public ResponseEntity<List<ConnectionRequestDto>> myConnectionRequest(@RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(userService.myConnectionRequest(authHeader));

    }

    @PostMapping("/update_connection_status/{requestId}")
    public ResponseEntity<String> updateConnectionStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String requestId,
            @RequestParam("action") String action) {

        String response = userService.updateConnectionStatus(authHeader, requestId, action);
        return ResponseEntity.ok(response);
    }


}
