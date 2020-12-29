package com.test.tickets.service;

import com.test.tickets.domain.Request;
import com.test.tickets.enums.Status;
import com.test.tickets.interfaces.RequestHandling;
import com.test.tickets.repos.RequestRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class RequestService implements RequestHandling {
    @Autowired
    private RequestRepo requestRepo;

    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    public Integer createRequest(String routeNumber, String date) {
        Request request = new Request();
        request.setRouteNumber(routeNumber);
        request.setDate(date);
        request.setStatus(Status.PROCESSED);
        requestRepo.save(request);
        return request.getId();
    }

    public Request changeStatus(Request request) {
        request.setStatus(Status.randomStatus());
        return request;
    }

    @Scheduled(cron = "*/60 * * * * *")
    public void processStatus() {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8099/process";
        Request request = requestRepo.findFirstByStatus(Status.PROCESSED);
        HttpEntity<?> requestEntity =
                new HttpEntity<>(request, headers);
        try {
            ResponseEntity<Request> forEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Request.class);
            Request processedRequest = forEntity.getBody();
            requestRepo.save(processedRequest);
        }catch (HttpClientErrorException e) {
            logger.info("There is no request to process " + new Date(System.currentTimeMillis()));
        }
    }

    private Status checkStatus(Integer requestId) {
        Request request = requestRepo.findById(requestId);
        return request.getStatus();
    }
}
