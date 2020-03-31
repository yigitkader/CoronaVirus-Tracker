package com.loops.coronavirustracker.services;


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

@Service
@Scheduled(cron = "* * * * * *")
public class CoronaVirusDataService {

    private static String CORONAVIRUS_DATA_URL ="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    @PostConstruct  //when this service's construct starts after that starts this func.
    public void getCoronaVirusData() throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest= HttpRequest.newBuilder()
                .uri(URI.create(CORONAVIRUS_DATA_URL))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            String provinceState = record.get("Province/State");
            System.out.println(provinceState);

        }




    }

}
