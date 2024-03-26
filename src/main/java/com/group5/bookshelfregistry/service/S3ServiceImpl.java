//package com.group5.bookshelfregistry.service;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.model.PutObjectResult;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.net.URL;
//
//@Service
//public class S3ServiceImpl implements  BookUploadService {
//    @Value("${s3.bucket.name}")
//    private String bucketName;
//    @Value("${aws.accessKeyId}")
//    private  String accessKeyId;
//    @Value("${aws.secretKey}")
//    private String secretKey;
//
//    private final AmazonS3 amazonS3;
//
//    public S3ServiceImpl() {
//        this.amazonS3 =  AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretKey)))
//                .build();
//    }
//
//    public String uploadBook(MultipartFile file) {
//        String fileName = file.getOriginalFilename();
//        String key = "documents/" + fileName;
//
//        try {
//            amazonS3.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), null)
//                    .withCannedAcl(CannedAccessControlList.PublicRead));
//            URL url = amazonS3.getUrl(bucketName, key);
//
//            return url.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
