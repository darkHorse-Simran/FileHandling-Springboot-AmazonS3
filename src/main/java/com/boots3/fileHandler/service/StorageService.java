package com.boots3.fileHandler.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageService {
	@Value("${cloud.aws.credentials.endpointUrl}")
	private String endpointUrl;

	@Value("${application.bucket.name}")
	private String bucketName;

	@Autowired
	private AmazonS3 s3Client;

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		s3Client.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public String uploadFile(String bucketName, MultipartFile multipartFile) {
		String fileUrl = "";
		try {
			File file = convertMultiPartFileToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
        //s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        //fileObj.delete();
			 uploadFileTos3bucket(fileName, file); 
			 file.delete();
        System.out.println("File uploaded: " + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
    }

	
	private File convertMultiPartFileToFile(MultipartFile file) {
		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
			fos.write(file.getBytes());
		} catch (IOException e) {
			// log.error("Error converting multipartFile to file", e);
			e.printStackTrace();
		}
		return convertedFile;
	}
	

    public byte[] downloadFile(String bucketName, String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            System.out.println("File not found");;
        }
        return null;
    }

	public boolean isBucketValid(String bucketName) {		
		if(s3Client.doesBucketExistV2(bucketName)) {
			return true;
		}		
		return false;
	}


}
