package com.javakit.data.exception;

import com.javakit.data.log.Log;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by cdts on 21/03/2017.
 */
public class NetException extends RuntimeException {
    public NetError error;

    public NetException(NetError error) {
        super(error.toString());
        this.error = error;
    }


    public static NetException BusinessException(String message, Exception e) {
        NetError error = NetError.BadRequest;
        if (StringUtils.isEmpty(message)) {
            message = e.getMessage();
        } else {
            message = message + ": " + e.getMessage();
        }
        error.setMessage(message);
        Log.error(message, error);
        return new NetException(error);
    }
    public static NetException BusinessException(String message) {
        NetError error = NetError.BadRequest;
        error.setMessage(message);
        Log.error(message, error);
        return new NetException(error);
    }
    public static NetException BusinessException(Exception e) {
        return BusinessException("", e);
    }
}
