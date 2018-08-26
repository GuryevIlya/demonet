package pergift;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mycompany.tinder2.model.vk.VKResponse;
import com.mycompany.tinder2.model.vk.WallPost;
import com.mycompany.tinder2.service.LoginManager;
import com.mycompany.tinder2.service.UserManager;
import com.mycompany.tinder2.service.Utils;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author delet
 */
public class CommonClass {
    private static final String targetDir = "C:\\gifts";
    LoginManager loginManager = new LoginManager();
    
    public Set<Integer> groupMembers(Integer groupId) throws IOException{
        String url = "https://api.vk.com/method/groups.getMembers?v=5.52&access_token=" + loginManager.getAccessToken() + 
                        "&group_id=" + groupId + "&fields=description";
        String responseJSON = Utils.getRequest(url);
       
        ObjectMapper objectMapper = new ObjectMapper();
        VKResponse<Integer> response = (VKResponse<Integer>) objectMapper.readValue(responseJSON, new TypeReference<VKResponse<Integer>>(){});

        if(response.getResponse() != null){
          //  FileUtils.write(new File("C:\\demonetData\\user2groups.txt"), userId + "\t" + objectMapper.writeValueAsString(response.getResponse().getItems()) + "\n", "utf-8", true);
            return new HashSet<Integer>(response.getResponse().getItems());
        }else{
            return null;
        }
    }
    
    public List<String> posts(Date startDate, Date endDate, String query) throws IOException{
        List<String> result = new ArrayList<String>();
        
        String startFrom = null;
        do{
            String url = "https://api.vk.com/method/newsfeed.search?v=5.80&access_token=" + loginManager.getAccessToken() + 
                        "&q=" + query + "&start_time=" + startDate.getTime()/1000 + "&end_time=" + endDate.getTime()/1000 + "&count=200" +
                        (startFrom != null ? "&start_from=" + startFrom : "");
            String response = Utils.getRequest(url);
       
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            try{
                for(JsonNode item : jsonNode.get("response").get("items")){
                    result.add(item.toString());
                }
            }catch(Exception e){
                int df= 34;
            }
            startFrom = jsonNode.get("response").get("next_from") != null ? jsonNode.get("response").get("next_from").asText().split("/")[0] : null;
        }while(startFrom != null); 
        
        
        
        return result;
    }
    
    public void downloadPosts(Date startDateP, String query) throws IOException{
        Set<String> downloaded = new HashSet<String>(Arrays.asList(new File(targetDir).list()));
        Date startDate = startDateP;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDateP);
        calendar.add(Calendar.MONTH, 2);
        Date endDate = calendar.getTime();
        
        
        while(endDate.compareTo(new Date()) < 0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fileName = sdf.format(startDate) + "-" + sdf.format(endDate);
            if(downloaded.contains(fileName)){
                continue;
            }
            for(String post: posts(startDate, endDate, query)){
                FileUtils.write(new File(targetDir, fileName), post + "\n", "utf-8", true);
            }
            
            startDate = endDate;
            calendar.add(Calendar.MONTH, 2);
            endDate = calendar.getTime();
        }
        
        
        
        
        String dfd ="dfd";
    }
    
    
    public static void main(String[] args) throws IOException, ParseException {
       
        CommonClass cc = new CommonClass();
        cc.downloadPosts(new SimpleDateFormat("yyyyMMddhhmmss").parse("20100101000000"), "mywishlist");
    
    }
}
