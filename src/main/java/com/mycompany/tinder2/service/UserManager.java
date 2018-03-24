package com.mycompany.tinder2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.tinder2.model.internal.UserVectors;
import com.mycompany.tinder2.model.pages.User;
import com.mycompany.tinder2.model.vk.GroupResponce;
import com.mycompany.tinder2.model.vk.GroupVK;
import com.mycompany.tinder2.model.vk.UserVK;
import com.mycompany.tinder2.model.vk.UserResponse;
import com.mycompany.tinder2.model.vk.VKResponse;
import com.mycompany.tinder2.model.vk.WallPost;
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
import org.apache.commons.lang.StringUtils;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
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
   @Autowired
   VectorManager vectorManager;
   @Autowired
   GroupManager groupManager;
   @Autowired
   LinkManager linkManager;
   private Map<Integer, Map<String, Integer>> user2vector = new HashMap<Integer, Map<String, Integer>>();
   private Map<Integer, UserVK> id2user = new HashMap<Integer, UserVK>();
   private Map<Integer, UserVectors> id2userVectors = new HashMap<Integer, UserVectors>();
   private Set<Integer> processedUsers = new HashSet<Integer>();
    
    @PostConstruct
    public void init() {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            
            File processedUsersFile = new File("C:\\demonetData\\processedUsers.txt");
            if(processedUsersFile.exists()){
                for(String userId : FileUtils.readLines(processedUsersFile, "utf-8")){
                    processedUsers.add(toInt(userId));
                }
            }
            
            File user2vectorFile = new File("C:\\demonetData\\user2vector.txt");
            if(user2vectorFile.exists()){
                for(String line : FileUtils.readLines(user2vectorFile, "utf-8")){
                    String[] parts = line.split("\t");
                    user2vector.put(toInt(parts[0]), (Map<String, Integer>) objectMapper.readValue(parts[1], new TypeReference<Map<String, Integer>>(){}));
                }
            }
            
            File id2userFile = new File("C:\\demonetData\\id2user.txt");
            if(id2userFile.exists()){
                for(String line : FileUtils.readLines(id2userFile, "utf-8")){
                    String[] parts = line.split("\t");
                    id2user.put(toInt(parts[0]), (UserVK) objectMapper.readValue(parts[1], UserVK.class));
                }
            }
            
            File id2userVectorsFile = new File("C:\\demonetData\\id2userVectors.txt");
            if(id2userVectorsFile.exists()){
                for(String line : FileUtils.readLines(id2userVectorsFile, "utf-8")){
                    String[] parts = line.split("\t");
                    id2userVectors.put(toInt(parts[0]), (UserVectors) objectMapper.readValue(parts[1], UserVectors.class));
                }
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
            return new ArrayList<WallPost>();
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
    
    private void addWallVectors(UserVectors user) throws IOException, InterruptedException{
        Map<String, Integer> wallMediaVector = new TreeMap<String, Integer>();
        Map<String, Integer> wallTextVector = new TreeMap<String, Integer>();
        Map<String, Integer> wallHelpRepostVector = new TreeMap<String, Integer>();
        
        int postCount = 0;
        for(int i = 0; i < 200; i+=100){
            Thread.currentThread().sleep(300);
            List<WallPost> posts = wallPosts(user.getId(), i, 100);
            for(WallPost post : posts){
                postCount++;
                if(post.getOwnerId() == post.getFromId() && !post.getText().equals("")){
                    wallTextVector = vectorManager.sumVectors(wallTextVector, VectorUtils.text2vector(post.getText()));  
                }else if(post.getCopyHistory()  != null && !post.getCopyHistory().isEmpty()){
                    if( (int) post.getCopyHistory().get(0).get("owner_id") < 0){
                        wallMediaVector = vectorManager.sumVectors(wallMediaVector, VectorUtils.text2vector((String) post.getCopyHistory().get(0).get("text")));
                    }else{
                        wallHelpRepostVector = vectorManager.sumVectors(wallHelpRepostVector, VectorUtils.text2vector((String) post.getCopyHistory().get(0).get("text")));
                    }
                
                }
            }
            if(posts.size() < 100){
                break;
            }
        }
        
        user.setWallTextVector(wallTextVector);
        user.setWallMediaVector(wallMediaVector);
        user.setWallHelpRepostVector(wallHelpRepostVector);
    }
    
   private Map<String, Integer> userInfoVector(UserVK userVK) throws IOException, InterruptedException{
       Map<String, Integer>  result = new HashMap<String, Integer>();
       
       if(userVK.getAbout() != null && !userVK.getAbout().equals("")){
           result =  VectorManager.text2vector(userVK.getAbout());
       }
       if(userVK.getActivities() != null && !userVK.getActivities().equals("")){
           result =  VectorManager.sumVectors(result, VectorManager.text2vector(userVK.getActivities()));
       }
       if(userVK.getBooks() != null && !userVK.getBooks().equals("")){
           result =  VectorManager.sumVectors(result, VectorManager.text2vector(userVK.getBooks()));
       }
       
       return result;
   }
    
   public List<User> users(Collection<Integer> ids) throws IOException, InterruptedException{ 
       List<User> result = new ArrayList<User>();
       
       for(Integer id: ids){
           UserVK userVK = userVK(id);
           User user = new User();

           user.setName(userVK.getFirstName());
           user.setSername(userVK.getLastName());
           user.setUrlPhoto(userVK.getPhotoURL());
           user.setId(userVK.getId());
           
           result.add(user);
       }
       
       return result;
   }
   
   
   
   
   public UserVectors userVectors(Integer userId) throws IOException, InterruptedException{
       if(id2userVectors.containsKey(userId)){
           return id2userVectors.get(userId);
       }
       
       UserVectors result = new UserVectors(userId);
       
       UserVK userVK = userVK(userId);
       result.setFirstName(userVK.getFirstName());
       result.setLastName(userVK.getLastName());
       result.setPhoto(userVK.getPhotoURL());
       result.setCity(userVK.getCity() != null ? userVK.getCity().getTitle(): null);
       result.setInfoVector(userInfoVector(userVK));
       addWallVectors(result);
       
       result.setGroups(groupManager.groups(linkManager.groups(userId)));
       ObjectMapper objectMapper = new ObjectMapper();
       FileUtils.write(new File("C:\\demonetData\\id2user.txt"), userId + "\t" + objectMapper.writeValueAsString(result) + "\n", "utf-8", true);
       return result;
   }
   
   
    public UserVK userVK(Integer userId) throws IOException, InterruptedException{
        if(id2user.containsKey(userId)){
            return id2user.get(userId);
        }
        
        Thread.currentThread().sleep(300);
        String fields = "about,activities,bdate,books,city,country,education,games,home_town,interests,sex,photo_100";
        
        String url = "https://api.vk.com/method/users.get?v=5.71&access_token=" + loginManager.getAccessToken() + "&user_ids=" + userId + "&fields=" + fields ;
    
        String request = Utils.getRequest(url);
        
        ObjectMapper objectMapper = new ObjectMapper();
        UserResponse userResponse = (UserResponse) objectMapper.readValue(request, UserResponse.class);
        
        UserVK result = (UserVK) userResponse.getResponse().get(0);
        
        id2user.put(userId, result);
        FileUtils.write(new File("C:\\demonetData\\id2user.txt"), userId + "\t" + objectMapper.writeValueAsString(result) + "\n", "utf-8", true);
        
        return result;
    }
    
    public List<UserVK> usersVK(Collection<Integer> ids) throws IOException, InterruptedException{
        List<UserVK> result = new ArrayList<UserVK>();
        List<Integer> missingUserIds = new ArrayList<Integer>();
        for(Integer id: ids){
            if(id2user.containsKey(id)){
                result.add(id2user.get(id));
            }else{
                missingUserIds.add(id);
            }
        }
        
        String fields = "about,activities,bdate,books,city,country,education,games,home_town,interests,sex,photo_100";
        String url = "https://api.vk.com/method/users.get?v=5.71&access_token=" + loginManager.getAccessToken() + "&user_ids=" + StringUtils.join(missingUserIds.toArray(), ",") + "&fields=" + fields;
        
        String request = Utils.getRequest(url);
        
        ObjectMapper objectMapper = new ObjectMapper();
        UserResponse userResponse = (UserResponse) objectMapper.readValue(request, UserResponse.class);
        
        result.addAll(userResponse.getResponse());
        
        return result;
    }
    
    
    
//    public Map<String, Integer> vector(Integer userId) throws IOException, InterruptedException{
//        if(user2vector.containsKey(userId)){
//           return user2vector.get(userId); 
//        }
//        Map<String, Integer> result =  userInfoVector(userId);
//        result = VectorManager.sumVectors(result, wallVector(userId));
//        user2vector.put(userId, result);
//        
//        ObjectMapper objectMapper = new ObjectMapper();
//        FileUtils.write(new File("C:\\demonetData\\user2vector.txt"), userId + "\t" + objectMapper.writeValueAsString(result) + "\n", "UTF-8", true);
//        
//        return result;
//    }
    
    public static void main(String[] args) throws IOException, InterruptedException{
        UserManager gm = new UserManager();
        gm.init();
       
//        gm.vector(173963709);
//        System.err.println("");
    }
}
