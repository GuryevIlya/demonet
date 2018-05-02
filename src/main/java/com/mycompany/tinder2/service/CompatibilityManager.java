package com.mycompany.tinder2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.tinder2.model.internal.Stat;
import com.mycompany.tinder2.model.internal.UserVectors;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class CompatibilityManager {
    @Autowired
    private UserManager userManager;
    @Autowired
    private LinkManager linkManager;
    
    Map<String, Stat> couple2Stat = new HashMap<String, Stat>();
    
    
    @PostConstruct
    public void init() {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            
            File couple2CompatibilityFile = new File("C:\\demonetData\\couple2Compatibility.txt");
            if(couple2CompatibilityFile.exists()){
                for(String line : FileUtils.readLines(couple2CompatibilityFile, "utf-8")){
                    String[] parts = line.split("\t");
                    if(!couple2Stat.containsKey(parts[0])){
                        couple2Stat.put(parts[0], new Stat());
                    }
                    String[] users = parts[0].split("_");
                    couple2Stat.get(parts[0]).setUserId1(toInt(users[0]));
                    couple2Stat.get(parts[0]).setUserId2(toInt(users[1]));
                    couple2Stat.get(parts[0]).setCompatibility(toDouble(parts[1]));
                }
            }
            
            File couple2CommonFriendsCountFile = new File("C:\\demonetData\\couple2CommonFriendsCount.txt");
            if(couple2CommonFriendsCountFile.exists()){
                for(String line : FileUtils.readLines(couple2CommonFriendsCountFile, "utf-8")){
                    String[] parts = line.split("\t");
                    if(!couple2Stat.containsKey(parts[0])){
                        couple2Stat.put(parts[0], new Stat());
                    }
                    String[] users = parts[0].split("_");
                    couple2Stat.get(parts[0]).setUserId1(toInt(users[0]));
                    couple2Stat.get(parts[0]).setUserId2(toInt(users[1]));
                    couple2Stat.get(parts[0]).setCommonFriendCount(toInt(parts[1]));
                }
            }
        }catch(Exception e){
            int i = 8;
        }
    }
    
    private String coupleId(Integer userId1, Integer userId2){
        if(userId1 <= userId2){
            return userId1 + "_" + userId2;
        }else{
            return userId2 + "_" + userId1;
        }
    }
   
    private Stat stat(Integer userId1, Integer userId2){
        String coupleId = coupleId(userId1, userId2); 
        if(couple2Stat.containsKey(coupleId)){
            return couple2Stat.get(coupleId);
        }
       
        Stat stat = new Stat();
        couple2Stat.put(coupleId, stat);
        
        return stat; 
    }
    
    private Map<Integer, Stat> stats(Integer user1Id, Collection<Integer> ids){
        Map<Integer, Stat> result = new HashMap<Integer, Stat>();
        
        for(Integer user2Id: ids){
            String coupleId = coupleId(user1Id, user2Id); 
            if(couple2Stat.containsKey(coupleId)){
                result.put(user2Id, couple2Stat.get(coupleId));
            }else{
                Stat stat = new Stat();
                result.put(user2Id, stat);
                couple2Stat.put(coupleId, stat);
            }
        }
        
        return result; 
    }
    
//    public Double getCompatibility(Integer userId1, Integer userId2) throws IOException, InterruptedException{
//        Map<String, Integer> vector1 = userManager.vector(userId1);
//        Map<String, Integer> vector2 = userManager.vector(userId2);
//    
//        Double result = VectorUtils.cosSim(vector1, vector2);
//        
//        FileUtils.write(new File("C:\\demonetData\\couple2Compatibility.txt"), couple.toString() + "\t" + result + "\n", "UTF-8", true);
//        
//        couple2Compatibility.put(couple.toString(), result);
//        return result;
//    }
   
    public Map<Integer, Stat> getProximityStat(Integer userId, Collection<Integer> friendsIds) throws IOException, InterruptedException{
        Map<Integer, Stat> result = new HashMap<Integer, Stat>();
        
        Map<Integer, Stat> stats = stats(userId, friendsIds);
        Set<Integer> missingStats = new HashSet<Integer>();
        for (Map.Entry<Integer, Stat> entry : stats.entrySet()) {
            if(entry.getValue().getCommonFriendCount() == null){
                missingStats.add(entry.getKey());
            }
        }
        
        Map<Integer, Integer> userId2CommonFriendCount = linkManager.user2CommonFriendsCount(userId, missingStats);
        for (Map.Entry<Integer, Stat> entry : stats.entrySet()) {
            Stat stat = entry.getValue();
            if(stat.getCommonFriendCount() == null  && userId2CommonFriendCount.get(entry.getKey()) != null){
                stat.setUserId1(userId);
                stat.setUserId2(entry.getKey());
                stat.setCommonFriendCount(userId2CommonFriendCount.get(entry.getKey()));
                result.put(entry.getKey(), stat);
            }else if(stat.getCommonFriendCount() != null){
                result.put(entry.getKey(), stat);
            }
        }
        
        return result;
    }
    
    public void addCommonFriendsCount(Integer userId1, Integer userId2, Integer count){
        stat(userId1, userId2).setCommonFriendCount(count);
    }

    public Double compatibility(UserVectors user1, UserVectors user2){
       // Double result = VectorUtils.cosSim(user1.getWallVector(), user2.getWallVector());
        Double result = 4.4;
        return result;
    }
    
//    public void processCompatibility(Integer userId, Collection<Integer> friendsIds) throws IOException, InterruptedException{
//        for(Integer friendId: friendsIds){
//            Stat stat = stat(userId, friendId);
//            
//            UserVectors user1 = userManager.user(userId);
//            UserVectors user2 = userManager.user(friendId);
//
//            Double compatibility  = compatibility(user1, user2);
//            stat.setCompatibility(compatibility);
//            
//            FileUtils.write(new File("C:\\demonetData\\couple2Compatibility.txt"), coupleId(userId, friendId) + "\t" + compatibility + "\n", "UTF-8", true);
//        
//        }
//    }

    public void processCommonFriendsCount(Integer userId) throws IOException, InterruptedException{
        Map<Integer, Integer> friendsOfFriends = linkManager.friendsOfFriends(userId);
        
        for (Map.Entry<Integer, Integer> entry : friendsOfFriends.entrySet()) {
            FileUtils.write(new File("C:\\demonetData\\couple2CommonFriendsCount.txt"), coupleId(userId, entry.getKey()) + "\t" + entry.getValue() + "\n", "UTF-8", true);
        }
    }


}
