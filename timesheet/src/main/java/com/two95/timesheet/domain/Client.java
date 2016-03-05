package com.two95.timesheet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "client_name")
    private String clientName;
    
    @Column(name = "client_location")
    private String clientLocation;
    
    @Column(name = "client_manager_name")
    private String clientManagerName;
    
    @Column(name = "project_joining_date")
    private LocalDate projectJoiningDate;
    
    @Column(name = "project_ending_date")
    private LocalDate projectEndingDate;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientLocation() {
        return clientLocation;
    }
    
    public void setClientLocation(String clientLocation) {
        this.clientLocation = clientLocation;
    }

    public String getClientManagerName() {
        return clientManagerName;
    }
    
    public void setClientManagerName(String clientManagerName) {
        this.clientManagerName = clientManagerName;
    }

    public LocalDate getProjectJoiningDate() {
        return projectJoiningDate;
    }
    
    public void setProjectJoiningDate(LocalDate projectJoiningDate) {
        this.projectJoiningDate = projectJoiningDate;
    }

    public LocalDate getProjectEndingDate() {
        return projectEndingDate;
    }
    
    public void setProjectEndingDate(LocalDate projectEndingDate) {
        this.projectEndingDate = projectEndingDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        if(client.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + id +
            ", clientName='" + clientName + "'" +
            ", clientLocation='" + clientLocation + "'" +
            ", clientManagerName='" + clientManagerName + "'" +
            ", projectJoiningDate='" + projectJoiningDate + "'" +
            ", projectEndingDate='" + projectEndingDate + "'" +
            '}';
    }
}
