package com.ibcon.sproject.domain.smet;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.math.BigDecimal;

public class EstimateSmrToPersistListener {

    @PrePersist
    @PreUpdate
    public void checkCalculatedCostBeforeSave(final EstimateSmr smr) {
        if (smr.getCostEmm() == null) {
            smr.setCostEmm(smr.getEmCost().subtract(smr.getZmCost() != null ?
                    smr.getZmCost() : BigDecimal.ZERO));
        } else {
            smr.setCostEmm(BigDecimal.ZERO);
        }

        if (smr.getCostFot() == null) {
            smr.setCostFot((smr.getOzCost() != null ? smr.getOzCost() : BigDecimal.ZERO)
                    .add(smr.getZmCost() != null ? smr.getZmCost() : BigDecimal.ZERO));
        }

        if (smr.getCostMtr() == null) {
            smr.setCostMtr(smr.getMtCost());
        }

        if (smr.getCostKon() == null) {
            smr.setCostKon((smr.getNrCost() != null ? smr.getNrCost() : BigDecimal.ZERO)
                    .add(smr.getSpCost() != null ? smr.getSpCost() : BigDecimal.ZERO));
        }
    }
}
