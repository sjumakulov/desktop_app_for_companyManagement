package yutong.BODY.messagesPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
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
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import static popups.addIconMenu.menuItem;
import static yutong.BODY.EMPLOYEE.fileIcon;
import static yutong.tools.DBManager.deleteFromDB;
import static yutong.tools.DBManager.getData;
import static yutong.tools.DBManager.insert;
import static yutong.tools.DBManager.moveFile;
import static yutong.tools.DBManager.valueOf;
import static yutong.tools.FileChooser.showFileChooser;
import static yutong.tools.FileChooser.showFilePathChooser;
import static yutong.tools.MyScrollBarUI.myScrollBarUI;
import static yutong.tools.OpenFile.openFile;
import static yutong.tools.buttons.icon;

public class messagesPanel extends JPanel {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int w = (int) (screenSize.getWidth() * 0.4);
    int h = (int) (screenSize.getHeight() * 0.8);

    public messagesPanel(String tableName, String TYPE, Connection connection, int fontSize) {

        setBackground(Color.white);
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#e8e8e8")));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(w, h));

        Calendar cal = Calendar.getInstance();
        String YIL = String.valueOf(cal.get(Calendar.YEAR));
        ArrayList list = getData("SELECT * FROM " + tableName + " WHERE YIL='" + YIL + "' AND TYPE='" + TYPE + "'", connection);

        JPanel messagesPanel = new JPanel(new GridBagLayout());
        messagesPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = -1;

        gbc.insets = new Insets(5, 10, 5, 65);
        for (Object p : list) {
            Map penaltyMap = (Map) p;

            String DATE = valueOf(penaltyMap, "KUN") + "." + valueOf(penaltyMap, "OY") + "." + valueOf(penaltyMap, "YIL");
            messagesPanel.add(message(DATE, valueOf(penaltyMap, "TEXT"), valueOf(penaltyMap, "ID"), tableName, valueOf(penaltyMap, "FILE"), connection, fontSize), gbc);
        }

        JPanel panelForScrollPane = new JPanel(new BorderLayout());
        panelForScrollPane.add(messagesPanel, BorderLayout.PAGE_END);
        panelForScrollPane.setBackground(Color.decode("#fffbf7"));

        JScrollPane pane = new JScrollPane(panelForScrollPane);
        pane.setBorder(null);
        pane.getVerticalScrollBar().setUI(myScrollBarUI());
        pane.getVerticalScrollBar().setUnitIncrement(26);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(new header(TYPE, tableName, messagesPanel, connection, fontSize), BorderLayout.PAGE_START);

        add(pane, BorderLayout.CENTER);
        add(inputPanel(tableName, messagesPanel, TYPE, connection, fontSize), BorderLayout.PAGE_END);
    }

