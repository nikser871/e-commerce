package com.shopme.admin.user.export;

import com.shopme.common.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;


import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExcelExporter() {
        this.workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        this.sheet = workbook.createSheet();
        XSSFRow row = sheet.createRow(0);


        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "User Id", cellStyle);
        createCell(row, 1, "E-maik", cellStyle);
        createCell(row, 2, "First Name", cellStyle);
        createCell(row, 3, "Last Name", cellStyle);
        createCell(row, 4, "Roles", cellStyle);
        createCell(row, 5, "Enabled", cellStyle);

    }

    private void createCell (XSSFRow row, int col, Object value, CellStyle style) {


        XSSFCell cell = row.createCell(col);
        sheet.autoSizeColumn(col);


        if (value instanceof Long)
            cell.setCellValue((Long) value);
        else if (value instanceof Boolean)
            cell.setCellValue((Boolean) value);
        else
            cell.setCellValue((String) value);

        cell.setCellStyle(style);


    }



    public void export(List<User> users, HttpServletResponse response) {
        setResponseHeader(response, "application/octet-stream", ".xlsx");


        writeHeaderLine();
        writeDataLines(users);

        try {
            ServletOutputStream outputStream = response.getOutputStream();

            workbook.write(outputStream);


            workbook.close();
            outputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeDataLines(List<User> users) {
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);



        for (User user : users) {
            XSSFRow row = sheet.createRow(rowIndex++);
            int colIndex = 0;
            createCell(row, colIndex++, user.getId(), cellStyle);
            createCell(row, colIndex++, user.getEmail(), cellStyle);
            createCell(row, colIndex++, user.getFirstName(), cellStyle);
            createCell(row, colIndex++, user.getLastName(), cellStyle);
            createCell(row, colIndex++, user.getRoles().toString(), cellStyle);
            createCell(row, colIndex, user.isEnabled(), cellStyle);
        }
    }
}
