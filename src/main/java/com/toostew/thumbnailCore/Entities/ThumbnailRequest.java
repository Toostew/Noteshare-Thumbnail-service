package com.toostew.thumbnailCore.Entities;


//this is the java class representing a Thumbnail request, and should include the File_recordID and the
//stored name of the file so the service can look it up directly in R2
//this pojo is not modeled in the database
public class ThumbnailRequest {

    private int file_records_id;

    private String stored_name;



    public int getFile_records_id() {
        return file_records_id;
    }
    public void setFile_records_id(int file_records_id) {
        this.file_records_id = file_records_id;
    }



    public String getStored_name() {
        return stored_name;
    }
    public void setStored_name(String stored_name) {
        this.stored_name = stored_name;
    }
}
