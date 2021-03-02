package com.javaproject.coronavirustracker.services;

import com.javaproject.coronavirustracker.models.LocationStats;
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

    private static String VIRUS_DATA_URL ="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    @PostConstruct//this annotation tells to execute this method after objects are instantiated
    @Scheduled(cron = "* * 1 * * *")//this annotation runs the code first hour of each day, each '*' indicate second,minute,hour,day,month and year
    public void fetchVirusData() throws IOException, InterruptedException {

        List<LocationStats> newStats = new ArrayList<>();

        //used to send request and retrieve their responses
        HttpClient client = HttpClient.newHttpClient();

        //used to create request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        //Its response after client sends request
        //BodyHandlers deal with what to do with body, here it returns the url body as String
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());

        //This code below helps to auto detect header of a csv file
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationStats locationStats = new LocationStats();
            locationStats.setState(record.get("Province/State"));
            locationStats.setCountry(record.get("Country/Region"));

            //getting total cases using the column number
            int latestCases = Integer.parseInt(record.get(record.size()-1));//latest column is total columns-1
            int prevDayCases = Integer.parseInt(record.get(record.size()-2));//prev day column is total columns-2

            locationStats.setLatestTotalCases(latestCases);
            locationStats.setDiffFromPrevDay(latestCases-prevDayCases);
            //System.out.println(locationStats);

            newStats.add(locationStats);
        }
        //Header auto-detect code ends here

        this.allStats = newStats;
    }

}
