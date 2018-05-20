package com.hub.anyspot.vo;

/**
 * Created by yeseul on 2018-02-17.
 */

public class StoreFileVO {
    private int atch_file_id;
    private int file_index;
    private String file_stre_url;
    private String stre_file_name;
    private String orign_file_name;
    private String file_extn;
    private String fileURL;

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public int getAtch_file_id() {
        return atch_file_id;
    }

    public void setAtch_file_id(int atch_file_id) {
        this.atch_file_id = atch_file_id;
    }

    public int getFile_index() {
        return file_index;
    }

    public void setFile_index(int file_index) {
        this.file_index = file_index;
    }

    public String getFile_stre_url() {
        return file_stre_url;
    }

    public void setFile_stre_url(String file_stre_url) {
        this.file_stre_url = file_stre_url;
    }

    public String getStre_file_name() {
        return stre_file_name;
    }

    public void setStre_file_name(String stre_file_name) {
        this.stre_file_name = stre_file_name;
    }

    public String getOrign_file_name() {
        return orign_file_name;
    }

    public void setOrign_file_name(String orign_file_name) {
        this.orign_file_name = orign_file_name;
    }

    public String getFile_extn() {
        return file_extn;
    }

    public void setFile_extn(String file_extn) {
        this.file_extn = file_extn;
    }
}
