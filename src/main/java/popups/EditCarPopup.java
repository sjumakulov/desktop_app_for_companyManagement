package popups;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import static popups.AddCarPopup.getDateEntryContent;
import static yutong.tools.FileChooser.showFileChooser;
import static yutong.tools.MyScrollBarUI.myScrollBarUI;
import static popups.AddEmployeePopup.MAForTextField;
import static popups.AddEmployeePopup.getFileDataEntryContent;
import static popups.AddEmployeePopup.getStringDataEntryContent;
import static popups.EditEmployeePopup.attachFile;
import static popups.AddEmployeePopup.labelForAddPopup;
import static popups.AddEmployeePopup.textField;
import static yutong.BODY.CAR.CAR;
import static yutong.tools.DBManager.getData;
import static yutong.tools.DBManager.getFontSize;
import static yutong.tools.DBManager.moveFile;
import static yutong.tools.DBManager.update;
import static yutong.tools.DBManager.valueOf;

public class EditCarPopup {

    public static void showEditCarPopup(JFrame FRAME, String ID, Connection connection) {
        GlassPane glassPane = new GlassPane();
        FRAME.getRootPane().setGlassPane(glassPane);

        glassPane.setVisible(true);
        carDialogPopup(FRAME, ID, connection);
        glassPane.setVisible(false);
    }

    public static JDialog carDialogPopup(JFrame FRAME, String ID, Connection connection) {
        JDialog jd = new JDialog(FRAME);

        jd.setLayout(new BorderLayout());
        jd.setSize(830, 600);
        jd.setModal(true);
        jd.setLocationRelativeTo(FRAME);
        jd.add(ScrollPane(jd, ID, connection), BorderLayout.CENTER);
        jd.setVisible(true);

        return jd;
    }

