package com.cm.Excel.utils;

import com.cm.annotations.Excel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.hutool.poi.excel.cell.CellUtil.setCellValue;

/**
 * @author 31373
 */
public class ExcelUtil {

    public static <T> byte[] exportExcel(List<T> list, Class<T> clazz) throws  Exception {
        //创建工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        //获取实体类中带@Excel注解的字段
        List<ExcelField> excelFields = getExcelFields(clazz);
        //创建表头
        Row headerRow = sheet.createRow(0);
        //创建表头样式
        CellStyle headerStyle = createHeaderStyle(workbook);
        for (int i = 0; i < excelFields.size(); i++) {
            ExcelField excelField = excelFields.get(i);
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(excelField.getName());
            cell.setCellStyle(headerStyle);
        }

        // 填充数据
        CellStyle dataStyle = createDataStyle(workbook);
        for (int i = 0; i < list.size(); i++) {
            T item = list.get(i);
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < excelFields.size(); j++) {
                ExcelField excelField = excelFields.get(j);
                Cell cell = row.createCell(j);

                // 通过反射获取字段值
                Field field = clazz.getDeclaredField(excelField.getFieldName());
                field.setAccessible(true);
                Object value = field.get(item);

                // 设置单元格值
                setCellValue(cell, value, dataStyle);
            }
        }
        // 自动调整列宽并设置默认宽度
        for (int i = 0; i < excelFields.size(); i++) {
            //自动调整列宽
            sheet.autoSizeColumn(i);
            //获取调整后的列宽
            int columnWidth = sheet.getColumnWidth(i);
            // 设置一个合理地默认宽度（例如：20个字符的宽度）
            int defaultWidth = 20 * 256;
            // 取自动调整宽度和默认宽度中的较大值作为最终列宽
            sheet.setColumnWidth(i, Math.max(columnWidth, defaultWidth));
        }

        // 将工作簿写入字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
        } finally {
            workbook.close();
            outputStream.close();
        }

        return outputStream.toByteArray();
    }

    private static void setCellValue(Cell cell, Object value, CellStyle dataStyle) {
        if (value instanceof LocalDateTime) {
            // 设置日期时间格式
            dataStyle.setDataFormat(cell.getSheet().getWorkbook().createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
            cell.setCellValue(((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else if (value instanceof LocalDate) {
            // 设置日期格式
            dataStyle.setDataFormat(cell.getSheet().getWorkbook().createDataFormat().getFormat("yyyy-MM-dd"));
            cell.setCellValue(((LocalDate) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else {
            // 处理其他类型的数据
            cell.setCellValue(String.valueOf(value));
        }
        cell.setCellStyle(dataStyle);
    }

    /**
     * 创建数据样式
     * @param workbook 工作簿
     * @return CellStyle
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }

    /**
     * 获取实体类中带@Excel注解的字段
     *
     * @param clazz 实体类
     * @param <T>   实体类
     * @return List<ExcelField>
     */
    private static <T> List<ExcelField> getExcelFields(Class<T> clazz) {
        List<ExcelField> excelFields = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Excel excelAnnotation = field.getAnnotation(Excel.class);
            ExcelField excelField = new ExcelField();
            excelField.setFieldName(field.getName());
            if (excelAnnotation != null) {
                excelField.setName(excelAnnotation.name().isEmpty() ? field.getName() : excelAnnotation.name());
            }
            if (excelAnnotation != null) {
                excelField.setSort(excelAnnotation.sort());
            }
            excelFields.add(excelField);
        }
        // 按sort字段排序
        Collections.sort(excelFields);
        return excelFields;
    }


    /**
     * 创建表头样式
     *
     * @param workbook 工作簿
     * @return 表头样式
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }


    /**
     * Excel字段信息类
     */
    private static class ExcelField implements Comparable<ExcelField> {

        /**
         * 字段名
         */
        private String fieldName;

        /**
         * 列名
         */
        private String name;

        /**
         * 排序
         */
        private int sort;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        @Override
        public int compareTo(ExcelField other) {
            return Integer.compare(this.sort, other.sort);
        }
    }
}
