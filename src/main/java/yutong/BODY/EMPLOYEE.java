package yutong.BODY;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ProgressBarUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import static yutong.BODY.mouseAdapters.MAForPenaltyMessage;
import static yutong.BODY.mouseAdapters.mouseAdapterForAddPenaltyButton;
import static yutong.BODY.mouseAdapters.mouseAdapterForDataCellWithFile;
import static yutong.BODY.mouseAdapters.mouseAdapterForEmployeeCollapsible;
import static yutong.BODY.mouseAdapters.mouseAdapterForEmployeeTools;
import static yutong.tools.DBManager.getData;
import static yutong.tools.DBManager.valueOf;
import yutong.tools.DateInput;
import static yutong.tools.MyScrollBarUI.myScrollBarUI;
import static yutong.tools.buttons.icon;
import static yutong.tools.otherFunctions.print;

public class EMPLOYEE {

    public static JPanel EMPLOYEE(Map employeeMap, String index, String iconName, Connection connection, int fontSize) {

        JPanel head = head(valueOf(employeeMap, "ISM"), valueOf(employeeMap, "ID"), iconName, connection, fontSize);

        JPanel body = body(employeeMap, connection, fontSize);

        JPanel EMPLOYEE = new JPanel(new GridBagLayout());
        EMPLOYEE.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;

        JLabel numLabel = new JLabel(index);
        numLabel.setForeground(Color.decode("#bdbdbd"));
        numLabel.setFont(new Font(null, Font.PLAIN, fontSize));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5);
        EMPLOYEE.add(numLabel, gbc);

        gbc.gridx = 1;
        EMPLOYEE.add(head, gbc);

        gbc.insets = new Insets(0, 9, 0, 0);
        gbc.gridy = 1;
        EMPLOYEE.add(body, gbc);

