package print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.text.NumberFormatter;
import static print.backPage.backPage;
import static print.frontPage.frontPage;
import static print.printFunction.printPage;
import popups.GlassPane;
import static yutong.tools.DBManager.getData;
import static yutong.tools.DBManager.update;

public class printPopup {

    public static void showPrintPopup(MouseEvent e, String monthIndex, String ID, Connection connection) {
        JComponent printIcon = (JComponent) e.getComponent();
        JFrame FRAME = (JFrame) SwingUtilities.getRoot(printIcon);

        GlassPane glassPane = new GlassPane();
        FRAME.getRootPane().setGlassPane(glassPane);

        glassPane.setVisible(true);
        jDialog(FRAME, monthIndex, ID, connection);
        glassPane.setVisible(false);

    }

    public static JDialog jDialog(JFrame FRAME, String monthIndex, String ID, Connection connection) {
        JDialog jd = new JDialog(FRAME);

        jd.setLayout(new BorderLayout());
        jd.setMinimumSize(new Dimension(1100, 580));
        jd.setModal(true);
        jd.setLocationRelativeTo(FRAME);
        jd.setUndecorated(true);
        jd.getContentPane().add(printDialog(jd, monthIndex, ID, connection), BorderLayout.CENTER);
        jd.setVisible(true);

        return jd;
    }

    public static JPanel printDialog(JDialog jd, String monthIndex, String ID, Connection connection) {
        JPanel printDialog = new JPanel(new GridBagLayout());
        printDialog.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.weightx = 0.5;

        gbc.gridx = 0;
        gbc.gridy = 0;

        if (ID.isBlank()) {
            printDialog.add(backPage(monthIndex), gbc);
        } else {
            printDialog.add(frontPage(monthIndex, ID, connection), gbc);
        }

        gbc.gridx = 1;
        gbc.gridy = 0;

        printDialog.add(controller(jd, connection, ID), gbc);

        return printDialog;
    }

