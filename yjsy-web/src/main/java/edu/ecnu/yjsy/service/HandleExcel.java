package edu.ecnu.yjsy.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpSession;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 处理电子表格文件（即Excel）的方法。
 *
 * @author xulinhao
 */

public abstract class HandleExcel extends HandleSearch {
    private static final String CSV_EXT = "csv";

    protected static final String PROGRESS_FIELD = "progress";

    @Autowired
    protected SessionStateStore stateStore;

    public static XSSFWorkbook csvToXLSX(InputStream is) throws IOException {
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("sheet1");
        try {
            String currentLine = null;
            int RowNum = 0;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((currentLine = br.readLine()) != null) {
                String str[] = currentLine.split(",", -1);
                XSSFRow currentRow = sheet.createRow(RowNum);
                RowNum++;
                for (int i = 0; i < str.length; i++) {
                    currentRow.createCell(i).setCellValue(str[i]);
                }
            }
            return workBook;
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    // FIXME: 为了兼容已有代码，暂时将csv数据转换为xlsx数据
    protected Workbook createWorkbook(InputStream is, String type)
            throws IOException, InvalidFormatException {
        if (type.equals(CSV_EXT)) {
            return csvToXLSX(is);
        } else {
            return createWorkbook(is);
        }
    }

    protected Workbook createWorkbook(InputStream is)
            throws IOException, InvalidFormatException {
        if (!is.markSupported()) {// 判断该输入流是否支持
            is = new PushbackInputStream(is, 8);
        }

        // xls file
        if (POIFSFileSystem.hasPOIFSHeader(is)) {
            return new HSSFWorkbook(is);

            // xlsx file
        } else if (POIXMLDocument.hasOOXMLHeader(is)) {
            return new XSSFWorkbook(OPCPackage.open(is));

            // neither xls file nor xlsx file
        } else {
            throw new IllegalArgumentException(
                    "Fail to parse the input stream!");
        }
    }

    protected void close(InputStream is) throws IOException {
        if (is != null) is.close();
    }

    /**
     * 检查导入文件的合法性，即检查文件的第一行标题。
     *
     * @param titles
     *            导入文件中的列
     * @param fileFields
     *            必须有的列
     * @return
     */
    protected boolean checkFormat(Row titles, String[] fileFields) {
        // 检查列的数量
        if (titles.getLastCellNum() < fileFields.length) { return false; }

        // 记录需要检查哪些列
        HashMap<String, Boolean> map = new HashMap<>();
        for (String fileField : fileFields) {
            map.put(fileField, false);
        }

        // 检查上传文件中是否存在必须的列
        for (int i = 0; i < titles.getLastCellNum(); i++) {
            if (map.get(titles.getCell(i).toString()) != null) {
                map.put(titles.getCell(i).toString(), true);
            }
        }

        if (map.values().contains(false)) {
            return false;
        } else {
            return true;
        }
    }

    protected Map<String, Integer> parseSchema(Row titles) {
        Map<String, Integer> schema = new HashMap<>();
        for (int i = titles.getFirstCellNum(); i < titles
                .getLastCellNum(); i++) {
            schema.put(titles.getCell(i).getStringCellValue(), i);
        }
        return schema;
    }

    protected List<String> parseRow(Row row, String[] fileFields,
            Map<String, Integer> schema) {
        List<String> result = new ArrayList<>();
        for (String fileField : fileFields) {
            if (schema.get(fileField) != null) {
                Cell cell = row.getCell(schema.get(fileField));
                if (cell != null) {
                    // XXX
                    // 电子表格数据有时候居然是null
                    String value = cell.getStringCellValue().trim();
                    if (value.equals("null")) {
                        result.add(null);
                    } else {
                        result.add(value);
                    }
                } else {
                    result.add(null);
                }
            }
        }
        return result;
    }

    protected boolean parseBoolean(String value) {
        if (value != null) { return value.equals("是") ? true : false; }
        return false;
    }

    protected String getCellValue(Row row, int idx) {
        if (row.getCell(idx) != null
                && !row.getCell(idx).toString().equals("")) {
            return row.getCell(idx).toString();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected void handleErrorData(Row row, String[] titles, String error,
            String errorReason) {
        List<Object> errorRecords = (List<Object>) stateStore
                .getState(error + "record"); // 存放导入时的错误信息
        if (errorRecords == null) errorRecords = new ArrayList<>();
        List<Object> r = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            r.add(getCellValue(row, i));
        }
        r.add(errorReason);
        errorRecords.add(r);
        if (errorRecords.size() != 0) {
            stateStore.setState(error + "record", errorRecords);
            stateStore.setState(error + "title", titles);
        } else {
            stateStore.removeState(error);
        }
    }

    @SuppressWarnings("unchecked")
    public byte[] exportError(String error) throws IOException {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest().getSession();
        List<Object> rows = (List<Object>) session
                .getAttribute(error + "record");
        String[] titles = (String[]) session.getAttribute(error + "title");

        List<String> errorTitles = new ArrayList<String>();
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("sheet1");
        XSSFRow header = sheet.createRow(0);

        for (int i = 0; i < titles.length; i++)
            errorTitles.add(titles[i]);
        errorTitles.add("错误原因");

        int j = 0;
        for (String errorTitle : errorTitles)
            header.createCell(j++).setCellValue(errorTitle);

        int rowNum = 1;
        for (Object r : rows) {
            XSSFRow row = sheet.createRow(rowNum++);
            List<Object> records = (List<Object>) r;
            for (int i = 0; i < errorTitles.size(); i++) {
                if (records.get(i) != null)
                    row.createCell(i).setCellValue(records.get(i).toString());
            }
        }

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            book.write(output);
            output.flush();
            session.removeAttribute(error + "record");
            session.removeAttribute(error + "title");
            return output.toByteArray();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected FlatFileItemReader createReader(byte[] data,
            DefaultLineMapper lineMapper) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(data));
        String line = scanner.nextLine();
        scanner.close();

        FlatFileItemReader itemReader = new FlatFileItemReader();
        itemReader.setResource(
                new InputStreamResource(new ByteArrayInputStream(data)));

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(line.split("\\s*,\\s*"));
        tokenizer.setStrict(false);

        lineMapper.setLineTokenizer(tokenizer);
        itemReader.setLineMapper(lineMapper);
        itemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
        itemReader.setLinesToSkip(1);
        itemReader.open(new ExecutionContext());

        return itemReader;
    }

}
