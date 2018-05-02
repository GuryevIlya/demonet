package com.mycompany.tinder2.service;

import com.mycompany.tinder2.model.internal.Stat;
import com.mycompany.tinder2.model.internal.UserVectors;
import com.mycompany.tinder2.model.pages.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    
    public Collection<Integer> filterPeopleByInterest(Collection<Integer> peopleIds, String[] interests) throws IOException, InterruptedException{
        List<Integer> result = new ArrayList<Integer>();
        
        if(interests.length == 0){
            return peopleIds;
        }
        for(Integer id: peopleIds){
            UserVectors userVectors = userManager.userVectors(id);
        
            for(String interest: interests){
                if(userVectors.isInterest(interest)){
                    result.add(id);
                    break;
                }
            }
        }
        
        return result;
    }
    
    public synchronized List<User> friends(Integer userId, int offset, int count, String sortType, String[] interests) throws IOException, InterruptedException{
       Collection<Integer> filteredPeopleByInterest = filterPeopleByInterest(linkManager.friendsOfFriends(userId).keySet(), interests);
       List<Integer> firstPeopleFilteredByInterest = Utils.first(filteredPeopleByInterest, 300);
       
       Map<Integer, Stat> user2Stat = compatibilityManager.getProximityStat(userId, firstPeopleFilteredByInterest);
       
       List<Integer> sortedFriends = new ArrayList<Integer>();
       List<Map.Entry<Integer, Stat>> list = new LinkedList<>(user2Stat.entrySet());
       if(sortType.equals("proximityAndNumber")){
           Collections.sort(list, new Comparator<Map.Entry<Integer, Stat>>(){
               @Override
               public int compare(Map.Entry<Integer, Stat> o1, Map.Entry<Integer, Stat> o2){
                   try{
                        if(o1.getValue().getCommonFriendCount() - o2.getValue().getCommonFriendCount() == 0){
                            return o1.getKey() - o2.getKey();
                        }else{
                            return  o2.getValue().getCommonFriendCount() - o1.getValue().getCommonFriendCount();
                        }
                   }catch(Exception e){
                       throw new RuntimeException(e);
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
           return new ArrayList<User>(userManager.users(sortedFriends).values());
       }
       
       List<Integer> resultIds = new ArrayList<Integer>();
       for(int i = 0; i < sortedFriends.size() && i < offset + count; i++){
            if(i >= offset){
                resultIds.add(sortedFriends.get(i));
            }
       }
       return new ArrayList<User>(userManager.users(resultIds).values());
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

    public Set<String> interests(){
        Set<String> result = new HashSet<String>();
        
        result.add("МГУ");
        result.add("Ломоносов");
        result.add("ВМиК");
        result.add("МГУ им. М.В.Ломоносова");
        result.add("Московский Государственный Университет");
        result.add("Muse");
        result.add("Тест");
        result.add("Пример");
        result.add("Хуй");
        result.add("Пизда");
        result.add("Танцы");
        result.add("Танцы до упада");
        result.add("Марки");
        result.add("Московская область");
        
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
