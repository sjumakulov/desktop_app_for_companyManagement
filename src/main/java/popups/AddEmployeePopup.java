package popups;

import com.github.sarxos.webcam.Webcam;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import static popups.AddCarPopup.dateDataEntryWithFile;
import static popups.AddCarPopup.fileDataEntry;
import static popups.AddCarPopup.stringDataEntry;
import static popups.AddCarPopup.stringDataEntryWithFile;
import static popups.webCamPopup.showWebCamPopup;
import static yutong.BODY.EMPLOYEE.EMPLOYEE;
import static yutong.tools.DBManager.createTable;
import static yutong.tools.DBManager.getFontSize;
import static yutong.tools.DBManager.insert;
import static yutong.tools.DBManager.moveFile;
import static yutong.tools.FileChooser.showFileChooser;
import yutong.tools.Item;
import static yutong.tools.MyScrollBarUI.myScrollBarUI;

public class AddEmployeePopup {

    public static void showEmployeePopup(JFrame FRAME, Connection connection) {
        GlassPane glassPane = new GlassPane();
        FRAME.getRootPane().setGlassPane(glassPane);

        glassPane.setVisible(true);
        personDialogPopup(FRAME, connection);
        glassPane.setVisible(false);
    }

    public static JDialog personDialogPopup(JFrame FRAME, Connection connection) {
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
        dataEntryContainer.add(labelForAddPopup("Маълумот қўшиш"), gbc);

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        dataEntryContainer.add(stringDataEntry("Хайдовчи Ф.И.О", connection), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(imageEntry(jd), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(driversLicenseDataEntry(jd), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(departmentChooser(), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(fileDataEntry("Паспорт копия", jd, ""), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(fileDataEntry("ИНПС китоби", jd, ""), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntryWithFile("ИНН", jd, "ИНН рақами"), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(fileDataEntry("Таржимаи ҳол", jd, ""), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(fileDataEntry("Ариза", jd, ""), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(fileDataEntry("Сертификатлар", jd, ""), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(fileDataEntry("Маълумотнома", jd, ""), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(fileDataEntry("Суровнома", jd, ""), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(dateDataEntryWithFile("Меҳнат шартнома муддати", jd), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntryWithFile("Тиббий кўрик муддати", jd), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(fileDataEntry("Ишга олинганлиги ҳақидаги приказ", jd, ""), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(fileDataEntry("Ишдан олинганлиги ҳақидаги приказ", jd, ""), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntry("Манзил", connection), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntry("Шахсий тел. рақам", connection), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntry("Оилавий тел. рақам", connection), gbc);

        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.gridx = 0;
        gbc.gridy = 11;
        dataEntryContainer.add(button("Бекор қилиш", "cancel", jd, dataEntryContainer, connection), gbc);
        gbc.gridx = 1;
        gbc.gridy = 11;
        dataEntryContainer.add(button("Сақлаш", "save", jd, dataEntryContainer, connection), gbc);

        return dataEntryContainer;
    }

    public static JPanel imageEntry(JDialog jd) {
        JLabel label = new JLabel("Расм");
        label.setFont(new Font(null, Font.PLAIN, 18));

        JPanel af = attachFile(jd, "jpg");

        JPanel container = new JPanel(new BorderLayout(10, 0));
        container.setOpaque(false);
        container.add(label, BorderLayout.PAGE_START);
        container.add(af, BorderLayout.CENTER);

        if (!Webcam.getWebcams().isEmpty()) {
            Webcam cam = Webcam.getDefault();
            ImageIcon icon = new ImageIcon(new ImageIcon("./resources/cam5.png").getImage().getScaledInstance(25, -1, Image.SCALE_SMOOTH));
            JLabel camLabel = new JLabel();
            camLabel.setIcon(icon);
            camLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            camLabel.setToolTipText("Камерани ёқиш");

            camLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFrame FRAME = (JFrame) SwingUtilities.getRoot(jd.getParent());
                    showWebCamPopup(FRAME, cam, (JTextField) af.getComponent(0));
                }
            });

            container.add(camLabel, BorderLayout.LINE_END);
        }

        return container;
    }

    public static JPanel driversLicenseDataEntry(JDialog jd) {

        JPanel container = new JPanel(new BorderLayout(4, 4));
        container.setBackground(Color.white);

        JLabel label = new JLabel("Ҳайдовчилик Гувоҳномаси");
        label.setFont(new Font(null, Font.PLAIN, 18));

        Color gray = Color.decode("#f5f5f5");
        JTextField licenseNumTextField = textField(new Dimension(220, 30));
        licenseNumTextField.addMouseListener(MAForTextField(licenseNumTextField, "серияси ва рақами"));
        licenseNumTextField.setBackground(gray);

        JTextField categoryTextField = textField(new Dimension(100, 30));
        categoryTextField.addMouseListener(MAForTextField(categoryTextField, "тоифаси"));
        categoryTextField.setBackground(gray);

        container.add(label, BorderLayout.PAGE_START);
        container.add(licenseNumTextField, BorderLayout.CENTER);
        container.add(categoryTextField, BorderLayout.LINE_END);
        container.add(attachFile(jd, ""), BorderLayout.PAGE_END);

        return container;
    }

    public static MouseAdapter MAForTextField(JTextField f, String s) {
        f.setFocusable(false);
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (f.getText().equals("")) {
                    f.setText(s);
                    f.setForeground(Color.gray);
                    f.setFocusable(false);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (f.getText().equals(s)) {
                    f.setText("");
                    f.setForeground(Color.gray);
                    f.setFocusable(false);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (f.getText().equals(s)) {
                    f.setText("");
                    f.setForeground(Color.black);
                    f.setFocusable(true);
                    f.requestFocus();
                }
            }

        };
    }

    public static JPanel attachFile(JDialog jd, String fileFormat) {
        ImageIcon icon = new ImageIcon(new ImageIcon("./resources/clip.png").getImage().getScaledInstance(18, -1, Image.SCALE_SMOOTH));

        JTextField tf = new JTextField();
        tf.setEditable(false);
        tf.setBorder(null);
        tf.setOpaque(false);
        tf.setPreferredSize(new Dimension(300, 20));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconLabel.setToolTipText("Файлни танланг");
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame FRAME = (JFrame) SwingUtilities.getRoot(jd.getParent());
                String filePath = showFileChooser(FRAME, fileFormat);
                tf.setText(filePath);
            }

        });

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(tf, BorderLayout.CENTER);
        wrapper.add(iconLabel, BorderLayout.LINE_END);
        wrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#bdbdbd")));

        return wrapper;
    }

    public static JTextField textField(Dimension d) {
        JTextField textField = new JTextField();
        textField.setPreferredSize(d);
        textField.setBorder(null);
        textField.setBackground(Color.decode("#e6e6e6"));
        textField.setFont(new Font(null, Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
                textField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        return textField;
    }

    public static JPanel departmentChooser() {
        ArrayList<Item> model = new ArrayList();

        model.add(new Item("", ""));
        model.add(new Item("haydovchi", "Ҳайдовчи"));
        model.add(new Item("dispetcher", "Диспетчер"));
        model.add(new Item("bazaishchisi", "База ишчиси"));
        model.add(new Item("boshqaruvchi", "Бошқарувчи ходим"));
        model.add(new Item("prikazichiqmagan", "Призаки чиқмаган"));
        model.add(new Item("ishdanolingan", "Ишдан олинган"));

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.white);

        JLabel label = new JLabel("Бўлимни танланг");
        label.setFont(new Font(null, Font.PLAIN, 18));

        JComboBox comboBox = new JComboBox(model.toArray());
        comboBox.setBackground(Color.white);
        comboBox.getComponent(0).setBackground(Color.white);
        comboBox.setFont(new Font(null, Font.PLAIN, 14));
        comboBox.setBorder(null);

        container.add(label, BorderLayout.PAGE_START);
        container.add(comboBox, BorderLayout.CENTER);

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

                    JComboBox comboBox = (JComboBox) ((JPanel) dataEntryContainer.getComponent(4)).getComponent(1);
                    Item selectedItem = (Item) comboBox.getSelectedItem();
                    String DEPARTMENT = selectedItem.getId();
                    if (DEPARTMENT.isBlank()) {
                        JScrollPane scrollPane = (JScrollPane) dataEntryContainer.getParent().getParent().getParent();
                        setBorderRed(scrollPane, dataEntryContainer, 4);
                    } else {
                        jd.dispose();
                        new SwingWorker() {
                            @Override
                            protected Object doInBackground() throws Exception {
                                Map employeeMap = saveEmployee(dataEntryContainer, connection);

                                addEmployeeToUI(employeeMap, jd, connection);
                                return 42;
                            }
                        }.execute();
                    }
                }
            }
        });

        return button;
    }

    private static void addEmployeeToUI(Map employeeMap, JDialog jd, Connection connection) {
        JFrame FRAME = (JFrame) SwingUtilities.getRoot(jd.getParent());
        JPanel BODY = (JPanel) FRAME.getContentPane().getComponent(1);

        JScrollPane scrollPane = (JScrollPane) BODY.getComponent(0);
        JPanel panelForScrollPane = (JPanel) scrollPane.getViewport().getComponent(0);
        JPanel EMPLOYEES = (JPanel) panelForScrollPane.getComponent(0);

        int departmentIndex = 0;
        

        switch (employeeMap.get("DEPARTMENT").toString()) {
            case "haydovchi" -> {
                departmentIndex = 0;
                break;
            }
            case "dispetcher" -> {
                departmentIndex = 1;
                break;
            }
            case "bazaishchisi" -> {
                departmentIndex = 2;
                break;
            }
            case "boshqaruvchi" -> {
                departmentIndex = 3;
                break;
            }
            case "prikazichiqmagan" -> {
                departmentIndex = 4;
                break;
            }
            case "ishdanolingan" -> {
                departmentIndex = 5;
                break;
            }
        }

        JPanel haydovchilarDEPARTMENT = (JPanel) EMPLOYEES.getComponent(departmentIndex);

        JPanel body = (JPanel) haydovchilarDEPARTMENT.getComponent(1);
        String index = String.valueOf(body.getComponentCount() + 1);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 0, 0);
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = -1;

        body.add(EMPLOYEE(employeeMap, index, "arrowright", connection, getFontSize(connection)), gbc);
        body.revalidate();
        body.repaint();
    }

    private static Map saveEmployee(JPanel dataEntryContainer, Connection connection) {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String startDate = df.format(now.getTime());

        String uid = "A" + UUID.randomUUID().toString().toUpperCase();
        String ID = uid.replace("-", "").substring(0, 32);

        String ISM = getStringDataEntryContent(dataEntryContainer, 1, 40);
        String RASM = getFileDataEntryContent(dataEntryContainer, 2, 1);

        JPanel driversLicenseContainer = (JPanel) dataEntryContainer.getComponent(3);
        String HGFILE = getFileDataEntryContent(dataEntryContainer, 3, 3);
        String HGNUMBER = subString(((JTextField) driversLicenseContainer.getComponent(1)).getText(), 15);
        String HGTOIFA = subString(((JTextField) driversLicenseContainer.getComponent(2)).getText(), 25);

        JComboBox comboBox = (JComboBox) ((JPanel) dataEntryContainer.getComponent(4)).getComponent(1);
        Item selectedItem = (Item) comboBox.getSelectedItem();
        String DEPARTMENT = selectedItem.getId();

        String PASPORT = getFileDataEntryContent(dataEntryContainer, 5, 1);
        String INPS = getFileDataEntryContent(dataEntryContainer, 6, 1);
        String INNNUMBER = getStringDataEntryContent(dataEntryContainer, 7, 50);
        String INNFILE = getFileDataEntryContent(dataEntryContainer, 7, 2);
        String TARJIMAIHOL = getFileDataEntryContent(dataEntryContainer, 8, 1);
        String ARIZA = getFileDataEntryContent(dataEntryContainer, 9, 1);
        String SERTIFIKAT = getFileDataEntryContent(dataEntryContainer, 10, 1);
        String MALUMOTNOMA = getFileDataEntryContent(dataEntryContainer, 11, 1);
        String SUROVNOMA = getFileDataEntryContent(dataEntryContainer, 12, 1);

        JPanel mehnatShPanel = (JPanel) dataEntryContainer.getComponent(13);
        JDateChooser mehnatShChooser = (JDateChooser) mehnatShPanel.getComponent(2);
        String MEHNATSHEXP = ((JTextFieldDateEditor) mehnatShChooser.getDateEditor()).getText().replaceAll("-", ".");
        String MEHNATSHFILE = getFileDataEntryContent(dataEntryContainer, 13, 1);

        JPanel tibbiyKurikPanel = (JPanel) dataEntryContainer.getComponent(14);
        JDateChooser tibbiyKurikPanelChooser = (JDateChooser) tibbiyKurikPanel.getComponent(2);
        String MEDEXP = ((JTextFieldDateEditor) tibbiyKurikPanelChooser.getDateEditor()).getText().replaceAll("-", ".");
        String MEDMALUMOTNOMA = getFileDataEntryContent(dataEntryContainer, 14, 1);

        String HIREDP = getFileDataEntryContent(dataEntryContainer, 15, 1);
        String FIREDP = getFileDataEntryContent(dataEntryContainer, 16, 1);

        String MANZIL = getStringDataEntryContent(dataEntryContainer, 17, 50);
        String PPHONE = getStringDataEntryContent(dataEntryContainer, 18, 60);
        String FPHONE = getStringDataEntryContent(dataEntryContainer, 19, 60);

        Map employeeMap = new HashMap();
        employeeMap.put("ID", ID);
        employeeMap.put("ISM", ISM);
        employeeMap.put("RASM", moveFile(RASM, ID, "EMPLOYEES", "RASM"));
        employeeMap.put("HGFILE", moveFile(HGFILE, ID, "EMPLOYEES", "HGFILE"));
        employeeMap.put("HGNUMBER", HGNUMBER);
        employeeMap.put("HGTOIFA", HGTOIFA);
        employeeMap.put("DEPARTMENT", DEPARTMENT);
        employeeMap.put("PASPORT", moveFile(PASPORT, ID, "EMPLOYEES", "PASPORT"));
        employeeMap.put("INPS", moveFile(INPS, ID, "EMPLOYEES", "INPS"));
        employeeMap.put("INNFILE", moveFile(INNFILE, ID, "EMPLOYEES", "INNFILE"));
        employeeMap.put("INNNUMBER", INNNUMBER);
        employeeMap.put("TARJIMAIHOL", moveFile(TARJIMAIHOL, ID, "EMPLOYEES", "TARJIMAIHOL"));
        employeeMap.put("ARIZA", moveFile(ARIZA, ID, "EMPLOYEES", "ARIZA"));
        employeeMap.put("SERTIFIKAT", moveFile(SERTIFIKAT, ID, "EMPLOYEES", "SERTIFIKAT"));
        employeeMap.put("MALUMOTNOMA", moveFile(MALUMOTNOMA, ID, "EMPLOYEES", "MALUMOTNOMA"));
        employeeMap.put("SUROVNOMA", moveFile(SUROVNOMA, ID, "EMPLOYEES", "SUROVNOMA"));
        employeeMap.put("MEHNATSHFILE", moveFile(MEHNATSHFILE, ID, "EMPLOYEES", "MEHNATSHFILE"));
        employeeMap.put("MEHNATSHEXP", MEHNATSHEXP);
        employeeMap.put("MEHNATSHSTART", startDate);
        employeeMap.put("MEDEXP", MEDEXP);
        employeeMap.put("MEDSTART", startDate);
        employeeMap.put("MEDMALUMOTNOMA", moveFile(MEDMALUMOTNOMA, ID, "EMPLOYEES", "MEDMALUMOTNOMA"));
        employeeMap.put("HIREDP", moveFile(HIREDP, ID, "EMPLOYEES", "HIREDP"));
        employeeMap.put("FIREDP", moveFile(FIREDP, ID, "EMPLOYEES", "FIREDP"));
        employeeMap.put("MANZIL", MANZIL);
        employeeMap.put("PPHONE", PPHONE);
        employeeMap.put("FPHONE", FPHONE);

        insert("EMPLOYEES", employeeMap, connection);
        createTable(ID, connection);

        return employeeMap;
    }

    public static String subString(String s, int numOfChars) {
        if (s.length() > numOfChars) {
            s = s.substring(0, numOfChars);
        }
        return s;
    }

    public static String getStringDataEntryContent(JPanel dataEntryContainer, int index, int numOfChars) {
        JPanel stringDataEntry = (JPanel) dataEntryContainer.getComponent(index);
        String text = ((JTextField) stringDataEntry.getComponent(1)).getText();

        if (text.length() > numOfChars) {
            text = text.substring(0, numOfChars);
        }

        return text;
    }

    public static String getFileDataEntryContent(JPanel dataEntryContainer, int index1, int index2) {
        JPanel container = (JPanel) dataEntryContainer.getComponent(index1);
        JPanel af = (JPanel) container.getComponent(index2);
        return ((JTextField) af.getComponent(0)).getText();
    }

    public static void setBorderRed(JScrollPane scrollPane, JPanel dataEntryContainer, int index) {
        Timer timer = new Timer();
        scrollPane.getViewport().setViewPosition(new Point(0, 0));
        JComponent comp = (JComponent) ((JComponent) dataEntryContainer.getComponent(index)).getComponent(1);

        if (comp.getBorder() == null) {
            comp.setBorder(new LineBorder(Color.red));
            comp.revalidate();
            comp.repaint();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    comp.setBorder(null);
                }
            }, 1000);

        }

    }

    public static JLabel labelForAddPopup(String text) {

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font(null, Font.BOLD, 25));

        return label;
    }

}
