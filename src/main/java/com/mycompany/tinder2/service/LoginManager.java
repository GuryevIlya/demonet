package com.mycompany.tinder2.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class LoginManager {
    private class Passport {
        private String userId;
        private String password;
        private long accessTokenDeathTime;
        private String vkId;
        private String accessToken;
        
        Passport(String userId){
            this.userId = userId;
        }

        public String getPassword() {
            return password;
        }
        
        public String getUserId() {
            return userId;
        }

        public long getAccessTokenDeathTime() {
            return accessTokenDeathTime;
        }

        public String getVkId() {
            return vkId;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        
        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setAccessTokenDeathTime(long shelfLife) {
            this.accessTokenDeathTime = (new Date()).getTime() + shelfLife;
        }

        public void setVkId(String vkId) {
            this.vkId = vkId;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }
    
    private Map<String, Passport> userId2passport = new HashMap<String, Passport>();
    private Map<String, Passport> vkId2Passport = new HashMap<String, Passport>();
    
    public String[] getIdAndPassport(String vkId){
        String[] result = new String[2];
        Passport passport = userId2passport.get(vkId);
        
        if(passport != null){
            result[0] = passport.getUserId();
            result[1] = passport.getPassword();
        }
        return result;
    }
    
    
    private String passwordMake(){
        Random random = new Random();
        return random.nextInt() + "";
    }
    
    public  boolean userIsRegistered(String userId){
        return userId2passport.containsKey(userId);
    }
    
    public  String[] registerUser(String vkId, String accessToken){
        String userId = Integer.toString(userId2passport.size() + 1);
        Passport passport = new Passport(userId);
        passport.setAccessToken(accessToken);
        passport.setVkId(vkId);
        passport.setPassword(passwordMake());
        
        userId2passport.put(userId, passport);
        
        String[] result = new String[2]; 
        result[0] = passport.getUserId();
        result[1] = passport.getPassword();
        
        return result;
    }
    
    public String getAccessToken(String userId){
        if(!userId2passport.containsKey(userId)){
            return null;
        }
        if(userId2passport.get(userId).accessTokenDeathTime < (new Date()).getTime()){
            return null;
        }
        
        return userId2passport.get(userId).getAccessToken();
    }
    
    public void setAccessToken(String accessToken, String userId){
        userId2passport.get(userId).setAccessToken(accessToken);
    }
    
    public String getAccessToken(){
        return "209beb52b684bb6f17cb216ca74618b9d10df195cad00df81255177510bd579f252e445ad39eb5591331d";
    }
    
    public Integer getVkId(){
        return 3461250;
    }
    
    
}
