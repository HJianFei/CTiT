package com.hjf.ctit.entity;

/**
 * Created by HJianFei on 2016-5-28.
 */
public class MessageBean {


    /**
     * hex : e2004074850e01700210f6de
     * memoryBanks : [{"memory":0,"length":1},{"memory":"69943916600488803976703178462","length":96},{"memory":0,"length":1},{"memory":0,"length":1},{"memory":0,"length":1}]
     */

    private TagBean tag;
    /**
     * tag : {"hex":"e2004074850e01700210f6de","memoryBanks":[{"memory":0,"length":1},{"memory":"69943916600488803976703178462","length":96},{"memory":0,"length":1},{"memory":0,"length":1},{"memory":0,"length":1}]}
     * antennaID : 3
     * timestamp : 1464746244495
     * readerID : LLRP_1
     * extraInformation : {"FIRSTSEENUTC":"1464746244495973","ROSPEC":"1","LASTSEENUTC":"1464746244495973","AIRPROT_CRC":"46438","AIRPROT_PC":"12288","TAGSEENCOUNT":"1","RSSI":"-40","CHANNELINDEX":"1","SPECINDEX":"1","INVPARAMSPECID":"40"}
     */

    private int antennaID;
    private long timestamp;
    private String readerID;

    public TagBean getTag() {
        return tag;
    }

    public void setTag(TagBean tag) {
        this.tag = tag;
    }

    public int getAntennaID() {
        return antennaID;
    }

    public void setAntennaID(int antennaID) {
        this.antennaID = antennaID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getReaderID() {
        return readerID;
    }

    public void setReaderID(String readerID) {
        this.readerID = readerID;
    }

    public static class TagBean {
        private String hex;

        public String getHex() {
            return hex;
        }

        public void setHex(String hex) {
            this.hex = hex;
        }
    }
}
