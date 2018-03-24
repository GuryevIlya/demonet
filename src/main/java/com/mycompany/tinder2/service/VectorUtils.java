package com.mycompany.tinder2.service;

import com.github.stagirs.lingvo.morpho.MorphoAnalyst;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author delet
 */
public class VectorUtils {
    private static List<String> toWords(String text){
        String withOutInvisibleChar = text.replace("\n", " ")
                                          .replace("\t", " ")
                                          .replace(".", " ")
                                          .replace(",", " ")
                                          .replace("?", " ")
                                          .replace(":", " ")
                                          .replace("!", " ");
        
        MorphoAnalyst ma = new MorphoAnalyst();
        return  MorphoAnalyst.normalize(Arrays.asList(withOutInvisibleChar.split(" ")));
    }
    
    
    
    public static Map<String, Integer> text2vector(String text){
        Map<String, Integer> result = new HashMap<String, Integer>();
        
        List<String> words = toWords(text);
        for(String word : words){
            if(result.containsKey(word)){
                result.put(word, result.get(word) + 1);
            }else{
                result.put(word, 1);
            }
        }
        
        return result;
    }
    
    public Map<String, Integer> sumVectors(List<Map<String, Integer>> vectors){
        Map<String, Integer> result = new HashMap<String, Integer>();
        
        for(Map<String, Integer> vector : vectors){
            for (Map.Entry<String, Integer> entry : vector.entrySet()) {
                if(!result.containsKey(entry.getKey())){
                    result.put(entry.getKey(), entry.getValue());
                }else{
                    result.put(entry.getKey(), result.get(entry.getKey()) + entry.getValue());
                }
            }
        }
        
        return result;
    }
    
    public static Map<String, Integer> sumVectors(Map<String, Integer> vector1, Map<String, Integer> vector2){
        Map<String, Integer> result = new HashMap<String, Integer>();
        
        for (Map.Entry<String, Integer> entry : vector1.entrySet()) {
            if(!result.containsKey(entry.getKey())){
                result.put(entry.getKey(), entry.getValue());
            }else{
                result.put(entry.getKey(), result.get(entry.getKey()) + entry.getValue());
            }
        }
        
        for (Map.Entry<String, Integer> entry : vector2.entrySet()) {
            if(!result.containsKey(entry.getKey())){
                result.put(entry.getKey(), entry.getValue());
            }else{
                result.put(entry.getKey(), result.get(entry.getKey()) + entry.getValue());
            }
        }
        
        
        return result;
    }
    
    
    public static Map<String, Integer> multVector(Map<String, Integer> vector, Integer coefficient){
        Map<String, Integer> result = new HashMap<String, Integer>();
        
        for (Map.Entry<String, Integer> entry : vector.entrySet()) {
            result.put(entry.getKey(), vector.get(entry.getKey()) * coefficient);
        }
        
        return result;
    }
    
    private static Double module(Map<String, Integer> vector){
        int sumOfSquares = 0;
        for (Map.Entry<String, Integer> entry : vector.entrySet()) {
            sumOfSquares += entry.getValue() * entry.getValue();
        }
        
        return Math.sqrt(sumOfSquares);
    
    }
    
    private static int scalarProduct(Map<String, Integer> vector1, Map<String, Integer> vector2){
        int result = 0;
        for (Map.Entry<String, Integer> entry : vector1.entrySet()) {
            Integer val1 = entry.getValue();
            Integer val2 = vector2.get(entry.getKey());
            result += val1 * (val2 != null ? val2 : 1);
            
        }
        
        return result;
    }
    
    public static Double cosSim(Map<String, Integer> vector1, Map<String, Integer> vector2){
        return scalarProduct(vector1, vector2)/(module(vector1) * module(vector2));
    }
    
    
    public static void main(String[] args){
        String text = "слово тест";
        System.out.print(text);
        MorphoAnalyst ma = new MorphoAnalyst();
        List<String>  list = MorphoAnalyst.normalize(Arrays.asList(text.split(" ")));
        int t = 5;
    }
}
