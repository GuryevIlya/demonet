package com.mycompany.tinder2.service;

import com.mycompany.tinder2.model.internal.Stat;
import com.mycompany.tinder2.model.internal.UserVectors;
import com.mycompany.tinder2.model.pages.User;
import com.mycompany.tinder2.model.vk.UserVK;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
       Map<Integer, Stat> user2Stat = compatibilityManager.getProximityStat(userId, linkManager.friendsOfFriends(userId).keySet());
       
       List<Integer> sortedFriends = new ArrayList<Integer>();
       List<Map.Entry<Integer, Stat>> list = new LinkedList<>(user2Stat.entrySet());
       if(sortType.equals("numberAndProximity")){
           Collections.sort(list, new Comparator<Map.Entry<Integer, Stat>>(){
               @Override
               public int compare(Map.Entry<Integer, Stat> o1, Map.Entry<Integer, Stat> o2){
                   if(o1.getValue().getCommonFriendCount() - o2.getValue().getCommonFriendCount() == 0){
                       return o1.getKey() - o2.getKey();
                   }else{
                       return  o1.getValue().getCommonFriendCount() - o2.getValue().getCommonFriendCount();
                   }
               }
            });
        }else if(sortType.equals("number")){
           Collections.sort(list, new Comparator<Map.Entry<Integer, Stat>>(){
               @Override
               public int compare(Map.Entry<Integer, Stat> o1, Map.Entry<Integer, Stat> o2){
                   return o1.getKey() - o2.getKey();
               }
            });

            
       }else if(sortType.equals("compatibilityAndNumber")){
           Collections.sort(list, new Comparator<Map.Entry<Integer, Stat>>(){
               @Override
               public int compare(Map.Entry<Integer, Stat> o1, Map.Entry<Integer, Stat> o2){
                   if(o1.getValue().getCommonFriendCount() - o2.getValue().getCommonFriendCount() == 0){
                       return o1.getKey() - o2.getKey();
                   }else{
                       return  o1.getValue().getCommonFriendCount() - o2.getValue().getCommonFriendCount();
                   }
               }
            });
       }
       
       for (Map.Entry<Integer, Stat> entry : list){
            sortedFriends.add(entry.getKey());
       }
       
       if(count == -1){
           return userManager.users(sortedFriends);
       }
       
       List<Integer> resultIds = new ArrayList<Integer>();
       for(int i = 0; i < sortedFriends.size() && i < offset + count; i++){
            if(i >= offset){
                resultIds.add(sortedFriends.get(i));
            }
       }
       return userManager.users(resultIds);
   }
    
    
//    private List<UserVectors> users(List<Integer> ids) throws IOException, InterruptedException{
//        List<UserVectors> result = new ArrayList<UserVectors>();
//        
//        for(Integer id: ids){
//            result.add(userManager.user(id));
//        }
//        
//        return result;
//    }

    public static void main(String[] args) throws IOException, InterruptedException{
        System.out.print("hjhjj");
        System.out.print("hjhjj");
        System.out.print("hjhjj");
        MainManager mm = new MainManager();
       // List<Integer> test = mm.friends(96274720, 0, -1, "numberAndProximity");
        
        int i = 4;
    }
}
