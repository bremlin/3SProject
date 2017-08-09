package com.ibcon.sproject.smet.controller;

import com.ibcon.sproject.smet.model.xml.XmlSmet;
import com.ibcon.sproject.smet.service.UploadedFile;
import org.jdom2.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
public class FileUploadController {

    @RequestMapping(value = "/save_file")
    public void saveFile(HttpServletRequest servletRequest,
                         @ModelAttribute UploadedFile uploadedFile,
                         BindingResult bindingResult, Model model) {

        try {
            MultipartFile multipartFile = uploadedFile.getMultipartFile();
            String fileName = multipartFile.getOriginalFilename();
            File file = new File("D:\\test", fileName);
            multipartFile.transferTo(file);
            if (fileName.contains(".xml")) try {
                new XmlSmet(file, 0);
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
