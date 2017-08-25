package com.ibcon.sproject.domain.smet;

import com.ibcon.sproject.domain.AbstractDomainClass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;

@EntityScan
@Entity
@Table(name = "estimate_Header")
public class EstimateHeader extends AbstractDomainClass {
    private String name;
    private String num;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "smet_id")
    private EstimateSmet smet;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private EstimateChapter chapter;

    public void addChapter(EstimateChapter lastChapter) {
        if (lastChapter != null) {
            this.chapter = lastChapter;
            lastChapter.getHeaderSet().add(this);
        }
    }

    public Integer getId() {
        return super.getId();
    }

    public void setId(Integer id) {
        super.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public EstimateSmet getSmet() {
        return smet;
    }

    public void setSmet(EstimateSmet smet) {
        this.smet = smet;
    }

    public EstimateChapter getChapter() {
        return chapter;
    }

    public void setChapter(EstimateChapter chapter) {
        this.chapter = chapter;
    }
}
