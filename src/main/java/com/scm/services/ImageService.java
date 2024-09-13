package com.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uplodeImage(MultipartFile picture ,String fileName);

    String getUrlFromPublicId(String publicId);

}
