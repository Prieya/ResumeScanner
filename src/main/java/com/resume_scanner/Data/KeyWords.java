package com.resume_scanner.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class KeyWords {
    private Set<String> softSkill;
    private Set<String> hardSkill;
    private Set<String> clichesNbuzzword ;
    private Set<String> ActionVerb;


    public KeyWords() throws FileNotFoundException {
        softSkill = fileToSet(new Scanner(new File("Skill-Fills/SoftSkill")) );
        hardSkill = fileToSet(new Scanner(new File("Skill-Fills/HardSkill")) );
        clichesNbuzzword = fileToSet(new Scanner(new File("Skill-Fills/ClichesAndBuzzwords")) );
        ActionVerb = fileToSet(new Scanner(new File("Skill-Fills/ActionVerb")) );
    }

    public Set<String> getSoftSkill() {
        return softSkill;
    }

    public void setSoftSkill(Set<String> softSkill) {
        this.softSkill = softSkill;
    }

    public Set<String> getHardSkill() {
        return hardSkill;
    }

    public void setHardSkill(Set<String> hardSkill) {
        this.hardSkill = hardSkill;
    }

    public Set<String> getClichesNbuzzword() {
        return clichesNbuzzword;
    }

    public void setClichesNbuzzword(Set<String> clichesNbuzzword) {
        this.clichesNbuzzword = clichesNbuzzword;
    }

    public Set<String> getActionVerb() {
        return ActionVerb;
    }

    public void setActionVerb(Set<String> actionVerb) {
        ActionVerb = actionVerb;
    }

    public static Set<String> fileToSet(Scanner file){
        Set<String> skill = new HashSet<>();
        while(file.hasNext()){
            skill.add(file.next().toLowerCase());
        }
        return skill;
    }

    public Set<String> SearchHardSkill(Map<String, Integer> map){
        Map<String, Integer> AllHArdSkill = new HashMap<>();
        for(String words: map.keySet()){
            if(this.hardSkill.contains(words)){
                AllHArdSkill.put(words, map.get(words));
            }
        }
        if(AllHArdSkill.size() < 7){
            HashSet<String> set = new HashSet<>();
            for (String item : AllHArdSkill.keySet()) {
                set.add(item);
            }
            return set;
        }

        return FiveMax(AllHArdSkill);
    }

    public Set<String> SearchSoftSkill(Map<String, Integer> map){
        Map<String, Integer> AllSoftSkill = new HashMap<>();
        for(String words: map.keySet()){
            if(this.softSkill.contains(words)){
                AllSoftSkill.put(words, map.get(words));
            }
        }
        if(AllSoftSkill.size() < 7){
            HashSet<String> set = new HashSet<>();
            for (String item : AllSoftSkill.keySet()) {
                set.add(item);
            }
            return set;
        }

        return FiveMax(AllSoftSkill);
    }

    public Set<String> SearchVerb(Map<String, Integer> map){
        Map<String, Integer> AllVerb = new HashMap<>();
        for(String words: map.keySet()){
            if(this.ActionVerb.contains(words)){
                AllVerb .put(words, map.get(words));
            }
        }
        if(AllVerb .size() < 7){
            HashSet<String> set = new HashSet<>();
            for (String item : AllVerb.keySet()) {
                set.add(item);
            }
            return set;
        }

        return FiveMax(AllVerb );
    }

    public Set<String> SearchCliches(Map<String, Integer> map){
        Map<String, Integer> AllCliches = new HashMap<>();
        for(String words: map.keySet()){
            if(this.clichesNbuzzword.contains(words)){
                AllCliches .put(words, map.get(words));
            }
        }
        if(AllCliches .size() < 7){
            HashSet<String> set = new HashSet<>();
            for (String item : AllCliches.keySet()) {
                set.add(item);
            }
            return set;
        }

        return FiveMax(AllCliches);
    }

    public Set<String> FiveMax(Map<String, Integer> map){
        Set<String> maxWords = new HashSet<>();
        int max = 0;
        int maxValue = Collections.max(map.values());
        for (String entry : map.keySet()) {
            if (map.get(entry) == maxValue && max <= 7) {
                maxWords.add(entry);
                max++;
            }
        }

        return maxWords;
    }

    public HashMap<String, Integer> StringToMap(String resume){
        String[] wordArray = resume.split(" ");
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



}
