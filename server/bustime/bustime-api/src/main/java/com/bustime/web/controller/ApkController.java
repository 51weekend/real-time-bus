package com.bustime.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bustime.common.logger.LoggerUtils;
import com.bustime.common.utils.FileContentTypeUtil;

/**
 * 下载文件
 * @author chengdong
 *
 */
@Controller
@Scope("prototype")
public class ApkController {

    @Value("${config.apkUploadUrl}")
    private String apkUploadUrl;

    @RequestMapping
    public String down(@RequestParam(value = "fileName", required = false) String fileName,
            HttpServletResponse response, Model model) {
        if ((StringUtils.isBlank(fileName))) {
            return null;
        }

        FileInputStream fis = null;
        String filePathAndName = null;
        if (apkUploadUrl != null) {
            filePathAndName = apkUploadUrl + "/" + fileName;
            try {
                File file = new File(filePathAndName);
                if (file.isFile()) {
                    fis = new FileInputStream(file);
                }
            } catch (Exception e) {
                LoggerUtils.error("读取文件: " + filePathAndName + " 出错: ", e);
            }
        } else {
            LoggerUtils.error("无法读取配置[upload.file.savepath]信息");
        }

        if (fis == null) {
            model.addAttribute("errMsg", "该文件不存在!");
            return "error";
        }

        ServletOutputStream out = null;
        try {
            String contentType = FileContentTypeUtil.getFileContentType(filePathAndName);

            // 二进制输出流
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            // 得到输出流
            out = response.getOutputStream();
            byte[] b = new byte[1024];
            int i = -1;
            while ((i = fis.read(b)) != -1) {
                out.write(b, 0, i);
            }

            // 强制刷新输出流
            out.flush();
        } catch (Exception e) {
            LoggerUtils.error("输出数据量出错: ", e);
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException e) {

            }
        }
        return null;
    }

}