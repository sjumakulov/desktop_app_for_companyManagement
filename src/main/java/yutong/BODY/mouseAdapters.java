package yutong.BODY;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import static popups.DeletePopup.showDeletePopup;
import static popups.EditCarPopup.showEditCarPopup;
import static popups.EditEmployeePopup.showEditEmployeePopup;
import static popups.addIconMenu.menuItem;
import static print.OneDayPutyovka.printPopup.showOneDayPrintPopup;
import static print.printCalendar.showPrintCalendar;
import static yutong.BODY.EMPLOYEE.penaltyMessage;
import static yutong.BODY.EMPLOYEE.penaltyPanel;
import static yutong.tools.DBManager.deleteFromDB;
import static yutong.tools.DBManager.insert;
import static yutong.tools.DBManager.update;
import static yutong.tools.FileChooser.showFilePathChooser;
import static yutong.tools.OpenFile.openFile;

public class mouseAdapters {

    public static MouseAdapter mouseAdapterForDataCellWithFile() {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JComponent component = (JComponent) e.getSource();
                component.setBackground(Color.decode("#e6f9ff"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JComponent component = (JComponent) e.getSource();
                component.setBackground(Color.white);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JComponent component = (JComponent) e.getSource();
                String filePath = ((JLabel) component.getComponent(0)).getName();

                File file = new File(filePath);
                component.setCursor(new Cursor(Cursor.WAIT_CURSOR));

                if (file.exists()) {
                    openFile(filePath);
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Файл топилмади!");
                }
                component.setCursor(new Cursor(Cursor.HAND_CURSOR));

            }
        };
    }

