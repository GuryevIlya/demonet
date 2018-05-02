package com.mycompany.tinder2.service;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class BackgroundProcessManager extends Thread{
    @Autowired 
    private UsersUploader usersUploader;
    
    @PostConstruct
    public void init() {
        usersUploader.start();
    }
    
    
}
