package com.example.homework.entity;

import android.graphics.drawable.Icon;
import android.media.Image;

public class MemberVO {

    private Icon icon;
    private String name;
    private String phone;

    public MemberVO(Icon icon, String name, String phone) {
        this.icon = icon;
        this.name = name;
        this.phone = phone;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setImage(Icon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
