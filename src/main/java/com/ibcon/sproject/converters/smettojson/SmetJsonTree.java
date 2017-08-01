package com.ibcon.sproject.converters.smettojson;

import com.ibcon.sproject.domain.smet.*;
import com.ibcon.sproject.services.crud.estimatechapter.EstimateChapterService;
import com.ibcon.sproject.services.crud.estimateheader.EstimateHeaderService;
import com.ibcon.sproject.services.crud.estimateregion.EstimateRegionService;
import com.ibcon.sproject.services.crud.estimatesmet.EstimateSmetService;
import com.ibcon.sproject.services.crud.estimatesmr.EstimateSmrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ComponentScan
@Component
public class SmetJsonTree {
    private EstimateSmetService estimateSmetService;
    private EstimateRegionService estimateRegionService;
    private EstimateChapterService estimateChapterService;
    private EstimateHeaderService estimateHeaderService;
    private EstimateSmrService estimateSmrService;

    private EstimateSmet smet = new EstimateSmet();
    private EstimateRegion region = new EstimateRegion();
    private List<EstimateChapter> chapters = new ArrayList<>();
    private Map<Integer, List<EstimateHeader>> headers = new HashMap<>();
    private List<EstimateSmr> smrs = new ArrayList<>();

    @Autowired
    public void setEstimateSmetService(EstimateSmetService estimateSmetService) {
        this.estimateSmetService = estimateSmetService;
    }

    @Autowired
    public void setEstimateRegionService(EstimateRegionService estimateRegionService) {
        this.estimateRegionService = estimateRegionService;
    }

    @Autowired
    public void setEstimateChapterService(EstimateChapterService estimateChapterService) {
        this.estimateChapterService = estimateChapterService;
    }

    @Autowired
    public void setEstimateHeaderService(EstimateHeaderService estimateHeaderService) {
        this.estimateHeaderService = estimateHeaderService;
    }

    @Autowired
    public void setEstimateSmrService(EstimateSmrService estimateSmrService) {
        this.estimateSmrService = estimateSmrService;
    }

    //TODO refactor
    public List<SmetTypeJson> convertSmetToJson(Integer smetId) {
        smet = estimateSmetService.getById(smetId);
        region = estimateRegionService.getById(smet.getRegion().getId());
        chapters = estimateChapterService.findAllBySmetId(smetId);
        List<EstimateHeader> headerList = estimateHeaderService.findAllBySmetId(smetId);
        smrs = estimateSmrService.findAllBySmetId(smetId);

        headers.put(null, new ArrayList<>());
        headerList.forEach(header -> {
            if (header.getChapter() == null) {
                headers.get(null).add(header);
            } else if (headers.containsKey(header.getChapter().getId())) {
                headers.get(header.getChapter().getId()).add(header);
            } else {
                List<EstimateHeader> list = new ArrayList<>();
                list.add(header);
                headers.put(header.getChapter().getId(), list);
            }
        });

        SmetTypeJson root = new SmetTypeJson(smet);
        if (!chapters.isEmpty()) {
            chapters.forEach(chapter -> {
                SmetTypeJson chapterJson = new SmetTypeJson(chapter);
                root.addChildren(chapterJson);
                if (headers.containsKey(chapter.getId())) {
                    headers.get(chapter.getId()).forEach(header -> {
                        SmetTypeJson headerJson = new SmetTypeJson(header);
                        chapterJson.addChildren(headerJson);
                        List<EstimateSmr> headerAndChapterSmrs = estimateSmrService.
                                findAllByChapterIdAndHeaderId(chapter.getId(), header.getId());
                        headerAndChapterSmrs.forEach(smr -> headerJson.addChildren(new SmetTypeJson(smr)));
                    });
                    headers.remove(chapter.getId());
                } else {
                    List<EstimateSmr> chapterSmrs = estimateSmrService.findAllByChapterId(chapter.getId());
                    chapterSmrs.forEach(smr -> chapterJson.addChildren(new SmetTypeJson(smr)));
                }
            });
        } else {
            if (!headers.isEmpty()) {
                headers.get(null).forEach(header -> {
                    SmetTypeJson headerJson = new SmetTypeJson(header);
                    root.addChildren(headerJson);
                    List<EstimateSmr> headerSmrs = estimateSmrService.findAllByHeaderId(header.getId());
                    headerSmrs.forEach(smr -> headerJson.addChildren(new SmetTypeJson(smr)));
                });
            }
        }

        List<SmetTypeJson> result = new ArrayList<>();
        result.add(root);
        return result;
    }
}
