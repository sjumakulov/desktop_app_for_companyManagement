package yutong.tools;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import static popups.addIconMenu.menuItem;
import static popups.addIconMenu.showAddMenu;
import static yutong.tools.DBManager.update;
import static yutong.tools.FileChooser.showFilePathChooser;

public class buttons {

    public static JButton menuButton(String label, String name, SearchBar SB, Connection connection, int fontSize) {

        Color fc = Color.decode("#4a4a4a");
        Color clickFC = Color.decode("#ffffff");
        Color enterBC = Color.decode("#e0e0e0");
        Color clickBC = Color.decode("#005780");

        JButton mb = new JButton(label);
        mb.setName(name);
        mb.setFocusable(false);
        mb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mb.setFont(new Font(null, Font.BOLD, fontSize));
        mb.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        mb.setBackground(null);
        mb.setForeground(fc);

        if (name.equals("excel") || name.equals("size") || name.equals("add")) {
            ImageIcon img = new ImageIcon("./resources/" + name + ".png");
            img = new ImageIcon(img.getImage().getScaledInstance(fontSize + 5, -1, java.awt.Image.SCALE_SMOOTH));
            mb.setIcon(img);
            mb.setText("");
            mb.setToolTipText(label);
        } else if (name.equals("EMPLOYEES")) {
            mb.setBackground(clickBC);
            mb.setForeground(clickFC);
        }

        mb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton button = (JButton) e.getComponent();
                if (button.getBackground().getRGB() != clickBC.getRGB()) {
                    mb.setBackground(enterBC);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JButton button = (JButton) e.getComponent();
                if (button.getBackground().getRGB() != clickBC.getRGB()) {
                    mb.setBackground(null);
                    mb.setForeground(fc);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JButton button = (JButton) e.getComponent();
                String name = button.getName();

                switch (name) {
                    case "excel" -> {
                        showFilePathChooser((JFrame) SwingUtilities.getRoot(button), null, "EXCEL", connection);
                        break;
                    }
                    case "add" -> {
                        showAddMenu(button, e, connection);
                        break;
                    }
                    case "LETTERS", "STOCK" -> {
                        SB.setEnabled(false);
                        showCards(button, name, clickBC, clickFC, fc);
                        break;
                    }
                    case "EMPLOYEES", "CARS" -> {
                        SB.setEnabled(true);
                        SB.setName(name);
                        showCards(button, name, clickBC, clickFC, fc);
                        break;
                    }

                    case "size" -> {
                        showFontSizeMenu(button, e, connection);
                        break;
                    }
                }
            }
        }
        );

