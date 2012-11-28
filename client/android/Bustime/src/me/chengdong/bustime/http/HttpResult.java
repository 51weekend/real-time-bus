package me.chengdong.bustime.http;

public class HttpResult {
    private int code = 0;
    private String response = "";

    public HttpResult(int code) {
        super();
        this.code = code;
    }

    public HttpResult(int code, String resp) {
        super();
        this.code = code;
        this.response = resp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return this.code == 0;
    }

    @Override
    public String toString() {
        return "code[" + code + "],resp[" + response + "]";
    }

}