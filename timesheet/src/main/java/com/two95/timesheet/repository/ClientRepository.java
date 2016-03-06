package com.two95.timesheet.repository;

import com.two95.timesheet.domain.Client;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Client entity.
 */
public interface ClientRepository extends JpaRepository<Client,Long> {

    @Query("select client from Client client where client.user.login = ?#{principal.username}")
    List<Client> findByUserIsCurrentUser();

}
