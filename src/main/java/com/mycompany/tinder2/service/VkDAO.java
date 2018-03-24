package com.mycompany.tinder2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.tinder2.model.vk.Couple;
import com.mycompany.tinder2.model.vk.GroupVK;
import com.mycompany.tinder2.model.vk.GroupResponce;
import com.mycompany.tinder2.model.vk.Items;
import com.mycompany.tinder2.model.vk.Response;
import com.mycompany.tinder2.model.vk.UserVK;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author delet
 */
@Component
public class VkDAO {
    @Autowired
    CommonDAO commonDAO;
    
    private static final int APP_CODE = 6318506;
    private static final String CLIENT_SECRET = "FAgQj1Fo3gSjaDeLuvf5";
    private class AccessToken {
        AccessToken(String value, int lifeTime){
            this.shelfLife = (new Date()).getTime() + lifeTime;
            this.value = value;
        }
        public long shelfLife;
        public String value;
    }
    
    private final String USER_AGENT = "Mozilla/5.0";
    private Map<String, AccessToken> userId2accessToken = new HashMap<String, AccessToken>();
    
    
//    public String addAccessToken(String userId, String token, String lif){
//       
//       userId2accessToken.put(userId, new AccessToken(0, token, 0));
//    }
    
    public String accessToken(String userId){
       if(!userId2accessToken.containsKey(userId)){
           return null;
       }
       
       AccessToken accessToken = userId2accessToken.get(userId);
       if(accessToken.shelfLife < (new Date()).getTime()){
           return null;
       }
       
       return userId2accessToken.get(userId).value;
    }
    
    
    
    public String[] idAndAccessToken(String code) throws Exception {
        String[] result = new String[2];
        String url = "https://oauth.vk.com/access_token?client_id=" + APP_CODE + "&client_secret=" + CLIENT_SECRET + "&redirect_uri=http://tinder2.com:8080/Tinder2/access_token?&code=" + code;

        

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readValue(getRequest(url), JsonNode.class);
        
        result[0] = rootNode.get("user_id").asText();
        result[1] = rootNode.get("access_token").asText();
        
        return result; 

   }
    
   public Set<String> friends1(String access_token, String user_id) throws IOException{
       Set<String> result = new HashSet<String>();
       
       String url = "https://api.vk.com/method/friends.get?user_id=" + user_id + "&v=5.52&access_token=" + access_token;
               
       String request = getRequest(url);
       Pattern p = Pattern.compile("\\[(.*)\\]");
       Matcher m = p.matcher(request);
       
       if(m.find()){
          result.addAll(Arrays.asList(m.group(1).split(",")));
       }
       
       return result;
   }
   
   public Set<String> friends1(String user_id) throws IOException{
       Set<String> result = new HashSet<String>();
       
       String url = "https://api.vk.com/method/friends.get?user_id=" + user_id + "&v=5.52";
               
       String request = getRequest(url);
       Pattern p = Pattern.compile("\"items\":\\[(.*)\\]");
       Matcher m = p.matcher(request);
       
       if(m.find()){
          if(m.group(1).contains("value")){
              System.err.println("");
          }
          result.addAll(Arrays.asList(m.group(1).split(","))); 
       }
       
       return result;
   }
   
   
   public Set<String> friends2(String access_token, String user_id) throws IOException{
       Set<String> result = new TreeSet<String>();
       Set<String> friends = friends1(access_token, user_id);
       
       for(String friend: friends){
           result.addAll(friends1(friend));
       }
       
       return result;
   }
    
   public Set<String> friendIds(String access_token, String user_id, int offset, int count, String sortType) throws IOException{
       Set<String> result = new HashSet<String>();
       
       final Set<String> friends1 = friends1(access_token, user_id);
       Set<String> friends2 = friends2(access_token, user_id);
       Set<String> allFriendsSet = new HashSet<String>(friends2);
       allFriendsSet.addAll(friends1);
       allFriendsSet.remove("");
       ArrayList<String> allFriends = new ArrayList<String>(allFriendsSet);
       
       if(sortType.equals("numberAndProximity")){
           
           
           Collections.sort(allFriends , new Comparator<String>() {
                                            @Override
                                            public int compare(String o1, String o2) {
                                               if(friends1.contains(o1) && !friends1.contains(o2)){
                                                   return -1;
                                               }else if(!friends1.contains(o1) && friends1.contains(o2)){
                                                   return 1;
                                               }else{
                                                   return toInt(o1) - toInt(o2);
                                               }
                                            }
                                        }
                            );
       }else{
           Collections.sort(allFriends , new Comparator<String>() {
                                            @Override
                                            public int compare(String o1, String o2) {
                                                return toInt(o1) - toInt(o2);
                                            }
                                        }
                            );
       }
       
       
       if(count == -1){
           result.addAll(allFriends);
           return result;
       }
       
       for(int i = 0; i < allFriends.size() && i < offset + count; i++){
            if(i >= offset){
                result.add(allFriends.get(i));
            }
       }
       
       return result;
   }
   
