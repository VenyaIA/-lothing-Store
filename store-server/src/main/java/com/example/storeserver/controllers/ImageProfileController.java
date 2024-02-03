package com.example.storeserver.controllers;

import com.example.storeserver.entity.ImageProfile;
import com.example.storeserver.payload.response.MessageResponse;
import com.example.storeserver.services.ImageProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("api/imageProfile")
public class ImageProfileController {

    private final ImageProfileService imageProfileService;

    @Autowired
    public ImageProfileController(ImageProfileService imageProfileService) {
        this.imageProfileService = imageProfileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageProfileCurrentCustomer(@RequestParam("file") MultipartFile file,
                                                                             Principal principal) throws IOException {
        imageProfileService.uploadImageProfileCurrentCustomer(file, principal);
        return new ResponseEntity<>(new MessageResponse("Image Upload Successfully"), HttpStatus.OK);
    }

    @GetMapping("/image")
    public ResponseEntity<ImageProfile> getImageProfileCurrentCustomer(Principal principal) {
        ImageProfile imageProfile = imageProfileService.getImageProfileCurrentCustomer(principal);

        return new ResponseEntity<>(imageProfile, HttpStatus.OK);
    }
}
