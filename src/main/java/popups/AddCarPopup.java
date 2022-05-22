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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
import static yutong.tools.FileChooser.showFileChooser;
import static yutong.tools.MyScrollBarUI.myScrollBarUI;
import static popups.AddEmployeePopup.MAForTextField;
import static popups.AddEmployeePopup.labelForAddPopup;
import static popups.AddEmployeePopup.textField;
import static popups.AddEmployeePopup.attachFile;
import static popups.AddEmployeePopup.getFileDataEntryContent;
import static popups.AddEmployeePopup.getStringDataEntryContent;
import static yutong.BODY.CAR.CAR;
import static yutong.tools.DBManager.getData;
import static yutong.tools.DBManager.getFontSize;
import static yutong.tools.DBManager.insert;
import static yutong.tools.DBManager.moveFile;

public class AddCarPopup {

    public static void showAddCarPopup(JFrame FRAME, Connection connection) {
        GlassPane glassPane = new GlassPane();
        FRAME.getRootPane().setGlassPane(glassPane);

        glassPane.setVisible(true);
        carDialogPopup(FRAME, connection);
        glassPane.setVisible(false);
    }

    public static JDialog carDialogPopup(JFrame FRAME, Connection connection) {
        JDialog jd = new JDialog(FRAME);

        jd.setLayout(new BorderLayout());
        jd.setSize(850, 600);
        jd.setModal(true);
        jd.setLocationRelativeTo(FRAME);
        jd.add(ScrollPane(jd, connection), BorderLayout.CENTER);
        jd.setVisible(true);

        return jd;
    }

