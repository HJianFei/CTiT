package com.hjf.ctit.entity;

/**
 * Created by HJianFei on 2016-6-6.
 */
public class Steal_Info_Detail {


    /**
     * result : 1
     * data : {"id":21,"created":"2016-07-26 15:35:22","updated":null,"barcode":"444","size":"M","color":"橙色","decription":"卓保达","price":44,"productNo":"","provider":"","placeLocation":"","season":null,"oldBarcode":null,"styleNo":"444","imgPath":"clothesPic/pic-444.png","imgFile":null}
     */

    private int result;
    /**
     * id : 21
     * created : 2016-07-26 15:35:22
     * updated : null
     * barcode : 444
     * size : M
     * color : 橙色
     * decription : 卓保达
     * price : 44.0
     * productNo :
     * provider :
     * placeLocation :
     * season : null
     * oldBarcode : null
     * styleNo : 444
     * imgPath : clothesPic/pic-444.png
     * imgFile : null
     */

    private DataBean data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String created;
        private Object updated;
        private String barcode;
        private String size;
        private String color;
        private String decription;
        private double price;
        private String productNo;
        private String provider;
        private String placeLocation;
        private String season;
        private String oldBarcode;
        private String styleNo;
        private String imgPath;
        private String imgFile;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public Object getUpdated() {
            return updated;
        }

        public void setUpdated(Object updated) {
            this.updated = updated;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getDecription() {
            return decription;
        }

        public void setDecription(String decription) {
            this.decription = decription;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getProductNo() {
            return productNo;
        }

        public void setProductNo(String productNo) {
            this.productNo = productNo;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getPlaceLocation() {
            return placeLocation;
        }

        public void setPlaceLocation(String placeLocation) {
            this.placeLocation = placeLocation;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public String getOldBarcode() {
            return oldBarcode;
        }

        public void setOldBarcode(String oldBarcode) {
            this.oldBarcode = oldBarcode;
        }

        public String getStyleNo() {
            return styleNo;
        }

        public void setStyleNo(String styleNo) {
            this.styleNo = styleNo;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getImgFile() {
            return imgFile;
        }

        public void setImgFile(String imgFile) {
            this.imgFile = imgFile;
        }
    }
}
