package com.mycompany.tinder2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class AuthFilter implements Filter{
    private FilterConfig filterConfig;
    private Map<String, String> userId2accessTocken = new HashMap<String, String>();
    
    
    @Override
    public void init(FilterConfig fConfig) throws ServletException
    {
        filterConfig = fConfig;
    }

    private String[] userIdAndPassword(HttpServletRequest httpServletRequest){
        String[] result =  new String[2];
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies == null){
            return result;
        }
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("userId")){
                result[0] = cookie.getValue();
            }else if(cookie.getName().equals("password")){
                result[1] = cookie.getValue();
            }
        }
        
        return result;
    }

    private String[] vkIdAndAccessToken(String code) throws Exception {
        int APP_CODE = 6318506;
        String CLIENT_SECRET = "FAgQj1Fo3gSjaDeLuvf5";
    
        String[] result = new String[2];
        String url = "https://oauth.vk.com/access_token?client_id=" + APP_CODE + "&client_secret=" + CLIENT_SECRET + "&redirect_uri=http://tinder2.com:8080/Tinder2/access_token?&code=" + code;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readValue(Utils.getRequest(url), JsonNode.class);
        
        result[0] = rootNode.get("user_id").asText();
        result[1] = rootNode.get("access_token").asText();
        
        return result; 

   }

    // @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
        HttpServletResponse httpServletResponse = ((HttpServletResponse) response);
        
        String[] userIdAndPassword = userIdAndPassword(httpServletRequest);
        
//        if(httpServletRequest.getRequestURI().startsWith("/Tinder2/access_token")){
//            try {
//                String[] vkIdAndAccessToken = vkIdAndAccessToken(httpServletRequest.getParameter("code"));
//                if(!loginManager.userIsRegistered(vkIdAndAccessToken[0])){
//                    loginManager.registerUser(httpServletRequest.getSession().getId(), vkIdAndAccessToken[0], vkIdAndAccessToken[1]);
//                }else if(userId != null){
//                    loginManager.setAccessToken(vkIdAndAccessToken[1], userId);
//                }else{
//                   Cookie cookie = new Cookie("id", httpServletRequest.getSession().getId());
//                   httpServletResponse.addCookie(cookie); 
//                   ServletContext ctx = filterConfig.getServletContext();
//                   RequestDispatcher dispatcher = ctx.getRequestDispatcher("/code");
//                   dispatcher.forward(request, response); 
//                }
//            } catch (Exception ex) {
//               
//            }
//        }
        
        if(userIdAndPassword[0] == null  && 
           !httpServletRequest.getRequestURI().startsWith("/Tinder2/access_token") &&
           !httpServletRequest.getRequestURI().startsWith("/Tinder2/vkIdAndPassword")){
            ServletContext ctx = filterConfig.getServletContext();
            RequestDispatcher dispatcher = ctx.getRequestDispatcher("/code");
            dispatcher.forward(request, response);
            return;
        }else if(httpServletRequest.getRequestURI().startsWith("/Tinder2/vkIdAndPassword")){
            Cookie id = new Cookie("userId", httpServletRequest.getParameter("userId"));
            httpServletResponse.addCookie(id);
            Cookie password = new Cookie("password", httpServletRequest.getParameter("password"));
            httpServletResponse.addCookie(password);
            
            ThreadContext.set("userId", httpServletRequest.getParameter("userId"));
            ThreadContext.set("password", httpServletRequest.getParameter("password"));
            
        }
        
        chain.doFilter(request, response);
//        else{
//            ThreadContext.set("userId", userId);
//            ThreadContext.set("mode", loginManager.getAccessToken(userId));
//        }
        
//        if(httpServletRequest.getRequestURI().split("/")[2].equals("access_tocken")){
//            String[] idAndAccessToken;
//            try {
//                idAndAccessToken =  vkDAO.idAndAccessToken(httpServletRequest.getParameter("code"));
//                userId2accessTocken.put(getDnId(httpServletRequest), idAndAccessToken[1]);
//                
//                
//            } catch (Exception ex) {
//                Logger.getLogger(AuthFilter.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            httpServletRequest.getAttribute("code");
//        }
//        
//        String accessTocken = null;
//        if(getDnId(httpServletRequest) != null){
//            accessTocken = userId2accessTocken.get(getDnId(httpServletRequest));
//        }
//        
//        
//        if(accessTocken == null){
//            Cookie cookie = new Cookie("dnId", httpServletRequest.getSession().getId());
//            
//            httpServletResponse.addCookie(cookie);
//            
//            ServletContext ctx = filterConfig.getServletContext();
//            RequestDispatcher dispatcher = ctx.getRequestDispatcher("/code");
//            dispatcher.forward(request, response); 
//        }else{
//            System.err.println("");
//        }
        
               
        
    }

   // @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
