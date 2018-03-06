package com.mycompany.tinder2.service;

import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class BackgroundProcessManager {
    @Autowired 
    private LinkManager linkManager;
    @Autowired 
    private LoginManager loginManager;
    @Autowired
    private CompatibilityManager compatibilityManager;
    
    public void friendsOfFriendsCompatibility() throws IOException, InterruptedException{
        for (Map.Entry<Integer, Integer> en : linkManager.friendsOfFriends(loginManager.getVkId()).entrySet()) {
            compatibilityManager.addCommonFriendsCount(loginManager.getVkId() ,en.getKey() ,en.getValue());
        }
    }
    
}
