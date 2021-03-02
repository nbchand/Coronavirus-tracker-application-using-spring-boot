package com.javaproject.coronavirustracker.controllers;

import com.javaproject.coronavirustracker.models.LocationStats;
import com.javaproject.coronavirustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//The difference between @Controller and @RestController is that @RestController returns data in
//the form of jason or xml whereas we get webpage(html+css) in return using @Controller
//Here, we need a webpage so better to use @Controller
@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model){
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();

        //here we just calculated total number of cases from allstats object
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();



        //Model le haami lai webpage maa kunai value pathaaunu parne chha vaney vann
        //ma pachhi webpage maa tyo value lai add garidinchhu vanchha
        //so we send an attribute and its value respectively
        //model.addAttribute("test","Wassup");
        model.addAttribute("locationStats",allStats);
        model.addAttribute("totalReportedCases",totalReportedCases);
        model.addAttribute("totalNewCases",totalNewCases);
        return "home";
    }
}
