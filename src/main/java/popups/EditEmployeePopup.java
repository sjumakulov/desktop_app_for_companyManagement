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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import static popups.EditCarPopup.dateDataEntryWithFile;
import static popups.EditCarPopup.fileDataEntry;
import static popups.EditCarPopup.stringDataEntry;
import static popups.EditCarPopup.stringDataEntryWithFile;
import static popups.AddEmployeePopup.MAForTextField;
import static popups.AddEmployeePopup.getFileDataEntryContent;
import static popups.AddEmployeePopup.getStringDataEntryContent;
import static popups.AddEmployeePopup.labelForAddPopup;
import static popups.AddEmployeePopup.setBorderRed;
import static popups.AddEmployeePopup.subString;
import static popups.AddEmployeePopup.textField;
import static popups.EditCarPopup.moveFile2;
import static popups.webCamPopup.showWebCamPopup;
import static yutong.BODY.EMPLOYEE.EMPLOYEE;
import static yutong.BODY.EMPLOYEE.penaltyPanel;
import static yutong.tools.DBManager.getData;
import static yutong.tools.DBManager.getFontSize;
import static yutong.tools.DBManager.update;
import static yutong.tools.DBManager.valueOf;
import static yutong.tools.FileChooser.showFileChooser;
import yutong.tools.Item;
import static yutong.tools.MyScrollBarUI.myScrollBarUI;

public class EditEmployeePopup {

    public static void showEditEmployeePopup(JFrame FRAME, String ID, Connection connection) {
        GlassPane glassPane = new GlassPane();
        FRAME.getRootPane().setGlassPane(glassPane);

        glassPane.setVisible(true);
        personDialogPopup(FRAME, ID, connection);
        glassPane.setVisible(false);
    }

