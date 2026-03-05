package com.toostew.thumbnailCore.Entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

@Entity
public class Thumbnail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int id;

    @Column(name = "stored_name")
    private String stored_name;

    @Column(name = "file_records_id")
    private int file_records_id;




    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public String getStored_name() {
        return stored_name;
    }
    public void setStored_name(String stored_name) {
        this.stored_name = stored_name;
    }


    public int getFile_records_id() {
        return file_records_id;
    }
    public void setFile_records_id(int file_records_id) {
        this.file_records_id = file_records_id;
    }
}
