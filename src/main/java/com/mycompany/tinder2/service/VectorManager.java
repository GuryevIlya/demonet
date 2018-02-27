package com.mycompany.tinder2.service;

import com.mycompany.tinder2.model.Group;
import com.mycompany.tinder2.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author delet
 */
public class VectorManager {
    public static Map<String, Integer> text2vector(String text){
        Map<String, Integer> result = new HashMap<String, Integer>();
        
        text = text.replace("\t", " ");
        String[] words = text.split(" ");
        for(String word : words){
            if(result.containsKey(word)){
                result.put(word, result.get(word) + 1);
            }else{
                result.put(word, 1);
            }
        }
        
        return result;
    }
    
//    public Map<String, Integer> userVector(String userId, String access_token) throws IOException{
//        Map<String, Integer> result = new HashMap<String, Integer>();
//        
//        VkDAO vkDAO = new VkDAO();
//        List<Group> groups = vkDAO.userGroups(userId, access_token);
//        
//        List<Map<String, Integer>> groupVectors = new ArrayList<Map<String, Integer>>();
//        for(Group group : groups){
//            groupVectors.add(text2vector(group.getDescription()));
//        }
//        
//        result = sumVectors(groupVectors);
//        return result;
//    }
    
    
    public static Map<String, Integer> sumVectors(List<Map<String, Integer>> vectors){
        Map<String, Integer> result = new HashMap<String, Integer>();
        
        for(Map<String, Integer> vector : vectors){
            for (Map.Entry<String, Integer> entry : vector.entrySet()) {
                if(!result.containsKey(entry.getKey())){
                    result.put(entry.getKey(), entry.getValue());
                }else{
                    result.put(entry.getKey(), result.get(entry.getKey()) + entry.getValue());
                }
            }
        }
        
        return result;
    }
    
    public static Map<String, Integer> sumVectors(Map<String, Integer> vector1, Map<String, Integer> vector2){
        Map<String, Integer> result = new HashMap<String, Integer>();
        
        for (Map.Entry<String, Integer> entry : vector1.entrySet()) {
            Integer oldValue = result.get(entry.getKey());
                    
            if(oldValue != null){
                result.put(entry.getKey(), oldValue + entry.getValue());
            }else{
                result.put(entry.getKey(), entry.getValue());
            }  
            
        }
        
        for (Map.Entry<String, Integer> entry : vector2.entrySet()) {
            Integer oldValue = result.get(entry.getKey());
                    
            if(oldValue != null){
                result.put(entry.getKey(), oldValue + entry.getValue());
            }else{
                result.put(entry.getKey(), entry.getValue());
            }        
        }
        
        return result;
    }
}
