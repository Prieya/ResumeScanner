package com.resume_scanner.Data;

import jakarta.persistence.Entity;
import org.springframework.boot.autoconfigure.batch.BatchProperties;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobDescription {
    private String Description;
    private String JobTitle;

    public JobDescription(String Description, String JobTitle){
        this.Description = Description;
        this.JobTitle = JobTitle;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public HashMap<String, Integer> StringToMap(){
        String text = this.Description.replace("[.,:;']", "");
        String[] wordArray = text.split(" ");
        HashMap<String, Integer> wordMap = new HashMap<>();
        for(String word: wordArray){
            if(wordMap.containsKey(word)){
                wordMap.put(word, wordMap.get(word)+1);
            }else{
                wordMap.put(word, 1);
            }
        }
        return wordMap;
    }

    @Override
    public String toString() {
        return "JobDescription{" +
                "Description='" + Description + '\'' +
                ", JobTitle='" + JobTitle + '\'' +
                '}';
    }
}