    private static JPanel controller(JDialog jd, Connection connection, String ID) {
        JPanel controller = new JPanel(new GridBagLayout());
        controller.setBackground(Color.decode("#fcfcfc"));
        controller.setPreferredSize(new Dimension(270, 580));
        controller.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.decode("#f0f0f0")));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // printer chooser:
        gbc.gridx = 0;
        gbc.gridy = 0;
        controller.add(printServiceChooser(), gbc);

        // number of copies:   
        gbc.insets = new Insets(30, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 1;
        controller.add(spinner("Нусха сони"), gbc);

        // scale of the page:
        gbc.gridx = 0;
        gbc.gridy = 2;
        controller.add(spinner("Масштаб"), gbc);

        gbc.insets = new Insets(300, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 3;
        controller.add(lineSeperator(), gbc);

        // cancel and print buttons:
        gbc.insets = new Insets(30, 0, 0, 10);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        controller.add(button("Печатлаш", jd, connection, ID), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;

        controller.add(button("Бекор қилиш", jd, connection, ID), gbc);

        return controller;
    }

    private static JPanel button(String type, JDialog jd, Connection connection, String ID) {
        JButton button = new JButton(type);
        button.setPreferredSize(new Dimension(100, 30));
        button.setFocusable(false);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(null, Font.CENTER_BASELINE, 12));

        JPanel bcon = new JPanel(new BorderLayout());
        bcon.setPreferredSize(new Dimension(100, 30));
        bcon.add(button);

        if (type.equals("Печатлаш")) {
            button.setBackground(Color.decode("#4285F4"));
            button.setForeground(Color.white);
        } else {
            button.setBackground(Color.white);
            button.setForeground(Color.decode("#4285F4"));
            bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#dedede")));

        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (type.equals("Печатлаш")) {
                    button.setBackground(Color.decode("#4f8ef5"));
                } else {
                    button.setBackground(Color.decode("#f7faff"));
                    bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#d6e5ff")));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (type.equals("Печатлаш")) {
                    button.setBackground(Color.decode("#4285F4"));
                } else {
                    button.setBackground(Color.white);
                    bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#dedede")));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (type.equals("Печатлаш")) {
                    button.setCursor(new Cursor(Cursor.WAIT_CURSOR));

                    JPanel controller = (JPanel) e.getComponent().getParent().getParent();
                    JPanel panelToPrint = (JPanel) controller.getParent().getComponent(0);

                    JComboBox printServiceChooser = (JComboBox) ((JPanel) controller.getComponent(0)).getComponent(1);
                    JSpinner copiesSpinner = (JSpinner) ((JPanel) controller.getComponent(1)).getComponent(1);
                    JSpinner ScaleSpinner = (JSpinner) ((JPanel) controller.getComponent(2)).getComponent(1);

                    PrintService ps = ((PrintServiceItem) printServiceChooser.getSelectedItem()).getPrintService();
                    Object numOfCopies = copiesSpinner.getValue();
                    Object scale = ScaleSpinner.getValue();

                    Map printInfo = new HashMap();
                    printInfo.put("ps", ps);
                    printInfo.put("numOfCopies", numOfCopies);
                    printInfo.put("scale", scale);
                    printInfo.put("panelToPrint", panelToPrint);

                    printPage(printInfo);
                    jd.dispose();

                    if (!ID.isBlank()) {
                        ArrayList otherList = getData("SELECT * FROM OTHER WHERE ID = 'PUTYOVKANUM'", connection);
                        Map otherMap = (Map) otherList.get(0);
                        int putyovkaNum = Integer.parseInt(otherMap.get("VALUE").toString());

                        Map map = new HashMap();
                        map.put("VALUE", putyovkaNum + 1);

                        update("OTHER", map, "PUTYOVKANUM", connection);
                    }

                } else {
                    jd.dispose();
                }
            }

        });

        return bcon;
    }

    private static JPanel printServiceChooser() {
        Color gray = Color.decode("#696969");

        PrintService[] printServices = PrinterJob.lookupPrintServices();
        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

        ArrayList<PrintServiceItem> model = new ArrayList();

        for (PrintService printService : printServices) {
            String printServiceName = printService.getName();
            if (defaultPrintService == printService) {
                model.add(0, new PrintServiceItem(printService, printServiceName));
            } else {
                model.add(new PrintServiceItem(printService, printServiceName));
            }
        }

        JPanel container = new JPanel(new BorderLayout(0, 2));
        container.setBackground(Color.white);

        JLabel label = new JLabel("Принтер");
        label.setFont(new Font(null, Font.PLAIN, 17));
        label.setForeground(gray);

        JComboBox comboBox = new JComboBox(model.toArray());
        comboBox.setSelectedItem(0);
        comboBox.setBackground(Color.white);
        comboBox.getComponent(0).setBackground(Color.white);
        comboBox.setRenderer(comboBoxRenderer());
        comboBox.setFont(new Font(null, Font.PLAIN, 14));

        container.add(label, BorderLayout.PAGE_START);
        container.add(comboBox, BorderLayout.CENTER);

        return container;
    }

    private static JPanel spinner(String type) {
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 100, 1);

        if (type.equals("Масштаб")) {
            model = new SpinnerNumberModel(1, 0.5, 1.5, 0.01);
        }

        Color gray = Color.decode("#696969");
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(new Font(null, Font.PLAIN, 16));
        spinner.setPreferredSize(new Dimension(50, 27));
        spinner.setBorder(BorderFactory.createLineBorder(gray));
        spinner.getComponent(0).setBackground(Color.white);
        spinner.getComponent(1).setBackground(Color.white);

        JFormattedTextField textField = ((JSpinner.NumberEditor) spinner
                .getEditor()).getTextField();
        ((NumberFormatter) textField.getFormatter()).setAllowsInvalid(false);

        textField.setForeground(gray);

        spinner.addChangeListener((ChangeEvent e) -> {
            textField.setForeground(gray);
        });

        JLabel label = new JLabel(type);
        label.setFont(new Font(null, Font.PLAIN, 17));
        label.setForeground(gray);

        JPanel container = new JPanel(new BorderLayout(0, 2));
        container.setBackground(Color.decode("#fcfcfc"));

        container.add(label, BorderLayout.CENTER);
        container.add(spinner, BorderLayout.LINE_END);

        return container;
    }

    private static JLabel lineSeperator() {
        JLabel line = new JLabel();

        line.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#ebebeb")));

        return line;
    }

    public static DefaultListCellRenderer comboBoxRenderer() {
        Color gray = Color.decode("#696969");
        Color blue = Color.decode("#4285F4");
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index,
                    boolean isSelected, boolean hasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, hasFocus);
                if (isSelected) {
                    l.setForeground(Color.WHITE);
                    l.setBackground(blue);
                } else {
                    l.setForeground(gray);
                    l.setBackground(Color.WHITE);
                }

                return l;
            }

        };
    }

}
