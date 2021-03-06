package com.javakit.data.exception;

/**
 * Created by cdts on 21/03/2017.
 */
public enum NetError {


    Success(NetErrorCode.Success, "成功"),
    BadRequest(NetErrorCode.BadRequest, "系统错误");

    private int code;
    private String message;


    private NetError(int code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Error: [" + getCode() + "] " + getMessage();
    }
}
