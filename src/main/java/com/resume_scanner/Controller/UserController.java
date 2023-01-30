package com.resume_scanner.Controller;
import com.resume_scanner.Data.*;

import com.resume_scanner.Util.ResumeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Controller
public class UserController {
    private JobDescription job;
    @Autowired
    private UserRepository repository;


    @PostMapping("/Resume")
    public String uploadFile(@ModelAttribute User users, @RequestParam(name="file")
            MultipartFile multipartFile, Model model)
            throws IOException {
        ResumeUtil downloadUtil = new ResumeUtil();
        Resource resource = null;
        String text="";
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();

        String filecode = ResumeUtil.saveFile(fileName, multipartFile);
        repository.save(users);
        System.out.println(repository.findByEmail(users.getEmail()));
        Resume response = new Resume();
        response.setFileName(fileName);
        response.setSize(size);
        response.setDownloadUri("/downloadFile/" + filecode);
        try {
            resource = downloadUtil.getFileAsResource(filecode);
            text = downloadUtil.FileTOString(resource);
        } catch (IOException e) {

        }
        model.addAttribute("ResumeText", text);
        return "resume";
    }

    @PostMapping("/Job")
    public String uploadJobDescription(@RequestParam(name="resume")String resume, Model model, JobDescription jobDescription) throws FileNotFoundException {
        KeyWords words = new KeyWords();
        ResumeUtil resumeUtil = new ResumeUtil();
        job = jobDescription;
        double practiseScore = 0, hardSkillScore = 0,
                softSkillScore = 0, indexScore = 0, totalScore = 0;

        HashMap<String, Integer> resumewords = words.StringToMap(resume);
        HashMap<String, Integer> description = job.StringToMap();
        Set<String> hardSkill = words.SearchHardSkill(description);
        Set<String> softSkill = words.SearchSoftSkill(description);
        Set<String> cliches = words.SearchCliches(description);
        Set<String> verbs = words.SearchVerb(resumewords);
        HashMap<String, Boolean> containHardSkill = resumeUtil.findWords(hardSkill, resumewords);
        HashMap<String, Boolean> containSoftSkill = resumeUtil.findWords(softSkill, resumewords);
        for(boolean hs: containHardSkill.values()){
            if(hs)hardSkillScore++;
        }
        for(boolean ss: containSoftSkill.values()){
            if(ss)softSkillScore++;
        }
        boolean email = resumeUtil.email(resumewords);
        if(email)practiseScore++;
        boolean linked = resumeUtil.linked(resumewords);
        if(linked)practiseScore++;
        boolean title = resumeUtil.jobTitle(resumewords, job.getJobTitle().toLowerCase());
        if(title)practiseScore++;
        boolean section = resumeUtil.email(resumewords);
        if(section)practiseScore++;
        boolean education = resumeUtil.Education(description, resumewords);
        if(education)practiseScore++;
        boolean date = resumeUtil.Date(resumewords);
        if(date)practiseScore++;

        boolean measureable = resumeUtil.measureable(resumewords);
        if(measureable)indexScore++;
        boolean WordCount = resumeUtil.WordCount(resumewords);
        if(WordCount)indexScore++;

        model.addAttribute("email", email);
        model.addAttribute("linkedIn", linked);
        model.addAttribute("title", title);
        model.addAttribute("education", education);
        model.addAttribute("section", section);
        model.addAttribute("measureable", measureable);
        model.addAttribute("date", date);
        model.addAttribute("word_count", WordCount);
        boolean clichesAndBuzz = false;
        if(cliches.size() > 3){
            clichesAndBuzz = true;
        }
        if(clichesAndBuzz)indexScore--;
        model.addAttribute("clichesAndBuzz", clichesAndBuzz);
        boolean verb = false;
        if(verbs.size() > 5){
            verb = true;
        }
        if(verb)indexScore++;
        model.addAttribute("verb", verb);
        model.addAttribute("softSkill", containSoftSkill);
        model.addAttribute("hardSkill", containHardSkill);
        practiseScore = (practiseScore/6);
        indexScore = (indexScore/4);
        hardSkillScore = (hardSkillScore/hardSkill.size());
        softSkillScore = (softSkillScore/softSkill.size());
        totalScore = (practiseScore + indexScore + hardSkillScore + softSkillScore)/4 * 100;
        model.addAttribute("totalScore", (int)totalScore);

        return "score";
    }


}
