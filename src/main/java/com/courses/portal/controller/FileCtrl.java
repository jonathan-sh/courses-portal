package com.courses.portal.controller;

import com.courses.portal.useful.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileCtrl {

    private final StorageService storageService;

    @Autowired
    public FileCtrl(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/file")
    public Boolean handleFileUpload(@RequestParam("file") MultipartFile file) {

      try
      {
          storageService.store(file);
          return true;
      }
      catch (Exception e)
      {
          return false;
      }

    }
}
