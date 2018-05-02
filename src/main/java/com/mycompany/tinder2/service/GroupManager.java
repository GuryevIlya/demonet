package com.mycompany.tinder2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.tinder2.model.internal.Group;
import com.mycompany.tinder2.model.vk.GroupVK;
import com.mycompany.tinder2.model.vk.GroupResponce;
import com.mycompany.tinder2.model.vk.VKResponse;
import com.mycompany.tinder2.model.vk.WallPost;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class GroupManager {
    @Autowired
    VectorManager vectorManager;
    @Autowired
    LoginManager loginManager;
    
    Map<String, Map<String, Integer>> id2wallVector = new HashMap<String, Map<String, Integer>>();
    Map<String, GroupVK> id2Group = new HashMap<String, GroupVK>();
    
    @PostConstruct
    public void init() {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            
//            File id2wallVectorFile = new File("C:\\demonetData\\id2wallVector.txt");
//            if(id2wallVectorFile.exists()){
//                for(String line : FileUtils.readLines(id2wallVectorFile, "utf-8")){
//                    String[] parts = line.split("\t");
//                    id2wallVector.put(parts[0], (Map<String, Integer>) objectMapper.readValue(parts[1], new TypeReference<Map<String, Integer>>(){}));
//                }
//            }
//            
            File groupsFile = new File("C:\\demonetData\\groups.txt");
            if(groupsFile.exists()){
                for(String line : FileUtils.readLines(groupsFile, "utf-8")){
                    GroupVK group = objectMapper.readValue(line, GroupVK.class);
                    id2Group.put(group.getId(), group);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private List<WallPost> wallPosts(String groupId, int offset, int count) throws IOException{
        String filter = "all";
        String fields = "text";
        
        List<WallPost> result = new ArrayList<WallPost>();
        
        String url = "https://api.vk.com/method/wall.get?v=5.71&access_token=" + loginManager.getAccessToken() + "&owner_id=-" + groupId + "&offset=" + offset + "&count=" + count;
    
        String request = Utils.getRequest(url);
        
        ObjectMapper objectMapper = new ObjectMapper();
        VKResponse<WallPost> response = (VKResponse<WallPost>) objectMapper.readValue(request, new TypeReference<VKResponse<WallPost>>(){});
       
        if(response.getResponse() != null){
             return response.getResponse().getItems();
        }else{
             return null;
        }
    }
    
    private Map<String, Integer> wallVector(String groupId) throws IOException{
        Map<String, Integer> result = new HashMap<String, Integer>();
        
        for(int i = 0; i < 1000; i+=100){
            List<WallPost> posts = wallPosts(groupId, i, 100);
            for(WallPost post : posts){
                result = VectorUtils.sumVectors(result, vectorManager.text2vector(post.getText()));
            }
            if(posts.size() < 100){
                break;
            }
        }
        
        return result;
    }
    
    private Map<String, Integer> titleVector(String groupId) throws IOException{
        String accessToken = "39bad9ad33a23248bd7665951ea285c69d0abd05d94f21123255c61ff2651dde35f6901bd1ef02a93a941";
        String fields = "city,country,place,description,members_count";
        
        String url = "https://api.vk.com/method/groups.getById?v=5.71&access_token=" + accessToken + "&group_id=" + groupId + "&fields=" + fields;
        
        String request = Utils.getRequest(url);
        
        ObjectMapper objectMapper = new ObjectMapper();
        GroupResponce response = (GroupResponce) objectMapper.readValue(request, GroupResponce.class);
        
        return vectorManager.text2vector(response.getResponse().get(0).getDescription());
    }
    
    
    private List<GroupVK> groupFromVK(Collection<Integer> ids) throws InterruptedException, IOException{
        String fields = "city,country,place,description,members_count";
        String url = "https://api.vk.com/method/groups.getById?v=5.71&access_token=" + loginManager.getAccessToken() + "&group_ids=" + StringUtils.join(ids, ",") + "&fields=" + fields;
        
        Thread.currentThread().sleep(350);
        String request = Utils.getRequest(url);
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        GroupResponce response;
        try{
            response = (GroupResponce) objectMapper.readValue(request, GroupResponce.class);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        
        return response.getResponse();
    }
    
    private List<GroupVK> groupsVK(Collection<Integer> groupIds) throws IOException, InterruptedException{
        List<GroupVK> result = new ArrayList<GroupVK>();
        List<Integer> missingGroupIds = new ArrayList<Integer>();
        for(Integer id: groupIds){
            if(id2Group.containsKey(id)){
                result.add(id2Group.get(id));
            }else{
                missingGroupIds.add(id);
            }
        }
        
        List<Integer> part = new ArrayList<Integer>();
        for(int i = 0; i < missingGroupIds.size(); i++){
            part.add(missingGroupIds.get(i));
            if(part.size() == 20 || i == missingGroupIds.size() - 1){
                List<GroupVK> missingGroups = groupFromVK(part);
        
                ObjectMapper objectMapper = new ObjectMapper();
                for(GroupVK groupVK: missingGroups){
                    id2Group.put(groupVK.getId(), groupVK);
                    FileUtils.write(new File("C:\\demonetData\\groups.txt"), objectMapper.writeValueAsString(groupVK) + "\n", "utf-8", true);
                }
                result.addAll(missingGroups);
                part = new ArrayList<Integer>();
            }
            
        }
        
        return result;
    }
    
    public Map<String, Integer> groupVector(String groupId) throws IOException{
        Map<String, Integer> titleVector = titleVector(groupId);
        Map<String, Integer> wallVector = wallVector("-" + groupId);
        
        return VectorUtils.sumVectors(wallVector, VectorUtils.multVector(titleVector, 100));
    }
    
    public List<Group> groups(Collection<Integer> ids) throws IOException, InterruptedException{
        List<Group> result = new ArrayList<Group>();
        for(GroupVK groupVK: groupsVK(ids)){
            Group group = new Group();
            
            group.setDescription(groupVK.getDescription());
            group.setName(groupVK.getName());
    //        group.setWallVector(wallVector(groupVK.getId()));
            result.add(group);
        }
    
        return result;
    }
    
    
    public static void main(String[] args) throws IOException{
       GroupManager gm = new GroupManager();
       
       gm.groupVector("28866484");
       System.err.println("");
    
    }
}
