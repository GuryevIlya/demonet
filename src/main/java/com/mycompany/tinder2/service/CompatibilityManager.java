package com.mycompany.tinder2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class CompatibilityManager {
    @Autowired
    private UserManager userManager;
    
    Map<String, Double> couple2Compatibility = new HashMap<String, Double>();
    
    
    @PostConstruct
    public void init() {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            
            File couple2CompatibilityFile = new File("C:\\demonetData\\couple2Compatibility.txt");
            if(couple2CompatibilityFile.exists()){
                for(String line : FileUtils.readLines(couple2CompatibilityFile, "utf-8")){
                    String[] parts = line.split("\t");
                    couple2Compatibility.put(parts[0], toDouble(parts[1]));
                }
            }
        }catch(Exception e){
            int i = 8;
        }
    }
    
    
   public Double getCompatibility(Integer userId1, Integer userId2) throws IOException, InterruptedException{
        StringBuilder couple = new StringBuilder();
        couple.append(userId1).append(",").append(userId2);
        StringBuilder couple1 = new StringBuilder();
        couple1.append(userId1).append(",").append(userId2);
        
        if(couple2Compatibility.containsKey(couple.toString())){
            return couple2Compatibility.get(couple.toString());
        }
        if(couple2Compatibility.containsKey(couple1.toString())){
            return couple2Compatibility.get(couple1.toString());
        }
        
        
        Map<String, Integer> vector1 = userManager.vector(userId1);
        Map<String, Integer> vector2 = userManager.vector(userId2);
    
        Double result = VectorUtils.cosSim(vector1, vector2);
        
        FileUtils.write(new File("C:\\demonetData\\couple2Compatibility.txt"), couple.toString() + "\t" + result + "\n", "UTF-8", true);
        
        couple2Compatibility.put(couple.toString(), result);
        return result;
    }
   
   
    public static void main(String[] args) throws IOException, InterruptedException{
        CompatibilityManager cm = new CompatibilityManager();
        
        Double dbl = cm.getCompatibility(57192688, 6397697);
        int i = 9;
    }
   
    
}
