package yutong.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.SwingWorker;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static yutong.tools.DBManager.getData;
import static yutong.tools.DBManager.valueOf;
import static yutong.tools.otherFunctions.print;

public class Excel {

    public static void writeToExcel(File filePath, Connection connection) {
        new SwingWorker() {
            @Override
            protected Integer doInBackground() throws Exception {

                ArrayList list = new ArrayList();
                list.add("ishdanolingan");
                list.add("prikazichiqmagan");
                list.add("bazaishchisi");
                list.add("haydovchi");
                list.add("haydovchiKZ");
                list.add("boshqaruvchi");
                list.add("dispetcher");
                list.add("CARS");

                ArrayList list2 = new ArrayList();
                list2.add("Ишдан олинганлар");
                list2.add("Прикази чиқмаганлар");
                list2.add("База ишчилари");
                list2.add("Ҳайдовчилар - UZ");
                list2.add("Ҳайдовчилар - KZ");
                list2.add("Бошқарувчи ҳодимлар");
                list2.add("Диспетчерлар");
                list2.add("Автобуслар");

                // workbook object
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet spreadsheet;

                for (int i = 0; i < 7; i++) {
                    ArrayList data;
                    String sheetName = list2.get(i).toString();

                    // spreadsheet object
                    spreadsheet = workbook.createSheet(sheetName);

                    // writing the header rows to spreadSheet
                    XSSFRow headerRow;
                    headerRow = spreadsheet.createRow(0);

                    if (i != 6) {
                        data = getData("SELECT * FROM EMPLOYEES WHERE DEPARTMENT='" + list.get(i) + "'", connection);

                        headerRow.createCell(0).setCellValue("Ф.И.О");
                        headerRow.createCell(1).setCellValue("Ҳайдовчилик гувохномаси серияси ва рақами");
                        headerRow.createCell(2).setCellValue("Тоифаси");
                        headerRow.createCell(3).setCellValue("ИНН рақами");
                        headerRow.createCell(4).setCellValue("Меҳнат шартномаси муддати");
                        headerRow.createCell(5).setCellValue("Тиббий кўрик муддати");
                        headerRow.createCell(6).setCellValue("Шахсий телефон рақами - UZ");
                        headerRow.createCell(7).setCellValue("Шахсий телефон рақами - KZ");
                        headerRow.createCell(8).setCellValue("Оилавий телефон рақами");
                        headerRow.createCell(9).setCellValue("Манзили");

                        // writing rows:
                        // creating a row object
                        XSSFRow row;

                        for (int x = 0; x < data.size(); x++) {
                            Map map = (Map) data.get(x);

                            // writing the data into the sheets...
                            row = spreadsheet.createRow(x + 1);
                            row.createCell(0).setCellValue(valueOf(map, "ISM"));
                            row.createCell(1).setCellValue(valueOf(map, "HGNUMBER"));
                            row.createCell(2).setCellValue(valueOf(map, "HGTOIFA"));
                            row.createCell(3).setCellValue(valueOf(map, "INNNUMBER"));
                            row.createCell(4).setCellValue(valueOf(map, "MEHNATSHEXP"));
                            row.createCell(5).setCellValue(valueOf(map, "MEDEXP"));
                            row.createCell(6).setCellValue(valueOf(map, "PPHONE"));
                            row.createCell(7).setCellValue(valueOf(map, "PPHONEKZ"));
                            row.createCell(8).setCellValue(valueOf(map, "FPHONE"));
                            row.createCell(9).setCellValue(valueOf(map, "MANZIL"));
                        }

                    } else {
                        data = getData("SELECT * FROM CARS", connection);

                        headerRow.createCell(0).setCellValue("Давлат рақами");
                        headerRow.createCell(1).setCellValue("Русуми");
                        headerRow.createCell(2).setCellValue("Фирма номи");
                        headerRow.createCell(3).setCellValue("1-масул шахс");
                        headerRow.createCell(4).setCellValue("2-масул шахс");
                        headerRow.createCell(5).setCellValue("Йўналиш номи");
                        headerRow.createCell(6).setCellValue("Лицензия АТ №");
                        headerRow.createCell(7).setCellValue("Лицензия АТ муддати");
                        headerRow.createCell(8).setCellValue("Тех. кўрик муддати");

                        headerRow.createCell(9).setCellValue("Газ балон муддати");
                        headerRow.createCell(10).setCellValue("Посажир суғурта муддати");
                        headerRow.createCell(11).setCellValue("GPS рухсатнома муддати");
                        headerRow.createCell(12).setCellValue("Ижара шартнома муддати");
                        headerRow.createCell(13).setCellValue("Суғурта Полиси муддати");
                        headerRow.createCell(14).setCellValue("Дивигател мойи муддати");

                        // writing rows:
                        // creating a row object
                        XSSFRow row;

                        for (int x = 0; x < data.size(); x++) {
                            Map map = (Map) data.get(x);

                            // writing the data into the sheets...
                            row = spreadsheet.createRow(x + 1);
                            row.createCell(0).setCellValue(valueOf(map, "NUM"));
                            row.createCell(1).setCellValue(valueOf(map, "BRAND"));
                            row.createCell(2).setCellValue(valueOf(map, "COMPANY"));
                            row.createCell(3).setCellValue(valueOf(map, "RESP1"));
                            row.createCell(4).setCellValue(valueOf(map, "RESP2"));
                            row.createCell(5).setCellValue(valueOf(map, "YUNALISH"));
                            row.createCell(6).setCellValue(valueOf(map, "ATNUM"));
                            row.createCell(7).setCellValue(valueOf(map, "ATEXP"));
                            row.createCell(8).setCellValue(valueOf(map, "TEXKEXP"));
                            row.createCell(9).setCellValue(valueOf(map, "GAZEXP"));
                            row.createCell(10).setCellValue(valueOf(map, "POSAJIRSEXP"));
                            row.createCell(11).setCellValue(valueOf(map, "GPSEXP"));
                            row.createCell(12).setCellValue(valueOf(map, "IJARASHEXP"));
                            row.createCell(13).setCellValue(valueOf(map, "SPEXP"));
                            row.createCell(14).setCellValue(valueOf(map, "ENGINEEXP"));
                        }
                    }
                }

                // writing the workbook into the file...
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(filePath + "\\База.xlsx");
                } catch (FileNotFoundException ex) {
                    print("error while writing to excel: " + ex.getMessage());
                }

                try {
                    workbook.write(out);
                } catch (IOException ex) {
                    print("error while writing to excel: " + ex.getMessage());
                }
                try {
                    out.close();
                } catch (IOException ex) {
                    print("error while writing to excel: " + ex.getMessage());
                }

                return 42;
            }

        }.execute();

    }

}
