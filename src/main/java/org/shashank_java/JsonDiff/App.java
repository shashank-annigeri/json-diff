package org.shashank_java.JsonDiff;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

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
				sortJsonElement(jsonObject1, treeMap1);
				sortJsonElement(jsonObject2, treeMap2);
				
				System.out.println("FirstMap:");
				System.out.println(treeMap1.toString().replace("=", ":"));
				System.out.println("SecondMap:");
				System.out.println(treeMap2.toString().replace("=", ":"));
				System.out.println("ResultMap:");
				checkForDifferences(treeMap1, treeMap2);	
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

	public static TreeMap<String, Object> sortJsonElement (JsonObject jsonObject, TreeMap<String, Object> parentMap){
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
		
		for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			if(entry.getValue().isJsonObject()){
				sortJsonElement(entry.getValue().getAsJsonObject(), treeMap);
				parentMap.put(entry.getKey(), treeMap);
			}else{
					parentMap.put(entry.getKey(), entry.getValue());
			}
		}
		return treeMap;
	}

	private static void checkForDifferences(TreeMap<String, Object> treeMap1, TreeMap<String, Object> treeMap2) {
		Set<String> set1 = new HashSet<String>(treeMap1.keySet());
		Set<String> set2 = new HashSet<String>(treeMap2.keySet());
		
		Set<String> set3 = new HashSet<String>(set1);
	    set3.addAll(set2);
	    	
	    for(String s:set3){
	    	if(treeMap2.containsKey(s) && treeMap1.containsKey(s)) {
	    		if(treeMap2.get(s) instanceof JsonObject){
	    			checkInnerLevel(treeMap1.get(s), treeMap2.get(s));
	    		}
	    		changeMap.put(s, treeMap2.get(s));
	    	}else if(treeMap2.containsKey(s) && !treeMap1.containsKey(s)){
	    		if(treeMap2.get(s) instanceof JsonObject){
	    			checkInnerLevel(treeMap1.get(s), treeMap2.get(s));
	    		}
	    		addMap.put(s, treeMap2.get(s));
	    	}else if(treeMap1.containsKey(s) && !treeMap2.containsKey(s)){
	    		if(treeMap2.get(s) instanceof JsonObject){
	    			checkInnerLevel(treeMap1.get(s), treeMap2.get(s));
	    		}
	    		removeMap.put(s, treeMap1.get(s));
	    	}
	    }
	    
	    System.out.println("Add:\n"+addMap.toString().replace("=", ":"));
	    System.out.println("Change:\n"+changeMap.toString().replace("=", ":"));
	    System.out.println("Remove:\n"+removeMap.toString().replace("=", ":"));
	}

	private static void checkInnerLevel(Object tm1,  Object tm2) {
		JsonElement innerJsonElement1 = (JsonElement) tm1;
		JsonObject innerJsonObject1 = innerJsonElement1.getAsJsonObject();
		JsonElement innerJsonElement2 = (JsonElement) tm1;
		JsonObject innerJsonObject2 = innerJsonElement2.getAsJsonObject();
		TreeMap<String, Object> treeMap1 = new TreeMap<String, Object>();
		TreeMap<String, Object> treeMap2 = new TreeMap<String, Object>();
		for(Entry<String, JsonElement> entry : innerJsonObject1.entrySet()){
			treeMap1.put(entry.getKey(), entry.getValue());
		}
		for(Entry<String, JsonElement> entry : innerJsonObject2.entrySet()){
			treeMap2.put(entry.getKey(), entry.getValue());
		}
		Set<String> set1 = new HashSet<String>(treeMap1.keySet());
		Set<String> set2 = new HashSet<String>(treeMap2.keySet());
		
		Set<String> set3 = new HashSet<String>(set1);
	    set3.addAll(set2);
	    
	    for(String s:set3){
	    	if(treeMap2.containsKey(s) && treeMap1.containsKey(s)) {
	    		if(treeMap2.get(s) instanceof JsonObject){
	    			checkInnerLevel(treeMap1.get(s), treeMap2.get(s));
	    		}
	    		changeMap.put(s, treeMap2.get(s));
	    	}else if(treeMap2.containsKey(s) && !treeMap1.containsKey(s)){
	    		if(treeMap2.get(s) instanceof JsonObject){
	    			checkInnerLevel(treeMap1.get(s), treeMap2.get(s));
	    		}
	    		addMap.put(s, treeMap2.get(s));
	    	}else if(treeMap1.containsKey(s) && !treeMap2.containsKey(s)){
	    		if(treeMap2.get(s) instanceof JsonObject){
	    			checkInnerLevel(treeMap1.get(s), treeMap2.get(s));
	    		}
	    		removeMap.put(s, treeMap1.get(s));
	    	}
	    }
		
	}
	
}
