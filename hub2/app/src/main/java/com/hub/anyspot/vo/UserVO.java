package com.hub.anyspot.vo;

/**
 * Created by LYS on 2017-11-25.
 */

public class UserVO {
    private int usr_key;
    private String usr_id;
    private String usr_password;
    private String usr_name;
    private String usr_phone;

    public int getUsr_key() {
        return usr_key;
    }

    public void setUsr_key(int usr_key) {
        this.usr_key = usr_key;
    }

    public String getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(String usr_id) {
        this.usr_id = usr_id;
    }

    public String getUsr_password() {
        return usr_password;
    }

    public void setUsr_password(String usr_password) {
        this.usr_password = usr_password;
    }

    public String getUsr_name() {
        return usr_name;
    }

    public void setUsr_name(String usr_name) {
        this.usr_name = usr_name;
    }

    public String getUsr_phone() {
        return usr_phone;
    }

    public void setUsr_phone(String usr_phone) {
        this.usr_phone = usr_phone;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "usr_key=" + usr_key +
                ", usr_id='" + usr_id + '\'' +
                ", usr_password='" + usr_password + '\'' +
                ", usr_name='" + usr_name + '\'' +
                ", usr_phone='" + usr_phone + '\'' +
                '}';
    }
}
