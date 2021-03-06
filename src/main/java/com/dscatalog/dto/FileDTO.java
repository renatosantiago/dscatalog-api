package com.dscatalog.dto;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class FileDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private MultipartFile file;

  public MultipartFile getFile() {
    return this.file;
  }

  public void setFile(MultipartFile file) {
    this.file = file;
  }

}
