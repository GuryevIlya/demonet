package com.mycompany.tinder2.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class UsersUploader extends Thread{
    @Autowired 
    private LinkManager linkManager;
    @Autowired 
    private UserManager userManager;
    @Autowired 
    private LoginManager loginManager;
   
    UsersUploader(){
        super("UsersUploader");
    }
    
    @Override
    public void run() {
        try {
            Collection<Integer> friendsOfFriends = linkManager.friendsOfFriends(loginManager.getVkId()).keySet();
            for (Integer id : friendsOfFriends) {
                userManager.userVectors(id);
                userManager.user(id);
            }
        } catch (IOException ex) {
            Logger.getLogger(BackgroundProcessManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(BackgroundProcessManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
