package com.mycompany.tinder2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.tinder2.model.vk.CommonFriends;
import com.mycompany.tinder2.model.vk.VKResponse;
import static com.mycompany.tinder2.service.Utils.getRequest;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class LinkManager {
    @Autowired 
    LoginManager loginManager;
    Map<Integer, Set<Integer>> user2friends = new HashMap<Integer, Set<Integer>>();
    Map<Integer, Set<Integer>> user2groups = new HashMap<Integer, Set<Integer>>();
    Map<Integer, Map<Integer, Integer>> user2friendsOfFriend = new HashMap<Integer, Map<Integer, Integer>>();
    Map<Integer, Map<Integer, Integer>> user2user2commonFriendsCount = new HashMap<Integer, Map<Integer, Integer>>();
    
    @PostConstruct
    public void init() {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            
            File user2friendsFile = new File("C:\\demonetData\\user2friends.txt");
            if(user2friendsFile.exists()){
                for(String line : FileUtils.readLines(user2friendsFile, "utf-8")){
                    String[] parts = line.split("\t");
                    user2friends.put(toInt(parts[0]), (Set<Integer>) objectMapper.readValue(parts[1], new TypeReference<Set<Integer>>(){}));
                }
            }
            
            File user2groupsFile = new File("C:\\demonetData\\user2groups.txt");
            if(user2groupsFile.exists()){
                for(String line : FileUtils.readLines(user2groupsFile, "utf-8")){
                    String[] parts = line.split("\t");
                    user2groups.put(toInt(parts[0]), (Set<Integer>) objectMapper.readValue(parts[1], new TypeReference<Set<Integer>>(){}));
                }
            }
            
            File user2friendsOfFriendFile = new File("C:\\demonetData\\user2friendsOfFriend.txt");
            if(user2friendsOfFriendFile.exists()){
                for(String line : FileUtils.readLines(user2friendsOfFriendFile, "utf-8")){
                    String[] parts = line.split("\t");
                    user2friendsOfFriend.put(toInt(parts[0]), (Map<Integer, Integer>) objectMapper.readValue(parts[1], new TypeReference<Map<Integer, Integer>>(){}));
                }
            }
            
            File user2user2commonFriendsCountFile = new File("C:\\demonetData\\user2user2commonFriendsCount.txt");
            if(user2user2commonFriendsCountFile.exists()){
                for(String line : FileUtils.readLines(user2user2commonFriendsCountFile, "utf-8")){
                    String[] parts = line.split("\t");
                    if(!user2user2commonFriendsCount.containsKey(toInt(parts[0]))){
                        user2user2commonFriendsCount.put(toInt(parts[0]), new HashMap<Integer, Integer>());
                    }
                    user2user2commonFriendsCount.get(toInt(parts[0])).put(toInt(parts[1]), toInt(parts[2]));
                }
            }
        }catch(Exception e){
            int i = 8;
        }
    }
    
    public Collection<Integer> friends(Integer userId) throws IOException, InterruptedException{
       if(user2friends.containsKey(userId)){
           return user2friends.get(userId);
       }
       Thread.currentThread().sleep(300);
       String url = "https://api.vk.com/method/friends.get?user_id=" + userId + "&v=5.52&access_token=" + loginManager.getAccessToken();          
       String request = getRequest(url);
       
       ObjectMapper objectMapper = new ObjectMapper();
       VKResponse<Integer> response = (VKResponse<Integer>) objectMapper.readValue(request, new TypeReference<VKResponse<Integer>>(){});
       
       if(response.getResponse() != null){
           user2friends.put(userId, new HashSet(response.getResponse().getItems()));
           FileUtils.write(new File("C:\\demonetData\\user2friends.txt"), userId + "\t" + objectMapper.writeValueAsString(response.getResponse().getItems()) + "\n", "utf-8", true);
           return response.getResponse().getItems();
       }else{
           return null;
       }
       
   }
   
    public Integer commonFriendsCount(Integer user1Id, Integer user2Id) throws IOException{
        if(!user2user2commonFriendsCount.containsKey(user1Id)){
            user2user2commonFriendsCount.put(user1Id, new HashMap<Integer, Integer>());
        }    
       
        if(!user2user2commonFriendsCount.get(user1Id).containsKey(user2Id)){
             String url = "https://api.vk.com/method/friends.getMutual?v=5.52&access_token=" + loginManager.getAccessToken() + "&source_uid=" + user1Id + "&target_uid=" + user2Id;
             String responseJSON = Utils.getRequest(url);
       
             ObjectMapper objectMapper = new ObjectMapper();
             CommonFriends response = (CommonFriends) objectMapper.readValue(responseJSON, new TypeReference<CommonFriends>(){});
        }
        
        return user2user2commonFriendsCount.get(user1Id).get(user2Id);
    }
    
    
    public Map<Integer, Integer> user2CommonFriendsCount(Integer userId, Collection<Integer> ids) throws IOException, InterruptedException{
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
       
        if(!user2user2commonFriendsCount.containsKey(userId)){
            user2user2commonFriendsCount.put(userId, new HashMap<Integer, Integer>());
        }
        Map<Integer, Integer> alreadyCalculated = user2user2commonFriendsCount.get(userId);

        for(Integer id: ids){
            if(alreadyCalculated.containsKey(id)){
               result.put(id, alreadyCalculated.get(id));
            }else{
                Thread.currentThread().sleep(300);
                String url = "https://api.vk.com/method/friends.getMutual?v=5.52&access_token=" + loginManager.getAccessToken() + "&source_uid=" + userId + "&target_uid=" + id;
                String responseJSON = Utils.getRequest(url);

                ObjectMapper objectMapper = new ObjectMapper();
                try{
                    CommonFriends response = (CommonFriends) objectMapper.readValue(responseJSON, new TypeReference<CommonFriends>(){});
                    if(response.getResponse() != null){
                        FileUtils.write(new File("C:\\demonetData\\user2user2commonFriendsCount.txt"), userId + "\t" + id + "\t" + response.getResponse().size() + "\n", "utf-8", true);
                        result.put(id, response.getResponse().size());
                    }else{
                        result.put(id, null);
                    }
                }catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
          
        return result;
    }
    
    
    public Map<Integer, Integer> friendsOfFriends(Integer userId) throws IOException, InterruptedException{
       Map<Integer, Integer> result = new HashMap<Integer, Integer>();
       
       if(user2friendsOfFriend.containsKey(userId)){
           return user2friendsOfFriend.get(userId);
       }
       
       Set<Integer> friends = new HashSet<Integer>(friends(userId));
       
       for(Integer friend: friends){
           Collection<Integer> friendsOfFriend = friends(friend);
           if(friendsOfFriend != null){
               for(Integer id : friendsOfFriend){
                   if(friends.contains(id)){
                       continue;
                   }
                   Integer oldValue = result.get(id);
                   if(oldValue != null){
                       result.put(id, oldValue + 1);
                   }else{
                       result.put(id, 1);
                   }
               }
           }
       }
       result.remove(loginManager.getVkId());
       
       user2friendsOfFriend.put(userId, result);
       if(!user2user2commonFriendsCount.containsKey(userId)){
           user2user2commonFriendsCount.put(userId, new HashMap<Integer, Integer>());
       }
       user2user2commonFriendsCount.get(userId).putAll(result);
       
       ObjectMapper objectMapper = new ObjectMapper();
       FileUtils.write(new File("C:\\demonetData\\user2friendsOfFriend.txt"), userId + "\t" + objectMapper.writeValueAsString(result) + "\n", "utf-8", true);
       return result;
    }
    
    
    public Set<Integer> groups(Integer userId) throws IOException{
        String url = "https://api.vk.com/method/groups.get?v=5.52&access_token=" + loginManager.getAccessToken() + "&user_id=" + userId + "&fields=description";
        String responseJSON = Utils.getRequest(url);
       
        ObjectMapper objectMapper = new ObjectMapper();
        VKResponse<Integer> response = (VKResponse<Integer>) objectMapper.readValue(responseJSON, new TypeReference<VKResponse<Integer>>(){});

        if(response.getResponse() != null){
            FileUtils.write(new File("C:\\demonetData\\user2groups.txt"), userId + "\t" + objectMapper.writeValueAsString(response.getResponse().getItems()) + "\n", "utf-8", true);
            return new HashSet<Integer>(response.getResponse().getItems());
        }else{
            return null;
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException{
        LinkManager lm = new LinkManager();
       
        lm.groups(96274720);
        System.err.println("");
    } 
}
