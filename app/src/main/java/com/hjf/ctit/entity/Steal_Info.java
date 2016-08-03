package com.hjf.ctit.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HJianFei on 2016-6-5.
 */
public class Steal_Info implements Serializable {

    /**
     * result : 1
     * msg : 警报！有货物被盗
     * data : [{"barcode":"444","decription":"卓保达","imgPath":"clothesPic\\pic-444.png","created":"2016-07-28 13:04"}]
     */

    private int result;
    private String msg;
    /**
     * barcode : 444
     * decription : 卓保达
     * imgPath : clothesPic\pic-444.png
     * created : 2016-07-28 13:04
     */

    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String barcode;
        private String decription;
        private String imgPath;
        private String created;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getDecription() {
            return decription;
        }

        public void setDecription(String decription) {
            this.decription = decription;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }
}
