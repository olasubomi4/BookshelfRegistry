package com.group5.bookshelfregistry.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
public class S3ServiceImpl implements  BookUploadService {
    @Value("${s3.bucket.name}")
    private String bucketName;
    @Value("${aws.accessKeyId}")
    private  String secretKey;
    @Value("${aws.secretKey}")
    private String accessKeyId;

    private final AmazonS3 amazonS3;

    public S3ServiceImpl() {
        this.amazonS3 =  AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(System.getenv("AWS_ACCESS_KEY_ID"), System.getenv("AWS_SECRET_KEY")))).withRegion(Regions.EU_WEST_2)
                .build();
    }

    public String upload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String key = "documents/" +UUID.randomUUID() +"_"+fileName;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            // Compliant: specifies the content length of the stream.
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            URL url = amazonS3.getUrl(bucketName, key);

            return url.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String update(MultipartFile file, String existingBookLocation) {

        if(existingBookLocation!=null) {
            if(!delete(existingBookLocation)) {
                return null;
            }
        }
        return upload(file);
    }

    @Override
    public Boolean delete(String existingBookLocation) {
        try {
            if (existingBookLocation != null) {
                AmazonS3URI key= new AmazonS3URI(existingBookLocation);
                amazonS3.deleteObject(bucketName,key.getKey());
                return true;
            }
            return false;
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

}
