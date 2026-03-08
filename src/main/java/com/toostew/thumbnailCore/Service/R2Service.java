package com.toostew.thumbnailCore.Service;


import com.toostew.thumbnailCore.AWSConfig.AWSServiceClientSource;
import com.toostew.thumbnailCore.exceptions.R2ServiceException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;


//service for communicating with the R2 server
@Service
public class R2Service {


    private S3Client s3Client;

    @Value("${First.Bucket.Name}")
    private String firstBucketName;

    public R2Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }



    //Buffered Input stream adds a memory buffer
    //normally, input stream reads 1 byte at a time from storage directly
    //Buffered input stream chunks to memory (8kb), and reads from memory
    public BufferedInputStream getImageFromR2AsBufferedInputStream(String stored_name) {
        try {
            ResponseInputStream<GetObjectResponse> temp = s3Client.getObject(GetObjectRequest.builder()
                    .bucket(firstBucketName)
                    .key(stored_name)
                    .build());


            return new BufferedInputStream(temp);
        } catch (NoSuchKeyException e) {
            throw new R2ServiceException("R2Service: no such key", e);
        } catch (S3Exception e) {
            throw new R2ServiceException("R2Service: s3 exception", e);
        } catch (Exception e) {
            throw new  R2ServiceException("R2Service: unknown issue", e);
        }
    }

    public void postObjectToR2(String bucket, String key, InputStream inputStream, long size, String contentType){
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromInputStream(inputStream, size));
    }


}
