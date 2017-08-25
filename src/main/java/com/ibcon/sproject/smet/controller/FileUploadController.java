package com.ibcon.sproject.smet.controller;

import com.ibcon.sproject.converters.xmlsmet.XmlSmetToDomainConverter;
import com.ibcon.sproject.domain.smet.EstimateSmet;
import com.ibcon.sproject.services.crud.estimatesmet.EstimateSmetService;
import com.ibcon.sproject.smet.model.xml.XmlSmet;
import com.ibcon.sproject.smet.service.UploadedFile;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
public class FileUploadController {

    private EstimateSmetService estimateSmetService;

    @Autowired
    public void setEstimateSmetService(EstimateSmetService estimateSmetService) {
        this.estimateSmetService = estimateSmetService;
    }

    @RequestMapping(value = "/save_file")
    @ResponseBody
    public void saveFile(HttpServletRequest servletRequest,
                         @ModelAttribute UploadedFile uploadedFile,
                         BindingResult bindingResult, Model model) {

        try {
            MultipartFile multipartFile = uploadedFile.getMultipartFile();
            String fileName = multipartFile.getOriginalFilename();
            File file = new File("D:\\test", fileName);
            multipartFile.transferTo(file);
            if (fileName.contains(".xml")) try {
                XmlSmet xmlSmet = new XmlSmet(file, 0);

                EstimateSmet smet = XmlSmetToDomainConverter.convert(xmlSmet);

                smet.setName("testSmet");

                estimateSmetService.saveOrUpdate(smet);

                System.out.println("uploadDone");
            } catch (JDOMException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //todo upload
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String upload(Model model) {
        return "test";
    }


}
