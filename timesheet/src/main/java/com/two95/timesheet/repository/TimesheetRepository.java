package com.two95.timesheet.repository;

import com.two95.timesheet.domain.Timesheet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Timesheet entity.
 */
public interface TimesheetRepository extends JpaRepository<Timesheet,Long> {

}