    public static JScrollPane ScrollPane(JDialog jd, Connection connection) {
        JPanel panelForScrollPane = new JPanel();
        panelForScrollPane.add(dataEntryContainer(jd, connection));
        panelForScrollPane.setBackground(Color.white);

        JScrollPane scrollPane = new JScrollPane(panelForScrollPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(26);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(myScrollBarUI());

        return scrollPane;
    }

    public static JPanel dataEntryContainer(JDialog jd, Connection connection) {
        JPanel dataEntryContainer = new JPanel(new GridBagLayout());
        dataEntryContainer.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 30, 15, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = -1;

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        dataEntryContainer.add(labelForAddPopup("Автомобил қўшиш"), gbc);

        gbc.gridwidth = 1;
        dataEntryContainer.add(stringDataEntry("Давлат рақами", connection), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntry("Автомобил русуми", connection), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntry("1-масъул шахс", connection), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntry("2-масъул шахс", connection), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntry("Фирма номи", connection), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntryWithFile("Тех. кўрик муддати", jd), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(litsenziyaATDataEntry(jd), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntryWithFile("Посажир суғурта муддати", jd), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(dateDataEntryWithFile("GPS рухсатномаси ва муддати", jd), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntryWithFile("Йўналиш номи, харитаси ва паспорти", jd, "Йўналиш номи"), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(fileDataEntry("Хитой завод хужжатлари", jd, ""), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntry("Двигател мойи муддати"), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntryWithFile("Умумий ҳолати", jd, "Умумий ҳолати"), gbc);

        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntryWithFile("Ходовой холати ва ремонти", jd, "Ходовой ҳолати"), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntryWithFile("Балонлар", jd, "Балон ҳолати"), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntryWithFile("Салон", jd, "Салон ҳолати"), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntryWithFile("Богажник", jd, "Богажник ҳолати"), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntryWithFile("Боковой ойналар", jd, "Боковой ойна ҳолати"), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntryWithFile("Лобовой ойна", jd, "Лобовой ойна ҳолати"), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntryWithFile("Суғурта Полиси ", jd), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntryWithFile("Кузов", jd, "Кузов ҳолати"), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntryWithFile("Газ балон муддати", jd), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(dateDataEntryWithFile("Ижара шартнома муддати", jd), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntryWithFile("Тахогров хужжати ва муддати", jd), gbc);

        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.gridx = 0;
        gbc.gridy = 13;
        dataEntryContainer.add(button("Бекор қилиш", "cancel", jd, dataEntryContainer, connection), gbc);
        gbc.gridx = 1;
        gbc.gridy = 13;
        dataEntryContainer.add(button("Сақлаш", "save", jd, dataEntryContainer, connection), gbc);

        return dataEntryContainer;
    }

    public static JPanel fileDataEntry(String dataLabel, JDialog jd, String fileFormat) {
        Color white = Color.white;

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JPanel textField = attachFile(jd, fileFormat);

        container.add(label, BorderLayout.PAGE_START);
        container.add(textField, BorderLayout.CENTER);

        return container;
    }

    public static JPanel dateDataEntry(String dataLabel) {
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

    public static JPanel stringDataEntry(String dataLabel, Connection connection) {

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JTextField textField = textField(new Dimension(320, 30));
        textField.setBackground(Color.decode("#f5f5f5"));
        textField.setBorder(null);

        container.add(label, BorderLayout.PAGE_START);
        container.add(textField, BorderLayout.CENTER);

        if (dataLabel.equals("Давлат рақами")) {
            checkForCarWhenAdding(textField, connection);
        }

        return container;
    }

    public static JPanel stringDataEntryWithFile(String dataLabel, JDialog jd, String popUpText) {

        JPanel container = new JPanel(new BorderLayout(0, 4));
        container.setBackground(Color.white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JTextField textField = textField(new Dimension(320, 30));
        textField.setBackground(Color.decode("#f5f5f5"));
        textField.addMouseListener(MAForTextField(textField, popUpText));

        container.add(label, BorderLayout.PAGE_START);
        container.add(textField, BorderLayout.CENTER);

        JPanel af = attachFile(jd, "");
        container.add(af, BorderLayout.PAGE_END);

        return container;
    }

    public static JPanel dateDataEntryWithFile(String dataLabel, JDialog jd) {
        JPanel container = new JPanel(new BorderLayout(4, 0));
        container.setBackground(Color.white);

        JLabel label = new JLabel(dataLabel);
        label.setFont(new Font(null, Font.PLAIN, 18));

        JDateChooser chooser = dateChooser();
        chooser.setPreferredSize(new Dimension(120, 30));

        container.add(label, BorderLayout.PAGE_START);

        JPanel af = attachFile(jd, "");
        af.setPreferredSize(new Dimension(200, 30));
        container.add(af, BorderLayout.CENTER);
        container.add(chooser, BorderLayout.LINE_END);

        return container;
    }

    private static JButton button(String label, String name, JDialog jd, JPanel dataEntryContainer, Connection connection) {
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
            button.setEnabled(false);
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
        });

        button.addActionListener((ActionEvent e) -> {
            if (name.equals("cancel")) {
                jd.dispose();
            } else {
                jd.dispose();
                new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        Map carMap = saveCar(dataEntryContainer, connection);
                        addCarToUI(carMap, jd, connection);
                        return 42;
                    }
                }.execute();
            }
        });

        return button;
    }

    private static void addCarToUI(Map carMap, JDialog jd, Connection connection) {
        JFrame FRAME = (JFrame) SwingUtilities.getRoot(jd.getParent());
        JPanel BODY = (JPanel) FRAME.getContentPane().getComponent(1);

        JScrollPane scrollPane = (JScrollPane) BODY.getComponent(1);
        JPanel panelForScrollPane = (JPanel) scrollPane.getViewport().getComponent(0);
        JPanel CARS = (JPanel) panelForScrollPane.getComponent(0);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenSize.getWidth() * 0.05);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, w, 0, 0);
        gbc.weightx = 0.5;

        gbc.gridx = 0;
        gbc.gridy = -1;

        CARS.add(CAR(carMap, String.valueOf(CARS.getComponentCount() + 1), "arrowright", connection, getFontSize(connection)), gbc);
        CARS.revalidate();
    }

