package com.ibcon.sproject.smet.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by s_shmakov on 24.06.2017.
 */
public class UploadedFile {
    private static final long serialVersionUID = 13L;
    private MultipartFile multipartFile;

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
