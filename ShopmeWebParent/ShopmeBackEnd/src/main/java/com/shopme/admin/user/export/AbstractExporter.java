package com.shopme.admin.user.export;


import jakarta.servlet.http.HttpServletResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AbstractExporter {

    public void setResponseHeader(HttpServletResponse response, String contentType,
                                  String extension) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timesStamp = dateFormat.format(new Date());
        String filename = "users_" + timesStamp + extension;

        response.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + filename;
        response.setHeader(headerKey, headerValue);


    }
}
