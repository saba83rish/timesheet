package com.two95.timesheet.service.impl;

import com.two95.timesheet.service.TimesheetService;
import com.two95.timesheet.domain.Timesheet;
import com.two95.timesheet.repository.TimesheetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Timesheet.
 */
@Service
@Transactional
public class TimesheetServiceImpl implements TimesheetService{

    private final Logger log = LoggerFactory.getLogger(TimesheetServiceImpl.class);
    
    @Inject
    private TimesheetRepository timesheetRepository;
    
    /**
     * Save a timesheet.
     * @return the persisted entity
     */
    public Timesheet save(Timesheet timesheet) {
        log.debug("Request to save Timesheet : {}", timesheet);
        Timesheet result = timesheetRepository.save(timesheet);
        return result;
    }

    /**
     *  get all the timesheets.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Timesheet> findAll(Pageable pageable) {
        log.debug("Request to get all Timesheets");
        Page<Timesheet> result = timesheetRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one timesheet by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Timesheet findOne(Long id) {
        log.debug("Request to get Timesheet : {}", id);
        Timesheet timesheet = timesheetRepository.findOne(id);
        return timesheet;
    }

    /**
     *  delete the  timesheet by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Timesheet : {}", id);
        timesheetRepository.delete(id);
    }
}
