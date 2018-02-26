package com.mycompany.tinder2.service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author NPSP-MalakhovDA
 */
public class ThreadContext {
    
    private static ThreadLocal<ThreadContext> threadLocal = new ThreadLocal<ThreadContext>();
    private Map<String, String> params = new HashMap<String, String>();

    public void add(String[] params) {
        for (int i = 0; i < params.length; i+=2) {
            this.params.put(params[i], params[i + 1]);
        }
    }

    public static void set(String ... params){
        ThreadContext threadContext = threadLocal.get();
        if(threadContext == null){
            threadContext = new ThreadContext();
            threadLocal.set(threadContext);
        }
        threadContext.add(params);
    }
    
    public static String get(String param){
        if(threadLocal.get() == null){
            return null;
        }
        return threadLocal.get().params.get(param);
    }
}
