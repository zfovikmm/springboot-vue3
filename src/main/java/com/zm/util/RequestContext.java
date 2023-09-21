package com.zm.util;


import java.io.Serializable;

public class RequestContext implements Serializable {

    //ThreadLocal线程的本地变量  10-3节
    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestContext.remoteAddr.set(remoteAddr);
    }

}
