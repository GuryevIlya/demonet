package com.mycompany.tinder2.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static javafx.scene.input.KeyCode.K;
import static javafx.scene.input.KeyCode.V;
import static org.apache.commons.lang3.math.NumberUtils.toInt;

/**
 *
 * @author delet
 */
public class Utils {
    private final static String USER_AGENT = "Mozilla/5.0";
    
    public static String getRequest(String url) throws MalformedURLException, IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        
        try(BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"));){
            
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            
            return response.toString();
        }
    }
    
    public static List<Integer> sortKeyByValueAndKey(Map<Integer, Integer> map){
        List<Map.Entry<Integer, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>(){
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2){
                if(o1.getValue() - o2.getValue() == 0){
                    return o1.getKey() - o2.getKey();
                }else{
                    return  o2.getValue() - o1.getValue();
                }
            }
        });

        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : list)
        {
            result.add(entry.getKey());
        }
        return result;
    
    }
}
