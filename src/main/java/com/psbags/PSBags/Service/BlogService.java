package com.psbags.PSBags.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psbags.PSBags.DTO.requests.BlogRequest;
import com.psbags.PSBags.DTO.response.BlogResponse;
import com.psbags.PSBags.Exception.CustomException;
import com.psbags.PSBags.Model.Blog;
import com.psbags.PSBags.Model.User;
import com.psbags.PSBags.Repo.BlogRepo;
import com.psbags.PSBags.Repo.UserRepo;

@Service
public class BlogService {

    @Autowired
    private BlogRepo blogRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    public BlogResponse addBlog(BlogRequest blogRequest, String email) throws IOException {
        User user = this.userRepo.findByEmail(email).orElseThrow();

        // Validate required fields
        if (blogRequest.getVideo() == null || blogRequest.getVideo().isEmpty()) {
            throw new IOException("Video file is required");
        }
        if (blogRequest.getThumbnail() == null || blogRequest.getThumbnail().isEmpty()) {
            throw new IOException("Thumbnail file is required");
        }

        Blog blog = new Blog();
        LocalDateTime now = LocalDateTime.now();

        blog.setTitle(blogRequest.getTitle());
        blog.setSlug(generateSlug(blogRequest.getTitle()));
        blog.setDescription(blogRequest.getDescription());
        blog.setIsActive(blogRequest.getIsActive());

        // Parallel uploads using CompletableFuture (following Product pattern)
        List<CompletableFuture<Void>> uploadFutures = new ArrayList<>();

        // Video upload
        CompletableFuture<Void> videoFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return cloudinaryService.uploadBlogVideo(blogRequest.getVideo());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload video", e);
            }
        }).thenAccept(uploadResult -> {
            blog.setVideoUrl(uploadResult.get("secure_url").toString());
            blog.setVideoPublicId(uploadResult.get("public_id").toString());
        });
        uploadFutures.add(videoFuture);

        // Thumbnail upload
        CompletableFuture<Void> thumbnailFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return cloudinaryService.uploadBlogThumbnail(blogRequest.getThumbnail());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload thumbnail", e);
            }
        }).thenAccept(uploadResult -> {
            blog.setThumbnailUrl(uploadResult.get("secure_url").toString());
            blog.setThumbnailPublicId(uploadResult.get("public_id").toString());
        });
        uploadFutures.add(thumbnailFuture);

        // Wait for all uploads to complete before saving to database
        try {
            CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            throw new IOException("One or more uploads failed", e);
        }

        // Set date and time (following Product pattern)
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = now.format(dateFormatter);
        blog.setDate(date);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = now.format(timeFormatter);
        blog.setTime(time);

        blogRepo.save(blog);

        return new BlogResponse(blog.getId(), "Blog Added Successfully", blog.getTitle());
    }

    public BlogResponse updateBlog(int blogId, BlogRequest request, String email) throws IOException {
        User user = this.userRepo.findByEmail(email).orElseThrow();

        Blog blog = blogRepo.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found with id " + blogId));

        blog.setTitle(request.getTitle());
        blog.setSlug(generateSlug(request.getTitle()));
        blog.setDescription(request.getDescription());
        blog.setIsActive(request.getIsActive());

        // Handle video update (following Product image update pattern)
        if (request.getVideo() != null && !request.getVideo().isEmpty()) {
            // Delete old video if exists
            if (blog.getVideoPublicId() != null && !blog.getVideoPublicId().isEmpty()) {
                cloudinaryService.delete(blog.getVideoPublicId());
            }

            // Upload new video
            Map<String, Object> videoUploadResult = cloudinaryService.uploadBlogVideo(request.getVideo());
            blog.setVideoUrl(videoUploadResult.get("secure_url").toString());
            blog.setVideoPublicId(videoUploadResult.get("public_id").toString());
        }

        // Handle thumbnail update (following Product image update pattern)
        if (request.getThumbnail() != null && !request.getThumbnail().isEmpty()) {
            // Delete old thumbnail if exists
            if (blog.getThumbnailPublicId() != null && !blog.getThumbnailPublicId().isEmpty()) {
                cloudinaryService.delete(blog.getThumbnailPublicId());
            }

            // Upload new thumbnail
            Map<String, Object> thumbnailUploadResult = cloudinaryService.uploadBlogThumbnail(request.getThumbnail());
            blog.setThumbnailUrl(thumbnailUploadResult.get("secure_url").toString());
            blog.setThumbnailPublicId(thumbnailUploadResult.get("public_id").toString());
        }

        blogRepo.save(blog);

        return new BlogResponse(
                blog.getId(),
                "Blog updated successfully!",
                blog.getTitle()
        );
    }

    public BlogResponse deleteBlog(int id, String email) throws IOException {
        User user = this.userRepo.findByEmail(email).orElseThrow();

        if (!blogRepo.existsById(id)) {
            return new BlogResponse(id, "Blog not found!", null);
        }

        Blog blog = blogRepo.getReferenceById(id);

        // Delete associated media from Cloudinary (following Product delete pattern)
        if (blog.getThumbnailPublicId() != null && !blog.getThumbnailPublicId().isEmpty()) {
            cloudinaryService.delete(blog.getThumbnailPublicId());
        }
        if (blog.getVideoPublicId() != null && !blog.getVideoPublicId().isEmpty()) {
            cloudinaryService.delete(blog.getVideoPublicId());
        }

        blogRepo.delete(blog);
        return new BlogResponse(id, "Blog deleted successfully!", blog.getTitle());
    }

    public List<Blog> getAllBlogs() {
        return blogRepo.findAll();
    }

    public Blog getByIdBlogId(int id) {
        return this.blogRepo.findById(id).orElseThrow(() -> new CustomException("Blog not found with id " + id));
    }

    public Blog getBlogBySlug(String slug) {
        return this.blogRepo.findBySlug(slug).orElseThrow(() -> new CustomException("Blog not found with slug " + slug));
    }

    public List<Blog> getActiveBlogs() {
        return blogRepo.findByIsActive("1");
    }

    public List<Blog> getLatestBlogs() {
        return blogRepo.findTop4ByOrderByDateDescTimeDesc();
    }

    // Helper method for generating SEO-friendly slugs
    private String generateSlug(String title) {
        if (title == null || title.trim().isEmpty()) {
            return "";
        }
        
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Remove special characters
                .replaceAll("\\s+", "-") // Replace spaces with hyphens
                .replaceAll("-+", "-") // Replace multiple hyphens with single
                .replaceAll("^-|-$", ""); // Remove leading/trailing hyphens
    }
}