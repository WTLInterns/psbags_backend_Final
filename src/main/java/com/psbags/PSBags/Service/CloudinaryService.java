package com.psbags.PSBags.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public Map upload(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "products"));
    }

    public void delete(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    // Blog-specific upload methods
    public Map uploadBlogVideo(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                    "folder", "blogs/videos",
                    "resource_type", "video"
                ));
    }

    public Map uploadBlogThumbnail(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "blogs/thumbnails"));
    }

    // Generic delete method for any media type
    public void deleteMedia(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
