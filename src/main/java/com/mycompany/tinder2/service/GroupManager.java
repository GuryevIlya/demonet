package com.mycompany.tinder2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.tinder2.model.vk.Group;
import com.mycompany.tinder2.model.vk.GroupResponce;
import com.mycompany.tinder2.model.vk.Response;
import com.mycompany.tinder2.model.vk.User;
import com.mycompany.tinder2.model.vk.VKResponse;
import com.mycompany.tinder2.model.vk.WallPost;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    private List<WallPost> wallPosts(String groupId, int offset, int count) throws IOException{
        String accessToken = "39bad9ad33a23248bd7665951ea285c69d0abd05d94f21123255c61ff2651dde35f6901bd1ef02a93a941";
        String filter = "all";
        String fields = "text";
        
        List<WallPost> result = new ArrayList<WallPost>();
        
        String url = "https://api.vk.com/method/wall.get?v=5.71&access_token=" + accessToken + "&owner_id=" + groupId + "&offset=" + offset + "&count=" + count;
    
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
    
    
    public Map<String, Integer> groupVector(String groupId) throws IOException{
        Map<String, Integer> titleVector = titleVector(groupId);
        Map<String, Integer> wallVector = wallVector("-" + groupId);
        
        return VectorUtils.sumVectors(wallVector, VectorUtils.multVector(titleVector, 100));
    }
    
    
    public static void main(String[] args) throws IOException{
       GroupManager gm = new GroupManager();
       
       gm.groupVector("28866484");
       System.err.println("");
    
    }
}
