package com.two95.timesheet.web.rest;

import com.two95.timesheet.Application;
import com.two95.timesheet.domain.Timesheet;
import com.two95.timesheet.repository.TimesheetRepository;
import com.two95.timesheet.service.TimesheetService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TimesheetResource REST controller.
 *
 * @see TimesheetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TimesheetResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_WEEK_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_WEEK_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_WEEK_START_STR = dateTimeFormatter.format(DEFAULT_WEEK_START);

    private static final ZonedDateTime DEFAULT_WEEK_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_WEEK_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_WEEK_END_STR = dateTimeFormatter.format(DEFAULT_WEEK_END);
    private static final String DEFAULT_USER_NAME = "AAAAA";
    private static final String UPDATED_USER_NAME = "BBBBB";
    private static final String DEFAULT_MANAGER_NAME = "AAAAA";
    private static final String UPDATED_MANAGER_NAME = "BBBBB";

    @Inject
    private TimesheetRepository timesheetRepository;

    @Inject
    private TimesheetService timesheetService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTimesheetMockMvc;

    private Timesheet timesheet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TimesheetResource timesheetResource = new TimesheetResource();
        ReflectionTestUtils.setField(timesheetResource, "timesheetService", timesheetService);
        this.restTimesheetMockMvc = MockMvcBuilders.standaloneSetup(timesheetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        timesheet = new Timesheet();
        timesheet.setWeekStart(DEFAULT_WEEK_START);
        timesheet.setWeekEnd(DEFAULT_WEEK_END);
        timesheet.setUserName(DEFAULT_USER_NAME);
        timesheet.setManagerName(DEFAULT_MANAGER_NAME);
    }

    @Test
    @Transactional
    public void createTimesheet() throws Exception {
        int databaseSizeBeforeCreate = timesheetRepository.findAll().size();

        // Create the Timesheet

        restTimesheetMockMvc.perform(post("/api/timesheets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timesheet)))
                .andExpect(status().isCreated());

        // Validate the Timesheet in the database
        List<Timesheet> timesheets = timesheetRepository.findAll();
        assertThat(timesheets).hasSize(databaseSizeBeforeCreate + 1);
        Timesheet testTimesheet = timesheets.get(timesheets.size() - 1);
        assertThat(testTimesheet.getWeekStart()).isEqualTo(DEFAULT_WEEK_START);
        assertThat(testTimesheet.getWeekEnd()).isEqualTo(DEFAULT_WEEK_END);
        assertThat(testTimesheet.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testTimesheet.getManagerName()).isEqualTo(DEFAULT_MANAGER_NAME);
    }

    @Test
    @Transactional
    public void getAllTimesheets() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheets
        restTimesheetMockMvc.perform(get("/api/timesheets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(timesheet.getId().intValue())))
                .andExpect(jsonPath("$.[*].weekStart").value(hasItem(DEFAULT_WEEK_START_STR)))
                .andExpect(jsonPath("$.[*].weekEnd").value(hasItem(DEFAULT_WEEK_END_STR)))
                .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
                .andExpect(jsonPath("$.[*].managerName").value(hasItem(DEFAULT_MANAGER_NAME.toString())));
    }

    @Test
    @Transactional
    public void getTimesheet() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get the timesheet
        restTimesheetMockMvc.perform(get("/api/timesheets/{id}", timesheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(timesheet.getId().intValue()))
            .andExpect(jsonPath("$.weekStart").value(DEFAULT_WEEK_START_STR))
            .andExpect(jsonPath("$.weekEnd").value(DEFAULT_WEEK_END_STR))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.managerName").value(DEFAULT_MANAGER_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTimesheet() throws Exception {
        // Get the timesheet
        restTimesheetMockMvc.perform(get("/api/timesheets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimesheet() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

		int databaseSizeBeforeUpdate = timesheetRepository.findAll().size();

        // Update the timesheet
        timesheet.setWeekStart(UPDATED_WEEK_START);
        timesheet.setWeekEnd(UPDATED_WEEK_END);
        timesheet.setUserName(UPDATED_USER_NAME);
        timesheet.setManagerName(UPDATED_MANAGER_NAME);

        restTimesheetMockMvc.perform(put("/api/timesheets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timesheet)))
                .andExpect(status().isOk());

        // Validate the Timesheet in the database
        List<Timesheet> timesheets = timesheetRepository.findAll();
        assertThat(timesheets).hasSize(databaseSizeBeforeUpdate);
        Timesheet testTimesheet = timesheets.get(timesheets.size() - 1);
        assertThat(testTimesheet.getWeekStart()).isEqualTo(UPDATED_WEEK_START);
        assertThat(testTimesheet.getWeekEnd()).isEqualTo(UPDATED_WEEK_END);
        assertThat(testTimesheet.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testTimesheet.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    public void deleteTimesheet() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

		int databaseSizeBeforeDelete = timesheetRepository.findAll().size();

        // Get the timesheet
        restTimesheetMockMvc.perform(delete("/api/timesheets/{id}", timesheet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Timesheet> timesheets = timesheetRepository.findAll();
        assertThat(timesheets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