        return mb;
    }

    public static void showFontSizeMenu(JButton button, MouseEvent e, Connection connection) {
        JPopupMenu menu = new JPopupMenu();
        menu.removeAll();
        menu.setBorder(BorderFactory.createLineBorder(Color.gray));
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JMenuItem Aa1 = menuItem("Аа 1");
        Aa1.setFont(new Font("", Font.PLAIN, 15));
        Aa1.addActionListener((ActionEvent e1) -> {
            updateFontSizeInDB(15, connection);
        });

        JMenuItem Aa2 = menuItem("Аа 2");
        Aa2.setFont(new Font("", Font.PLAIN, 17));
        Aa2.addActionListener((ActionEvent e1) -> {
            updateFontSizeInDB(17, connection);
        });

        JMenuItem Aa3 = menuItem("Аа 3");
        Aa3.setFont(new Font("", Font.PLAIN, 19));
        Aa3.addActionListener((ActionEvent e1) -> {
            updateFontSizeInDB(19, connection);
        });

        JMenuItem Aa4 = menuItem("Аа 4");
        Aa4.setFont(new Font("", Font.PLAIN, 21));
        Aa4.addActionListener((ActionEvent e1) -> {
            updateFontSizeInDB(21, connection);
        });

        JMenuItem Aa5 = menuItem("Аа 5");
        Aa5.setFont(new Font("", Font.PLAIN, 22));
        Aa5.addActionListener((ActionEvent e1) -> {
            updateFontSizeInDB(22, connection);
        });

        JMenuItem Aa6 = menuItem("Аа 6");
        Aa6.setFont(new Font("", Font.PLAIN, 23));
        Aa6.addActionListener((ActionEvent e1) -> {
            updateFontSizeInDB(23, connection);
        });

        JMenuItem Aa7 = menuItem("Аа 7");
        Aa7.setFont(new Font("", Font.PLAIN, 24));
        Aa7.addActionListener((ActionEvent e1) -> {
            updateFontSizeInDB(24, connection);
        });

        JMenuItem Aa8 = menuItem("Аа 8");
        Aa8.setFont(new Font("", Font.PLAIN, 25));
        Aa8.addActionListener((ActionEvent e1) -> {
            updateFontSizeInDB(25, connection);
        });

        menu.add(Aa1);
        menu.add(Aa2);
        menu.add(Aa3);
        menu.add(Aa4);
        menu.add(Aa5);
        menu.add(Aa6);
        menu.add(Aa7);
        menu.add(Aa8);

        menu.show(button, e.getX() - 70, e.getY());
    }

    public static void updateFontSizeInDB(int size, Connection connection) {
        Map map = new HashMap();
        map.put("VALUE", size);
        update("OTHER", map, "SIZE", connection);
    }

    public static JPanel deleteButton(String label, String name, ActionListener ac) {
        JButton button = new JButton(label);
        button.setName(name);
        button.setFocusable(false);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(null, Font.BOLD, 18));

        JPanel bcon = new JPanel(new BorderLayout());
        bcon.setPreferredSize(new Dimension(135, 35));
        bcon.add(button);

        if (name.equals("no")) {
            button.setBackground(Color.decode("#DB4437"));
            button.setForeground(Color.white);
        } else {
            button.setBackground(Color.white);
            button.setForeground(Color.decode("#4285F4"));
            bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#dedede")));
        }

        button.addActionListener(ac);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (name.equals("no")) {
                    button.setBackground(Color.decode("#df5549"));

                } else {
                    button.setBackground(Color.decode("#f7faff"));
                    bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#d6e5ff")));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (name.equals("no")) {
                    button.setBackground(Color.decode("#DB4437"));

                } else {
                    button.setBackground(Color.white);
                    bcon.setBorder(BorderFactory.createLineBorder(Color.decode("#dedede")));
                }

            }
        });

        return bcon;
    }

    public static JButton button(String label, String name, ActionListener ac) {
        JButton button = new JButton(label);
        button.setName(name);
        button.setPreferredSize(new Dimension(320, 35));
        button.setFocusable(false);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(null, Font.BOLD, 18));
        button.addActionListener(ac);

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
        });

        return button;
    }

    public static JLabel icon(String iconName, String tooltiptext, MouseAdapter ma, int fontSize) {
        ImageIcon img = new ImageIcon("./resources/" + iconName + ".png");
        img = new ImageIcon(img.getImage().getScaledInstance(fontSize + 3, -1, java.awt.Image.SCALE_SMOOTH));

        JLabel label = new JLabel(img);

        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setToolTipText(tooltiptext);
        label.setName(iconName);
        label.addMouseListener(ma);

        return label;
    }

    private static void showCards(JButton button, String name, Color clickBC, Color clickFC, Color fc) {
        JPanel HEADER = (JPanel) button.getParent();
        JPanel contentPane = (JPanel) HEADER.getParent();
        JPanel BODY = (JPanel) contentPane.getComponent(1);

        CardLayout cl = (CardLayout) (BODY.getLayout());
        cl.show(BODY, name);

        for (int i = 0; i < HEADER.getComponentCount() - 3; i++) {
            JButton currentButton = (JButton) HEADER.getComponent(i);

            String currentName = currentButton.getName();

            if (currentName.equals(name)) {
                currentButton.setBackground(clickBC);
                currentButton.setForeground(clickFC);
            } else {
                currentButton.setBackground(null);
                currentButton.setForeground(fc);
            }
        }
    }

}
