package com.ibcon.sproject.converters.xmlsmet;

import com.ibcon.sproject.domain.smet.*;
import com.ibcon.sproject.services.crud.estimateregion.EstimateRegionService;
import com.ibcon.sproject.services.crud.user.UserServiceCrud;
import com.ibcon.sproject.smet.model.objects.ChapterNode;
import com.ibcon.sproject.smet.model.objects.SmetNode;
import com.ibcon.sproject.smet.model.objects.SmetTreeNode;
import com.ibcon.sproject.smet.model.objects.SmrNode;
import com.ibcon.sproject.smet.model.xml.HeaderNode;
import com.ibcon.sproject.smet.model.xml.XmlSmet;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class XmlSmetToDomainConverter {
    private static EstimateHeader lastHeader;
    private static EstimateChapter lastChapter;

    private static UserServiceCrud userServiceCrud;
    private static EstimateRegionService estimateRegionService;

    @Autowired
    public void setUserService(UserServiceCrud userServiceCrud) {
        this.userServiceCrud = userServiceCrud;
    }

    @Autowired
    public void setEstimateRegionService(EstimateRegionService estimateRegionService) {
        this.estimateRegionService = estimateRegionService;
    }

    private EstimateSmet smet;
    private List<EstimateChapter> chapters;
    private List<EstimateHeader> headers;
    private List<EstimateRegion> regions;
    private List<EstimateSmr> smrs;

    public static EstimateSmet convert(XmlSmet xmlSmet) {
        if (xmlSmet.getSmet() == null) {
            //TODO throw exception
        }

        SmetNode root = xmlSmet.getSmet();
        EstimateSmet smet = new EstimateSmet();

        updateEstimateSmet(root, smet);

        if (root.getChildren() != null) {
            List<SmetTreeNode> childrens = root.getChildren();
            childrens.forEach(smetTreeNode -> addChild(smet, smetTreeNode));
        }

        return smet;
    }

    private static void addChild(EstimateSmet smet, SmetTreeNode smetTreeNode) {
        if (smetTreeNode.isChapter()) {
            addChapter(smet, smetTreeNode);
        } else if (smetTreeNode.isHeader()) {
            addHeader(smet, smetTreeNode);
        } else if (smetTreeNode.isSmr()) {
            addSmr(smet, smetTreeNode);
        }
        if (smetTreeNode.getChildren() != null) {
            smetTreeNode.getChildren().forEach(child -> addChild(smet, child));
        }
    }

    private static void addChapter(EstimateSmet smet, SmetTreeNode smetTreeNode) {
        EstimateChapter chapter = new EstimateChapter();
        ChapterNode chapterNode = (ChapterNode) smetTreeNode;
        chapter.setName(chapterNode.getName());
        chapter.setNum(String.valueOf(chapterNode.getNum()));
        smet.addChapter(chapter);
        lastChapter = chapter;
    }

    private static void addHeader(EstimateSmet smet, SmetTreeNode smetTreeNode) {
        EstimateHeader header = new EstimateHeader();
        HeaderNode headerNode = (HeaderNode) smetTreeNode;
        header.setName(headerNode.getName());
//        header.setNum(headerNode.getNum());
        header.addChapter(lastChapter);
        smet.addHeader(header, Integer.valueOf(((ChapterNode) headerNode.getParent()).getSysId()));
        lastHeader = header;
    }

    private static void addSmr(EstimateSmet smet, SmetTreeNode smetTreeNode) {
        EstimateSmr smr = new EstimateSmr();
        SmrNode smrNode = (SmrNode) smetTreeNode;

        smr.setName(smrNode.getName());
        smr.setNum(smrNode.getNum());
        smr.setUnits(smrNode.getUnits());
        smr.setQuantity(BigDecimal.valueOf(smrNode.getQuantity()));
        smr.setOz(BigDecimal.valueOf(smrNode.getOz()));
        smr.setEm(BigDecimal.valueOf(smrNode.getEm()));
        smr.setZm(BigDecimal.valueOf(smrNode.getZm()));
        smr.setMt(BigDecimal.valueOf(smrNode.getMt()));
        smr.setPz(BigDecimal.valueOf(smrNode.getPz()));
        smr.setNr(BigDecimal.valueOf(smrNode.getNr()));
        smr.setSp(BigDecimal.valueOf(smrNode.getSp()));
        smr.setSum(BigDecimal.valueOf(smrNode.getSum()));
        smr.setTzr(BigDecimal.valueOf(smrNode.getTzr()));
        smr.setTzm(BigDecimal.valueOf(smrNode.getTzm()));
        smr.setOzCost(BigDecimal.valueOf(smrNode.getOzCost()));
        smr.setEmCost(BigDecimal.valueOf(smrNode.getEmCost()));
        smr.setZmCost(BigDecimal.valueOf(smrNode.getZmCost()));
        smr.setMtCost(BigDecimal.valueOf(smrNode.getMtCost()));
        smr.setPzCost(BigDecimal.valueOf(smrNode.getPzCost()));
        smr.setNrCost(BigDecimal.valueOf(smrNode.getNrCost()));
        smr.setSpCost(BigDecimal.valueOf(smrNode.getSpCost()));
        smr.setTzrCost(BigDecimal.valueOf(smrNode.getTzrCost()));
        smr.setTzmCost(BigDecimal.valueOf(smrNode.getTzmCost()));

        smet.addSmr(smr);
        if (lastHeader != null) {
            smr.addHeader(lastHeader);
        }
        if (lastChapter != null) {
            smr.addChapter(lastChapter);
        }
    }

    private static void updateEstimateSmet(SmetNode root, EstimateSmet smet) {
        smet.setName(root.getName());
        smet.setNum(root.getNum());
        if (root.getVirtual()) {
            smet.setIsVirtual(1);
        } else {
            smet.setIsVirtual(0);
        }
        smet.setComment(root.getComment());
        smet.setStatus(root.getStatus());
        smet.setDateCreate(DateTime.now());
        //TODO check
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        smet.setUserCreated(userServiceCrud.findByUserName(name));
        smet.setRegion(estimateRegionService.getById(root.getRegionId()));
        smet.setFilePath(root.getFilePath());
        smet.setOz(BigDecimal.valueOf(root.getOz()));
        smet.setEm(BigDecimal.valueOf(root.getEm()));
        smet.setZm(BigDecimal.valueOf(root.getZm()));
        smet.setMt(BigDecimal.valueOf(root.getMt()));
        smet.setPz(BigDecimal.valueOf(root.getPz()));
        smet.setNr(BigDecimal.valueOf(root.getNr()));
        smet.setSp(BigDecimal.valueOf(root.getSp()));
        smet.setSum(BigDecimal.valueOf(root.getSum()));
        smet.setTzr(BigDecimal.valueOf(root.getTzr()));
        smet.setTzm(BigDecimal.valueOf(root.getTzm()));
    }

    private static EstimateChapter convertToEstimateChapter(SmetTreeNode smetTreeNode) {
        EstimateChapter chapter = new EstimateChapter();
        ChapterNode chapterNode = (ChapterNode) smetTreeNode;
        chapter.setName(chapterNode.getName());
        chapter.setNum(String.valueOf(chapterNode.getNum()));

        return chapter;
    }

    private static EstimateHeader convertToEstimateHeader(SmetTreeNode smetTreeNode) {
        EstimateHeader header = new EstimateHeader();
        HeaderNode headerNode = (HeaderNode) smetTreeNode;
        header.setName(headerNode.getName());

        return header;
    }
}
