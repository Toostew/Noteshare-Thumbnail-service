package com.toostew.thumbnailCore.RESTController;


import com.toostew.thumbnailCore.Entities.Thumbnail;
import com.toostew.thumbnailCore.Entities.ThumbnailRequest;
import com.toostew.thumbnailCore.Service.R2Service;
import com.toostew.thumbnailCore.Service.ThumbnailService;
import com.toostew.thumbnailCore.exceptions.RestControllerHandlerException;
import com.toostew.thumbnailCore.exceptions.ThumbnailServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

//at it's core the thumbnail service is a RESTFUL service that receives thumbnail requests, converts file
//to appropriate thumbnail, thne uploads to MYSQL and R2
@RestController
public class RestControllerHandler {

    private ThumbnailService thumbnailService;
    private JsonMapper jsonMapper;
    private R2Service r2Service;


    @Value("${First.Bucket.Name}")
    private String firstBucket;

    @Value("${Thumbnail.Bucket.Name}")
    private String thumbnailBucket;

    public RestControllerHandler(ThumbnailService thumbnailService, JsonMapper jsonMapper,
                                 R2Service r2Service) {
        this.thumbnailService = thumbnailService;
        this.jsonMapper = jsonMapper;
        this.r2Service = r2Service;
    }

    @GetMapping("/test")
    public void createThumbnail(@RequestBody ThumbnailRequest thumbnailRequest) {
        try {
            BufferedInputStream test = r2Service.getImageFromR2AsBufferedInputStream(thumbnailRequest.getStored_name());
            BufferedImage step = thumbnailService.generateThumbnailFromInputStream(200,200,test,thumbnailRequest.getContent_type());
            byte[] data = thumbnailService.bufferedImageToByteArray(step,thumbnailRequest.getContent_type());
            InputStream stream = new ByteArrayInputStream(data);
            r2Service.postObjectToR2(thumbnailBucket,"test",stream,data.length,thumbnailRequest.getContent_type());


            System.out.println("Received Inputstream proper");
        } catch (ThumbnailServiceException e) {
            throw new RestControllerHandlerException("Rest Controller Handler: Couldn't create thumbnail", e);
        } catch (IOException e){
            throw new  RestControllerHandlerException("Rest Controller Handler: Couldn't create thumbnail", e);
        }
    }

    @GetMapping("/test/{id}")
    public Thumbnail getThumbnail(@PathVariable int id) {
        try {
            return thumbnailService.getThumbnail(id);
        } catch (ThumbnailServiceException e) {
            throw new RestControllerHandlerException("Rest Controller Handler: Couldn't get thumbnail", e);
        }
    }




}
