package com.two95.timesheet.service;

import com.two95.timesheet.domain.Timesheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Timesheet.
 */
public interface TimesheetService {

    /**
     * Save a timesheet.
     * @return the persisted entity
     */
    public Timesheet save(Timesheet timesheet);

    /**
     *  get all the timesheets.
     *  @return the list of entities
     */
    public Page<Timesheet> findAll(Pageable pageable);

    /**
     *  get the "id" timesheet.
     *  @return the entity
     */
    public Timesheet findOne(Long id);

    /**
     *  delete the "id" timesheet.
     */
    public void delete(Long id);
}
