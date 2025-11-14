package com.example.backend.service;

import com.example.backend.config.CurrentUserUtil;
import com.example.backend.dto.*;
import com.example.backend.model.*;
import com.example.backend.model.type.Status;
import com.example.backend.repo.*;
import com.example.backend.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final ProfileRepo profileRepo;

    private final JwtService jwtService;

    private final CurrentUserUtil currentUserUtil;

    private final EducationRepo educationRepo;
    private final WorkRepo workRepo;

    private final ConnectionsRepo connectionsRepo;


    public String register(@Valid RegistryDto registryDto) {
        User user1=userRepo.findByEmail(registryDto.getEmail());
        if(user1!=null){
            throw new IllegalArgumentException("User Exist");
        }
        User user=User.builder()
                        .name(registryDto.getName())
                        .username(registryDto.getUsername())
                        .email(registryDto.getEmail())
                        .password(passwordEncoder.encode(registryDto.getPassword()))
                        .createdAt(new Date())
                        .token("")
                        .active(true)
                        .build();

        userRepo.save(user);
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setBio("");
        profile.setCurrentPost("");
        profile.setPastWork(new ArrayList<>());
        profile.setEducation(new ArrayList<>());
        profileRepo.save(profile);
        return "User Saved in Db";
    }

    public String login(@Valid LoginDto loginDto) {
        User user=userRepo.findByEmail(loginDto.getEmail());
        if(user==null){
            throw new IllegalArgumentException("User does not exist");
        }
        if(!passwordEncoder.matches(loginDto.getPassword(),user.getPassword())) {
            throw new IllegalArgumentException("Password not matches");
        }
        String token = jwtService.generateToken(user);
        user.setToken(token);
        userRepo.save(user);
        return token;
    }

    public String updateProfilePicture(MultipartFile file, String authHeader) {
        try{
            User user=currentUserUtil.getCurrentUser(authHeader);
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs(); // create folder if not exists
            }

// Create unique file name (to avoid overwriting old images)
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

// Full path on your disk
            File destination = new File(dir, fileName);
            file.transferTo(destination);

// Public URL that client can access
            String fileUrl = "http://localhost:8080/uploads/" + fileName;
            user.setProfilePicture(fileUrl);
            userRepo.save(user);

            return "Profile picture updated";
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateUserProfile(String authHeader, @Valid UpdateUserDto newUserData) {

        try{
            User user=currentUserUtil.getCurrentUser(authHeader);
            User existing = userRepo.findByName(newUserData.getName());
            if (existing != null && !existing.getId().equals(user.getId())) {
                throw new IllegalArgumentException("Username already taken");
            }

            user.setName(newUserData.getName());
            user.setEmail(newUserData.getEmail()!=null? newUserData.getEmail(): user.getEmail());
            userRepo.save(user);

            return "User profile updated successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String updateProfileData(String authHeader, @Valid ProfileDto profileDto) {

        User user=currentUserUtil.getCurrentUser(authHeader);
        Profile profile=profileRepo.findByUser(user);
        if (profileDto.getBio() != null) profile.setBio(profileDto.getBio());
        if (profileDto.getCurrentPost() != null) profile.setCurrentPost(profileDto.getCurrentPost());
        profile.setUser(user);
        try {
            for (Work work : profileDto.getPastWork()) {

                workRepo.save(work);
            }
            for (Education education : profileDto.getEducation()) {
                educationRepo.save(education);
            }
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to update profile: " + e.getMessage());
        }

        if (profileDto.getPastWork() != null) profile.setPastWork(profileDto.getPastWork());
        if (profileDto.getEducation() != null) profile.setEducation(profileDto.getEducation());

        profileRepo.save(profile);
        return "Profile updated";
    }

    public UserProfileResponse getUserAndProfile(String authHeader) {
        User user=currentUserUtil.getCurrentUser(authHeader);
        Profile profile=profileRepo.findByUser(user);
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .bio(profile != null ? profile.getBio() : "")
                .currentPost(profile != null ? profile.getCurrentPost() : "")
                .pastWork(profile != null ? profile.getPastWork() : new ArrayList<>())
                .education(profile != null ? profile.getEducation() : new ArrayList<>())
                .build();
    }

    public List<UserResponseWithProfile> getAllUserProfile() {
        List<Profile> profiles = profileRepo.findAll();
        List<UserResponseWithProfile> result = new ArrayList<>();

        for (Profile profile : profiles) {
            result.add(new UserResponseWithProfile(profile));
        }

        return result;
    }

    public UserProfileResponse getUserProfileAndUserBasedOnUsername(String username){
        User user=userRepo.findByUsername(username);
        if(user==null){
            throw new IllegalArgumentException("User Name Not Found");
        }
        Profile profile=profileRepo.findByUser(user);
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .bio(profile != null ? profile.getBio() : "")
                .currentPost(profile != null ? profile.getCurrentPost() : "")
                .pastWork(profile != null ? profile.getPastWork() : new ArrayList<>())
                .education(profile != null ? profile.getEducation() : new ArrayList<>())
                .build();
    }


    public String sendConnectionRequest(String authHeader, String connectionId) {
        User currentUser=currentUserUtil.getCurrentUser(authHeader);
        User targetUser=userRepo.findById(connectionId).orElseThrow(
                () -> new IllegalArgumentException("Target user not found")
        );
        if (currentUser.getId().equals(targetUser.getId())) {
            throw new IllegalArgumentException("You cannot connect with yourself!");
        }
        if (connectionsRepo.existsByUserIdAndConnectionId(currentUser.getId(), targetUser.getId()) ||
                connectionsRepo.existsByUserIdAndConnectionId(targetUser.getId(), currentUser.getId())) {
            throw new IllegalArgumentException("Connection request already exists!");
        }


        ConnectionRequest connection = ConnectionRequest.builder()
                .userId(currentUser.getId())
                .connectionId(targetUser.getId())
                .accepted(Status.PENDING)
                .build();

        connectionsRepo.save(connection);
        return "Connection Sent";
    }

    public List<ConnectionRequest> getMyConnectionRequest(String authHeader) {
        User user=currentUserUtil.getCurrentUser(authHeader);
        return connectionsRepo.findByUserId(user.getId());
    }

    public List<ConnectionRequestDto> myConnectionRequest(String authHeader) {
        User currentUser = currentUserUtil.getCurrentUser(authHeader);

        // Get all requests where current user is the receiver (connectionId)
        List<ConnectionRequest> requests = connectionsRepo.findByConnectionId(currentUser.getId());

        // Convert to DTOs
        List<ConnectionRequestDto> result = new ArrayList<>();

        for (ConnectionRequest request : requests) {
            // Get sender user details
            User sender = userRepo.findById(request.getUserId())
                    .orElse(null);

            if (sender != null) {
                result.add(ConnectionRequestDto.builder()
                        .id(request.getId())
                        .userId(sender.getId())
                        .username(sender.getUsername())
                        .name(sender.getName())
                        .profilePicture(sender.getProfilePicture())
                        .connectionId(request.getConnectionId())
                        .accepted(request.getAccepted())
                        .build());
            }
        }

        return result;
    }

    public String updateConnectionStatus(String authHeader, String requestId, String action) {
        User user = currentUserUtil.getCurrentUser(authHeader);

        ConnectionRequest request = connectionsRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (!request.getConnectionId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to update this request");
        }

        Status newStatus;
        try {
            newStatus = Status.valueOf(action.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid action: must be one of ACCEPTED, REJECTED, BLOCKED");
        }

        request.setAccepted(newStatus);
        connectionsRepo.save(request);

        return "Connection request " + newStatus.name().toLowerCase();
    }

}
