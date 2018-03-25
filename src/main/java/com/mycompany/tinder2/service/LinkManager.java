package com.mycompany.tinder2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.tinder2.model.vk.GroupVK;
import com.mycompany.tinder2.model.vk.Items;
import com.mycompany.tinder2.model.vk.UserVK;
import com.mycompany.tinder2.model.vk.VKResponse;
import com.mycompany.tinder2.model.vk.WallPost;
import static com.mycompany.tinder2.service.Utils.getRequest;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
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
    Map<Integer, Map<Integer, Integer>> user2friendsOfFriend = new HashMap<Integer, Map<Integer, Integer>>();
    Map<Integer, Map<Integer, Integer>> user2user2commonFriendsCount = new HashMap<Integer, Map<Integer, Integer>>();
    
    @PostConstruct
    public void init() {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            
           
            File user2friendsFile = new File("C:\\demonetData\\user2friends.txt");
            if(user2friendsFile.exists()){
                user2friends = objectMapper.readValue(user2friendsFile, new TypeReference<Map<Integer, Set<Integer>>>(){});
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
           objectMapper.writeValue(new File("C:\\demonetData\\user2friends.txt"), user2friends);
           
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
             VKResponse<Integer> response = (VKResponse<Integer>) objectMapper.readValue(responseJSON, new TypeReference<VKResponse<Integer>>(){});
        }
        
        return user2user2commonFriendsCount.get(user1Id).get(user2Id);
   }
    
    
    public Map<Integer, Integer> user2CommonFriendsCount(Integer userId, Collection<Integer> ids) throws IOException{
       Map<Integer, Integer> result = new HashMap<Integer, Integer>();
       
       Map<Integer, Integer> alreadyCalculated = user2user2commonFriendsCount.get(userId);
       for(Integer id: ids){
           if(alreadyCalculated.containsKey(id)){
               result.put(id, alreadyCalculated.get(id));
           }else{
               String url = "https://api.vk.com/method/friends.getMutual?v=5.52&access_token=" + loginManager.getAccessToken() + "&source_uid=" + userId + "&target_uid=" + id;
               String responseJSON = Utils.getRequest(url);
       
               ObjectMapper objectMapper = new ObjectMapper();
               VKResponse<Integer> response = (VKResponse<Integer>) objectMapper.readValue(responseJSON, new TypeReference<VKResponse<Integer>>(){});
           }
       }
       return result;
   }
    
    
    public Map<Integer, Integer> friendsOfFriends(Integer userId) throws IOException, InterruptedException{
       Map<Integer, Integer> result = new HashMap<Integer, Integer>();
       
       if(user2friendsOfFriend.containsKey(userId)){
           return user2friendsOfFriend.get(userId);
       }
       
       Collection<Integer> friends = friends(userId);
       
       for(Integer friend: friends){
           Collection<Integer> friendsOfFriend = friends(friend);
           if(friendsOfFriend != null){
               for(Integer id : friendsOfFriend){
                   Integer oldValue = result.get(id);
                   if(oldValue != null){
                       result.put(id, oldValue + 1);
                   }else{
                       result.put(id, 1);
                   }
               }
           }
       }
       
       user2friendsOfFriend.put(userId, result);
       if(!user2user2commonFriendsCount.containsKey(userId)){
           user2user2commonFriendsCount.put(userId, new HashMap<Integer, Integer>());
       }
       user2user2commonFriendsCount.get(userId).putAll(result);
       return result;
   }
    
    
    public Set<Integer> groups(Integer userId) throws IOException{
        
        String url = "https://api.vk.com/method/groups.get?v=5.52&access_token=" + loginManager.getAccessToken() + "&user_id=" + userId + "&fields=description";
        String responseJSON = Utils.getRequest(url);
       
        ObjectMapper objectMapper = new ObjectMapper();
        VKResponse<Integer> response = (VKResponse<Integer>) objectMapper.readValue(responseJSON, new TypeReference<VKResponse<Integer>>(){});

        return new HashSet<Integer>(response.getResponse().getItems());
   }
    
    public static void main(String[] args) throws IOException, InterruptedException{
        LinkManager lm = new LinkManager();
       
        lm.groups(96274720);
        System.err.println("");
    } 
}
