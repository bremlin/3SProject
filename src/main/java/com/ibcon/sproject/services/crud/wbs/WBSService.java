package com.ibcon.sproject.services.crud.wbs;

import com.ibcon.sproject.domain.WBS;
import com.ibcon.sproject.domain.smet.EstimateSmet;
import com.ibcon.sproject.services.crud.CrudService;

import java.util.List;

public interface WBSService extends CrudService<WBS> {
    List<WBS> findByProjectId(Integer id);
//    List<EstimateSmet> findEstimateSmetsByWbsId(Integer id);
}
