package com.toostew.thumbnailCore.Service;


import com.toostew.thumbnailCore.exceptions.PDFServiceException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;


//service for manipulating PDF documents
@Service
public class PDFService {

    public BufferedImage renderFirstPageAsBufferedImage(PDDocument pdfDocument){
        try {
            return new PDFRenderer(pdfDocument).renderImage(0);
        } catch (IOException e) {
            throw new PDFServiceException("PDF Service: IO Exception", e);
        } catch (Exception e) {
            throw new  PDFServiceException("PDF Service: Unknown issue", e);
        }
    }

    public BufferedImage renderFirstPageWithBufferedInputStream(BufferedInputStream bufferedInputStream){
        try (PDDocument pdfDocument = PDDocument.load(bufferedInputStream)) {
            return new PDFRenderer(pdfDocument).renderImage(0);
        } catch (IOException e) {
            throw new PDFServiceException("PDF Service: IO Exception", e);
        } catch (Exception e) {
            throw new  PDFServiceException("PDF Service: Unknown issue", e);
        }

    }



}
