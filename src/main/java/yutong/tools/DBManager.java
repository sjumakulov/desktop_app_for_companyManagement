package yutong.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBManager {

    public static Connection connectToDatabase() {

        String dataBaseURL = "jdbc:derby:./data/database;";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dataBaseURL);

        } catch (SQLException ex) {
//            ex.printStackTrace();
        }

        return connection;
    }

    public static ArrayList getData(String sql, Connection connection) {
//        Connection connection = connectToDatabase();
        // retrieve:
        ArrayList list = new ArrayList();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            int numOfCols = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                Map obj = new HashMap();
                for (int colNum = 1; colNum <= numOfCols; colNum++) {
                    String colLabel = rs.getMetaData().getColumnLabel(colNum);
                    obj.put(colLabel, rs.getString(colLabel));
                }
                list.add(obj);

            }

            rs.close();
            st.close();
//            connection.close();
//            DriverManager.getConnection("jdbc:derby:./data/database;shutdown=true");
        } catch (SQLException ex) {
//            ex.printStackTrace();
        }

        return list;
    }

    public static void insert(String tableName, Map obj, Connection connection) {
//        Connection connection = connectToDatabase();

        Statement st;

        String keys = "(";
        String values = "(";
        for (Object k : obj.keySet()) {
            String key = (String) k;
            keys += key + ",";

            String val = obj.get(k).toString();
            if (val.contains("'")) {
                val = val.replaceAll("'", "''");
            }
            values += "'" + val + "'" + ",";
        }

        keys = keys.substring(0, keys.length() - 1) + ")";
        values = values.substring(0, values.length() - 1) + ")";

        String sql = "INSERT INTO " + tableName + keys + " VALUES " + values;

        try {
            st = connection.createStatement();
            st.executeUpdate(sql);

            st.close();
//            connection.close();

//            DriverManager.getConnection("jdbc:derby:./data/database;shutdown=true");
        } catch (SQLException ex) {
//            ex.printStackTrace();
        }
    }

    public static void update(String tableName, Map map, String ID, Connection connection) {
        Statement st;

        String set = "";

        for (Object k : map.keySet()) {

            String val = map.get(k).toString();
            if (val.contains("'")) {
                val = val.replaceAll("'", "''");
            }

            set += k.toString() + "=" + "'" + val + "',";
        }
        set = set.substring(0, set.length() - 1);

        String sql = "UPDATE " + tableName + " SET " + set + " WHERE ID = " + "'" + ID + "'";

        try {
            st = connection.createStatement();
            st.executeUpdate(sql);

            st.close();
//            connection.close();
//            DriverManager.getConnection("jdbc:derby:./data/database;shutdown=true");
        } catch (SQLException ex) {
//            ex.printStackTrace();
        }

    }

    public static void createTable(String employeeID, Connection connection) {
//        Connection connection = connectToDatabase();

        Statement st;

        String sql = "CREATE TABLE " + employeeID + " (\n"
                + "    ID varchar(32) NOT NULL,\n"
                + "    KUN varchar(2),\n"
                + "    OY varchar(2),\n"
                + "    YIL varchar(4) NOT NULL,\n"
                + "    TEXT varchar(300),\n"
                + "    PRIMARY KEY (ID)"
                + ")";

        try {
            st = connection.createStatement();
            st.executeUpdate(sql);

            st.close();
//            connection.close();
//            DriverManager.getConnection("jdbc:derby:./data/database;shutdown=true");
        } catch (SQLException ex) {
//            ex.printStackTrace();
        }

    }

    public static void deleteFromDB(String tableName, String ID, Connection connection) {

//        Connection connection = connectToDatabase();
        Statement st;

        String sql = "DELETE FROM " + tableName + " WHERE ID= '" + ID + "'";

        try {
            st = connection.createStatement();
            st.executeUpdate(sql);

            if (tableName.equals("EMPLOYEES")) {
                st.executeUpdate("DROP TABLE " + ID);
            }

            st.close();
//            connection.close();
//            DriverManager.getConnection("jdbc:derby:./data/database;shutdown=true");
        } catch (SQLException ex) {
//            ex.printStackTrace();
        }
    }

    public static String moveFile(String oldPath, String ID, String folder, String newFileName) {
        String newPath = "";
        File oldFile = new File(oldPath);
        if (oldFile.exists()) {
            String oldFileName = oldFile.getName();
            String extension = "";

            int i = oldFileName.lastIndexOf('.');
            if (i > 0) {
                extension = oldFileName.substring(i + 1).toLowerCase();
            }

            switch (folder) {
                case "EMPLOYEES" -> {
                    new File("./data/files/EMPLOYEES/" + ID).mkdir();
                    newPath = "./data/files/EMPLOYEES/" + ID + "/" + newFileName + "." + extension;
                    CopyFile(oldPath, newPath);
                    break;
                }
                case "CARS" -> {
                    new File("./data/files/CARS/" + ID).mkdir();
                    newPath = "./data/files/CARS/" + ID + "/" + newFileName + "." + extension;
                    CopyFile(oldPath, newPath);
                    break;
                }
                case "LETTERS/IN/", "LETTERS/OUT/" -> {
                    newPath = "./data/files/" + folder + ID + "." + extension;
                    CopyFile(oldPath, newPath);

                    break;
                }
                case "STOCK/IN/", "STOCK/OUT/" -> {
                    newPath = "./data/files/" + folder + ID + "." + extension;
                    CopyFile(oldPath, newPath);

                    break;
                }

            }
        }

        return newPath;
    }

    public static String valueOf(Map obj, String key) {
        if (obj.get(key) == null) {
//            print(obj);
//            print(key + " == null");
        }

        return obj.get(key).toString();
    }

    public static void CopyFile(String oldFile, String newFile) {
        try {
            Files.copy(Paths.get(oldFile), Paths.get(newFile), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
//            ex.printStackTrace();
        }
    }

    public static int getFontSize(Connection con) {
        ArrayList list = (ArrayList) getData("SELECT * FROM OTHER WHERE ID='SIZE'", con);
        Map sizeMap = (Map) list.get(0);
        return Integer.parseInt(sizeMap.get("VALUE").toString());
    }
}
