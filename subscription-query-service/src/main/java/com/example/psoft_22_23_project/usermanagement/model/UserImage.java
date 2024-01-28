package com.example.psoft_22_23_project.usermanagement.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prefix;
    private String fileName;
    private String fileDownloadUri;
    private String contentType;
    private long fileSize;

    protected UserImage() {
    }

    public UserImage(String prefix, String fileName, String fileDownloadUri, String contentType, long fileSize) {
        this.prefix = prefix;
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }
}

