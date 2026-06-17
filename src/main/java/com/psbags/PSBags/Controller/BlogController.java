package com.psbags.PSBags.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;

import com.psbags.PSBags.DTO.requests.BlogRequest;
import com.psbags.PSBags.DTO.response.BlogResponse;
import com.psbags.PSBags.Model.Blog;
import com.psbags.PSBags.Service.BlogService;

@RestController
@RequestMapping("/admin")
public class BlogController {
    
    @Autowired
    private BlogService blogService;

    @PostMapping(value = "/addBlog", consumes = "multipart/form-data")
    public ResponseEntity<?> addBlog(@ModelAttribute BlogRequest blogRequest) {
        try {
            // Validate required fields
            if (blogRequest.getTitle() == null || blogRequest.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new BlogResponse(0, "Title is required", null));
            }
            if (blogRequest.getVideo() == null || blogRequest.getVideo().isEmpty()) {
                return ResponseEntity.badRequest().body(new BlogResponse(0, "Video file is required", null));
            }
            if (blogRequest.getThumbnail() == null || blogRequest.getThumbnail().isEmpty()) {
                return ResponseEntity.badRequest().body(new BlogResponse(0, "Thumbnail file is required", null));
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            BlogResponse response = blogService.addBlog(blogRequest, email);
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BlogResponse(0, "Upload failed: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BlogResponse(0, "Error creating blog: " + e.getMessage(), null));
        }
    }

    @PutMapping("/updateBlog/{id}")
    public ResponseEntity<?> updateBlog(
            @PathVariable int id,
            @ModelAttribute BlogRequest request) {
        try {
            // Validate required fields for update
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new BlogResponse(id, "Title is required", null));
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            BlogResponse response = blogService.updateBlog(id, request, email);
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BlogResponse(id, "Upload failed: " + e.getMessage(), null));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BlogResponse(id, e.getMessage(), null));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BlogResponse(id, "Error updating blog: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BlogResponse(id, "Error updating blog: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/deleteBlog/{id}")
    public ResponseEntity<BlogResponse> deleteBlog(@PathVariable int id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            BlogResponse response = blogService.deleteBlog(id, email);
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BlogResponse(id, "Failed to delete media: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BlogResponse(id, "Error deleting blog: " + e.getMessage(), null));
        }
    }

    @GetMapping("/blogs")
    public ResponseEntity<?> getAllBlogs() {
        try {
            List<Blog> blogs = blogService.getAllBlogs();
            return ResponseEntity.ok(blogs);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving blogs: " + e.getMessage());
        }
    }
}