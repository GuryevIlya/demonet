package com.mycompany.tinder2.service;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import com.mycompany.tinder2.model.vk.GroupVK;
import com.mycompany.tinder2.model.vk.UserVK;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class VectorManager {
    private static List<String> toWords(String text){
        String withOutInvisibleChar = text.replace("\n", " ")
                                          .replace("\t", " ")
                                          .replace(".", " ")
                                          .replace(",", " ")
                                          .replace("?", " ")
                                          .replace(":", " ")
                                          .replace("!", " ");
        
        MorphoAnalyst ma = new MorphoAnalyst();
        
        return  MorphoAnalyst.normalize(Arrays.asList(withOutInvisibleChar.split(" ")));
    }
    
    
    
    public static Map<String, Integer> text2vector(String text){
        Map<String, Integer> result = new HashMap<String, Integer>();
        
        List<String> words = toWords(text);
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
        Map<String, Integer> result = new TreeMap<String, Integer>();
        
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
    
    public static void main(String[] args){
        MorphoAnalyst ma = new MorphoAnalyst();
        List<String> result = MorphoAnalyst.normalize(Arrays.asList("dfsd fds fsdfsd fsdfsd fdfd".split(" ")));
        
        int fdd = 3;
    }
}
