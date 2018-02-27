package com.mycompany.tinder2.service;

import com.mycompany.tinder2.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class MainManager {
    @Autowired
    private UserManager userManager;
    @Autowired
    private LinkManager linkManager;
    @Autowired
    private CompatibilityManager compatibilityManager;
    @Autowired 
    private LoginManager loginManager;
    
    public List<User> friends(Integer userId, int offset, int count, String sortType) throws IOException, InterruptedException{
       Map<Integer, Integer> allFriends = new HashMap<Integer, Integer>();
       for(Integer id : linkManager.friends(userId)){
           allFriends.put(id, Integer.MAX_VALUE);
       }
       
       for (Map.Entry<Integer, Integer> entry : linkManager.friendsOfFriends(userId).entrySet()){
           if(!allFriends.containsKey(entry.getKey())){
              allFriends.put(entry.getKey(), entry.getValue()); 
           }
       }
       
       List<Integer> sortedFriends;
       
       if(sortType.equals("numberAndProximity")){
           sortedFriends = Utils.sortKeyByValueAndKey(allFriends);
       }else if(sortType.equals("number")){
           sortedFriends = new ArrayList<Integer>(allFriends.values());
           Collections.sort(sortedFriends, new Comparator<Integer>() {
                                            @Override
                                            public int compare(Integer o1, Integer o2) {
                                                return o1 - o2;
                                            }
                                        }
                            );
       }else if(sortType.equals("compatibility")){
           Map<Integer, Double> user2compb  = new HashMap<Integer, Double>();
           
           for (Map.Entry<Integer, Integer> entry : allFriends.entrySet()) {
               user2compb.put(entry.getKey(), compatibilityManager.getCompatibility(loginManager.getVkId(), entry.getKey()));
           }
           
           
       }else{
           sortedFriends = new ArrayList<Integer>(allFriends.values()); 
       }
       
       
       if(count == -1){
           return users(sortedFriends);
       }
       
       List<Integer> resultIds = new ArrayList<Integer>();
       for(int i = 0; i < sortedFriends.size() && i < offset + count; i++){
            if(i >= offset){
                resultIds.add(sortedFriends.get(i));
            }
       }
       
       
       return users(resultIds);
   }
    
    
    private List<User> users(List<Integer> ids) throws IOException, InterruptedException{
        List<User> result = new ArrayList<User>();
        
        for(Integer id: ids){
            result.add(userManager.user(id));
        }
        
        return result;
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        System.out.print("hjhjj");
        System.out.print("hjhjj");
        System.out.print("hjhjj");
        MainManager mm = new MainManager();
       // List<Integer> test = mm.friends(96274720, 0, -1, "numberAndProximity");
        
        int i = 4;
    }
}
