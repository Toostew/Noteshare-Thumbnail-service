package com.toostew.thumbnailCore.RESTController;


import com.toostew.thumbnailCore.Entities.Thumbnail;
import com.toostew.thumbnailCore.Service.ThumbnailService;
import com.toostew.thumbnailCore.exceptions.RestControllerHandlerException;
import com.toostew.thumbnailCore.exceptions.ThumbnailServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.json.JsonMapper;

//at it's core the thumbnail service is a RESTFUL service that receives thumbnail requests, converts file
//to appropriate thumbnail, thne uploads to MYSQL and R2
@RestController
public class RestControllerHandler {

    private ThumbnailService thumbnailService;
    private JsonMapper jsonMapper;

    public RestControllerHandler(ThumbnailService thumbnailService, JsonMapper jsonMapper) {
        this.thumbnailService = thumbnailService;
        this.jsonMapper = jsonMapper;
    }

    @PostMapping("/test")
    public void createThumbnail(@RequestBody Thumbnail thumbnail) {
        try {

            thumbnailService.createThumbnail(thumbnail);
        } catch (ThumbnailServiceException e) {
            throw new RestControllerHandlerException("Rest Controller Handler: Couldn't create thumbnail", e);
        }

    }


}
