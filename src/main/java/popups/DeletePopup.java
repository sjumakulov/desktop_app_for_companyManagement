package popups;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import static yutong.tools.DBManager.deleteFromDB;
import static yutong.tools.DBManager.getFontSize;

public class DeletePopup {

    public static void showDeletePopup(JFrame FRAME, String ID, String tableName, JPanel panel, Connection connection) {
        GlassPane glassPane = new GlassPane();
        FRAME.getRootPane().setGlassPane(glassPane);

        glassPane.setVisible(true);
        deleteDialogPopup(FRAME, ID, tableName, panel, connection);
        glassPane.setVisible(false);
    }

    public static JDialog deleteDialogPopup(JFrame FRAME, String ID, String tableName, JPanel panel, Connection connection) {
        int fontSize = getFontSize(connection);

        JDialog jd = new JDialog(FRAME);

        jd.setLayout(new BorderLayout());
        jd.setMinimumSize(new Dimension(375 + fontSize, 130 + fontSize));
        jd.setModal(true);
        jd.setLocationRelativeTo(FRAME);
        jd.setUndecorated(true);
        jd.getContentPane().setBackground(Color.white);
        jd.add(warningText(fontSize), BorderLayout.PAGE_START);
        jd.add(buttonContainer(jd, ID, tableName, panel, connection, fontSize), BorderLayout.CENTER);
        jd.setVisible(true);

        return jd;
    }

    private static JPanel warningText(int fontSize) {
        JLabel label1 = new JLabel("Ростдан ҳам ўчирмоқчимисиз?", JLabel.CENTER);

        label1.setFont(new Font(null, Font.BOLD, fontSize));
        label1.setForeground(Color.decode("#696969"));

        JPanel container = new JPanel(new GridLayout(2, 1));
        container.setBackground(Color.white);
        container.setBorder(BorderFactory.createEmptyBorder(15, 5, 25, 5));

        container.add(label1);

        return container;
    }

    private static JPanel buttonContainer(JDialog jd, String ID, String tableName, JPanel panel, Connection connection, int fontSize) {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        container.setBackground(Color.white);

        container.add(button("Ха", "yes", jd, ID, tableName, panel, connection, fontSize));
        container.add(button("Йўқ", "no", jd, ID, tableName, panel, connection, fontSize));

        return container;
    }

    private static JPanel button(String label, String name, JDialog jd, String ID, String tableName, JPanel panel, Connection connection, int fontSize) {
        JButton button = new JButton(label);
        button.setName(name);
        button.setFocusable(false);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(null, Font.BOLD, fontSize));

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

            @Override
            public void mouseClicked(MouseEvent e) {
                if (name.equals("no")) {
                    jd.dispose();
                } else {
                    jd.dispose();

                    new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            switch (tableName) {
                                case "EMPLOYEES", "CARS" -> {

                                    deleteFiles("./data/files/" + tableName + "/" + ID);
                                    deleteFromDB(tableName, ID, connection);

                                    JPanel departmentBody = (JPanel) panel.getParent();
                                    departmentBody.remove(panel);
                                    departmentBody.revalidate();

                                    for (int i = 0; i < departmentBody.getComponentCount(); i++) {
                                        JPanel currentEmployee = (JPanel) departmentBody.getComponent(i);
                                        JLabel numLabel = (JLabel) currentEmployee.getComponent(0);
                                        numLabel.setText(String.valueOf(i + 1));
                                    }
                                    break;
                                }

                            }
                            return 42;
                        }

                    }.execute();

                }
            }
        });

        return bcon;
    }

    public static void deleteFiles(String path) {
        File file = new File(path);

        if (file.exists()) {
            String[] entries = file.list();
            for (String s : entries) {
                File currentFile = new File(file.getPath(), s);
                currentFile.delete();
            }
            file.delete();
        }
    }
}
