package pergift;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author delet
 */
public class UserProfileParser {
    public static final String USER_VK_MODEL = "C:\\gifts\\users\\model";
    
    private static void setInfo(Map<Integer, User> id2User){
        ObjectMapper objectMapper = new ObjectMapper();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(UserProfileDownloader.USER_VK_RAW_INFO_DIR), "UTF-8"))){
            String strLine;
            while ((strLine = br.readLine()) != null){
                JsonNode jsonNode = objectMapper.readTree(strLine);
                
                Integer id = jsonNode.get("id").asInt();
                if(id2User.containsKey(id)){
                    id2User.put(id, new User(id));
                }
                User user = id2User.get(id);
                
                String firstName = jsonNode.get("first_name").toString();
                user.setName(firstName);
                
                
                id2User.put(id, user);
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    private static void setGroups(Map<Integer, User> id2User){
        ObjectMapper objectMapper = new ObjectMapper();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(UserProfileDownloader.USER_VK_RAW_GROUPS_DIR), "UTF-8"))){
            String strLine;
            while ((strLine = br.readLine()) != null){
                JsonNode jsonNode = objectMapper.readTree(strLine);
                
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) throws IOException {
        Set<Integer> userIds = new HashSet<Integer>();
        Map<Integer, User> id2User = new HashMap<Integer, User>();
        
        ObjectMapper objectMapper = new ObjectMapper();
        for(String line: FileUtils.readLines(new File(MyWishListParser.MODEL_DIR), "UTF-8")){
            WishList wishList = objectMapper.readValue(line, WishList.class);
            userIds.add(wishList.getUserVKId());
            id2User.put(wishList.getUserVKId(), null);
        }
        
        setInfo(id2User);
        setGroups(id2User);
        
        for (Map.Entry<Integer, User> entry : id2User.entrySet()) {
            FileUtils.write(new File(USER_VK_MODEL), objectMapper.writeValueAsString(entry.getValue()) + "\n", "UTF-8", true);
        }
    }
    
}