    public static MouseAdapter mouseAdapterForEmployeeTools(Connection connection) {
        return new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JComponent component = (JComponent) e.getSource();
                String name = component.getName();

                JPanel toolsPanel = (JPanel) component.getParent();
                String ID = toolsPanel.getName();

                JFrame FRAME = (JFrame) SwingUtilities.getRoot(component);
                switch (name) {

                    case "print" -> {
                        handlePrint(ID, e, connection, component);
                        break;
                    }
                    case "pencil" -> {
                        showEditEmployeePopup(FRAME, ID, connection);
                        break;
                    }
                    case "download" -> {
                        showFilePathChooser(FRAME, ID, "EMPLOYEES", connection);
                        break;
                    }
                    case "bin" -> {
                        JPanel mainPanel = (JPanel) toolsPanel.getParent();
                        JPanel employeeBody = (JPanel) mainPanel.getParent();
                        JPanel EMPLOYEE = (JPanel) employeeBody.getParent();

                        showDeletePopup(FRAME, ID, "EMPLOYEES", EMPLOYEE, connection);
                        break;
                    }
                }

            }
        };
    }

    public static JMenu subMenu(String label, ActionListener al1, ActionListener al2) {
        JMenu subMenu = new JMenu(label);
        subMenu.setBackground(Color.white);
        subMenu.setOpaque(true);

        JMenuItem item1 = menuItem("Олд тарафи");
        item1.addActionListener(al1);

        JMenuItem item2 = menuItem("Орқа тарафи");
        item2.addActionListener(al2);

        subMenu.add(item1);
        subMenu.add(item2);

        return subMenu;
    }

    public static void handlePrint(String ID, MouseEvent e, Connection connection, JComponent component) {
        JPopupMenu menu = new JPopupMenu();
        menu.removeAll();
        menu.setBorder(BorderFactory.createLineBorder(Color.gray));
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JMenu oylik = subMenu("Ойлик путёвка", (ActionEvent e1) -> {
            showPrintCalendar(e, ID, connection);
        }, (ActionEvent e1) -> {
            showPrintCalendar(e, "", connection);
        });

        JMenu kunlik = subMenu("Кунлик путёвка", (ActionEvent e1) -> {
            showOneDayPrintPopup(e, ID, connection);

        }, (ActionEvent e1) -> {
            showOneDayPrintPopup(e, "", connection);
        });

        menu.add(oylik);
        menu.add(kunlik);

        menu.show(component, e.getX() - 180, e.getY());
    }

    public static MouseAdapter mouseAdapterForCarTools(Connection connection) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JComponent component = (JComponent) e.getSource();
                String name = component.getName();

                JPanel toolsPanel = (JPanel) component.getParent();
                String ID = toolsPanel.getName();

                JFrame FRAME = (JFrame) SwingUtilities.getRoot(component);
                switch (name) {
                    case "pencil" -> {
                        showEditCarPopup(FRAME, ID, connection);
                        break;
                    }
                    case "bin" -> {
                        JPanel mainPanel = (JPanel) toolsPanel.getParent();
                        JPanel employeeBody = (JPanel) mainPanel.getParent();
                        JPanel CAR = (JPanel) employeeBody.getParent();

                        showDeletePopup(FRAME, ID, "CARS", CAR, connection);
                        break;
                    }
                    case "download" -> {
                        showFilePathChooser(FRAME, ID, "CARS", connection);
                        break;
                    }
                }

            }
        };
    }

    public static MouseAdapter mouseAdapterForAddPenaltyButton(JPanel messagesPanel, String employeeID, Connection connection, int fontSize) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton b = (JButton) e.getSource();
                JTextArea ta = (JTextArea) b.getParent().getComponent(2);
                String input = ta.getText();
                String bName = b.getName();

                if (input.isBlank() || input.equals("Изоҳ...")) {
                    ta.setText("Бўш изоҳ мумкин эмас...");
                    ta.setForeground(Color.red);
                    ta.setFocusable(false);

                    java.util.Timer timer = new java.util.Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (ta.getText().equals("Бўш изоҳ мумкин эмас...")) {
                                ta.setForeground(Color.gray);
                                ta.setText("Изоҳ...");
                                ta.setFocusable(true);
                                ta.setBorder(null);
                            }
                        }
                    }, 1500);
                } else {
                    if (bName.equals("add")) {
                        String uid = "A" + UUID.randomUUID().toString().toUpperCase();
                        String ID = uid.replace("-", "").substring(0, 32);

                        Calendar cal = Calendar.getInstance();

                        String KUN = String.valueOf(cal.get(Calendar.DATE));
                        String OY = String.valueOf(cal.get(Calendar.MONTH) + 1);
                        String YIL = String.valueOf(cal.get(Calendar.YEAR));

                        KUN = KUN.length() == 2 ? KUN : "0" + KUN;
                        OY = OY.length() == 2 ? OY : "0" + OY;

                        Map penaltyMap = new HashMap();
                        penaltyMap.put("ID", ID);
                        penaltyMap.put("KUN", KUN);
                        penaltyMap.put("OY", OY);
                        penaltyMap.put("YIL", YIL);

                        String text = ta.getText().length() > 300 ? ta.getText().substring(0, 300) : ta.getText();
                        penaltyMap.put("TEXT", text);

                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.weightx = 0.5;
                        gbc.gridx = 0;
                        gbc.gridy = -1;
                        gbc.insets = new Insets(5, 10, 5, 15);

                        JPanel pm = penaltyMessage(KUN + "." + OY + "." + YIL, text, ID, employeeID, connection, fontSize);
                        new SwingWorker() {
                            @Override
                            protected Object doInBackground() throws Exception {
                                insert(employeeID, penaltyMap, connection);
                                messagesPanel.add(pm, gbc);
                                messagesPanel.revalidate();
                                messagesPanel.repaint();
                                return 42;
                            }
                        }.execute();

                        ta.setText("Изоҳ...");
                        ta.setForeground(Color.gray);
                        ta.setFocusable(false);
                        ta.setBorder(null);
                    } else if (bName.equals("edit")) {
                        String messageID = ta.getName();
                        String text = ta.getText().length() > 300 ? ta.getText().substring(0, 300) : ta.getText();

                        for (Object obj : messagesPanel.getComponents()) {
                            JPanel pmPanel = (JPanel) obj;

                            if (pmPanel.getName().equals(messageID)) {
                                JPanel messageTextWrapper = (JPanel) pmPanel.getComponent(1);
                                JTextArea messageText = (JTextArea) messageTextWrapper.getComponent(0);
                                messageText.setBackground(Color.decode("#dcffdb"));
                                messageText.setForeground(Color.black);
                                messageText.setText(text);
                                messageText.revalidate();
                                messageText.repaint();

                                ta.setText("Изоҳ...");
                                ta.setForeground(Color.gray);
                                ta.setFocusable(false);
                                ta.setBorder(null);

                                break;
                            }
                        }

                        ImageIcon icon = new ImageIcon(new ImageIcon("./resources/plus.png").getImage().getScaledInstance(15, -1, Image.SCALE_SMOOTH));
                        b.setName("add");
                        b.setIcon(icon);

                        Map penaltyMap = new HashMap();
                        penaltyMap.put("ID", messageID);
                        penaltyMap.put("TEXT", text);

                        update(employeeID, penaltyMap, messageID, connection);

                    }

                }
            }
        };
    }

    public static MouseAdapter mouseAdapterForDepartmentCollapsible(JPanel headContainer, int fontSize) {
        return new MouseAdapter() {
            String iconName = "arrowright.png";

            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel departmentBODY = (JPanel) headContainer.getParent().getComponent(1);
                departmentBODY.setVisible(!departmentBODY.isVisible());
                JLabel icon = ((JLabel) e.getComponent());

                if (iconName.equals("arrowright.png")) {
                    iconName = "arrowdown.png";
                    icon.setToolTipText("Ёпиш");
                } else {
                    iconName = "arrowright.png";
                    icon.setToolTipText("Очиш");
                }
                ImageIcon img = new ImageIcon("./resources/" + iconName);
                img = new ImageIcon(img.getImage().getScaledInstance(fontSize + 3, -1, java.awt.Image.SCALE_SMOOTH));
                icon.setIcon(img);

            }
        };
    }

    public static MouseAdapter mouseAdapterForEmployeeCollapsible(JPanel headContainer, String ID, Connection connection, int fontSize) {
        return new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel departmentBODY = (JPanel) headContainer.getParent().getComponent(2);
                boolean isVisible = departmentBODY.isVisible();
                departmentBODY.setVisible(!isVisible);
                JLabel iconLabel = ((JLabel) e.getComponent());

                JPanel dataPanel = (JPanel) departmentBODY.getComponent(0);

                new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        if (!isVisible) {
                            GridBagConstraints gbc = new GridBagConstraints();
                            gbc.fill = GridBagConstraints.HORIZONTAL;
                            gbc.insets = new Insets(5, 0, 5, 10);
                            gbc.weightx = 0.5;

                            gbc.gridwidth = 4;
                            gbc.gridy = 5;
                            gbc.gridx = 1;
                            dataPanel.add(penaltyPanel(ID, connection, fontSize), gbc);
                            dataPanel.revalidate();
                            dataPanel.repaint();

                        } else {
                            dataPanel.remove(18);
                            dataPanel.revalidate();
                            dataPanel.repaint();
                        }

                        return 42;
                    }
                }.execute();

                if (iconLabel.getName().equals("arrowright")) {
                    iconLabel.setName("arrowdown");
                    iconLabel.setToolTipText("Ёпиш");
                } else {
                    iconLabel.setName("arrowright");
                    iconLabel.setToolTipText("Очиш");
                }

                ImageIcon img = new ImageIcon("./resources/" + iconLabel.getName() + ".png");
                img = new ImageIcon(img.getImage().getScaledInstance(fontSize + 3, -1, java.awt.Image.SCALE_SMOOTH));
                iconLabel.setIcon(img);

            }
        };
    }

    public static MouseAdapter mouseAdapterForCarCollapsible(JPanel headContainer, int fontSize) {
        return new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel departmentBODY = (JPanel) headContainer.getParent().getComponent(2);
                boolean isVisible = departmentBODY.isVisible();
                departmentBODY.setVisible(!isVisible);
                JLabel iconLabel = ((JLabel) e.getComponent());

                if (iconLabel.getName().equals("arrowright")) {
                    iconLabel.setName("arrowdown");
                    iconLabel.setToolTipText("Ёпиш");
                } else {
                    iconLabel.setName("arrowright");
                    iconLabel.setToolTipText("Очиш");
                }
                ImageIcon img = new ImageIcon("./resources/" + iconLabel.getName() + ".png");
                img = new ImageIcon(img.getImage().getScaledInstance(fontSize + 3, -1, java.awt.Image.SCALE_SMOOTH));
                iconLabel.setIcon(img);

            }
        };
    }

    public static MouseAdapter MAForPenaltyMessage(String mID, String eID, JPanel pm, Connection connection) {
        return new MouseAdapter() {
            @Override()
            public void mouseEntered(MouseEvent e) {
                showToolsOnHover(e);
            }

            @Override()
            public void mouseExited(MouseEvent e) {
                showToolsOnHover(e);
            }

            @Override()
            public void mouseClicked(MouseEvent e) {
                JComponent comp = (JComponent) e.getComponent();
                String name = comp.getName();
                switch (name) {
                    case "pencil3" -> {
                        JTextArea ta = (JTextArea) ((JPanel) pm.getComponent(1)).getComponent(0);
                        ta.setBackground(Color.decode("#4e804e"));
                        ta.setForeground(Color.white);

                        String messageText = (ta).getText();

                        JPanel container = (JPanel) pm.getParent().getParent().getParent().getParent().getParent();

                        JTextArea inputField = (JTextArea) container.getComponent(2);
                        inputField.setText(messageText);
                        inputField.setForeground(Color.black);
                        inputField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
                        inputField.setName(mID);
                        inputField.setFocusable(true);
                        inputField.requestFocus();

                        ImageIcon icon = new ImageIcon(new ImageIcon("./resources/check.png").getImage().getScaledInstance(15, -1, Image.SCALE_SMOOTH));
                        JButton addButton = (JButton) container.getComponent(3);
                        addButton.setName("edit");
                        addButton.setIcon(icon);

                        break;
                    }
                    case "bin2" -> {

                        JPopupMenu menu = new JPopupMenu();
                        menu.removeAll();
                        menu.setBorder(BorderFactory.createLineBorder(Color.gray));
                        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));

                        JMenuItem confirmMenu = menuItem("Ҳақиқатан ўчирмоқчиман");
                        confirmMenu.addMouseListener(this);
                        confirmMenu.setName("confirmMenu");
                        confirmMenu.addActionListener((ActionEvent e1) -> {
                            deleteFromDB(eID, mID, connection);

                            JPanel messagesPanel = (JPanel) pm.getParent();
                            messagesPanel.remove(pm);
                            messagesPanel.revalidate();
                            messagesPanel.repaint();
                        });

                        menu.add(confirmMenu);
                        menu.show(comp, e.getX() - 200, e.getY());
                        break;
                    }

                }

            }
        };
    }

    private static void showToolsOnHover(MouseEvent e) {
        JComponent comp = (JComponent) e.getComponent();
        String name = comp.getName();

        switch (name) {
            case "tp" -> {
                JLabel pencil = (JLabel) comp.getComponent(0);
                JLabel bin = (JLabel) comp.getComponent(1);
                pencil.setVisible(!pencil.isVisible());
                bin.setVisible(!bin.isVisible());

                break;
            }
            case "mt" -> {
                JPanel messageTextWrapper = (JPanel) comp.getParent();
                JPanel toolsPanel = (JPanel) messageTextWrapper.getComponent(1);
                JLabel pencil = (JLabel) toolsPanel.getComponent(0);
                JLabel bin = (JLabel) toolsPanel.getComponent(1);
                pencil.setVisible(!pencil.isVisible());
                bin.setVisible(!bin.isVisible());
                break;
            }

            case "bin2" -> {
                comp.setVisible(!comp.isVisible());
                JLabel pencil = (JLabel) comp.getParent().getComponent(0);

                pencil.setVisible(!pencil.isVisible());

                break;
            }
            case "pencil3" -> {
                comp.setVisible(!comp.isVisible());
                JLabel bin = (JLabel) comp.getParent().getComponent(1);

                bin.setVisible(!bin.isVisible());
                break;
            }
            case "confirmMenu" -> {
                JPopupMenu popupMenu = (JPopupMenu) comp.getParent();
                JLabel bin = (JLabel) popupMenu.getInvoker();
                bin.setVisible(!bin.isVisible());

                JPanel toolsPanel = (JPanel) bin.getParent();
                JLabel pencil = (JLabel) toolsPanel.getComponent(0);
                pencil.setVisible(!pencil.isVisible());

                break;
            }
        }
    }
}
