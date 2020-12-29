package com.test.tickets.repos;

import com.test.tickets.domain.Request;
import com.test.tickets.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepo extends JpaRepository<Request, Long> {
    Request findById(Integer id);

    List<Request> findByStatus(Status status);

    Request findFirstByStatus (Status status);
}
