package com.boots3.fileHandler.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.amazonaws.services.s3.model.*;

import com.boots3.fileHandler.service.StorageService;

@SpringBootTest
@AutoConfigureMockMvc
public class StorageControllerTest {
    @Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StorageService service;
	// set Mock behavior
	
	@Test
	public void testBucketPresentOrNot() {
		String bucketName = "testbucketforfileupload";
	when(service.isBucketValid(bucketName)).thenReturn(true); 
	}
	
	@Test 
    public void deleteFileTest() { 
		String bucketName = "testbucketforfileupload";
		String fileName = "1629689882133-features.png";
        service.deleteFile(bucketName, fileName); 
        verify(service).deleteFile(bucketName, fileName); 
    }
	
	 @Test 
	    public void downloadFileTest() { 
		 String bucketName = "testbucketforfileupload";
			String fileName = "1629689882133-features.png";
	        service.downloadFile(bucketName, fileName); 
	        verify(service).downloadFile(bucketName, fileName); 
	    } 
	 @Test 
	    public void uploadFileTest() { 
		 String bucketName = "testbucketforfileupload";
			String fileName = "simran.png";
	        File file = mock(File.class); 
	        PutObjectResult result = mock(PutObjectResult.class); 
	        when(service.putObject(anyString(), anyString(), (File) any())).thenReturn(result); 
	        
	        assertThat(service.putObject(bucketName, fileName, file)).isEqualTo(result); 
	        verify(service).putObject(bucketName, fileName, file); 
	    }
	
    
}
