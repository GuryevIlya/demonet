package com.mycompany.tinder2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.tinder2.model.internal.Group;
import com.mycompany.tinder2.model.internal.UserVectors;
import com.mycompany.tinder2.model.vk.GroupVK;
import com.mycompany.tinder2.model.vk.GroupResponce;
import com.mycompany.tinder2.model.vk.Response;
import com.mycompany.tinder2.model.vk.UserVK;
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
import static org.apache.commons.lang3.math.NumberUtils.toInt;
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
    Map<Integer, GroupVK> id2Group = new HashMap<Integer, GroupVK>();
    
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
//            File groupFolder = new File("C:\\demonetData\\groups");
//            if(groupFolder.exists()){
//                for(File file: groupFolder.listFiles()){
//                    for(String line : FileUtils.readLines(file, "utf-8")){
//                        GroupVK group = objectMapper.readValue(line, GroupVK.class);
//                        id2Group.put(toInt(group.getId()), group);
//                    }
//                }
//            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private List<WallPost> wallPosts(String groupId, int offset, int count) throws IOException{
        String filter = "all";
        String fields = "text";
        
        List<WallPost> result = new ArrayList<WallPost>();
        
        String url = "https://api.vk.com/method/wall.get?v=5.71&access_token=" + loginManager.getAccessToken() + "&owner_id=" + groupId + "&offset=" + offset + "&count=" + count;
    
        String request = Utils.getRequest(url);
        
        ObjectMapper objectMapper = new ObjectMapper();
        VKResponse<WallPost> response = (VKResponse<WallPost>) objectMapper.readValue(request, new TypeReference<VKResponse<WallPost>>(){});
       
        return response.getResponse().getItems();
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
    
    private List<GroupVK> groupsVK(Collection<Integer> groupIds) throws IOException{
        List<GroupVK> result = new ArrayList<GroupVK>();
        List<Integer> missingGroupIds = new ArrayList<Integer>();
        for(Integer id: groupIds){
            if(id2Group.containsKey(id)){
                result.add(id2Group.get(id));
            }else{
                missingGroupIds.add(id);
            }
        }
        
        String fields = "city,country,place,description,members_count";
        String url = "https://api.vk.com/method/groups.getById?v=5.71&access_token=" + loginManager.getAccessToken() + "&group_ids=" + StringUtils.join(missingGroupIds.toArray(), ",") + "&fields=" + fields;
        
        String request = Utils.getRequest(url);
        
        ObjectMapper objectMapper = new ObjectMapper();
        GroupResponce response = (GroupResponce) objectMapper.readValue(request, GroupResponce.class);
        
        result.addAll(response.getResponse());
        
        return result;
    }
    
    public Map<String, Integer> groupVector(String groupId) throws IOException{
        Map<String, Integer> titleVector = titleVector(groupId);
        Map<String, Integer> wallVector = wallVector("-" + groupId);
        
        return VectorUtils.sumVectors(wallVector, VectorUtils.multVector(titleVector, 100));
    }
    
    public List<Group> groups(Collection<Integer> ids) throws IOException{
        List<Group> result = new ArrayList<Group>();
        for(GroupVK groupVK: groupsVK(ids)){
            Group group = new Group();
            
            group.setDescription(groupVK.getDescription());
            group.setName(groupVK.getName());
            group.setVallVector(wallVector(groupVK.getId()));
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
