package com.toostew.thumbnailCore.Service;


import com.toostew.thumbnailCore.AWSConfig.AWSServiceClientSource;
import com.toostew.thumbnailCore.exceptions.R2ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.io.OutputStream;


//service for communicating with the R2 server
@Service
public class R2Service {


    private S3Client s3Client;

    @Value("${First.Bucket.Name}")
    private String bucketName;

    public R2Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }




    public InputStream getImageAsInputStream(String stored_name) {
        try {
            ResponseInputStream<GetObjectResponse> temp = s3Client.getObject(GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(stored_name)
                    .build());

            return temp;
        } catch (NoSuchKeyException e) {
            throw new R2ServiceException("R2Service: no such key", e);
        } catch (S3Exception e) {
            throw new R2ServiceException("R2Service: s3 exception", e);
        } catch (Exception e) {
            throw new  R2ServiceException("R2Service: unknown issue", e);
        }

    }
}
