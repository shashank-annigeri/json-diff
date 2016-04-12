package org.shashank_java.JsonDiff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Hello world!
 *
 */
public class App 
{
	static TreeMap<String, Object> mainTreeMap;
	static TreeMap<String, Object> addMap = new TreeMap<String, Object>();
	static TreeMap<String, Object> removeMap = new TreeMap<String, Object>();
	static TreeMap<String, Object> changeMap = new TreeMap<String, Object>();
	
	public static void main( String[] args )
	{
		JsonElement root;
		TreeMap<String, Object> treeMap1 = new TreeMap<String, Object>();
		TreeMap<String, Object> treeMap2 = new TreeMap<String, Object>();
		try {
			root = new JsonParser().parse(new FileReader("/Users/sannigeri/Desktop/file1.json"));
			JsonObject jsonObject1 = root.getAsJsonObject();
			
			root = new JsonParser().parse(new FileReader("/Users/sannigeri/Desktop/file2.json"));
			JsonObject jsonObject2 = root.getAsJsonObject();
			
			if(jsonObject1.equals(jsonObject2)){
				System.out.println("They are same");
			}else{
				flatJson(jsonObject1, treeMap1,"",0);
				flatJson(jsonObject2, treeMap2,"",0);
				
				System.out.println("FirstMap:");
				System.out.println(treeMap1.toString().replace("=", ":"));
				System.out.println("SecondMap:");
				System.out.println(treeMap2.toString().replace("=", ":"));
				System.out.println("ResultingMaps:");
				checkForDifferences(treeMap1, treeMap2);
				
				writeJson(addMap, "add.json");
				writeJson(changeMap, "edit.json");
				writeJson(removeMap, "remove.json");
				
				System.out.println("Added: ");
				System.out.println(addMap.toString().replace("=", ":"));
				System.out.println("Changed: ");
				System.out.println(changeMap.toString().replace("=", ":"));
				System.out.println("Removed: ");
				System.out.println(removeMap.toString().replace("=", ":"));
			}
			
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeJson(TreeMap<String, Object> map, String fileName) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File("/Users/sannigeri/Desktop/" + fileName), map);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		
	}

	public static void flatJson (JsonObject jsonObject, TreeMap<String, Object> parentMap, String parentKey, int jsonLevel){
		for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			if(entry.getValue().isJsonObject()){
				if(parentKey.equals("")){
					parentKey = entry.getKey()+".";
				}else{
					if(parentKey.endsWith(".")){
						parentKey = parentKey+entry.getKey();
					}else{
						parentKey = parentKey+"."+entry.getKey();
					}
				}
				++jsonLevel;
				flatJson(entry.getValue().getAsJsonObject(), parentMap, parentKey, jsonLevel);
				
			}else{
				if(jsonLevel >= 2){
					if(!parentKey.endsWith(".")){
						parentKey = parentKey + ".";
					}
				}
				parentMap.put(parentKey + entry.getKey(), entry.getValue());
			}
		}
		
	}

	private static void checkForDifferences(TreeMap<String, Object> treeMap1, TreeMap<String, Object> treeMap2){
		Set<String> set1 = new HashSet<String>(treeMap1.keySet());
		Set<String> set2 = new HashSet<String>(treeMap2.keySet());
		
		Set<String> set3 = new HashSet<String>(set1);
	    set3.addAll(set2);
	    
	    for(String s:set3){
	    	if(treeMap2.containsKey(s) && treeMap1.containsKey(s)) {
	    		changeMap.put(s, treeMap2.get(s));
	    	}else if(treeMap2.containsKey(s) && !treeMap1.containsKey(s)){
	    		addMap.put(s, treeMap2.get(s));
	    	}else if(treeMap1.containsKey(s) && !treeMap2.containsKey(s)){
	    		removeMap.put(s, treeMap1.get(s));
	    	}else{
	    		
	    	}
	    }
	}
}
