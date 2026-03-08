package com.toostew.thumbnailCore.Service;

import com.toostew.thumbnailCore.DAO.ThumbnailDAO;
import com.toostew.thumbnailCore.Entities.Thumbnail;
import com.toostew.thumbnailCore.exceptions.ThumbnailDAOException;
import com.toostew.thumbnailCore.exceptions.ThumbnailServiceException;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.hibernate.result.Output;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class ThumbnailService {

    private ThumbnailDAO thumbnailDAO;

    public ThumbnailService(ThumbnailDAO thumbnailDAO) {
        this.thumbnailDAO = thumbnailDAO;
    }

    public void createThumbnail(Thumbnail thumbnail) {
        try {
            thumbnailDAO.createThumbnail(thumbnail);
        } catch (ThumbnailDAOException e) {
            throw new ThumbnailServiceException("ThumbnailService: Thumbnail couldn't be created", e);
        }
    }

    public Thumbnail getThumbnail(int id) {
        try {
            return thumbnailDAO.getThumbnail(id);
        } catch (ThumbnailDAOException e) {
            throw new ThumbnailServiceException("ThumbnailService: Thumbnail couldn't be retrieved", e);
        }
    }

    public void updateThumbnail(Thumbnail thumbnail) {
        try {
            thumbnailDAO.updateThumbnail(thumbnail);
        } catch (ThumbnailDAOException e) {
            throw new ThumbnailServiceException("ThumbnailService: Thumbnail couldn't be updated", e);
        }
    }

    public void deleteThumbnail(int id) {
        try {
            thumbnailDAO.deleteThumbnail(id);
        } catch (ThumbnailDAOException e) {
            throw new ThumbnailServiceException("ThumbnailService: Thumbnail couldn't be deleted", e);
        }
    }




    //Thumbnail generation


    //return BufferedImage from InputStream
    public BufferedImage generateThumbnailFromInputStream(int h, int w, BufferedInputStream image, String format) {
        try {
            //fuck my stupid chud life
            String reformat = format.replaceFirst("image/","");
             return Thumbnails.of(image)
                    .outputFormat(reformat)
                    .size(h, w)
                     .keepAspectRatio(false)
                    .asBufferedImage();
        } catch (Exception e) {
            throw new  ThumbnailServiceException("ThumbnailService: Thumbnail couldn't be generated", e);
        }
    }

    public byte[] generateByteArrayThumbnailFromBufferedImage(int h, int w, BufferedImage image) {
        try {

            BufferedImage temp = Thumbnails.of(image)
                    .outputFormat("jpeg")
                    .size(h,w)
                    .keepAspectRatio(false)
                    .asBufferedImage();
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                ImageIO.write(temp, "jpeg", out); //technically not needed since ByteArray lives in memory
                return out.toByteArray();
            }

        } catch (IOException e) {
            throw new ThumbnailServiceException("ThumbnailService: Thumbnail couldn't be generated", e);
        }
    }

  public byte[] generateByteArrayThumbnailFromBufferedInputStream(int h, int w, BufferedInputStream image, String format) {
      try {
          //fuck my stupid chud life
          String reformat = format.replaceFirst("image/","");
          BufferedImage temp = Thumbnails.of(image)
                                .outputFormat(reformat)
                                .size(h, w)
                                .keepAspectRatio(false)
                                .asBufferedImage();

          try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
              ImageIO.write(temp, reformat, out);
              return out.toByteArray();
          }
      } catch (Exception e) {
          throw new  ThumbnailServiceException("ThumbnailService: Thumbnail couldn't be generated", e);
      }
  }




    //return byte array from BufferedImage (should probably conolidate this)
    public byte[] bufferedImageToByteArray(BufferedImage bufferedImage, String formatName) throws IOException {
        String reformat = formatName.replaceFirst("image/","");
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            ImageIO.write(bufferedImage, reformat, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new ThumbnailServiceException("ThumbnailService: Thumbnail couldn't be generated", e);
        }
    }

}
