package com.test.tickets.controller;

import com.test.tickets.domain.Request;
import com.test.tickets.interfaces.RequestHandling;
import com.test.tickets.service.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class RequestController {
    @Autowired
    private RequestHandling requestHandling;

    @Autowired
    private RequestValidator requestValidator;

    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createRequest(@RequestBody Request request, BindingResult result) {
        logger.info("Request created " + new Date(System.currentTimeMillis()));

        requestValidator.validate(request, result);
        if (result.hasErrors()) {
            logger.info("Invalid data entered " + new Date(System.currentTimeMillis()));

            return new ResponseEntity<>(result.getFieldError(), HttpStatus.BAD_REQUEST);
        }
        Integer id = requestHandling.createRequest(request.getRouteNumber(), request.getDate());
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PostMapping(value = "/process", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Request> processRequest(@RequestBody Request request) throws NullPointerException {
        logger.info("Response status changed" + new Date(System.currentTimeMillis()));

        requestHandling.changeStatus(request);
        if (request == null) {
            logger.info("No responses passed" + new Date(System.currentTimeMillis()));
            throw new NullPointerException();
        }
        return new ResponseEntity<>(request, HttpStatus.OK);
    }
}