    public static JScrollPane ScrollPane(JDialog jd, String ID, Connection connection) {
        JPanel panelForScrollPane = new JPanel();
        panelForScrollPane.add(dataEntryContainer(jd, ID, connection));
        panelForScrollPane.setBackground(Color.white);

        JScrollPane scrollPane = new JScrollPane(panelForScrollPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(26);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(myScrollBarUI());

        return scrollPane;
    }

    public static JPanel dataEntryContainer(JDialog jd, String ID, Connection connection) {
        ArrayList carList = getData("SELECT * FROM CARS WHERE ID=" + "'" + ID + "'", connection);
        Map carMap = (Map) carList.get(0);

        JPanel dataEntryContainer = new JPanel(new GridBagLayout());
        dataEntryContainer.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 30, 15, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = -1;

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        dataEntryContainer.add(labelForAddPopup("Ўзгартириш"), gbc);

        gbc.gridwidth = 1;
        dataEntryContainer.add(stringDataEntry("Давлат рақами", valueOf(carMap, "NUM"), connection), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntry("Автомобил русуми", valueOf(carMap, "BRAND"), connection), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntry("1-масъул шахс", valueOf(carMap, "RESP1"), connection), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntry("2-масъул шахс", valueOf(carMap, "RESP2"), connection), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntry("Фирма номи", valueOf(carMap, "COMPANY"), connection), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntryWithFile("Тех. кўрик муддати", jd, valueOf(carMap, "TEXKEXP"), valueOf(carMap, "TEXKFILE")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(litsenziyaATDataEntry(jd, valueOf(carMap, "ATEXP"), valueOf(carMap, "ATNUM"), valueOf(carMap, "ATFILE")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntryWithFile("Посажир суғурта муддати", jd, valueOf(carMap, "POSAJIRSEXP"), valueOf(carMap, "POSAJIRSFILE")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(dateDataEntryWithFile("GPS рухсатномаси ва муддати", jd, valueOf(carMap, "GPSEXP"), valueOf(carMap, "GPSFILE")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntryWithFile("Йўналиш номи, харитаси ва паспорти", jd, "Йўналиш номи", valueOf(carMap, "YUNALISH"), valueOf(carMap, "YUNALISHFILE")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(fileDataEntry("Хитой завод хужжатлари", jd, "", valueOf(carMap, "XITOYFILE")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntry("Двигател мойи муддати", valueOf(carMap, "ENGINEEXP")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntryWithFile("Умумий ҳолати", jd, "Умумий ҳолати", valueOf(carMap, "UX"), valueOf(carMap, "UXFILE")), gbc);

        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntryWithFile("Ходовой холати ва ремонти", jd, "Ходовой ҳолати", valueOf(carMap, "XODOVOY"), valueOf(carMap, "XODOVOYFILE")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntryWithFile("Балонлар", jd, "Балон ҳолати", valueOf(carMap, "BALON"), valueOf(carMap, "BALONFILE")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntryWithFile("Салон", jd, "Салон ҳолати", valueOf(carMap, "SALON"), valueOf(carMap, "SALONFILE")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntryWithFile("Богажник", jd, "Богажник ҳолати", valueOf(carMap, "BOGAJ"), valueOf(carMap, "BOGAJFILE")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntryWithFile("Боковой ойналар", jd, "Боковой ойна ҳолати", valueOf(carMap, "BOKOVOY"), valueOf(carMap, "BOKOVOYFILE")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntryWithFile("Лобовой ойна", jd, "Лобовой ойна ҳолати", valueOf(carMap, "LOBOVOY"), valueOf(carMap, "LOBOVOYFILE")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntryWithFile("Суғурта Полиси", jd, valueOf(carMap, "SPEXP"), valueOf(carMap, "SPFILE")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntryWithFile("Кузов", jd, "Кузов ҳолати", valueOf(carMap, "KUZOV"), valueOf(carMap, "KUZOVFILE")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntryWithFile("Газ балон муддати", jd, valueOf(carMap, "GAZEXP"), valueOf(carMap, "GAZFILE")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(dateDataEntryWithFile("Ижара шартнома муддати", jd, valueOf(carMap, "IJARASHEXP"), valueOf(carMap, "IJARASHFILE")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntryWithFile("Тахогров хужжати ва муддати", jd, valueOf(carMap, "TAXOGROVEXP"), valueOf(carMap, "TAXOGROVFILE")), gbc);

        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.gridx = 0;
        gbc.gridy = 13;
        dataEntryContainer.add(button("Бекор қилиш", "cancel", jd, dataEntryContainer, carMap, connection), gbc);
        gbc.gridx = 1;
        gbc.gridy = 13;
        dataEntryContainer.add(button("Сақлаш", "save", jd, dataEntryContainer, carMap, connection), gbc);

        return dataEntryContainer;
    }

    public static JPanel dateDataEntry(String dataLabel, String expDate) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JDateChooser chooser = dateChooser();
        chooser.setPreferredSize(new Dimension(320, 30));

        container.add(label, BorderLayout.PAGE_START);
        container.add(chooser, BorderLayout.CENTER);

        return container;
    }

    public static JPanel fileDataEntry(String dataLabel, JDialog jd, String fileFormat, String path) {
        Color white = Color.white;

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JPanel textField = attachFile(jd, fileFormat, path);

        container.add(label, BorderLayout.PAGE_START);
        container.add(textField, BorderLayout.CENTER);

        return container;
    }

    public static JPanel stringDataEntry(String dataLabel, String value, Connection connection) {

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JTextField textField = textField(new Dimension(320, 30));
        textField.setBackground(Color.decode("#f5f5f5"));
        textField.setText(value);

        container.add(label, BorderLayout.PAGE_START);
        container.add(textField, BorderLayout.CENTER);

        if (dataLabel.equals("Давлат рақами")) {
            checkForCarWhenAdding(textField, value, connection);
        }

        return container;
    }

    public static JPanel stringDataEntryWithFile(String dataLabel, JDialog jd, String popUpText, String value, String path) {

        JPanel container = new JPanel(new BorderLayout(0, 4));
        container.setBackground(Color.white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JTextField textField = textField(new Dimension(320, 30));
        textField.setBackground(Color.decode("#f5f5f5"));
        textField.addMouseListener(MAForTextField(textField, popUpText));
        textField.setText(value);

        container.add(label, BorderLayout.PAGE_START);
        container.add(textField, BorderLayout.CENTER);

        JPanel af = attachFile(jd, "", path);
        container.add(af, BorderLayout.PAGE_END);

        return container;
    }

    public static JPanel dateDataEntryWithFile(String dataLabel, JDialog jd, String expDate, String path) {
        JPanel container = new JPanel(new BorderLayout(4, 0));
        container.setBackground(Color.white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JDateChooser chooser = dateChooser();
        chooser.setPreferredSize(new Dimension(120, 30));

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try {
            if (!expDate.isBlank()) {
                chooser.setDate(df.parse(expDate));
            }
        } catch (ParseException ex) {
            Logger.getLogger(EditCarPopup.class.getName()).log(Level.SEVERE, null, ex);
        }

        container.add(label, BorderLayout.PAGE_START);

        JPanel af = attachFile(jd, "", path);
        af.setPreferredSize(new Dimension(200, 30));
        container.add(af, BorderLayout.CENTER);
        container.add(chooser, BorderLayout.LINE_END);

        return container;
    }

    private static JButton button(String label, String name, JDialog jd, JPanel dataEntryContainer, Map oldCarMap, Connection connection) {
        JButton button = new JButton(label);
        button.setName(name);
        button.setPreferredSize(new Dimension(320, 35));
        button.setFocusable(false);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(null, Font.BOLD, 16));

        if (name.equals("cancel")) {
            button.setBackground(Color.decode("#DB4437"));
            button.setForeground(Color.white);
        } else {
            button.setBackground(Color.decode("#4285F4"));
            button.setForeground(Color.white);
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (name.equals("cancel")) {
                    button.setBackground(Color.decode("#df5549"));
                } else {
                    button.setBackground(Color.decode("#4f8ef5"));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (name.equals("cancel")) {
                    button.setBackground(Color.decode("#DB4437"));
                } else {
                    button.setBackground(Color.decode("#4285F4"));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (name.equals("cancel")) {
                    jd.dispose();
                } else {
                    jd.dispose();
                    new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            Map newCarMap = saveCar(dataEntryContainer, oldCarMap, connection);

                            editCarInUI(jd, newCarMap, connection);
                            return 42;
                        }
                    }.execute();
                }
            }

        });

        return button;
    }

    private static void editCarInUI(JDialog jd, Map newCarMap, Connection connection) {
        JFrame FRAME = (JFrame) SwingUtilities.getRoot(jd.getParent());
        JPanel BODY = (JPanel) FRAME.getContentPane().getComponent(1);

        JScrollPane scrollPane = (JScrollPane) BODY.getComponent(1);
        JPanel panelForScrollPane = (JPanel) scrollPane.getViewport().getComponent(0);
        JPanel CARS = (JPanel) panelForScrollPane.getComponent(0);

        for (int i = 0; i < CARS.getComponentCount(); i++) {
            JPanel currentCar = (JPanel) CARS.getComponent(i);
            JPanel currentCarBody = (JPanel) currentCar.getComponent(2);
            JPanel toolsPanel = (JPanel) currentCarBody.getComponent(1);
            JPanel mainPanel = (JPanel) toolsPanel.getComponent(0);
            String currentID = mainPanel.getName();

            if (valueOf(newCarMap, "ID").equals(currentID)) {
                CARS.remove(currentCar);

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int w = (int) (screenSize.getWidth() * 0.05);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(0, w, 0, 0);
                gbc.weightx = 0.5;

                gbc.gridx = 0;
                gbc.gridy = -1;

                JPanel newCAR = CAR(newCarMap, String.valueOf(i + 1), "arrowdown", connection, getFontSize(connection));
                newCAR.getComponent(2).setVisible(true);

                CARS.add(newCAR, gbc, i);
                CARS.revalidate();
                CARS.repaint();

                break;
            }
        }

    }

    private static Map saveCar(JPanel dataEntryContainer, Map oldCarMap, Connection connection) {
        String ID = valueOf(oldCarMap, "ID");

        String NUM = getStringDataEntryContent(dataEntryContainer, 1, 15);
        String BRAND = getStringDataEntryContent(dataEntryContainer, 2, 25);
        String RESP1 = getStringDataEntryContent(dataEntryContainer, 3, 40);
        String RESP2 = getStringDataEntryContent(dataEntryContainer, 4, 40);
        String COMPANY = getStringDataEntryContent(dataEntryContainer, 5, 100);

        String TEXKFILE = getFileDataEntryContent(dataEntryContainer, 6, 1);
        TEXKFILE = moveFile2(TEXKFILE, "TEXKFILE", ID, "CARS", oldCarMap);

        String TEXTKEXP = getDateEntryContent(dataEntryContainer, 6, 2);

        String ATNUM = getStringDataEntryContent(dataEntryContainer, 7, 50);
        String ATEXP = getDateEntryContent(dataEntryContainer, 7, 2);
        String ATFILE = getFileDataEntryContent(dataEntryContainer, 7, 3);
        ATFILE = moveFile2(ATFILE, "ATFILE", ID, "CARS", oldCarMap);

        String POSAJIRSEXP = getDateEntryContent(dataEntryContainer, 8, 2);
        String POSAJIRSFILE = getFileDataEntryContent(dataEntryContainer, 8, 1);
        POSAJIRSFILE = moveFile2(POSAJIRSFILE, "POSAJIRSFILE", ID, "CARS", oldCarMap);

        String GPSEXP = getDateEntryContent(dataEntryContainer, 9, 2);
        String GPSFILE = getFileDataEntryContent(dataEntryContainer, 9, 1);
        GPSFILE = moveFile2(GPSFILE, "GPSFILE", ID, "CARS", oldCarMap);

        String YUNALISH = getStringDataEntryContent(dataEntryContainer, 10, 50);
        String YUNALISHFILE = getFileDataEntryContent(dataEntryContainer, 10, 2);
        YUNALISHFILE = moveFile2(YUNALISHFILE, "YUNALISHFILE", ID, "CARS", oldCarMap);

        String XITOYFILE = getFileDataEntryContent(dataEntryContainer, 11, 1);
        XITOYFILE = moveFile2(XITOYFILE, "XITOYFILE", ID, "CARS", oldCarMap);

        String ENGINEEXP = getDateEntryContent(dataEntryContainer, 12, 1);

        String UX = getStringDataEntryContent(dataEntryContainer, 13, 20);
        String UXFILE = getFileDataEntryContent(dataEntryContainer, 13, 2);
        UXFILE = moveFile2(UXFILE, "UXFILE", ID, "CARS", oldCarMap);

        String XODOVOY = getStringDataEntryContent(dataEntryContainer, 14, 20);
        String XODOVOYFILE = getFileDataEntryContent(dataEntryContainer, 14, 2);
        XODOVOYFILE = moveFile2(XODOVOYFILE, "XODOVOYFILE", ID, "CARS", oldCarMap);

        String BALON = getStringDataEntryContent(dataEntryContainer, 15, 20);
        String BALONFILE = getFileDataEntryContent(dataEntryContainer, 15, 2);
        BALONFILE = moveFile2(BALONFILE, "BALONFILE", ID, "CARS", oldCarMap);

        String SALON = getStringDataEntryContent(dataEntryContainer, 16, 20);
        String SALONFILE = getFileDataEntryContent(dataEntryContainer, 16, 2);
        SALONFILE = moveFile2(SALONFILE, "SALONFILE", ID, "CARS", oldCarMap);

        String BOGAJ = getStringDataEntryContent(dataEntryContainer, 17, 20);
        String BOGAJFILE = getFileDataEntryContent(dataEntryContainer, 17, 2);
        BOGAJFILE = moveFile2(BOGAJFILE, "BOGAJFILE", ID, "CARS", oldCarMap);

        String BOKOVOY = getStringDataEntryContent(dataEntryContainer, 18, 20);
        String BOKOVOYFILE = getFileDataEntryContent(dataEntryContainer, 18, 2);
        BOKOVOYFILE = moveFile2(BOKOVOYFILE, "BOKOVOYFILE", ID, "CARS", oldCarMap);

        String LOBOVOY = getStringDataEntryContent(dataEntryContainer, 19, 20);
        String LOBOVOYFILE = getFileDataEntryContent(dataEntryContainer, 19, 2);
        LOBOVOYFILE = moveFile2(LOBOVOYFILE, "LOBOVOYFILE", ID, "CARS", oldCarMap);

        String SPEXP = getDateEntryContent(dataEntryContainer, 20, 2);
        String SPFILE = getFileDataEntryContent(dataEntryContainer, 20, 1);
        SPFILE = moveFile2(SPFILE, "SPFILE", ID, "CARS", oldCarMap);

        String KUZOV = getStringDataEntryContent(dataEntryContainer, 21, 20);
        String KUZOVFILE = getFileDataEntryContent(dataEntryContainer, 21, 2);
        KUZOVFILE = moveFile2(KUZOVFILE, "KUZOVFILE", ID, "CARS", oldCarMap);

        String GAZFILE = getFileDataEntryContent(dataEntryContainer, 22, 1);
        GAZFILE = moveFile2(GAZFILE, "GAZFILE", ID, "CARS", oldCarMap);
        String GAZEXP = getDateEntryContent(dataEntryContainer, 22, 2);

        String IJARASHFILE = getFileDataEntryContent(dataEntryContainer, 23, 1);
        IJARASHFILE = moveFile2(IJARASHFILE, "IJARASHFILE", ID, "CARS", oldCarMap);
        String IJARAEXP = getDateEntryContent(dataEntryContainer, 23, 2);

        String TAXOGROVFILE = getFileDataEntryContent(dataEntryContainer, 24, 1);
        TAXOGROVFILE = moveFile2(TAXOGROVFILE, "TAXOGROVFILE", ID, "CARS", oldCarMap);
        String TAXOGROVEXP = getDateEntryContent(dataEntryContainer, 24, 2);

        Map newCarMap = new HashMap();
        newCarMap.put("ID", ID);
        newCarMap.put("NUM", NUM);
        newCarMap.put("BRAND", BRAND);
        newCarMap.put("RESP1", RESP1);
        newCarMap.put("RESP2", RESP2);
        newCarMap.put("COMPANY", COMPANY);
        newCarMap.put("TEXKFILE", TEXKFILE);
        newCarMap.put("TEXKEXP", TEXTKEXP);
        newCarMap.put("TEXKSTART", valueOf(oldCarMap, "TEXKSTART"));
        newCarMap.put("ATNUM", ATNUM);
        newCarMap.put("ATEXP", ATEXP);
        newCarMap.put("ATSTART", valueOf(oldCarMap, "ATSTART"));
        newCarMap.put("ATFILE", ATFILE);
        newCarMap.put("POSAJIRSFILE", POSAJIRSFILE);
        newCarMap.put("POSAJIRSEXP", POSAJIRSEXP);
        newCarMap.put("POSAJIRSSTART", valueOf(oldCarMap, "POSAJIRSSTART"));

        newCarMap.put("GPSFILE", GPSFILE);
        newCarMap.put("GPSEXP", GPSEXP);
        newCarMap.put("GPSSTART", valueOf(oldCarMap, "GPSSTART"));

        newCarMap.put("YUNALISH", YUNALISH);
        newCarMap.put("YUNALISHFILE", YUNALISHFILE);

        newCarMap.put("XITOYFILE", XITOYFILE);
        newCarMap.put("ENGINEEXP", ENGINEEXP);
        newCarMap.put("ENGINESTART", valueOf(oldCarMap, "ENGINESTART"));

        newCarMap.put("UX", UX);
        newCarMap.put("UXFILE", UXFILE);

        newCarMap.put("XODOVOY", XODOVOY);
        newCarMap.put("XODOVOYFILE", XODOVOYFILE);

        newCarMap.put("BALON", BALON);
        newCarMap.put("BALONFILE", BALONFILE);

        newCarMap.put("SALON", SALON);
        newCarMap.put("SALONFILE", SALONFILE);

        newCarMap.put("BOGAJ", BOGAJ);
        newCarMap.put("BOGAJFILE", BOGAJFILE);

        newCarMap.put("BOKOVOY", BOKOVOY);
        newCarMap.put("BOKOVOYFILE", BOKOVOYFILE);

        newCarMap.put("LOBOVOY", LOBOVOY);
        newCarMap.put("LOBOVOYFILE", LOBOVOYFILE);

        newCarMap.put("SPEXP", SPEXP);
        newCarMap.put("SPSTART", valueOf(oldCarMap, "SPSTART"));
        newCarMap.put("SPFILE", SPFILE);

        newCarMap.put("KUZOV", KUZOV);
        newCarMap.put("KUZOVFILE", KUZOVFILE);

        newCarMap.put("GAZEXP", GAZEXP);
        newCarMap.put("GAZSTART", valueOf(oldCarMap, "GAZSTART"));
        newCarMap.put("GAZFILE", GAZFILE);

        newCarMap.put("IJARASHEXP", IJARAEXP);
        newCarMap.put("IJARASHFILE", IJARASHFILE);
        newCarMap.put("IJARASHSTART", valueOf(oldCarMap, "IJARASHSTART"));

        newCarMap.put("TAXOGROVEXP", TAXOGROVEXP);
        newCarMap.put("TAXOGROVFILE", TAXOGROVFILE);
        newCarMap.put("TAXOGROVSTART", valueOf(oldCarMap, "TAXOGROVSTART"));

        Map editCarMap = new HashMap();
        for (Object k : oldCarMap.keySet()) {
            if (k.toString().contains("EXP")) {
                Calendar now = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                String startDate = df.format(now.getTime());

                String startDateKey = k.toString().replace("EXP", "START");
                if (!newCarMap.get(k).equals(oldCarMap.get(k))) {
                    editCarMap.put(k, newCarMap.get(k));
                    editCarMap.put(startDateKey, startDate);

                    newCarMap.put(startDateKey, startDate);
                }
            } else {
                if (!newCarMap.get(k).equals(oldCarMap.get(k))) {
                    if (k.toString().contains("FILE")) {
                        String path = moveFile(valueOf(newCarMap, k.toString()), valueOf(oldCarMap, "ID"), "CARS", k.toString());

                        editCarMap.put(k, path);
                        newCarMap.put(k, path);
                    } else {
                        editCarMap.put(k, newCarMap.get(k));
                    }
                }
            }
        }
        if (!editCarMap.isEmpty()) {
            update("CARS", editCarMap, valueOf(oldCarMap, "ID"), connection);
        }

        return newCarMap;

    }

    public static String moveFile2(String newPath, String key, String ID, String tableName, Map oldMap) {
        String oldPath = valueOf(oldMap, key);

        if (newPath.isBlank()) {
            return valueOf(oldMap, key);
        } else if (!oldPath.equals(newPath)) {

            File file = new File(oldPath);
            file.delete();

            return moveFile(newPath, ID, tableName, key);
        } else {
            return oldPath;
        }

    }

    public static JTextField fileChooserTextField(JDialog jd) {
        Color blue = Color.decode("#edf6ff");

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(320, 30));
        textField.setBorder(null);
        textField.setBackground(blue);
        textField.setFont(new Font(null, Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
                textField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        textField.setEditable(false);
        textField.setForeground(Color.gray);
        textField.setCursor(new Cursor(Cursor.HAND_CURSOR));
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame FRAME = (JFrame) SwingUtilities.getRoot(jd.getParent());
                String filePath = showFileChooser(FRAME, "");
                textField.setText(filePath);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                textField.setBackground(Color.decode("#e0f0ff"));
                if (textField.getText().isBlank()) {
                    textField.setText("Файлни танлаш учун, босинг");
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                textField.setBackground(blue);
                if (textField.getText().equals("Файлни танлаш учун, босинг")) {
                    textField.setText("");
                }
            }
        });

        return textField;
    }

    public static JPanel litsenziyaATDataEntry(JDialog jd, String EXP, String ATNUM, String path) {
        JDateChooser chooser = dateChooser();
        chooser.setPreferredSize(new Dimension(120, 30));

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try {
            if (!EXP.isBlank()) {
                chooser.setDate(df.parse(EXP));
            }
        } catch (ParseException ex) {
            Logger.getLogger(EditCarPopup.class.getName()).log(Level.SEVERE, null, ex);
        }

        JPanel container = new JPanel(new BorderLayout(4, 4));
        container.setBackground(Color.white);

        JLabel label = new JLabel("Лицензия АТ");
        label.setFont(new Font(null, Font.PLAIN, 18));

        JTextField litsenziyaATTextField = textField(new Dimension(200, 30));
        litsenziyaATTextField.setBackground(Color.decode("#f5f5f5"));
        litsenziyaATTextField.setText(ATNUM);
        litsenziyaATTextField.addMouseListener(MAForTextField(litsenziyaATTextField, "№"));

        container.add(label, BorderLayout.PAGE_START);
        container.add(litsenziyaATTextField, BorderLayout.CENTER);

        container.add(chooser, BorderLayout.LINE_END);
        container.add(attachFile(jd, "", path), BorderLayout.PAGE_END);

        return container;
    }

    public static JDateChooser dateChooser() {
        JDateChooser chooser = null;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            chooser = new JDateChooser();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException e) {
        }

        chooser.setBorder(null);
        chooser.setPreferredSize(new Dimension(130, 30));
        chooser.setFont(new Font(null, Font.PLAIN, 15));
        chooser.setMinSelectableDate(new GregorianCalendar().getTime());
        chooser.setDateFormatString("dd-MM-yyy");

        JTextFieldDateEditor editor = (JTextFieldDateEditor) chooser.getDateEditor();
        editor.setEditable(false);

        return chooser;
    }

    private static void checkForCarWhenAdding(JTextField tf, String NUM, Connection connection) {

        tf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String enteredNUM = tf.getText();
                enteredNUM = enteredNUM.replaceAll(" ", "").toUpperCase();

                JPanel dataEntryContainer = (JPanel) tf.getParent().getParent();
                JButton saveButton = (JButton) dataEntryContainer.getComponent(25);
                if (enteredNUM.isBlank()) {
                    saveButton.setEnabled(false);
                } else {
                    saveButton.setEnabled(true);

                    if (enteredNUM.length() > 15) {
                        enteredNUM = enteredNUM.substring(0, 15);
                    }
                    tf.setText(enteredNUM);

                    if (!NUM.equals(enteredNUM)) {
                        ArrayList CARlist = getData("SELECT NUM FROM CARS WHERE NUM=" + "'" + enteredNUM + "'", connection);

                        if (CARlist.isEmpty()) {
                            saveButton.setEnabled(true);
                        } else {
                            saveButton.setEnabled(false);
                        }

                    }
                }

            }
        });

    }
}
