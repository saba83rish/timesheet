package com.two95.timesheet.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Timesheet.
 */
@Entity
@Table(name = "timesheet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Timesheet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "week_start")
    private ZonedDateTime weekStart;
    
    @Column(name = "week_end")
    private ZonedDateTime weekEnd;
    
    @Column(name = "user_name")
    private String userName;
    
    @Column(name = "manager_name")
    private String managerName;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
    @JoinColumn(name = "timesheet_id")
    private List<Timesheettask> tasks;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getWeekStart() {
        return weekStart;
    }
    
    public void setWeekStart(ZonedDateTime weekStart) {
        this.weekStart = weekStart;
    }

    public ZonedDateTime getWeekEnd() {
        return weekEnd;
    }
    
    public void setWeekEnd(ZonedDateTime weekEnd) {
        this.weekEnd = weekEnd;
    }

    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getManagerName() {
        return managerName;
    }
    
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    
    public List<Timesheettask> getTasks() {
		return tasks;
	}

	public void setTasks(List<Timesheettask> tasks) {
		this.tasks = tasks;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Timesheet timesheet = (Timesheet) o;
        if(timesheet.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, timesheet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Timesheet{" +
            "id=" + id +
            ", weekStart='" + weekStart + "'" +
            ", weekEnd='" + weekEnd + "'" +
            ", userName='" + userName + "'" +
            ", managerName='" + managerName + "'" +
            '}';
    }
}
