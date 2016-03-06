package com.two95.timesheet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.two95.timesheet.domain.Timesheet;
import com.two95.timesheet.service.TimesheetService;
import com.two95.timesheet.web.rest.util.HeaderUtil;
import com.two95.timesheet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Timesheet.
 */
@RestController
@RequestMapping("/api")
public class TimesheetResource {

    private final Logger log = LoggerFactory.getLogger(TimesheetResource.class);
        
    @Inject
    private TimesheetService timesheetService;
    
    /**
     * POST  /timesheets -> Create a new timesheet.
     */
    @RequestMapping(value = "/timesheets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Timesheet> createTimesheet(@RequestBody Timesheet timesheet) throws URISyntaxException {
        log.debug("REST request to save Timesheet : {}", timesheet);
        if (timesheet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("timesheet", "idexists", "A new timesheet cannot already have an ID")).body(null);
        }
        Timesheet result = timesheetService.save(timesheet);
        return ResponseEntity.created(new URI("/api/timesheets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("timesheet", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /timesheets -> Updates an existing timesheet.
     */
    @RequestMapping(value = "/timesheets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Timesheet> updateTimesheet(@RequestBody Timesheet timesheet) throws URISyntaxException {
        log.debug("REST request to update Timesheet : {}", timesheet);
        if (timesheet.getId() == null) {
            return createTimesheet(timesheet);
        }
        Timesheet result = timesheetService.save(timesheet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("timesheet", timesheet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /timesheets -> get all the timesheets.
     */
    @RequestMapping(value = "/timesheets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Timesheet>> getAllTimesheets(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Timesheets");
        Page<Timesheet> page = timesheetService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/timesheets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /timesheets/:id -> get the "id" timesheet.
     */
    @RequestMapping(value = "/timesheets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Timesheet> getTimesheet(@PathVariable Long id) {
        log.debug("REST request to get Timesheet : {}", id);
        Timesheet timesheet = timesheetService.findOne(id);
        return Optional.ofNullable(timesheet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /timesheets/:id -> delete the "id" timesheet.
     */
    @RequestMapping(value = "/timesheets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTimesheet(@PathVariable Long id) {
        log.debug("REST request to delete Timesheet : {}", id);
        timesheetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("timesheet", id.toString())).build();
    }
}
