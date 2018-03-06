package com.mycompany.tinder2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.tinder2.model.vk.User;
import com.mycompany.tinder2.model.pages.FriendsPage;
import java.io.IOException;
import static java.time.Clock.offset;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author delet
 */
@Controller
public class CommonController {
    @Autowired
    VkDAO vkDAO;
    @Autowired
    LoginManager loginManager;
//    @Autowired
//    FriendsPage friendsPage;
    @Autowired
    CompatibilityManager compatibilityManager; 
    @Autowired
    MainManager mainManager;
    @Autowired
    LinkManager linkManager;
    
    private String VK_AUTH_URL = "https://oauth.vk.com/authorize?client_id=6318506&display=page&redirect_uri=http://tinder2.com:8080/Tinder2/access_token?&scope=friends&response_type=code&v=5.52";
    
    @RequestMapping(value="/", method = RequestMethod.GET)
    public  String start(HttpServletRequest request) throws IOException, InterruptedException {
       // compatibilityManager.processCommonFriendsCount(loginManager.getVkId());
       compatibilityManager.processCompatibility(loginManager.getVkId(), linkManager.friendsOfFriends(loginManager.getVkId()).keySet());
     //  return "redirect:" + VK_AUTH_URL; 
     //  return "newjsp";
        return "redirect:friendsPage";  
    }
    
    @RequestMapping(value="/code", method = RequestMethod.GET)
    public  String code(HttpServletRequest request) throws IOException {
        
       return "redirect:" + VK_AUTH_URL;
    }
    
    @RequestMapping(value="/access_token", method = RequestMethod.GET)
    public String accessToken(ModelMap model,
                           @RequestParam(value = "code", defaultValue = "") String code) throws IOException, Exception {
       String[] vkIdAndAccessToken =  vkDAO.idAndAccessToken("3612dd12be8a64efbf");
       String[] idAndPassport = loginManager.getIdAndPassport(vkIdAndAccessToken[0]);
       
       if(idAndPassport[0] == null){
           idAndPassport = loginManager.registerUser(vkIdAndAccessToken[0], vkIdAndAccessToken[1]);
       }
        
       return "redirect:vkIdAndPassword?vkId=" + idAndPassport[0] + "&password=" + idAndPassport[1];
    }
    
//    @RequestMapping(value="/couplesPage", method = RequestMethod.GET)
//    public String couplesPage(ModelMap model,
//                           @RequestParam(value = "user_id", defaultValue = "") String userId,
//                           @RequestParam(value= "access_token", defaultValue = "") String access_token,
//                           @RequestParam(value= "offset", defaultValue = "") int offset,
//                           @RequestParam(value= "count", defaultValue = "") int count) throws IOException {
//        
//        ObjectMapper om = new ObjectMapper();
//        model.addAttribute("userId", userId);
//        model.addAttribute("accessToken", access_token);
//        model.addAttribute("users", om.writeValueAsString(vkDAO.couples(access_token, userId, offset, count, "numberAndProximity")));
//        
//        return "couples";
//    }
    
    @RequestMapping(value="/friendsPage", method = RequestMethod.GET)
    public String friendsPage(ModelMap model) throws IOException, InterruptedException {
        
        ObjectMapper om = new ObjectMapper();
        model.addAttribute("users", om.writeValueAsString(mainManager.friends(loginManager.getVkId(), 0, 10, "numberAndProximity")));
        
        return "friends";
    }
    
    @RequestMapping(value="/friends", method = RequestMethod.GET)
    public @ResponseBody List<User> friends(ModelMap model,
                           @RequestParam(value= "offset", defaultValue = "") int offset,
                           @RequestParam(value= "count", defaultValue = "") int count,
                           @RequestParam(value= "sortType", defaultValue = "") String sortType) throws IOException, InterruptedException {
        return mainManager.friends(loginManager.getVkId(), offset, count, sortType);
    }
    
//    @RequestMapping(value="/couples", method = RequestMethod.GET)
//    public @ResponseBody User[][] couples(ModelMap model,
//                           @RequestParam(value = "user_id", defaultValue = "") String userId,
//                           @RequestParam(value= "access_token", defaultValue = "") String access_token,
//                           @RequestParam(value= "offset", defaultValue = "") int offset,
//                           @RequestParam(value= "count", defaultValue = "") int count,
//                           @RequestParam(value= "sortType", defaultValue = "") String sortType) throws IOException {
//        
//        
//        return vkDAO.couples(access_token, userId, offset, -1, sortType);
//    }
}
