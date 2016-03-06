package com.two95.timesheet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.ZonedDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Timesheettask.
 */
@Entity
@Table(name = "timesheettask")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Timesheettask implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "timeshet_date")
    private ZonedDateTime timeshetDate;
    
    @Column(name = "task")
    private String task;
    
    @Column(name = "time_spent")
    private Float timeSpent;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "timesheet_id")
    private Timesheet timesheet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimeshetDate() {
        return timeshetDate;
    }
    
    public void setTimeshetDate(ZonedDateTime timeshetDate) {
        this.timeshetDate = timeshetDate;
    }

    public String getTask() {
        return task;
    }
    
    public void setTask(String task) {
        this.task = task;
    }

    public Float getTimeSpent() {
        return timeSpent;
    }
    
    public void setTimeSpent(Float timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Timesheet getTimesheet() {
        return timesheet;
    }

    public void setTimesheet(Timesheet timesheet) {
        this.timesheet = timesheet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Timesheettask timesheettask = (Timesheettask) o;
        if(timesheettask.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, timesheettask.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Timesheettask{" +
            "id=" + id +
            ", timeshetDate='" + timeshetDate + "'" +
            ", task='" + task + "'" +
            ", timeSpent='" + timeSpent + "'" +
            '}';
    }
}