    private static Map saveCar(JPanel dataEntryContainer, Connection connection) {
        String uid = "A" + UUID.randomUUID().toString().toUpperCase();
        String ID = uid.replace("-", "").substring(0, 32);
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String startDate = df.format(now.getTime());

        String NUM = getStringDataEntryContent(dataEntryContainer, 1, 15);

        String BRAND = getStringDataEntryContent(dataEntryContainer, 2, 25);
        String RESP1 = getStringDataEntryContent(dataEntryContainer, 3, 40);
        String RESP2 = getStringDataEntryContent(dataEntryContainer, 4, 40);
        String COMPANY = getStringDataEntryContent(dataEntryContainer, 5, 100);

        String TEXKFILE = getFileDataEntryContent(dataEntryContainer, 6, 1);
        String TEXTKEXP = getDateEntryContent(dataEntryContainer, 6, 2);

        String ATNUM = getStringDataEntryContent(dataEntryContainer, 7, 50);
        String ATEXP = getDateEntryContent(dataEntryContainer, 7, 2);
        String ATFILE = getFileDataEntryContent(dataEntryContainer, 7, 3);

        String POSAJIRSFILE = getFileDataEntryContent(dataEntryContainer, 8, 1);
        String POSAJIRSEXP = getDateEntryContent(dataEntryContainer, 8, 2);

        String GPSFILE = getFileDataEntryContent(dataEntryContainer, 9, 1);
        String GPSEXP = getDateEntryContent(dataEntryContainer, 9, 2);

        String YUNALISH = getStringDataEntryContent(dataEntryContainer, 10, 50);
        String YUNALISHFILE = getFileDataEntryContent(dataEntryContainer, 10, 2);

        String XITOYFILE = getFileDataEntryContent(dataEntryContainer, 11, 1);

        String ENGINEEXP = getDateEntryContent(dataEntryContainer, 12, 1);

        String UX = getStringDataEntryContent(dataEntryContainer, 13, 20);
        String UXFILE = getFileDataEntryContent(dataEntryContainer, 13, 2);

        String XODOVOY = getStringDataEntryContent(dataEntryContainer, 14, 20);
        String XODOVOYFILE = getFileDataEntryContent(dataEntryContainer, 14, 2);

        String BALON = getStringDataEntryContent(dataEntryContainer, 15, 20);
        String BALONFILE = getFileDataEntryContent(dataEntryContainer, 15, 2);

        String SALON = getStringDataEntryContent(dataEntryContainer, 16, 20);
        String SALONFILE = getFileDataEntryContent(dataEntryContainer, 16, 2);

        String BOGAJ = getStringDataEntryContent(dataEntryContainer, 17, 20);
        String BOGAJFILE = getFileDataEntryContent(dataEntryContainer, 17, 2);

        String BOKOVOY = getStringDataEntryContent(dataEntryContainer, 18, 20);
        String BOKOVOYFILE = getFileDataEntryContent(dataEntryContainer, 18, 2);

        String LOBOVOY = getStringDataEntryContent(dataEntryContainer, 19, 20);
        String LOBOVOYFILE = getFileDataEntryContent(dataEntryContainer, 19, 2);

        String SPFILE = getFileDataEntryContent(dataEntryContainer, 20, 1);
        String SPEXP = getDateEntryContent(dataEntryContainer, 20, 2);

        String KUZOV = getStringDataEntryContent(dataEntryContainer, 21, 20);
        String KUZOVFILE = getFileDataEntryContent(dataEntryContainer, 21, 2);

        String GAZFILE = getFileDataEntryContent(dataEntryContainer, 22, 1);
        String GAZEXP = getDateEntryContent(dataEntryContainer, 22, 2);

        String IJARASHFILE = getFileDataEntryContent(dataEntryContainer, 23, 1);
        String IJARAEXP = getDateEntryContent(dataEntryContainer, 23, 2);
        
        String TAXOGROVFILE = getFileDataEntryContent(dataEntryContainer, 24, 1);
        String TAXOGROVEXP = getDateEntryContent(dataEntryContainer, 24, 2);

        Map carMap = new HashMap();
        carMap.put("ID", ID);
        carMap.put("NUM", NUM);
        carMap.put("BRAND", BRAND);
        carMap.put("RESP1", RESP1);
        carMap.put("RESP2", RESP2);
        carMap.put("COMPANY", COMPANY);
        carMap.put("TEXKFILE", moveFile(TEXKFILE, ID, "CARS", "TEXKFILE"));
        carMap.put("TEXKEXP", TEXTKEXP);
        carMap.put("TEXKSTART", startDate);
        carMap.put("ATNUM", ATNUM);
        carMap.put("ATEXP", ATEXP);
        carMap.put("ATSTART", startDate);
        carMap.put("ATFILE", moveFile(ATFILE, ID, "CARS", "ATFILE"));

        carMap.put("POSAJIRSFILE", moveFile(POSAJIRSFILE, ID, "CARS", "POSAJIRSFILE"));
        carMap.put("POSAJIRSEXP", POSAJIRSEXP);
        carMap.put("POSAJIRSSTART", startDate);

        carMap.put("GPSFILE", moveFile(GPSFILE, ID, "CARS", "GPSFILE"));
        carMap.put("GPSEXP", GPSEXP);
        carMap.put("GPSSTART", startDate);

        carMap.put("YUNALISH", YUNALISH);
        carMap.put("YUNALISHFILE", moveFile(YUNALISHFILE, ID, "CARS", "YUNALISHFILE"));

        carMap.put("XITOYFILE", moveFile(XITOYFILE, ID, "CARS", "XITOYFILE"));
        carMap.put("ENGINEEXP", ENGINEEXP);
        carMap.put("ENGINESTART", startDate);

        carMap.put("UX", UX);
        carMap.put("UXFILE", moveFile(UXFILE, ID, "CARS", "UXFILE"));

        carMap.put("XODOVOY", XODOVOY);
        carMap.put("XODOVOYFILE", moveFile(XODOVOYFILE, ID, "CARS", "XODOVOYFILE"));

        carMap.put("BALON", BALON);
        carMap.put("BALONFILE", moveFile(BALONFILE, ID, "CARS", "BALONFILE"));

        carMap.put("SALON", SALON);
        carMap.put("SALONFILE", moveFile(SALONFILE, ID, "CARS", "SALONFILE"));

        carMap.put("BOGAJ", BOGAJ);
        carMap.put("BOGAJFILE", moveFile(BOGAJFILE, ID, "CARS", "BOGAJFILE"));

        carMap.put("BOKOVOY", BOKOVOY);
        carMap.put("BOKOVOYFILE", moveFile(BOKOVOYFILE, ID, "CARS", "BOKOVOYFILE"));

        carMap.put("LOBOVOY", LOBOVOY);
        carMap.put("LOBOVOYFILE", moveFile(LOBOVOYFILE, ID, "CARS", "LOBOVOYFILE"));

        carMap.put("SPEXP", SPEXP);
        carMap.put("SPSTART", startDate);
        carMap.put("SPFILE", moveFile(SPFILE, ID, "CARS", "SPFILE"));

        carMap.put("KUZOV", KUZOV);
        carMap.put("KUZOVFILE", moveFile(KUZOVFILE, ID, "CARS", "KUZOVFILE"));

        carMap.put("GAZEXP", GAZEXP);
        carMap.put("GAZSTART", startDate);
        carMap.put("GAZFILE", moveFile(GAZFILE, ID, "CARS", "GAZFILE"));

        carMap.put("IJARASHEXP", IJARAEXP);
        carMap.put("IJARASHFILE", moveFile(IJARASHFILE, ID, "CARS", "IJARASHFILE"));
        carMap.put("IJARASHSTART", startDate);
        
        carMap.put("TAXOGROVEXP", TAXOGROVEXP);
        carMap.put("TAXOGROVFILE", moveFile(TAXOGROVFILE, ID, "CARS", "TAXOGROVFILE"));
        carMap.put("TAXOGROVSTART", startDate);
        

        insert("CARS", carMap, connection);

//        createTable(ID);
        return carMap;
    }

