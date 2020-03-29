package com.brainfish.components;


import lombok.Data;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.TreeMap;

@Data
@Getter
public class InMemoryDB
{
    // leaving this as is for now but ideally we should
    // limit our hashmap growth to never exceed jvm allocated
    // memory
    private final LinkedHashMap<String, String> inMemoryDB;
    private final TreeMap<String, Integer> countIndex;

    private InMemoryDB(){
        this.inMemoryDB = new LinkedHashMap<>();
        this.countIndex = new TreeMap<>();
    }

    public static InMemoryDB getInstance(){
        return new InMemoryDB();
    }

    public Integer getCount(String value){
        return countIndex.getOrDefault(value, 0);
    }
    /**
     * Wrapper functions to hashmap
     */
    public void addItem(String key, String value){
        try {
            Integer count;
            if (inMemoryDB.get(key) != null)
            {
                if (!inMemoryDB.get(key).equals(value)) {
                    count = countIndex.getOrDefault(inMemoryDB.get(key), 0);
                    count--;
                    countIndex.put(inMemoryDB.get(key),count);
                }
            }
            if(inMemoryDB.get(key) == null || !inMemoryDB.get(key).equals(value)) {
                count =  countIndex.getOrDefault(value, 0);
                count++;
                inMemoryDB.put(key, value);
                countIndex.put(value,count);
            }
        } catch (OutOfMemoryError outOfMemoryError){
            System.out.println("Ran out of memory for inmemoryDB. Please remove an item");
        }
    }

    /**
     * Wrapper functions to hashmap
     */
    public String getItem(String key){
        return inMemoryDB.getOrDefault(key, "NULL");
    }

    /**
     * Wrapper functions to hashmap
     */
    public void deleteItem(String key){
        String value = inMemoryDB.get(key);
        if (value != null) {
            Integer count = countIndex.getOrDefault(value, 0);
            count--;
            if (count == 0) {
                countIndex.remove(value);
            } else {
                countIndex.put(value,Math.max(count, 0));
            }
            inMemoryDB.remove(key);
        }
    }

    /**
     * Wrapper functions to hashmap
     */
    public void updateItem(String key, String value){
        String currValue = inMemoryDB.get(key);
        int count;
        if (currValue != null && !currValue.equals(value))
        {
            count = countIndex.getOrDefault(currValue, 0);
            count--;
            if (count == 0) {
                countIndex.remove(currValue);
            } else {
                countIndex.put(currValue,Math.max(count, 0));
            }
            inMemoryDB.put(key, value);
        }
        count = countIndex.getOrDefault(value, 0);
        count++;
        countIndex.put(value,Math.max(count,0));
    }

}