    public static JPanel message(String date, String message, String messageID, String tableName, String FILE, Connection connection, int fontSize) {
        Font f = new Font("Verdana", Font.PLAIN, fontSize);

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
        messageText.addMouseListener(MAForMessage(messageID, tableName, null, connection));

        JLabel pencil = icon("pencil3", "Ўзгартириш", MAForMessage(messageID, tableName, pm, connection), fontSize);
        pencil.setVisible(false);

        JLabel bin = icon("bin2", "Ўчириш", MAForMessage(messageID, tableName, pm, connection), fontSize);
        bin.setVisible(false);

        JPanel toolsPanel = new JPanel(new BorderLayout(2, 0));
        toolsPanel.add(pencil, BorderLayout.LINE_START);
        toolsPanel.add(bin, BorderLayout.LINE_END);
        toolsPanel.setOpaque(false);
        toolsPanel.setPreferredSize(new Dimension(fontSize + 34, 25));
        toolsPanel.setName("tp");
        toolsPanel.addMouseListener(MAForMessage(messageID, tableName, null, connection));

        JPanel messageTextWrapper = new JPanel(new GridBagLayout());
        messageTextWrapper.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridy = 0;
        gbc.gridx = -1;

        File file = new File(FILE);
        JLabel fileIcon = new JLabel();
        if (file.exists()) {
            fileIcon = new JLabel(fileIcon(file, fontSize));
            fileIcon.setToolTipText("Файлни очиш учун устига босинг");
            fileIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            fileIcon.setName("FILE");
            fileIcon.addMouseListener(MAForMessage(messageID, tableName, pm, connection));
            messageTextWrapper.setName(FILE);

        }
        gbc.anchor = GridBagConstraints.NORTHWEST;
        messageTextWrapper.add(fileIcon, gbc);

        gbc.anchor = GridBagConstraints.NORTHWEST;
        messageTextWrapper.add(messageText, gbc);

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

    private static JPanel inputPanel(String tableName, JPanel messagesPanel, String type, Connection connection, int fontSize) {
        JTextArea ta = new JTextArea();
        ta.setFont(new Font("", Font.PLAIN, fontSize));
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

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = -1;

        container.add(ta, gbc);

        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(0, 4, 0, 0);
        container.add(attachFile(), gbc);
        container.add(addButton(tableName, messagesPanel, type, connection, fontSize), gbc);

        return container;
    }

    private static JLabel attachFile() {
        ImageIcon icon = new ImageIcon(new ImageIcon("./resources/clip3.png").getImage().getScaledInstance(18, -1, Image.SCALE_SMOOTH));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconLabel.setToolTipText("Файлни танланг");
        iconLabel.setName("");
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ImageIcon icon = new ImageIcon(new ImageIcon("./resources/clip.png").getImage().getScaledInstance(18, -1, Image.SCALE_SMOOTH));
                String filePath = showFileChooser(new JFrame(), "");

                if (filePath.isBlank()) {
                    icon = new ImageIcon(new ImageIcon("./resources/clip3.png").getImage().getScaledInstance(18, -1, Image.SCALE_SMOOTH));
                }

                iconLabel.setIcon(icon);
                iconLabel.setName(filePath);
            }

        });

        return iconLabel;
    }

    private static JButton addButton(String tableName, JPanel messagesPanel, String type, Connection connection, int fontSize) {
        ImageIcon icon = new ImageIcon(new ImageIcon("./resources/plus.png").getImage().getScaledInstance(15, -1, Image.SCALE_SMOOTH));
        JButton ab = new JButton(icon);
        ab.setFocusable(false);
        ab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ab.setPreferredSize(new Dimension(40, 25));
        ab.setName("add");

        ab.addActionListener((ActionEvent e) -> {
            actionPerformed(e, tableName, messagesPanel, type, connection, fontSize);

        });

        return ab;
    }

    private static void actionPerformed(ActionEvent e, String tableName, JPanel messagesPanel, String type, Connection connection, int fontSize) {
        JButton b = (JButton) e.getSource();
        JPanel container = (JPanel) b.getParent();

        JTextArea ta = (JTextArea) container.getComponent(0);
        JLabel attachFile = (JLabel) container.getComponent(1);

        String TEXT = ta.getText().length() > 200 ? ta.getText().substring(0, 200) : ta.getText();
        String FILE = attachFile.getName();
        String bName = b.getName();

        if (TEXT.isBlank() || TEXT.equals("Изоҳ...")) {
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

                Map map = new HashMap();
                map.put("ID", ID);
                map.put("KUN", KUN);
                map.put("OY", OY);
                map.put("YIL", YIL);
                map.put("TYPE", type);
                FILE = moveFile(FILE, ID, tableName + "/" + type + "/", "");
                map.put("FILE", FILE);

                map.put("TEXT", TEXT);

                insert(tableName, map, connection);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 0.5;
                gbc.gridx = 0;
                gbc.gridy = -1;
                gbc.insets = new Insets(5, 10, 5, 65);

                JPanel m = message(KUN + "." + OY + "." + YIL, TEXT, ID, tableName, FILE, connection, fontSize);
                new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        messagesPanel.add(m, gbc);
                        messagesPanel.revalidate();
                        messagesPanel.repaint();
                        return 42;
                    }
                }.execute();

                ta.setText("Изоҳ...");
                ta.setForeground(Color.gray);
                ta.setFocusable(false);
                ta.setBorder(null);

                ImageIcon icon = new ImageIcon(new ImageIcon("./resources/clip3.png").getImage().getScaledInstance(18, -1, Image.SCALE_SMOOTH));
                attachFile.setIcon(icon);
                attachFile.setName("");

            } else if (bName.equals("edit")) {
                String messageID = ta.getName();
                ImageIcon plusIcon = new ImageIcon(new ImageIcon("./resources/plus.png").getImage().getScaledInstance(15, -1, Image.SCALE_SMOOTH));
                b.setName("add");
                b.setIcon(plusIcon);

                ImageIcon clipIcon = new ImageIcon(new ImageIcon("./resources/clip3.png").getImage().getScaledInstance(18, -1, Image.SCALE_SMOOTH));
                attachFile.setIcon(clipIcon);
                attachFile.setName("");

                Map map = new HashMap();
                map.put("ID", messageID);
                map.put("TEXT", TEXT);

                ArrayList messageList = (ArrayList) getData("SELECT * FROM " + tableName + " WHERE ID='" + messageID + "'", connection);
                Map oldMessageMap = (Map) messageList.get(0);

                map.put("FILE", moveMessageFile(FILE, messageID, tableName, type, oldMessageMap));

                new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        yutong.tools.DBManager.update(tableName, map, messageID, connection);
                        updateMessageInUI(messagesPanel, messageID, tableName, ta, connection, fontSize);
                        return 42;
                    }
                }.execute();

            }
        }
    }

    public static String moveMessageFile(String newPath, String ID, String tableName, String type, Map oldMap) {
        String oldPath = valueOf(oldMap, "FILE");

        if (newPath.isBlank()) {
            return valueOf(oldMap, "FILE");
        } else if (!oldPath.equals(newPath)) {

            File file = new File(oldPath);
            file.delete();

            return moveFile(newPath, ID, tableName + "/" + type + "/", "");
        } else {
            return oldPath;
        }

    }

    private static void updateMessageInUI(JPanel messagesPanel, String messageID, String tableName, JTextArea ta, Connection connection, int fontSize) {

        for (int i = 0; i < messagesPanel.getComponentCount(); i++) {

            JPanel pmPanel = (JPanel) messagesPanel.getComponent(i);

            if (pmPanel.getName().equals(messageID)) {

                ArrayList list = getData("SELECT * FROM " + tableName + " WHERE ID='" + messageID + "'", connection);
                Map map = (Map) list.get(0);

                messagesPanel.remove(pmPanel);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 0.5;
                gbc.gridx = 0;
                gbc.gridy = -1;

                gbc.insets = new Insets(5, 10, 5, 65);

                String DATE = valueOf(map, "KUN") + "." + valueOf(map, "OY") + "." + valueOf(map, "YIL");
                messagesPanel.add(message(DATE, valueOf(map, "TEXT"), valueOf(map, "ID"), tableName, valueOf(map, "FILE"), connection, fontSize), gbc, i);
                messagesPanel.revalidate();
                messagesPanel.repaint();

                ta.setText("Изоҳ...");
                ta.setForeground(Color.gray);
                ta.setFocusable(false);
                ta.setBorder(null);
                ta.revalidate();
                ta.repaint();
                break;
            }
        }
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
                JPanel toolsPanel = (JPanel) messageTextWrapper.getComponent(2);
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

    public static MouseAdapter MAForMessage(String mID, String tableName, JPanel pm, Connection connection) {
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
                        JTextArea ta = (JTextArea) comp.getParent().getParent().getComponent(1);
                        ta.setBackground(Color.decode("#4e804e"));
                        ta.setForeground(Color.white);

                        ArrayList mList = getData("SELECT * FROM " + tableName + " WHERE ID='" + mID + "'", connection);
                        Map mMap = (Map) mList.get(0);

                        JPanel container = (JPanel) pm.getParent().getParent().getParent().getParent().getParent();
                        JPanel inputPanel = (JPanel) container.getComponent(2);

                        JTextArea inputField = (JTextArea) inputPanel.getComponent(0);
                        inputField.setText(valueOf(mMap, "TEXT"));
                        inputField.setForeground(Color.black);
                        inputField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
                        inputField.setName(mID);
                        inputField.setFocusable(true);
                        inputField.requestFocus();

                        JLabel attachFile = (JLabel) inputPanel.getComponent(1);
                        attachFile.setName("");

                        String FILE = valueOf(mMap, "FILE");
                        File file = new File(FILE);
                        if (file.exists()) {
//                            attachFile.setName(FILE);
                            ImageIcon clipIcon = new ImageIcon(new ImageIcon("./resources/clip.png").getImage().getScaledInstance(18, -1, Image.SCALE_SMOOTH));
                            attachFile.setIcon(clipIcon);
                        }

                        ImageIcon icon = new ImageIcon(new ImageIcon("./resources/check.png").getImage().getScaledInstance(15, -1, Image.SCALE_SMOOTH));
                        JButton addButton = (JButton) inputPanel.getComponent(2);
                        addButton.setName("edit");
                        addButton.setIcon(icon);

                        break;
                    }
                    case "bin2" -> {
                        ArrayList mList = getData("SELECT * FROM " + tableName + " WHERE ID='" + mID + "'", connection);
                        Map mMap = (Map) mList.get(0);

                        JPopupMenu menu = new JPopupMenu();
                        menu.removeAll();
                        menu.setBorder(BorderFactory.createLineBorder(Color.gray));
                        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));

                        JMenuItem confirmMenu = menuItem("Ҳақиқатан ўчирмоқчиман");
                        confirmMenu.addMouseListener(this);
                        confirmMenu.setName("confirmMenu");
                        confirmMenu.addActionListener((ActionEvent e1) -> {
                            File file = new File(valueOf(mMap, "FILE"));
                            file.delete();

                            deleteFromDB(tableName, mID, connection);

                            JPanel messagesPanel = (JPanel) pm.getParent();
                            messagesPanel.remove(pm);
                            messagesPanel.revalidate();
                            messagesPanel.repaint();
                        });

                        menu.add(confirmMenu);
                        menu.show(comp, e.getX() - 200, e.getY());
                        break;
                    }
                    case "FILE" -> {

                        if (e.getButton() == MouseEvent.BUTTON3) {
                            JPopupMenu menu = new JPopupMenu();
                            menu.removeAll();
                            menu.setBorder(BorderFactory.createLineBorder(Color.gray));
                            menu.setCursor(new Cursor(Cursor.HAND_CURSOR));

                            JMenuItem downloadMenu = menuItem("Юклаб олиш");
                            downloadMenu.addActionListener((ActionEvent e1) -> {
                                showFilePathChooser((JFrame) SwingUtilities.getRoot(comp), mID, tableName, connection);
                            });

                            menu.add(downloadMenu);
                            menu.show(comp, e.getX() - 130, e.getY());

                        } else if (e.getButton() == MouseEvent.BUTTON1) {
                            String filePath = comp.getParent().getName();

                            File file = new File(filePath);
                            comp.setCursor(new Cursor(Cursor.WAIT_CURSOR));

                            if (file.exists()) {
                                openFile(filePath);
                            } else {
                                JOptionPane.showMessageDialog(new JFrame(), "Файл топилмади!");
                            }
                            comp.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        }

                        break;
                    }

                }

            }
        };
    }
}
