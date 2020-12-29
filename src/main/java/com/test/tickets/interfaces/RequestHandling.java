package com.test.tickets.interfaces;

import com.test.tickets.domain.Request;

public interface RequestHandling {
    Integer createRequest(String routeNumber, String date);

    Request changeStatus(Request request);

    void processStatus();

}
