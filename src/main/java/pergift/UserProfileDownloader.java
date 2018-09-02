package pergift;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.tinder2.model.vk.UserResponse;
import com.mycompany.tinder2.model.vk.VKResponse;
import com.mycompany.tinder2.service.LoginManager;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import com.mycompany.tinder2.service.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author delet
 */
public class UserProfileDownloader {
    public static final String USER_VK_RAW_INFO_DIR = "C:\\gifts\\users\\raw_info";
    public static final String USER_VK_RAW_GROUPS_DIR = "C:\\gifts\\users\\raw_groups";
    LoginManager loginManager = new LoginManager();
    
    public List<String> usersInfo(Collection<Integer> ids) throws IOException, InterruptedException{
        List<String> result = new ArrayList<String>();
        
        Set<Integer> part = new HashSet<Integer>();
        for(Integer id: ids){
            part.add(id);
            if(part.size() < 200){
                continue;
            }
            
            String fields = "id,about,activities,bdate,books,city,country,education,games,home_town,interests,sex,photo_100";
            String url = "https://api.vk.com/method/users.get?v=5.71&access_token=" + loginManager.getAccessToken() + "&user_ids=" + StringUtils.join(part.toArray(), ",") + "&fields=" + fields;
            String request = Utils.getRequest(url);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(request);
            
            for(JsonNode item : jsonNode.get("response")){
                result.add(item.toString());
            }
            
            part = new HashSet<Integer>();
        }
        
        return result;
    }
    
    public String groups(Integer userId) throws IOException{
        String fields = "city,country,place,description,wiki_page,members_count,counters,start_date,finish_date,can_post,can_see_all_posts,activity,status,contacts,links,fixed_post,verified,site,can_create_topic";
        String url = "https://api.vk.com/method/groups.get?v=5.52&access_token=" + loginManager.getAccessToken() + "&user_id=" + userId + "&extended=1&fields=" + fields;
        String request = Utils.getRequest(url);
       
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(request);
       
        
        return jsonNode.toString();
    }
    
    
    public static void main(String[] args) throws IOException, InterruptedException {
        UserProfileDownloader userProfileDownloader = new UserProfileDownloader();
        Set<Integer> userIds = new HashSet<Integer>();
        
        ObjectMapper objectMapper = new ObjectMapper();
        for(String line: FileUtils.readLines(new File(MyWishListParser.MODEL_DIR), "UTF-8")){
            WishList wishList = objectMapper.readValue(line, WishList.class);
            userIds.add(wishList.getUserVKId());
        }
       
//        for(String profile: userProfileDownloader.usersInfo(userIds)){
//            FileUtils.write(new File(USER_VK_RAW_INFO_DIR), profile + "\n", "UTF-8", true);
//        }
        
        for(Integer id: userIds){
            FileUtils.write(new File(USER_VK_RAW_GROUPS_DIR), id + "\t" + userProfileDownloader.groups(id) + "\n", "UTF-8", true);
        }
    }
    
    
}
