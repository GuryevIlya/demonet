package pergift;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.tinder2.model.vk.WallPost;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author delet
 */
public class MyWishListParser {
    public static final String OTHER_DIR = "C:\\gifts\\mywishlist\\other_posts";
    public static final String GREETINGER_DIR = "C:\\gifts\\mywishlist\\greetinger";
    public static final String VALENTINR_DIR = "C:\\gifts\\mywishlist\\valentinr";
    public static final String PIC_DIR = "C:\\gifts\\mywishlist\\pic";
    public static final String MODEL_DIR = "C:\\gifts\\mywishlist\\model";
    
    
    public static WishList parse(String post) throws IOException{
        WishList result = new WishList();
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(post);
        
        Pattern p = Pattern.compile(".*[^A-Za-z0-9-_](mywishlist.ru/me/[A-Za-z0-9-_]+).*");
        Matcher m = p.matcher(post);
        if(m.find()){
            result.setHref(m.group(1));
            
        }
        
        if(result.getHref() == null){
            Pattern p1 = Pattern.compile(".*[^A-Za-z0-9-_](mywishlist.ru/wishlist/[A-Za-z0-9-_]+).*");
            Matcher m1 = p1.matcher(post);
            if(m1.find()){
                result.setHref(m1.group(1));
            }
        }
       
        
        if(result.getHref() == null){
            Pattern p1 = Pattern.compile(".*[^A-Za-z0-9-_]([A-Za-z0-9-_]+\\.mywishlist.ru).*");
            Matcher m1 = p1.matcher(post);
            if(m1.find()){
                result.setHref(m1.group(1));
            }
        }
       
        if(result.getHref() == null && !post.contains("greetinger") && !post.contains("valentinr") && !post.contains("mywishlist.ru/pic")){
            Pattern p1 = Pattern.compile(".*[^A-Za-z0-9-_](mywishlist.ru/[A-Za-z0-9-_]+).*");
            Matcher m1 = p1.matcher(post);
            if(m1.find()){
                result.setHref(m1.group(1));
            }
        }
        
        if(result.getHref() == null){
            return null;
        }
        
        result.setUserVKId(jsonNode.get("owner_id").asInt());
        result.setPostDate(jsonNode.get("date").asInt());
        
        
        return  result;
    }
    
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        for(File file: new File(Downloader.MYWISHLIST_DIR).listFiles()){
            for(String post: FileUtils.readLines(file, "UTF-8")){
                WishList wallPost = parse(post);
                if(wallPost != null){
                    FileUtils.write(new File(MODEL_DIR), objectMapper.writeValueAsString(wallPost) + "\n", "UTF-8", true);
                }else if(post.contains("greetinger")){
                    FileUtils.write(new File(GREETINGER_DIR), post  + "\n", "UTF-8", true);
                }else if(post.contains("valentinr")){
                    FileUtils.write(new File(VALENTINR_DIR), post  + "\n", "UTF-8", true);
                }else if(post.contains("mywishlist.ru/pic")){
                    FileUtils.write(new File(PIC_DIR), post  + "\n", "UTF-8", true);
                }else{
                    FileUtils.write(new File(OTHER_DIR), post  + "\n", "UTF-8", true);
                }
            }
        }
//        int dfd = 9;
//    String test = "{\"id\":147,\"date\":1265649004,\"owner_id\":32696,\"from_id\":32696,\"post_type\":\"post\",\"text\":\"http://mywishlist.ru/me/mylilac.\",\"comments\":{\"count\":0},\"likes\":{\"count\":3},\"reposts\":{\"count\":1}}";
//    parse(test);
    
    
    
    }
}