   public List<UserVK> friends(String access_token, String user_id, int offset, int count, String sortType) throws IOException{
       return users(friendIds(access_token, user_id, offset, count, sortType), access_token);
   }
   
   
   private String getRequest(String url) throws MalformedURLException, IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
        }
        in.close();
       
       
       return response.toString();
   }
   
   private List<UserVK> users(Collection<String> users, String access_token) throws IOException{
       List<UserVK> result = new ArrayList<UserVK>();
       
       List<String> part = new ArrayList<String>();
       for(String user : users){
           if(part.size() == 900){
               result.addAll(usersPart(part, access_token));
           }else{
               part.add(user);
           }
       }
       result.addAll(usersPart(part, access_token));
       
       return result;
   }
   
//   private Map<String, User> usersMap(Collection<String> users, String access_token) throws IOException{
//       Map<String, User> result = new HashMap<String, User>();
//       
//       List<String> part = new ArrayList<String>();
//       for(String user : users){
//           if(part.size() == 300){
//               for(User u: usersPart(part, access_token)){
//                   result.put(u.getId(), u);
//               }
//               part = new ArrayList<String>();
//           }else{
//               part.add(user);
//           }
//       }
//       for(User u: usersPart(part, access_token)){
//            result.put(u.getId(), u);
//       }
//       
//       return result;
//   }
   
   private List<UserVK> usersPart(Collection<String> users, String access_token) throws IOException{
       List<UserVK> result = new ArrayList<UserVK>();
       
//       String url = "https://api.vk.com/method/users.get?v=5.52&access_token=" + access_token + "&user_ids=" + String.join(",", users) + "&fields=sex,maiden_name,relation,relation_partner,relation_pending";
//       
//       
//       try{
//            String request = getRequest(url);
//            ObjectMapper objectMapper = new ObjectMapper();
//            Response<User> response = (Response<User>) objectMapper.readValue(request, Response.class);
//
//            for(User user: response.getResponse()){
//                 result.add(user);
//            }
//       }catch(Exception e){
//           System.err.println("");
//       }
       return result;
   }



//   public User[][] couples(String access_token, String user_id, int offset, int count, String sortType) throws IOException{
//       Map<String, User> allUsers = usersMap(friendIds(access_token, user_id, offset, count, sortType), access_token);
//       
//       Map<Integer, Integer> relationStatus = new HashMap<Integer, Integer>();
//       Map<String, Couple> couples = new HashMap<String, Couple>();
//       
//       
//       for (Map.Entry<String, User> entry : allUsers.entrySet()) {
//           User user = entry.getValue();
//           
//           Integer totalRelation = relationStatus.get(user.getRelation());
//           if(totalRelation == null){
//               relationStatus.put(user.getRelation(), 1);
//           }else{
//               relationStatus.put(user.getRelation(), totalRelation + 1);
//           }
//
//           
//           
//           if(user.getRelationPartner() != null){
//               User user2 = allUsers.get(user.getRelationPartner());
//               
//               String coupleKey1 = user.getId() + "\t" + user.getRelationPartner();
//               String coupleKey2 = "efe";
//               if(user2 != null){
//                   coupleKey2 = user2.getId() + "\t" + user.getId();
//               }
//               
//               if(!couples.containsKey(coupleKey2)){
//                  Couple couple = new Couple();
//                  couples.put(coupleKey1, couple);
//                  couple.setUser1(user);
//                  couple.setUser2(user2);
//                  couple.setPending1(user.getPending());
//               }
//           }
//           
//       }
//       
//       Map<String, Couple> fullCouples = new HashMap<String, Couple>();
//       for (Map.Entry<String, Couple> entry : couples.entrySet()) {
//           String key = entry.getKey();
//           Couple value = entry.getValue();
//           if(value.getUser1() == null || value.getUser2() == null){
//               continue;
//           }
//           fullCouples.put(key, value);
//       }
//       
//       return new User[2][4];
//   }
   
   public List<GroupVK> userGroups(String userId, String access_token) throws IOException{
       String url = "https://api.vk.com/method/groups.get?v=5.52&access_token=" + access_token + "&user_id=" + userId + "&extended=1&fields=description";
       
       String request = getRequest(url);
       
       Pattern p =  Pattern.compile("(\"items\":\\[.*\\])\\}\\}");
       Matcher m = p.matcher(request);
       
       Items response = null;
       if(m.find()){
          ObjectMapper objectMapper = new ObjectMapper();
          response = (Items) objectMapper.readValue("{ " + m.group(1) + " }", Items.class); 
       }
       
       return response != null ? response.getItems(): null;
   }
   
   
   public static void main(String[] args) throws IOException{
       String access_token = "dc2665cae6acd3003d70bfb8fd4613923bd277451be3f81aa53a5f924baa39d891611855fc50fdb5241ec";
       String userId = "96274720";
       
       VkDAO vkDAO = new VkDAO();
       vkDAO.userGroups(userId, access_token);
   }
   

   
   
}
