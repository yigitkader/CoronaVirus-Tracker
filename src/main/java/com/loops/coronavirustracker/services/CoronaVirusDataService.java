package com.loops.coronavirustracker.services;


import com.loops.coronavirustracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


@Service
public class CoronaVirusDataService {

    private static String CORONAVIRUS_DATA_URL ="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationStats> allStats = new ArrayList<LocationStats>();


    public List<LocationStats> getAllStats() {
        return allStats;
    }

    @PostConstruct  //when this service's construct starts after that this func starts.
    @Scheduled(cron = "* * 1 * * *") // (second,minute,hour,day,...) it works every hour
    public void getCoronaVirusData() throws IOException, InterruptedException {

         List<LocationStats> newStats = new ArrayList<LocationStats>();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest= HttpRequest.newBuilder()
                .uri(URI.create(CORONAVIRUS_DATA_URL))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {

            LocationStats locationStats = new LocationStats();
            locationStats.setState(record.get("Province/State"));
            locationStats.setCountry(record.get("Country/Region"));
            locationStats.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));

            System.out.println(locationStats);
            newStats.add(locationStats);
        }

        this.allStats = newStats;




    }

}
