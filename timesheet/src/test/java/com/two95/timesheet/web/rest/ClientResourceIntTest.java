package com.two95.timesheet.web.rest;

import com.two95.timesheet.Application;
import com.two95.timesheet.domain.Client;
import com.two95.timesheet.repository.ClientRepository;
import com.two95.timesheet.service.ClientService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ClientResource REST controller.
 *
 * @see ClientResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ClientResourceIntTest {

    private static final String DEFAULT_CLIENT_NAME = "AAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBB";
    private static final String DEFAULT_CLIENT_LOCATION = "AAAAA";
    private static final String UPDATED_CLIENT_LOCATION = "BBBBB";
    private static final String DEFAULT_CLIENT_MANAGER_NAME = "AAAAA";
    private static final String UPDATED_CLIENT_MANAGER_NAME = "BBBBB";

    private static final LocalDate DEFAULT_PROJECT_JOINING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROJECT_JOINING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PROJECT_ENDING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROJECT_ENDING_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ClientRepository clientRepository;

    @Inject
    private ClientService clientService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClientMockMvc;

    private Client client;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientResource clientResource = new ClientResource();
        ReflectionTestUtils.setField(clientResource, "clientService", clientService);
        this.restClientMockMvc = MockMvcBuilders.standaloneSetup(clientResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        client = new Client();
        client.setClientName(DEFAULT_CLIENT_NAME);
        client.setClientLocation(DEFAULT_CLIENT_LOCATION);
        client.setClientManagerName(DEFAULT_CLIENT_MANAGER_NAME);
        client.setProjectJoiningDate(DEFAULT_PROJECT_JOINING_DATE);
        client.setProjectEndingDate(DEFAULT_PROJECT_ENDING_DATE);
    }

    @Test
    @Transactional
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client

        restClientMockMvc.perform(post("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(client)))
                .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clients.get(clients.size() - 1);
        assertThat(testClient.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
        assertThat(testClient.getClientLocation()).isEqualTo(DEFAULT_CLIENT_LOCATION);
        assertThat(testClient.getClientManagerName()).isEqualTo(DEFAULT_CLIENT_MANAGER_NAME);
        assertThat(testClient.getProjectJoiningDate()).isEqualTo(DEFAULT_PROJECT_JOINING_DATE);
        assertThat(testClient.getProjectEndingDate()).isEqualTo(DEFAULT_PROJECT_ENDING_DATE);
    }

    @Test
    @Transactional
    public void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clients
        restClientMockMvc.perform(get("/api/clients?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
                .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME.toString())))
                .andExpect(jsonPath("$.[*].clientLocation").value(hasItem(DEFAULT_CLIENT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].clientManagerName").value(hasItem(DEFAULT_CLIENT_MANAGER_NAME.toString())))
                .andExpect(jsonPath("$.[*].projectJoiningDate").value(hasItem(DEFAULT_PROJECT_JOINING_DATE.toString())))
                .andExpect(jsonPath("$.[*].projectEndingDate").value(hasItem(DEFAULT_PROJECT_ENDING_DATE.toString())));
    }

    @Test
    @Transactional
    public void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME.toString()))
            .andExpect(jsonPath("$.clientLocation").value(DEFAULT_CLIENT_LOCATION.toString()))
            .andExpect(jsonPath("$.clientManagerName").value(DEFAULT_CLIENT_MANAGER_NAME.toString()))
            .andExpect(jsonPath("$.projectJoiningDate").value(DEFAULT_PROJECT_JOINING_DATE.toString()))
            .andExpect(jsonPath("$.projectEndingDate").value(DEFAULT_PROJECT_ENDING_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

		int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        client.setClientName(UPDATED_CLIENT_NAME);
        client.setClientLocation(UPDATED_CLIENT_LOCATION);
        client.setClientManagerName(UPDATED_CLIENT_MANAGER_NAME);
        client.setProjectJoiningDate(UPDATED_PROJECT_JOINING_DATE);
        client.setProjectEndingDate(UPDATED_PROJECT_ENDING_DATE);

        restClientMockMvc.perform(put("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(client)))
                .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clients.get(clients.size() - 1);
        assertThat(testClient.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
        assertThat(testClient.getClientLocation()).isEqualTo(UPDATED_CLIENT_LOCATION);
        assertThat(testClient.getClientManagerName()).isEqualTo(UPDATED_CLIENT_MANAGER_NAME);
        assertThat(testClient.getProjectJoiningDate()).isEqualTo(UPDATED_PROJECT_JOINING_DATE);
        assertThat(testClient.getProjectEndingDate()).isEqualTo(UPDATED_PROJECT_ENDING_DATE);
    }

    @Test
    @Transactional
    public void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

		int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Get the client
        restClientMockMvc.perform(delete("/api/clients/{id}", client.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeDelete - 1);
    }
}
