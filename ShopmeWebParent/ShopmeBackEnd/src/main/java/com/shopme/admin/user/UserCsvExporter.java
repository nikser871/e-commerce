package com.shopme.admin.user;

import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserCsvExporter {

    public void export(List<User> users, HttpServletResponse response) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timesStamp = dateFormat.format(new Date());
        String filename = "users" + timesStamp + ".csv";

        response.setContentType("text/csv");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + filename;
        response.setHeader(headerKey, headerValue);

        try {
            ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

            String[] csvHeader = {"User Id", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};
            String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};

            csvWriter.writeHeader(csvHeader);

            for (User user : users) {
                csvWriter.write(user, fieldMapping);
            }

            csvWriter.close();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }






    }
}
