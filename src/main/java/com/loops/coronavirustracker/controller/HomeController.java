package com.loops.coronavirustracker.controller;

import com.loops.coronavirustracker.models.LocationStats;
import com.loops.coronavirustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model){

        //model.addAttribute("locationStats",coronaVirusDataService.getAllStats());

        List<LocationStats> allStats=coronaVirusDataService.getAllStats();
        int totalCases = allStats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
        model.addAttribute("locationStats",allStats);
        model.addAttribute("totalReportedCases",totalCases);
        return "index";
    }
}
