package com.boots3.fileHandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.boots3.fileHandler.service.StorageService;


@RestController
@RequestMapping("/storage")
public class StorageController {
	 @Autowired
	    private StorageService service;

	 @PostMapping("/upload/{bucketName}")
	    public ResponseEntity<String> uploadFile(@PathVariable String bucketName, @RequestParam("file") MultipartFile file) {
	    	
	    	if(service.isBucketValid(bucketName)) {
	    		return new ResponseEntity<>(service.uploadFile(bucketName, file), HttpStatus.OK);
	    	} else {
	    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    	}        
	    }

	    @GetMapping("/download/{bucketName}/{fileName}")
	    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("bucketName") String bucketName, @PathVariable("fileName") String fileName) {
	    	
	    	if(service.isBucketValid(bucketName)) {
	    		byte[] data = service.downloadFile(bucketName, fileName);
	            ByteArrayResource resource = new ByteArrayResource(data);
	            return ResponseEntity
	                    .ok()
	                    .contentLength(data.length)
	                    .header("Content-type", "application/octet-stream")
	                    .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
	                    .body(resource);
	    	} else {
	    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    	}        
	    }
}
