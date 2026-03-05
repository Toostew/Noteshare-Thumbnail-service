package com.toostew.thumbnailCore.DAO;

import com.toostew.thumbnailCore.exceptions.ThumbnailDAOException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.toostew.thumbnailCore.Entities.Thumbnail;

@Repository
public class ThumbnailDAO {

    private EntityManager entityManager;

    public ThumbnailDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Transactional
    public void createThumbnail(Thumbnail thumbnail) {
        try {
            entityManager.persist(thumbnail);
        } catch (EntityExistsException e) {
            throw new ThumbnailDAOException("ThumbnailDAO: Thumbnail already exists", e);
        } catch (IllegalArgumentException e) {
            throw new ThumbnailDAOException("ThumbnailDAO: Illegal Arguments", e);
        } catch (Exception e) {
            throw new ThumbnailDAOException("ThumbnailDAO: Unexpected Error", e);
        }
    }

    public Thumbnail getThumbnail(int id) {
        try {
            return entityManager.find(Thumbnail.class, id);
        } catch (EntityNotFoundException e) {
            throw new ThumbnailDAOException("ThumbnailDAO: Thumbnail not found", e);
        } catch (IllegalArgumentException e) {
            throw new ThumbnailDAOException("ThumbnailDAO: Illegal Arguments", e);
        } catch (Exception e) {
            throw new ThumbnailDAOException("ThumbnailDAO: Unexpected Error", e);
        }

    }

    @Transactional
    public void updateThumbnail(Thumbnail thumbnail) {
        try {
            entityManager.merge(thumbnail);
        } catch (EntityNotFoundException e) {
            throw new ThumbnailDAOException("ThumbnailDAO: Thumbnail not found", e);
        } catch (IllegalArgumentException e) {
            throw new ThumbnailDAOException("ThumbnailDAO: Illegal Arguments", e);
        } catch (Exception e) {
            throw new ThumbnailDAOException("ThumbnailDAO: Unexpected Error", e);
        }
    }

    @Transactional
    public void deleteThumbnail(int id) {
        try {
            System.out.println("Attempting to delete thumbnail with id " + id);
            Thumbnail temp = entityManager.find(Thumbnail.class, id);
            entityManager.remove(temp);
        } catch (EntityNotFoundException e) {
            throw new ThumbnailDAOException("ThumbnailDAO: Thumbnail not found", e);
        } catch (IllegalArgumentException e) {
            throw new ThumbnailDAOException("ThumbnailDAO: Illegal Arguments", e);
        } catch (Exception e) {
            throw new ThumbnailDAOException("ThumbnailDAO: Unexpected Error", e);
        }
        System.out.println("Deleted thumbnail with id " + id);

    }


}
