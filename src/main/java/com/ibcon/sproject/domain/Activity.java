package com.ibcon.sproject.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;

@EntityScan
@Entity
//@Proxy(lazy = false)    //без этого не получается достать projectId из wbs перед сохранением
//@EntityListeners(ActivityListener.class)
@Table(name = "activities")
@EqualsAndHashCode(exclude = "wbs")
public @Data class Activity extends AbstractDomainClass {
    private Integer projectObjectId;
    private Integer projectId;
//    private Integer wbsId;
    private String activityId;
    private String activityName;
    private Double duration;
    private Double remainingDuration;
    private DateTime actualStartDate;
    private DateTime earlyStartDate;
    private DateTime lateStartDate;
    private DateTime plannedStartDate;
    private DateTime actualFinishDate;
    private DateTime earlyFinishDate;
    private DateTime lateFinishDate;
    private DateTime plannedFinishDate;
    private Integer typeId;
    private Integer statusId;
    private Integer percentageCompletion;
    private Boolean isCritical;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "wbs_id")
//    @JoinTable(name = "wbs", joinColumns = @JoinColumn(name = "id"),
//        inverseJoinColumns = @JoinColumn(name = "wbs_id"))
    @JsonBackReference
    private WBS wbs;


    public void setPercentageCompletion(Integer percentageCompletion) {
        if (percentageCompletion < 0 && percentageCompletion > 100) {
            throw new IllegalArgumentException();
        } else {
            this.percentageCompletion = percentageCompletion;
        }
    }
}
