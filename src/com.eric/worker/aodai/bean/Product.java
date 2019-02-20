package com.eric.worker.aodai.bean;

import java.util.Arrays;

public class Product {

    private String _id;
    private String productId;
    private String productName;
    private Float weight;
    private String cNotes;
    private String storeId;
    private String[] desc;
    private boolean deleted;
    private boolean active;
    private boolean virtual;
    private String[] images;
    private String currency;
    private Float price;
    private Float originPrice;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getcNotes() {
        return cNotes;
    }

    public void setcNotes(String cNotes) {
        this.cNotes = cNotes;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String[] getDesc() {
        return desc;
    }

    public void setDesc(String[] desc) {
        this.desc = desc;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Float originPrice) {
        this.originPrice = originPrice;
    }

    @Override
    public String toString() {
        return
                "名称:      " + productName + '\n' +
                        "重量:      " + weight + '\n' +
                        "简介:      " + cNotes + '\n' +
                        "详情:      " + Arrays.toString(desc) + '\n' +
                        "图片:      " + Arrays.toString(images) + '\n' +
                        "单位:      " + currency + '\n' +
                        "现价:      " + price / 100.0f + '\n' +
                        "原价:      " + originPrice / 100.0f + '\n' + '\n';
    }
}