    public static JDialog personDialogPopup(JFrame FRAME, String ID, Connection connection) {
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

        ArrayList employeeList = getData("SELECT * FROM EMPLOYEES WHERE ID=" + "'" + ID + "'", connection);
        Map employeeMap = (Map) employeeList.get(0);

        JPanel dataEntryContainer = new JPanel(new GridBagLayout());
        dataEntryContainer.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 30, 15, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = -1;

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        dataEntryContainer.add(labelForAddPopup("Ўзгартириш"), gbc);

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        dataEntryContainer.add(stringDataEntry("Хайдовчи Ф.И.О", valueOf(employeeMap, "ISM"), connection), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(imageEntry(jd, valueOf(employeeMap, "RASM")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(driversLicenseDataEntry(jd, valueOf(employeeMap, "HGFILE"), valueOf(employeeMap, "HGNUMBER"), valueOf(employeeMap, "HGTOIFA")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(departmentChooser(valueOf(employeeMap, "DEPARTMENT")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(fileDataEntry("Паспорт копия", jd, "", valueOf(employeeMap, "PASPORT")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(fileDataEntry("ИНПС китоби", jd, "", valueOf(employeeMap, "INPS")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntryWithFile("ИНН", jd, "ИНН рақами", valueOf(employeeMap, "INNNUMBER"), valueOf(employeeMap, "INNFILE")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(fileDataEntry("Таржимаи ҳол", jd, "", valueOf(employeeMap, "TARJIMAIHOL")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(fileDataEntry("Ариза", jd, "", valueOf(employeeMap, "ARIZA")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(fileDataEntry("Сертификатлар", jd, "", valueOf(employeeMap, "SERTIFIKAT")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(fileDataEntry("Маълумотнома", jd, "", valueOf(employeeMap, "MALUMOTNOMA")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(fileDataEntry("Суровнома", jd, "", valueOf(employeeMap, "SUROVNOMA")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(dateDataEntryWithFile("Меҳнат шартнома муддати", jd, valueOf(employeeMap, "MEHNATSHEXP"), valueOf(employeeMap, "MEHNATSHFILE")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(dateDataEntryWithFile("Тиббий кўрик муддати", jd, valueOf(employeeMap, "MEDEXP"), valueOf(employeeMap, "MEDMALUMOTNOMA")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(fileDataEntry("Ишга олинганлиги ҳақидаги приказ", jd, "", valueOf(employeeMap, "HIREDP")), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(fileDataEntry("Ишдан олинганлиги ҳақидаги приказ", jd, "", valueOf(employeeMap, "FIREDP")), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntry("Манзил", valueOf(employeeMap, "MANZIL"), connection), gbc);
        gbc.gridx = 1;
        dataEntryContainer.add(stringDataEntry("Шахсий тел. рақам", valueOf(employeeMap, "PPHONE"), connection), gbc);

        gbc.gridx = 0;
        dataEntryContainer.add(stringDataEntry("Оилавий тел. рақам", valueOf(employeeMap, "FPHONE"), connection), gbc);

        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.gridx = 0;
        gbc.gridy = 11;
        dataEntryContainer.add(button("Бекор қилиш", "cancel", jd, dataEntryContainer, employeeMap, connection), gbc);
        gbc.gridx = 1;
        gbc.gridy = 11;
        dataEntryContainer.add(button("Сақлаш", "save", jd, dataEntryContainer, employeeMap, connection), gbc);

        return dataEntryContainer;
    }

    public static JPanel imageEntry(JDialog jd, String path) {
        JLabel label = new JLabel("Расм");
        label.setFont(new Font(null, Font.PLAIN, 18));

        JPanel af = attachFile(jd, "jpg", path);

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

    public static JPanel attachFile(JDialog jd, String fileFormat, String path) {
        ImageIcon icon = new ImageIcon(new ImageIcon("./resources/clip.png").getImage().getScaledInstance(18, -1, Image.SCALE_SMOOTH));

        JTextField tf = new JTextField();
        tf.setEditable(false);
        tf.setBorder(null);
        tf.setOpaque(false);
        tf.setPreferredSize(new Dimension(300, 20));
        tf.setText(path);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconLabel.setToolTipText("Файлни танлаш");
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

    public static JPanel driversLicenseDataEntry(JDialog jd, String path, String num, String category) {

        JPanel container = new JPanel(new BorderLayout(4, 4));
        container.setBackground(Color.white);

        JLabel label = new JLabel("Ҳайдовчилик Гувоҳномаси");
        label.setFont(new Font(null, Font.PLAIN, 18));

        Color gray = Color.decode("#f5f5f5");
        JTextField licenseNumTextField = textField(new Dimension(220, 30));
        licenseNumTextField.addMouseListener(MAForTextField(licenseNumTextField, "серияси ва рақами"));
        licenseNumTextField.setBackground(gray);
        licenseNumTextField.setText(num);

        JTextField categoryTextField = textField(new Dimension(100, 30));
        categoryTextField.addMouseListener(MAForTextField(categoryTextField, "тоифаси"));
        categoryTextField.setBackground(gray);
        categoryTextField.setText(category);

        container.add(label, BorderLayout.PAGE_START);
        container.add(licenseNumTextField, BorderLayout.CENTER);
        container.add(categoryTextField, BorderLayout.LINE_END);
        container.add(attachFile(jd, "", path), BorderLayout.PAGE_END);

        return container;
    }

    public static JPanel departmentChooser(String value) {
        ArrayList<Item> model = new ArrayList();

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

        for (int i = 0; i < 6; i++) {
            Item item = (Item) model.get(i);
            if (item.getId().equals(value)) {
                comboBox.setSelectedItem(item);
                break;
            }
        }

        container.add(label, BorderLayout.PAGE_START);
        container.add(comboBox, BorderLayout.CENTER);

        return container;
    }

    private static JButton button(String label, String name, JDialog jd, JPanel dataEntryContainer, Map oldEmployeeMap, Connection connection) {
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
                                Map newEmployeeMap = saveEmployee(dataEntryContainer, oldEmployeeMap, connection);

                                editEmployeeInUI(newEmployeeMap, oldEmployeeMap, valueOf(oldEmployeeMap, "ID"), jd, connection);
                                return 42;
                            }
                        }.execute();
                    }
                }
            }
        });

        return button;
    }

    private static void editEmployeeInUI(Map employeeMap, Map oldEmployeeMap, String ID, JDialog jd, Connection connection) {
        int fontSize = getFontSize(connection);

        JFrame FRAME = (JFrame) SwingUtilities.getRoot(jd.getParent());
        JPanel BODY = (JPanel) FRAME.getContentPane().getComponent(1);

        JScrollPane scrollPane = (JScrollPane) BODY.getComponent(0);
        JPanel panelForScrollPane = (JPanel) scrollPane.getViewport().getComponent(0);
        JPanel EMPLOYEES = (JPanel) panelForScrollPane.getComponent(0);

        int oldDepartmentIndex = departmentIndex(oldEmployeeMap);
        int newDepartmentIndex = departmentIndex(employeeMap);

        System.out.println(oldDepartmentIndex);
        System.out.println(newDepartmentIndex);

        JPanel oldDEPARTMENT = (JPanel) EMPLOYEES.getComponent(oldDepartmentIndex);

        JPanel oldDepartmentBody = (JPanel) oldDEPARTMENT.getComponent(1);
        for (int i = 0; i < oldDepartmentBody.getComponentCount(); i++) {
            JPanel cuurentEMPLOYEE = (JPanel) oldDepartmentBody.getComponent(i);

            if (cuurentEMPLOYEE.getComponent(2).getName().equals(ID)) {

                if (oldDepartmentIndex == newDepartmentIndex) {
                    oldDepartmentBody.remove(cuurentEMPLOYEE);

                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.insets = new Insets(5, 15, 0, 0);
                    gbc.weightx = 0.5;
                    gbc.anchor = GridBagConstraints.WEST;
                    gbc.gridx = 0;
                    gbc.gridy = -1;

                    JPanel newEMPLOYEE = EMPLOYEE(employeeMap, String.valueOf(i + 1), "arrowdown", connection, fontSize);
                    JPanel newEmployeeBody = (JPanel) newEMPLOYEE.getComponent(2);
                    newEmployeeBody.setVisible(true);

                    oldDepartmentBody.add(newEMPLOYEE, gbc, i);

                    gbc.fill = GridBagConstraints.HORIZONTAL;
                    gbc.insets = new Insets(5, 0, 5, 10);

                    gbc.gridwidth = 4;
                    gbc.gridy = 5;
                    gbc.gridx = 1;
                    JPanel dataPanel = (JPanel) newEmployeeBody.getComponent(0);
                    dataPanel.add(penaltyPanel(ID, connection, fontSize), gbc);

                    oldDepartmentBody.revalidate();
                    oldDepartmentBody.repaint();

                } else {
                    oldDepartmentBody.remove(cuurentEMPLOYEE);
                    for (int x = 0; x < oldDepartmentBody.getComponentCount(); x++) {
                        JPanel currentEMPLOYEE = (JPanel) oldDepartmentBody.getComponent(x);
                        ((JLabel) currentEMPLOYEE.getComponent(0)).setText(String.valueOf(x + 1));
                    }

                    oldDepartmentBody.revalidate();
                    oldDepartmentBody.repaint();

                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.insets = new Insets(5, 15, 0, 0);
                    gbc.weightx = 0.5;
                    gbc.anchor = GridBagConstraints.WEST;
                    gbc.gridx = 0;
                    gbc.gridy = -1;

                    JPanel newDEPARTMENT = (JPanel) EMPLOYEES.getComponent(newDepartmentIndex);

                    JPanel newDepartmentBody = (JPanel) newDEPARTMENT.getComponent(1);
                    newDepartmentBody.add(EMPLOYEE(employeeMap, String.valueOf(newDepartmentBody.getComponentCount() + 1), "arrowright", connection, fontSize), gbc);
                    newDepartmentBody.revalidate();
                    newDepartmentBody.repaint();
                }

                break;

            }
        }

    }

    private static int departmentIndex(Map map) {
        int index = 0;

        switch (valueOf(map, "DEPARTMENT")) {
            case "haydovchi" -> {
                index = 0;
                break;
            }
            case "dispetcher" -> {
                index = 1;
                break;
            }
            case "bazaishchisi" -> {
                index = 2;
                break;
            }
            case "boshqaruvchi" -> {
                index = 3;
                break;
            }
            case "prikazichiqmagan" -> {
                index = 4;
                break;
            }
            case "ishdanolingan" -> {
                index = 5;
                break;
            }
        }
        return index;
    }

    private static Map saveEmployee(JPanel dataEntryContainer, Map oldEmployeeMap, Connection connection) {
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
        employeeMap.put("ID", valueOf(oldEmployeeMap, "ID"));
        employeeMap.put("ISM", ISM);
        employeeMap.put("RASM", RASM);
        employeeMap.put("HGFILE", HGFILE);
        employeeMap.put("HGNUMBER", HGNUMBER);
        employeeMap.put("HGTOIFA", HGTOIFA);
        employeeMap.put("DEPARTMENT", DEPARTMENT);
        employeeMap.put("PASPORT", PASPORT);
        employeeMap.put("INPS", INPS);
        employeeMap.put("INNFILE", INNFILE);
        employeeMap.put("INNNUMBER", INNNUMBER);
        employeeMap.put("TARJIMAIHOL", TARJIMAIHOL);
        employeeMap.put("ARIZA", ARIZA);
        employeeMap.put("SERTIFIKAT", SERTIFIKAT);
        employeeMap.put("MALUMOTNOMA", MALUMOTNOMA);
        employeeMap.put("SUROVNOMA", SUROVNOMA);
        employeeMap.put("MEHNATSHFILE", MEHNATSHFILE);
        employeeMap.put("MEHNATSHEXP", MEHNATSHEXP);
        employeeMap.put("MEHNATSHSTART", valueOf(oldEmployeeMap, "MEHNATSHSTART"));
        employeeMap.put("MEDEXP", MEDEXP);
        employeeMap.put("MEDSTART", valueOf(oldEmployeeMap, "MEDSTART"));
        employeeMap.put("MEDMALUMOTNOMA", MEDMALUMOTNOMA);
        employeeMap.put("HIREDP", HIREDP);
        employeeMap.put("FIREDP", FIREDP);
        employeeMap.put("MANZIL", MANZIL);
        employeeMap.put("PPHONE", PPHONE);
        employeeMap.put("FPHONE", FPHONE);

        Map editEmployeeMap = new HashMap();
        for (Object k : oldEmployeeMap.keySet()) {
            if (k.equals("MEHNATSHEXP") || k.equals("MEDEXP")) {
                Calendar now = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                String startDate = df.format(now.getTime());

                String startDateKey = k.toString().replace("EXP", "START");
                if (!employeeMap.get(k).equals(oldEmployeeMap.get(k))) {
                    editEmployeeMap.put(k, employeeMap.get(k));
                    editEmployeeMap.put(startDateKey, startDate);

                    employeeMap.put(startDateKey, startDate);
                }
            } else {
                if (!employeeMap.get(k).equals(oldEmployeeMap.get(k))) {
                    switch (k.toString()) {
                        case "RASM", "HGFILE", "PASPORT", "INPS", 
                                "INNFILE", "TARJIMAIHOL", "ARIZA", 
                                "SERTIFIKAT", "MALUMOTNOMA", "SUROVNOMA", 
                                "MEHNATSHFILE", "MEDMALUMOTNOMA", "HIREDP", "FIREDP" -> {

                            String path = moveFile2(valueOf(employeeMap, k.toString()), k.toString(), valueOf(oldEmployeeMap, "ID"), "EMPLOYEES", oldEmployeeMap);

                            editEmployeeMap.put(k, path);
                            employeeMap.put(k, path);

                            break;
                        }
                        default -> {
                            editEmployeeMap.put(k, employeeMap.get(k));
                        }
                    }
                }
            }
        }
        if (!editEmployeeMap.isEmpty()) {
            update("EMPLOYEES", editEmployeeMap, valueOf(oldEmployeeMap, "ID"), connection);

        }
        return employeeMap;

    }
}