        return EMPLOYEE;
    }

    public static JPanel head(String employeeName, String ID, String iconName, Connection connection, int fontSize) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenSize.getWidth() * 0.18);

        JLabel line = new JLabel();
        line.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#bdbdbd")));

        JPanel lineContainer = new JPanel(new GridBagLayout());
        lineContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridy = 0;
        gbc.gridx = 0;
        lineContainer.add(line, gbc);

        JPanel head = new JPanel(new BorderLayout());
        head.setPreferredSize(new Dimension(w, 30));
        head.setOpaque(false);

        JLabel icon = icon(iconName, "Очиш", mouseAdapterForEmployeeCollapsible(head, ID, connection, fontSize), fontSize);
        icon.setText(employeeName + " ");
        icon.setFont(new Font(null, Font.BOLD, fontSize));
        icon.setForeground(Color.decode("#545454"));

        head.add(icon, BorderLayout.LINE_START);
        head.add(lineContainer, BorderLayout.CENTER);

        return head;
    }

    public static JPanel body(Map employeeMap, Connection connection, int fontSize) {
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.white);
        body.setVisible(false);
        body.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.decode("#dbdbdb")));
        body.setName(valueOf(employeeMap, "ID"));

        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 15, 10, 10);
        gbc.weightx = 0.5;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        dataPanel.add(image(valueOf(employeeMap, "RASM")), gbc);

        gbc.gridheight = 1;
        gbc.insets = new Insets(5, 5, 10, 60);
        gbc.gridx = 1;
        dataPanel.add(dataCell("Ф.И.О", valueOf(employeeMap, "ISM"), "", fontSize), gbc);

        gbc.gridx = 2;

        String HGValue = "";
        if (!valueOf(employeeMap, "HGNUMBER").isBlank()) {
            if (!valueOf(employeeMap, "HGTOIFA").isBlank()) {
                HGValue = valueOf(employeeMap, "HGNUMBER") + " | " + valueOf(employeeMap, "HGTOIFA");
            } else {
                HGValue = valueOf(employeeMap, "HGNUMBER");
            }
        } else {
            if (!valueOf(employeeMap, "HGTOIFA").isBlank()) {
                HGValue = valueOf(employeeMap, "HGTOIFA");
            }
        }

        dataPanel.add(dataCell("Ҳайдовчилик гувохномаси", HGValue, valueOf(employeeMap, "HGFILE"), fontSize), gbc);

        gbc.gridx = 3;
        dataPanel.add(dataCell("ИНН", valueOf(employeeMap, "INNNUMBER"), valueOf(employeeMap, "INNFILE"), fontSize), gbc);

        gbc.gridx = 4;
        dataPanel.add(dataCell("Манзил", valueOf(employeeMap, "MANZIL"), "", fontSize), gbc);

        gbc.gridy = 1;
        gbc.gridx = 1;
        dataPanel.add(dataCell("Сертификатлар", "", valueOf(employeeMap, "SERTIFIKAT"), fontSize), gbc);

        gbc.gridx = 2;
        dataPanel.add(dataCell("Маълумотнома", "", valueOf(employeeMap, "MALUMOTNOMA"), fontSize), gbc);

        gbc.gridx = 3;
        dataPanel.add(dataCell("Суровнома", "", valueOf(employeeMap, "SUROVNOMA"), fontSize), gbc);

        gbc.gridx = 4;
        dataPanel.add(dataCell("Шахсий тел. рақами", valueOf(employeeMap, "PPHONE"), "", fontSize), gbc);

        gbc.gridy = 2;
        gbc.gridx = 1;
        dataPanel.add(dataCell("Таржимаи ҳол", "", valueOf(employeeMap, "TARJIMAIHOL"), fontSize), gbc);

        gbc.gridx = 2;
        dataPanel.add(dataCell("ИНПС китоби", "", valueOf(employeeMap, "INPS"), fontSize), gbc);

        gbc.gridx = 3;
        dataPanel.add(dataCellProgress("Меҳнат шартнома муддати", valueOf(employeeMap, "MEHNATSHEXP"), valueOf(employeeMap, "MEHNATSHSTART"), valueOf(employeeMap, "MEHNATSHFILE"), fontSize), gbc);

        gbc.gridx = 4;
        dataPanel.add(dataCell("Оилавий тел. рақами", valueOf(employeeMap, "FPHONE"), "", fontSize), gbc);

        gbc.gridy = 3;
        gbc.gridx = 1;
        dataPanel.add(dataCell("Ариза", "", valueOf(employeeMap, "ARIZA"), fontSize), gbc);

        gbc.gridx = 2;
        dataPanel.add(dataCell("Паспорт копия", "", valueOf(employeeMap, "PASPORT"), fontSize), gbc);

        gbc.gridx = 3;
        dataPanel.add(dataCellProgress("Тиббий кўрик муддати", valueOf(employeeMap, "MEDEXP"), valueOf(employeeMap, "MEDSTART"), valueOf(employeeMap, "MEDMALUMOTNOMA"), fontSize), gbc);

        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        dataPanel.add(dataCell("Ишга олинганлиги ҳақидаги приказ", "", valueOf(employeeMap, "HIREDP"), fontSize), gbc);

        gbc.gridx = 3;
        dataPanel.add(dataCell("Ишдан олинганлиги ҳақидаги приказ", "", valueOf(employeeMap, "FIREDP"), fontSize), gbc);

        body.add(dataPanel, BorderLayout.CENTER);
        body.add(toolsPanel(valueOf(employeeMap, "ID"), valueOf(employeeMap, "DEPARTMENT"), connection, fontSize), BorderLayout.LINE_END);

        return body;
    }

    public static JPanel toolsPanel(String ID, String department, Connection connection, int fontSize) {

        JPanel toolsPanel = new JPanel(new GridLayout(1, 4, 15, 0));

        if (department.equals("haydovchi") || department.equals("haydovchiKZ")) {
            toolsPanel.add(icon("print", "Печатлаш", mouseAdapterForEmployeeTools(connection), fontSize));
        }

        toolsPanel.add(icon("pencil", "Ўзгартириш", mouseAdapterForEmployeeTools(connection), fontSize));
        toolsPanel.add(icon("download", "Хужжатларни юклаб олиш", mouseAdapterForEmployeeTools(connection), fontSize));
        toolsPanel.add(icon("bin", "Ўчириш", mouseAdapterForEmployeeTools(connection), fontSize));
        toolsPanel.setBackground(Color.white);
        toolsPanel.setName(ID);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.white);
        mainPanel.add(toolsPanel, BorderLayout.PAGE_START);

        return mainPanel;
    }

    public static JPanel dataCell(String label, String content, String pathToFile, int fontSize) {
        JLabel dataLabel = new JLabel(label);
        dataLabel.setForeground(Color.gray);
        dataLabel.setFont(new Font(null, Font.BOLD, fontSize));

        JLabel dataContent = new JLabel(content);
        dataContent.setFont(new Font(null, Font.BOLD, fontSize));

        switch (label) {
            case "Ф.И.О", "Шахсий тел. рақами", "Оилавий тел. рақами", "Манзил", "2 - масул шахс", "1 - масул шахс", "Фирма номи" -> {
                if (content.length() > 25) {
                    dataContent.setText(content.substring(0, 25) + "...");
                    dataContent.setToolTipText(content);
                }
            }
        }

        JPanel dataCell = new JPanel(new GridLayout(2, 1));
        dataCell.setBackground(Color.white);
        dataCell.add(dataLabel);
        dataCell.add(dataContent);

        File file = new File(pathToFile);
        if (file.exists()) {

            dataContent.setIcon(fileIcon(file, fontSize));

            dataCell.setToolTipText("Файлни очиш учун устига босинг");
            dataCell.setCursor(new Cursor(Cursor.HAND_CURSOR));
            dataCell.addMouseListener(mouseAdapterForDataCellWithFile());
            dataLabel.setName(pathToFile);
        }
        return dataCell;
    }

    public static ImageIcon fileIcon(File file, int fontSize) {
        String iconName = "file.png";
        String fileName = file.getName();

        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1).toLowerCase();
        }

        switch (extension) {
            case "doc", "docx" -> {
                iconName = "doc.png";
                break;
            }
            case "xlsx" -> {
                iconName = "excelFile.png";
                break;
            }
            case "pdf" -> {
                iconName = "pdf.png";
                break;
            }
            case "jpg" -> {
                iconName = "jpg.png";
                break;
            }
        }

        return new ImageIcon(new ImageIcon("./resources/" + iconName).getImage().getScaledInstance(fontSize + 3, -1, Image.SCALE_SMOOTH));

    }

    public static JPanel dataCellProgress(String label, String expireDate, String startDate, String pathToFile, int fontSize) {
        Calendar expireTime = Calendar.getInstance();
        Calendar startTime = Calendar.getInstance();

        JLabel dataLabel = new JLabel(label);
        dataLabel.setForeground(Color.gray);
        dataLabel.setFont(new Font(null, Font.BOLD, fontSize));

        JLabel dataContent = new JLabel(expireDate);
        dataContent.setFont(new Font(null, Font.BOLD, fontSize));

        JPanel dataCell = new JPanel(new BorderLayout());
        dataCell.setBackground(Color.white);
        dataCell.add(dataLabel, BorderLayout.PAGE_START);
        dataCell.add(dataContent, BorderLayout.CENTER);

        if (new File(pathToFile).exists()) {
            dataCell.setToolTipText("Файлни очиш учун устига босинг");
            dataCell.setCursor(new Cursor(Cursor.HAND_CURSOR));
            dataCell.addMouseListener(mouseAdapterForDataCellWithFile());
            dataLabel.setName(pathToFile);
        }

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try {
            if (!expireDate.isBlank()) {
                expireTime.setTime(df.parse(expireDate));
                startTime.setTime(df.parse(startDate));
                dataCell.add(progressBar(expireTime, startTime), BorderLayout.PAGE_END);
            }
        } catch (ParseException e) {
            print("error while parsing date in EMPLOYEE.java");
        }

        return dataCell;
    }

    private static JProgressBar bar(Calendar expireTime, Calendar startTime) {
        Calendar now = Calendar.getInstance();

        long totalTime = expireTime.getTimeInMillis() - startTime.getTimeInMillis();
        long timeLeft = expireTime.getTimeInMillis() - now.getTimeInMillis();

        int timeLeftInPercentage = 0;
        if (totalTime > 0) {
            timeLeftInPercentage = (int) (timeLeft * 100 / totalTime);
        }

        JProgressBar bar = new JProgressBar();

        bar.setValue(timeLeftInPercentage);
        bar.setPreferredSize(new Dimension(100, 2));
        bar.setBackground(Color.decode("#ebebeb"));
        bar.setForeground(Color.decode("#44cf71"));
        bar.setUI((ProgressBarUI) BasicProgressBarUI.createUI(bar));
        bar.setBorder(null);

        Timer timer = new Timer((int) (totalTime / 100), null);

        ActionListener actionListener = (ActionEvent e) -> {
            if (e.getSource() == timer) {
                bar.setValue(bar.getValue() - 1);
            }
        };

        timer.addActionListener(actionListener);
        timer.start();

        return bar;
    }

    public static JProgressBar progressBar(Calendar expireTime, Calendar startTime) {
        JProgressBar progressBar = null;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            progressBar = bar(expireTime, startTime);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException e) {
        }

        return progressBar;
    }

    public static JPanel image(String pathToImg) {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JLabel label = new JLabel();
        container.add(label);

        File file = new File(pathToImg);
        if (file.exists()) {
            ImageIcon img = new ImageIcon(pathToImg);
            img = new ImageIcon(img.getImage().getScaledInstance(90, -1, Image.SCALE_SMOOTH));

            label.setIcon(img);
            label.setName(pathToImg);

            container.setToolTipText("Расмни очиш учун устига босинг");
            container.addMouseListener(mouseAdapterForDataCellWithFile());
        } else {
            ImageIcon img = new ImageIcon("./resources/unknown.jpg");
            img = new ImageIcon(img.getImage().getScaledInstance(90, -1, Image.SCALE_SMOOTH));
            label.setIcon(img);
        }

        container.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return container;
    }

    public static JPanel penaltyPanel(String employeeID, Connection connection, int fontSize) {
        Calendar cal = Calendar.getInstance();
        String YIL = String.valueOf(cal.get(Calendar.YEAR));
        ArrayList penaltiesList = getData("SELECT * FROM " + employeeID + " WHERE YIL='" + YIL + "'", connection);

        JLabel label = new JLabel("Жарималари");
        label.setForeground(Color.white);
        label.setOpaque(false);
        label.setFont(new Font(null, Font.BOLD, fontSize));
        label.setBorder(new EmptyBorder(0, 5, 0, 0));

        JPanel messagesPanel = new JPanel(new GridBagLayout());
        messagesPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = -1;

        gbc.insets = new Insets(5, 10, 5, 15);
        for (Object p : penaltiesList) {
            Map penaltyMap = (Map) p;

            String DATE = valueOf(penaltyMap, "KUN") + "." + valueOf(penaltyMap, "OY") + "." + valueOf(penaltyMap, "YIL");
            messagesPanel.add(penaltyMessage(DATE, valueOf(penaltyMap, "TEXT"), valueOf(penaltyMap, "ID"), employeeID, connection, fontSize), gbc);
        }

        JPanel panelForScrollPane = new JPanel(new BorderLayout());
        panelForScrollPane.add(messagesPanel, BorderLayout.PAGE_END);
        panelForScrollPane.setBackground(Color.decode("#fffbf7"));

        JScrollPane pane = new JScrollPane(panelForScrollPane);
        pane.setPreferredSize(new Dimension(100, 200));
        pane.setBorder(null);
        pane.getVerticalScrollBar().setUI(myScrollBarUI());
        pane.getVerticalScrollBar().setUnitIncrement(26);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(Color.white);
        container.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.decode("#e8e8e8")));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 0.5;
        gbc.gridwidth = 3;
        gbc.gridx = 0;

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#db8000"));

        header.add(label, BorderLayout.LINE_START);
        header.add(new DateInput(employeeID, messagesPanel, connection, fontSize), BorderLayout.LINE_END);
        gbc.gridy = 0;
        container.add(header, gbc);

        gbc.gridy = 1;
        container.add(pane, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.9;
        container.add(inputField(fontSize), gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.01;
        gbc.anchor = GridBagConstraints.PAGE_END;
        container.add(addPenaltyButton(employeeID, messagesPanel, connection, fontSize), gbc);

        return container;
    }

    private static JButton addPenaltyButton(String employeeID, JPanel messagesPanel, Connection connection, int fontSize) {
        ImageIcon icon = new ImageIcon(new ImageIcon("./resources/plus.png").getImage().getScaledInstance(15, -1, Image.SCALE_SMOOTH));
        JButton ab = new JButton(icon);
        ab.setFocusable(false);
        ab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ab.setPreferredSize(new Dimension(40, 25));

        ab.addMouseListener(mouseAdapterForAddPenaltyButton(messagesPanel, employeeID, connection, fontSize));
        ab.setName("add");

        return ab;
    }

    public static JPanel penaltyMessage(String date, String message, String messageID, String employeeID, Connection connection, int fontSize) {
        Font f = new Font(null, Font.PLAIN, fontSize);

        JPanel pm = new JPanel(new GridBagLayout());
        pm.setOpaque(false);
        pm.setName(messageID);

        JTextArea dateText = new JTextArea(date);
        dateText.setEditable(false);
        dateText.setBackground(Color.decode("#def5ff"));
        dateText.setFont(f);
        dateText.setForeground(Color.black);

        JTextArea messageText = new JTextArea(message);
        messageText.setEditable(false);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setBorder(new EmptyBorder(3, 3, 3, 3));
        messageText.setFont(f);
        messageText.setForeground(Color.black);
        messageText.setBackground(Color.decode("#dcffdb"));
        messageText.setName("mt");
        messageText.addMouseListener(MAForPenaltyMessage(messageID, employeeID, null, connection));

        JLabel pencil = icon("pencil3", "Ўзгартириш", MAForPenaltyMessage(messageID, employeeID, pm, connection), fontSize);
        pencil.setVisible(false);

        JLabel bin = icon("bin2", "Ўчириш", MAForPenaltyMessage(messageID, employeeID, pm, connection), fontSize);
        bin.setVisible(false);

        JPanel toolsPanel = new JPanel(new BorderLayout(2, 0));
        toolsPanel.add(pencil, BorderLayout.LINE_START);
        toolsPanel.add(bin, BorderLayout.LINE_END);
        toolsPanel.setOpaque(false);
        toolsPanel.setPreferredSize(new Dimension(fontSize + 34, 25));
        toolsPanel.setName("tp");
        toolsPanel.addMouseListener(MAForPenaltyMessage(messageID, employeeID, null, connection));

        JPanel messageTextWrapper = new JPanel(new GridBagLayout());
        messageTextWrapper.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridy = 0;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        messageTextWrapper.add(messageText, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        messageTextWrapper.add(toolsPanel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.gridy = 0;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        pm.add(dateText, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 1;
        pm.add(messageTextWrapper, gbc);

        return pm;
    }

    private static JTextArea inputField(int fontSize) {
        JTextArea ta = new JTextArea();
        ta.setFont(new Font(null, Font.PLAIN, fontSize));
        ta.setLineWrap(true);
        ta.setText("Изоҳ...");

        ta.setForeground(Color.gray);
        ta.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                String t = ta.getText();
                if (t.equals("Изоҳ...") || t.equals("Бўш изоҳ мумкин эмас...")) {
                    ta.setText("");
                    ta.setForeground(Color.black);
                    ta.setFocusable(true);
                    ta.requestFocus();
                    ta.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
                }
            }
        });

        return ta;
    }

}
