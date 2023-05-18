package com.sytoss.producer.services;

import com.sytoss.producer.bom.RequestContext;

public class RequestContextThreadLocal {
    private static final ThreadLocal<RequestContext> context = new ThreadLocal<>();

    public static RequestContext getRequestContext() {
        if (context.get() == null) {
            context.set( new RequestContext());
        }
        return context.get();
    }

    public static void drop() {
        context.set(null);
    }
}