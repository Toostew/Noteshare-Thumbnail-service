package com.toostew.thumbnailCore.Controller;


import com.toostew.thumbnailCore.Entities.Thumbnail;
import com.toostew.thumbnailCore.Entities.ThumbnailRequest;
import com.toostew.thumbnailCore.Service.PDFService;
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
import java.io.InputStream;
import java.util.UUID;

//at it's core the thumbnail service is a RESTFUL service that receives thumbnail requests, converts file
//to appropriate thumbnail, thne uploads to MYSQL and R2
@RestController
@RequestMapping("/generate")
public class RestControllerHandler {

    private ThumbnailService thumbnailService;
    private JsonMapper jsonMapper;
    private R2Service r2Service;
    private PDFService pdfService;


    @Value("${First.Bucket.Name}")
    private String firstBucket;

    @Value("${Thumbnail.Bucket.Name}")
    private String thumbnailBucket;

    @Value("${Thumbnail.Height}")
    private int thumbnailHeight;

    @Value("${Thumbnail.Width}")
    private int  thumbnailWidth;

    public RestControllerHandler(ThumbnailService thumbnailService, JsonMapper jsonMapper,
                                 R2Service r2Service, PDFService pdfService) {
        this.thumbnailService = thumbnailService;
        this.jsonMapper = jsonMapper;
        this.r2Service = r2Service;
        this.pdfService = pdfService;
    }

    @GetMapping("/thumbnail")
    public void createThumbnail(@RequestBody ThumbnailRequest thumbnailRequest) {
        try {
            String stored_thumbnail_UUID = UUID.randomUUID().toString();
            BufferedInputStream temp = r2Service.getObjectFromR2AsBufferedInputStream(thumbnailRequest.getStored_name());

            //if the contentType is an image we will process it according to the image track
            if (thumbnailRequest.getContent_type().startsWith("image/")) {

                //creating the thumbnail
                byte[] byteTemp = thumbnailService.generateByteArrayThumbnailFromBufferedInputStream(thumbnailHeight, thumbnailWidth, temp, thumbnailRequest.getContent_type());
                int size = byteTemp.length;
                InputStream input = new ByteArrayInputStream(byteTemp);
                r2Service.postObjectToR2(thumbnailBucket, stored_thumbnail_UUID, input, size, thumbnailRequest.getContent_type());




            }
            //if the content type is a PDF.
            else if (thumbnailRequest.getContent_type().startsWith("application/pdf")) {
                BufferedImage tempImage = pdfService.renderFirstPageWithBufferedInputStream(temp);

                //creating the thumbnail
                byte[] byteTemp = thumbnailService.generateByteArrayThumbnailFromBufferedImage(thumbnailHeight, thumbnailWidth, tempImage);
                int size = byteTemp.length;
                InputStream input = new ByteArrayInputStream(byteTemp);
                r2Service.postObjectToR2(thumbnailBucket, stored_thumbnail_UUID, input, size, "image/jpeg");
            }

            //creating thumbnail entry in database
            Thumbnail tempThumbnail = new  Thumbnail();
            tempThumbnail.setStored_name(stored_thumbnail_UUID);
            tempThumbnail.setFile_records_id(thumbnailRequest.getFile_records_id());
            thumbnailService.createThumbnail(tempThumbnail);

        } catch (ThumbnailServiceException e) {
            throw new RestControllerHandlerException("Rest Controller Handler: Couldn't create thumbnail", e);
        } catch (Exception e){
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
