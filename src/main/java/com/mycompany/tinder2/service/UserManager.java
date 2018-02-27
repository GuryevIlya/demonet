package com.mycompany.tinder2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.tinder2.model.User;
import com.mycompany.tinder2.model.UserResponse;
import com.mycompany.tinder2.model.VKResponse;
import com.mycompany.tinder2.model.WallPost;
import static com.mycompany.tinder2.service.Utils.getRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class UserManager {
   @Autowired
   LoginManager loginManager;
   private Map<Integer, Map<String, Integer>> user2vector = new HashMap<Integer, Map<String, Integer>>();
   private Map<Integer, User> id2user = new HashMap<Integer, User>();
    
    @PostConstruct
    public void init() {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            
            File user2vectorFile = new File("C:\\demonetData\\user2vector.txt");
            if(user2vectorFile.exists()){
                user2vector = objectMapper.readValue(user2vectorFile, new TypeReference<Map<Integer, Map<String, Integer>>>(){});
            }
            
            File id2userFile = new File("C:\\demonetData\\id2user.txt");
            if(id2userFile.exists()){
                id2user = objectMapper.readValue(id2userFile, new TypeReference<Map<Integer, User>>(){});
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
   
   
   
    private List<WallPost> wallPosts(Integer userId, int offset, int count) throws IOException{
        String filter = "all";
        String fields = "text";
        
        List<WallPost> result = new ArrayList<WallPost>();
        
        String url = "https://api.vk.com/method/wall.get?v=5.71&access_token=" + loginManager.getAccessToken() + "&owner_id=" + userId + "&offset=" + offset + "&count=" + count;
    
        String request = Utils.getRequest(url);
        
        ObjectMapper objectMapper = new ObjectMapper();
        VKResponse<WallPost> response = (VKResponse<WallPost>) objectMapper.readValue(request, new TypeReference<VKResponse<WallPost>>(){});
       
        if(response == null || response.getResponse() == null){
            System.err.println("");
        }
        
        return response.getResponse().getItems();
    }
    
    private Map<String, Integer> normalizeVector(Map<String, Integer> vector, int coeff){
        Map<String, Integer> result = new HashMap<String, Integer>();
        
        for (Map.Entry<String, Integer> entry : vector.entrySet()) {
            result.put(entry.getKey(), entry.getValue()/coeff);
        }
        
        return result;
    }
    
    private Map<String, Integer> wallVector(Integer userId) throws IOException, InterruptedException{
        Map<String, Integer> result = new HashMap<String, Integer>();
        
        int postCount = 0;
        for(int i = 0; i < 10000; i+=100){
            Thread.currentThread().sleep(1000);
            List<WallPost> posts = wallPosts(userId, i, 100);
            for(WallPost post : posts){
                postCount++;
                result = VectorUtils.sumVectors(result, VectorUtils.text2vector(post.getText()));
            }
            if(posts.size() < 100){
                break;
            }
        }
        
        return result;
    }
    
   private Map<String, Integer> userInfoVector(Integer userId) throws IOException, InterruptedException{
       Map<String, Integer>  result = new HashMap<String, Integer>();
       
       User user = user(userId);
       if(user.getAbout() != null && !user.getAbout().equals("")){
           result =  VectorManager.text2vector(user.getAbout());
       }
       if(user.getActivities() != null && !user.getActivities().equals("")){
           result =  VectorManager.sumVectors(result, VectorManager.text2vector(user.getActivities()));
       }
       if(user.getBooks() != null && !user.getBooks().equals("")){
           result =  VectorManager.sumVectors(result, VectorManager.text2vector(user.getBooks()));
       }
       if(user.getCity() != null){
           result =  VectorManager.sumVectors(result, VectorManager.text2vector(user.getCity().getTitle()));
       }
       
       return result;
   }
    
    
    public User user(Integer userId) throws IOException, InterruptedException{
        if(id2user.containsKey(userId)){
            return id2user.get(userId);
        }
        
        Thread.currentThread().sleep(300);
        String fields = "about,activities,bdate,books,city,country,education,games,home_town,interests,sex,photo_100";
        
        String url = "https://api.vk.com/method/users.get?v=5.71&access_token=" + loginManager.getAccessToken() + "&user_ids=" + userId + "&fields=" + fields ;
    
        String request = Utils.getRequest(url);
        
        ObjectMapper objectMapper = new ObjectMapper();
        UserResponse userResponse = (UserResponse) objectMapper.readValue(request, UserResponse.class);
        
        User result = (User) userResponse.getResponse().get(0);
        
        id2user.put(userId, result);
        objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("C:\\demonetData\\id2user.txt"), id2user);
        
        return result;
    }
    
    
    public Map<String, Integer> vector(Integer userId) throws IOException, InterruptedException{
        if(user2vector.containsKey(userId)){
           return user2vector.get(userId); 
        }
        Map<String, Integer> result =  userInfoVector(userId);
        result = VectorManager.sumVectors(result, wallVector(userId));
        user2vector.put(userId, result);
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("C:\\demonetData\\user2vector.txt"), user2vector);
        
        return result;
    }
    
    /*public static void main(String[] args) throws IOException, InterruptedException{
        UserManager gm = new UserManager();
        gm.init();
       
//        gm.vector(173963709);
//        System.err.println("");
    }*/
}
