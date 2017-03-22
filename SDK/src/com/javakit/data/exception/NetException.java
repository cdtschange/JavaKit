package com.javakit.data.exception;

/**
 * Created by cdts on 21/03/2017.
 */
public class NetException extends RuntimeException {
    public NetError error;

    public NetException(NetError error) {
        super(error.toString());
        this.error = error;
    }
}
