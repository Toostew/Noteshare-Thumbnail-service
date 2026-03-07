package com.toostew.thumbnailCore.Service;

import com.toostew.thumbnailCore.DAO.ThumbnailDAO;
import com.toostew.thumbnailCore.Entities.Thumbnail;
import com.toostew.thumbnailCore.exceptions.ThumbnailDAOException;
import com.toostew.thumbnailCore.exceptions.ThumbnailServiceException;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

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



}
