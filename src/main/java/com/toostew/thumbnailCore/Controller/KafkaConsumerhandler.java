package com.toostew.thumbnailCore.Controller;


import com.toostew.thumbnailCore.Entities.Thumbnail;
import com.toostew.thumbnailCore.Entities.ThumbnailRequest;
import com.toostew.thumbnailCore.Service.PDFService;
import com.toostew.thumbnailCore.Service.R2Service;
import com.toostew.thumbnailCore.Service.ThumbnailService;
import com.toostew.thumbnailCore.exceptions.RestControllerHandlerException;
import com.toostew.thumbnailCore.exceptions.ThumbnailServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Component
public class KafkaConsumerhandler {

    //NOT USED, @KAFKALISTENER REQUIRES CONSTANT
    @Value("${kafka.topic}")
    private String kafkaTopic;

    @Value("${First.Bucket.Name}")
    private String firstBucket;

    @Value("${Thumbnail.Bucket.Name}")
    private String thumbnailBucket;

    @Value("${Thumbnail.Height}")
    private int thumbnailHeight;

    @Value("${Thumbnail.Width}")
    private int  thumbnailWidth;

    private R2Service r2Service;
    private ThumbnailService thumbnailService;
    private PDFService pdfService;

    public KafkaConsumerhandler(R2Service r2Service, ThumbnailService thumbnailService, PDFService pdfService) {
        this.r2Service = r2Service;
        this.thumbnailService = thumbnailService;
        this.pdfService = pdfService;
    }

    //think of this like a while loop that constantly checks the "generate-thumbnail" topic
    //everytime a new message comes in, this method will run and when it terminates
    //will increment the "offset" on kafka
    //the offset is basically the bookmark. everything behind the bookmark is "settled"
    //unless the offset is reset all the messages before the offset will not be rerun
    //
    @KafkaListener(id = "kafka-thumbnail-consumer", topics = "generate-thumbnail")
    public void kafkaConsumer(ThumbnailRequest thumbnailRequest){
        System.out.println(thumbnailRequest);

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
            System.out.println("kafka request fulfilled");


        } catch (ThumbnailServiceException e) {
            throw new RestControllerHandlerException("Rest Controller Handler: Couldn't create thumbnail", e);
        } catch (Exception e){
            throw new  RestControllerHandlerException("Rest Controller Handler: Couldn't create thumbnail", e);
        }


    }

}