    public static String getDateEntryContent(JPanel dataEntryContainer, int index, int index2) {
        JPanel panel = (JPanel) dataEntryContainer.getComponent(index);
        JDateChooser chooser = (JDateChooser) panel.getComponent(index2);
        String EXP = ((JTextFieldDateEditor) chooser.getDateEditor()).getText().replaceAll("-", ".");

        return EXP;
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

    public static JPanel litsenziyaATDataEntry(JDialog jd) {

        JPanel container = new JPanel(new BorderLayout(4, 4));
        container.setBackground(Color.white);

        JLabel label = new JLabel("Лицензия АТ");
        label.setFont(new Font(null, Font.PLAIN, 18));

        JTextField litsenziyaATTextField = textField(new Dimension(200, 30));
        litsenziyaATTextField.setBackground(Color.decode("#f5f5f5"));
        litsenziyaATTextField.addMouseListener(MAForTextField(litsenziyaATTextField, "№"));

        container.add(label, BorderLayout.PAGE_START);
        container.add(litsenziyaATTextField, BorderLayout.CENTER);

        container.add(dateChooser(), BorderLayout.LINE_END);
        container.add(attachFile(jd, ""), BorderLayout.PAGE_END);

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

    private static void checkForCarWhenAdding(JTextField tf, Connection connection) {

        tf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String NUM = tf.getText();
                NUM = NUM.replaceAll(" ", "").toUpperCase();

                JPanel dataEntryContainer = (JPanel) tf.getParent().getParent();
                JButton saveButton = (JButton) dataEntryContainer.getComponent(26);
                if (NUM.isBlank()) {
                    saveButton.setEnabled(false);
                } else {
                    saveButton.setEnabled(true);

                    if (NUM.length() > 15) {
                        NUM = NUM.substring(0, 15);
                    }
                    tf.setText(NUM);
                    ArrayList CARlist = getData("SELECT NUM FROM CARS WHERE NUM=" + "'" + NUM + "'", connection);

                    if (CARlist.isEmpty()) {
                        saveButton.setEnabled(true);
                    } else {
                        saveButton.setEnabled(false);
                    }

                }

            }
        });

    }

}
