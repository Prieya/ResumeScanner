package com.resume_scanner.Util;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.Loader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.thymeleaf.expression.Strings;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeUtil {
    private Path foundFile;


    public static String saveFile(String fileName, MultipartFile multipartFile)
            throws IOException {
        Path uploadPath = Paths.get("Files-Upload");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileCode = RandomStringUtils.randomAlphanumeric(8);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return fileCode;
    }


    public Resource getFileAsResource(String fileCode) throws IOException {
        Path dirPath = Paths.get("Files-Upload");

        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileCode)) {
                foundFile = file;
                return;
            }
        });

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }

    public String  FileTOString(Resource resource) throws IOException{
        File file = new File(resource.getURI());
        PDDocument document = Loader.loadPDF(file);
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        pdfTextStripper.setStartPage(1);
        pdfTextStripper.setEndPage(5);
        String text  = pdfTextStripper.getText(document);
        return text;
    }
    public Boolean email(HashMap<String, Integer> resume){
        for(String words: resume.keySet()){
            if(words.contains("@")){
                return true;
            }
        }
        return false;
    }

    public Boolean linked(HashMap<String, Integer> resume){
        for(String words: resume.keySet()){
            String lowerCase = words.toLowerCase();
            if(lowerCase.contains("linked")){
                return true;
            }
        }
        return false;
    }

    public Boolean jobTitle(HashMap<String, Integer> resume, String title){
        for(String words: resume.keySet()){
            String lowerCase = words.toLowerCase();
            if(lowerCase.contains(title)){
                return true;
            }
        }
        return false;
    }

    public Boolean Section(HashMap<String, Integer> resume){
        Set<String> section = new HashSet<>();
        section.add("education");
        section.add("experience");
        section.add("skill");
        section.add("certificate");
        section.add("project");
        for(String words: section){
            if(resume.containsKey(words)){
                return true;
            }
            return false;
        }
        return false;
    }

    public Boolean Education(HashMap<String, Integer> description, HashMap<String, Integer> resume){
        Set<String> section = new HashSet<>();
        section.add("high school");
        section.add("bachelor");
        section.add("baster");
        section.add("associate");
        for(String words: section){
            if(description.containsKey(words.toLowerCase())){
                if(resume.containsKey(words))return true;
            }
        }
        return false;
    }

    public Boolean measureable(HashMap<String, Integer> resume){
        for(String words: resume.keySet()){
            if(words.contains("%")){
                return true;
            }
        }
        return false;
    }

    public Boolean Date(HashMap<String, Integer> resume){
        String regex = "(\\s+\\d+\\w+\\d+\\d+\\d+\\d+,\\d+\\d+\\d+\\d+)";
        for(String words: resume.keySet()){
            Matcher matcher = Pattern.compile(regex).matcher(words);
            if(matcher.find()){
                return true;
            }
        }
        return false;
    }

    public Boolean WordCount(HashMap<String, Integer> resume){
        int count = 0;
        for(Integer nums: resume.values()){
            count += nums;
        }
        if(count > 400)return true;
        return false;
    }
    public HashMap<String, Boolean> findWords(Set<String> description, HashMap<String, Integer> resume){
        HashMap<String, Boolean> containWords = new HashMap<String, Boolean>();
        for(String word: description){
            if(resume.containsKey(word)){
                String cap = word.substring(0, 1).toUpperCase() + word.substring(1);
                containWords.put(cap, true);
            }else if(word.length() > 0){
                String cap = word.substring(0, 1).toUpperCase() + word.substring(1);
                containWords.put(cap, false);
            }
        }
        return containWords;
    }

}
