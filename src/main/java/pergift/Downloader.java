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
public class Downloader {
    public static final String MYWISHLIST_DIR = "C:\\gifts\\mywishlist\\raw_posts";
    public static final String MYWISHBOARD_DIR = "C:\\gifts\\mywishboard\\raw_posts";
    LoginManager loginManager = new LoginManager();
    
    public List<String> posts(Date startDate, Date endDate, String query) throws IOException, InterruptedException{
        Thread.currentThread().sleep(350);
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
    
    public void downloadPosts(Date startDateP, String query, String targetDir) throws IOException, InterruptedException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Set<String> downloaded = new HashSet<String>(Arrays.asList(new File(targetDir).list()));
        Date currentDate = new Date();
        Date startDate = startDateP;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDateP);
        calendar.add(Calendar.MONTH, 2);
        Date endDate = calendar.getTime();
        
        while(endDate.compareTo(currentDate) < 0){
            String fileName = sdf.format(startDate) + "-" + sdf.format(endDate);
            if(downloaded.contains(fileName)){
                startDate = endDate;
                calendar.add(Calendar.MONTH, 2);
                endDate = calendar.getTime();
                continue;
            }
            for(String post: posts(startDate, endDate, query)){
                FileUtils.write(new File(targetDir, fileName), post + "\n", "utf-8", true);
            }
            
            startDate = endDate;
            calendar.add(Calendar.MONTH, 2);
            endDate = calendar.getTime();
        }
        for(String post: posts(startDate, currentDate, query)){
            FileUtils.write(new File(targetDir, sdf.format(startDate) + "-" + sdf.format(currentDate)), post + "\n", "utf-8", true);
        }
        
        
        
        String dfd ="dfd";
    }
    
    
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
       
        Downloader cc = new Downloader();
      //  cc.downloadPosts(new SimpleDateFormat("yyyyMMddhhmmss").parse("20100101000000"), "mywishlist", MYWISHLIST_DIR);
        cc.downloadPosts(new SimpleDateFormat("yyyyMMddhhmmss").parse("20121101000000"), "mywishboard", MYWISHBOARD_DIR);
   // cc.posts(new SimpleDateFormat("yyyyMMddhhmmss").parse("20180107000000"), new Date(), "mywishboard");
    }
}
