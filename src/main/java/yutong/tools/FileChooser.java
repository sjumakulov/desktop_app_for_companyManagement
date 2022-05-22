package yutong.tools;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import static yutong.tools.DBManager.CopyFile;
import static yutong.tools.DBManager.getData;
import static yutong.tools.DBManager.valueOf;
import static yutong.tools.Excel.writeToExcel;

public class FileChooser {

    public static String showFileChooser(JFrame FRAME, String format) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Downloads"));
        fileChooser.setDialogTitle("Файлни танланг");

        if (format.isBlank()) {
            fileChooser.setFileFilter(new FileNameExtensionFilter("Фақат PDF | word | excel | jpg", "pdf", "doc", "docx", "xlsx", "jpg"));
        } else {
            fileChooser.setFileFilter(new FileNameExtensionFilter("Фақат jpg", "jpg"));
        }

        int returnValue = fileChooser.showDialog(FRAME, "Танлаш");
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.toString();
        } else {
            return "";
        }
    }

    public static void showFilePathChooser(JFrame FRAME, String ID, String type, Connection connection) {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Downloads"));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Сақлаш учун манзилни танланг");

        int returnValue = fileChooser.showSaveDialog(FRAME);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedPath = fileChooser.getSelectedFile();
            if (selectedPath.exists()) {
                switch (type) {
                    case "EMPLOYEES" -> {
                        ArrayList employeeList = getData("SELECT * FROM EMPLOYEES WHERE ID=" + "'" + ID + "'", connection);
                        copyFilesOfEmployee((Map) employeeList.get(0), selectedPath);
                        break;
                    }
                    case "CARS" -> {
                        ArrayList employeeList = getData("SELECT * FROM CARS WHERE ID=" + "'" + ID + "'", connection);
                        copyFilesOfCar((Map) employeeList.get(0), selectedPath);

                        break;
                    }
                    case "LETTERS", "STOCK" -> {
                        ArrayList list = getData("SELECT * FROM " + type + " WHERE ID=" + "'" + ID + "'", connection);
                        Map map = (Map) list.get(0);

                        String date = valueOf(map, "KUN") + "." + valueOf(map, "OY") + "." + valueOf(map, "YIL");
                        String TEXT = valueOf(map, "TEXT");
                        TEXT = TEXT.length() > 20 ? TEXT.substring(0, 20) : TEXT;

                        String TYPE = valueOf(map, "TYPE");
                        TYPE = TYPE.equals("IN") ? "Кирим" : "Чиқим";

                        String tableName = type.equals("LETTERS") ? "Хат" : "Склад";
                        String fileName = TYPE + " " + tableName + " - " + TEXT + " - " + date;

                        moveFile(valueOf(map, "FILE"), selectedPath.toString() + "\\" + fileName);
                        break;
                    }
                    case "EXCEL" -> {
                        writeToExcel(selectedPath, connection);
                        break;
                    }
                }
            }
        }
    }

    private static void copyFilesOfCar(Map carMap, File selectedPath) {
        String TEXKFILE = valueOf(carMap, "TEXKFILE");
        String ATFILE = valueOf(carMap, "ATFILE");
        String POSAJIRSFILE = valueOf(carMap, "POSAJIRSFILE");
        String GPSFILE = valueOf(carMap, "GPSFILE");
        String YUNALISHFILE = valueOf(carMap, "YUNALISHFILE");
        String IJARASHFILE = valueOf(carMap, "IJARASHFILE");
        String GAZFILE = valueOf(carMap, "GAZFILE");
        String SPFILE = valueOf(carMap, "SPFILE");
        String XITOYFILE = valueOf(carMap, "XITOYFILE");
        String UXFILE = valueOf(carMap, "UXFILE");
        String XODOVOYFILE = valueOf(carMap, "XODOVOYFILE");
        String BALONFILE = valueOf(carMap, "BALONFILE");
        String SALONFILE = valueOf(carMap, "SALONFILE");
        String BOGAJFILE = valueOf(carMap, "BOGAJFILE");
        String BOKOVOYFILE = valueOf(carMap, "BOKOVOYFILE");
        String LOBOVOYFILE = valueOf(carMap, "LOBOVOYFILE");
        String KUZOVFILE = valueOf(carMap, "KUZOVFILE");
        String NUM = valueOf(carMap, "NUM");

        if (selectedPath.exists()) {
            String newPath = selectedPath + "\\" + NUM;
            File newFolder = new File(newPath);
            newFolder.mkdir();
            moveFile(ATFILE, newPath + "\\Litsenziya AT");
            moveFile(POSAJIRSFILE, newPath + "\\Posajir sug'urta");
            moveFile(TEXKFILE, newPath + "\\Tex ko'rik");
            moveFile(GPSFILE, newPath + "\\GPS ruxsatnoma");
            moveFile(YUNALISHFILE, newPath + "\\Yunalish xaritasi");
            moveFile(IJARASHFILE, newPath + "\\Ijara shartnoma");
            moveFile(GAZFILE, newPath + "\\Gazbalon");
            moveFile(SPFILE, newPath + "\\Sug'urta polisi");
            moveFile(XITOYFILE, newPath + "\\Xitoy zavod xujjatlari");
            moveFile(UXFILE, newPath + "\\Umumiy xolat");
            moveFile(XODOVOYFILE, newPath + "\\Xodovoy");
            moveFile(BALONFILE, newPath + "\\Balon");
            moveFile(SALONFILE, newPath + "\\Salon");
            moveFile(BOGAJFILE, newPath + "\\Bogaj");
            moveFile(BOKOVOYFILE, newPath + "\\Bokovoy");
            moveFile(LOBOVOYFILE, newPath + "\\Lobovoy");
            moveFile(KUZOVFILE, newPath + "\\Kuzov");
        }
    }

    private static void copyFilesOfEmployee(Map employeeMap, File selectedPath) {
        String HGFILE = valueOf(employeeMap, "HGFILE");
        String RASM = valueOf(employeeMap, "RASM");
        String SERTIFIKAT = valueOf(employeeMap, "SERTIFIKAT");
        String INNFILE = valueOf(employeeMap, "INNFILE");
        String MALUMOTNOMA = valueOf(employeeMap, "MALUMOTNOMA");
        String SUROVNOMA = valueOf(employeeMap, "SUROVNOMA");
        String TARJIMAIHOL = valueOf(employeeMap, "TARJIMAIHOL");
        String INPS = valueOf(employeeMap, "INPS");
        String MEHNATSHFILE = valueOf(employeeMap, "MEHNATSHFILE");
        String ARIZA = valueOf(employeeMap, "ARIZA");
        String PASPORT = valueOf(employeeMap, "PASPORT");
        String MEDMALUMOTNOMA = valueOf(employeeMap, "MEDMALUMOTNOMA");
        String HIREDP = valueOf(employeeMap, "HIREDP");
        String FIREDP = valueOf(employeeMap, "FIREDP");

        String ISM = valueOf(employeeMap, "ISM");
        if (ISM.isBlank()) {
            ISM = "Unknown";
        }

        if (selectedPath.exists()) {
            String newPath = selectedPath + "\\" + ISM;
            File newFolder = new File(newPath);
            newFolder.mkdir();
            moveFile(HGFILE, newPath + "\\HGuvohnoma");
            moveFile(SERTIFIKAT, newPath + "\\Sertifikatlar");
            moveFile(INNFILE, newPath + "\\INN");
            moveFile(MALUMOTNOMA, newPath + "\\Malumotnoma");
            moveFile(SUROVNOMA, newPath + "\\Surovnoma");
            moveFile(TARJIMAIHOL, newPath + "\\Tarjimaihol");
            moveFile(INPS, newPath + "\\INPS");
            moveFile(MEHNATSHFILE, newPath + "\\Mehnat shatnoma");
            moveFile(ARIZA, newPath + "\\Ariza");
            moveFile(PASPORT, newPath + "\\Pasport");
            moveFile(MEDMALUMOTNOMA, newPath + "\\Tibbiy malumotnoma");
            moveFile(HIREDP, newPath + "\\Ishga olinganligi tugrisidagi prikaz");
            moveFile(FIREDP, newPath + "\\Ishdan olinganligi tugrisidagi prikaz");
            moveFile(RASM, newPath + "\\RASM");
        }
    }

    private static void moveFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        if (oldFile.exists()) {
            String oldFileName = oldFile.getName();
            String extension = "";

            int i = oldFileName.lastIndexOf('.');
            if (i > 0) {
                extension = oldFileName.substring(i + 1).toLowerCase();
            }
            CopyFile(oldPath, newPath + "." + extension);
        }
    }
}
