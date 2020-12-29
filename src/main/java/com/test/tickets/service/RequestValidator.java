package com.test.tickets.service;

import com.test.tickets.domain.Request;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class RequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Request.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Request request = (Request) target;
        String regex = "\\D+";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");

        if(request.getRouteNumber().isEmpty()) {
            errors.rejectValue("routeNumber","Route number is empty");
        }
        if(request.getRouteNumber().length() > 6) {
            errors.rejectValue("routeNumber","Route number is too long");
        }
        if(request.getRouteNumber().matches(regex)){
            errors.rejectValue("routeNumber","Wrong format");
        }
        if(request.getDate().isEmpty()) {
            errors.rejectValue("date","Date is empty");
        }

        try {
            Date date = dateFormat.parse(request.getDate());
        }catch (Exception e) {
            errors.rejectValue("date", "wrong format");
        }
    }
}
