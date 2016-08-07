package com.app.precared.models;

import java.util.List;

/**
 * Created by prashant on 31/7/16.
 */
public class gsonFor {

    /**
     * response : success
     * message :
     * data : [{"sender_id":3267,"recevier_id":1,"message":"test","sender_name":"Prashant patil","recevier_name":"amar ","id":1135},{"sender_id":3267,"recevier_id":1,"message":"hii","sender_name":"Prashant patil","recevier_name":"amar ","id":1134},{"sender_id":3267,"recevier_id":1,"message":"hii","sender_name":"Prashant patil","recevier_name":"amar ","id":1133},{"sender_id":3267,"recevier_id":1,"message":"haa","sender_name":"Prashant patil","recevier_name":"amar ","id":1132},{"sender_id":3267,"recevier_id":1,"message":"fine","sender_name":"Prashant patil","recevier_name":"amar ","id":1131},{"sender_id":1,"recevier_id":3267,"message":"How are yoi","sender_name":"amar ","recevier_name":"Prashant patil","id":1130},{"sender_id":3267,"recevier_id":1,"message":"hii","sender_name":"Prashant patil","recevier_name":"amar ","id":1129},{"sender_id":1,"recevier_id":3267,"message":"Hi Praashant","sender_name":"amar ","recevier_name":"Prashant patil","id":1128}]
     */

    private String response;
    private String message;
    /**
     * sender_id : 3267
     * recevier_id : 1
     * message : test
     * sender_name : Prashant patil
     * recevier_name : amar
     * id : 1135
     */

    private List<DataBean> data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int sender_id;
        private int recevier_id;
        private String message;
        private String sender_name;
        private String recevier_name;
        private int id;

        public int getSender_id() {
            return sender_id;
        }

        public void setSender_id(int sender_id) {
            this.sender_id = sender_id;
        }

        public int getRecevier_id() {
            return recevier_id;
        }

        public void setRecevier_id(int recevier_id) {
            this.recevier_id = recevier_id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSender_name() {
            return sender_name;
        }

        public void setSender_name(String sender_name) {
            this.sender_name = sender_name;
        }

        public String getRecevier_name() {
            return recevier_name;
        }

        public void setRecevier_name(String recevier_name) {
            this.recevier_name = recevier_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
