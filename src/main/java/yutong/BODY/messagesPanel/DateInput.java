package yutong.BODY.messagesPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import static yutong.tools.DBManager.getData;
import static yutong.tools.DBManager.valueOf;
import static yutong.BODY.messagesPanel.messagesPanel.message;

public class DateInput extends JPanel {

    private final JTextField dd, MM, yyyy;

    public DateInput(String ID, String TYPE, JPanel messagesPanel, Connection connection, int fontSize) {

        dd = textField(new Dimension(fontSize + 9, 22), "кк", fontSize-1);
        MM = textField(new Dimension(fontSize + 9, 22), "оо", fontSize-1);
        yyyy = textField(new Dimension(fontSize + 32, 22), "йййй", fontSize-1);

        dd.addKeyListener(KAonlyNum(dd, 1));
        MM.addKeyListener(KAonlyNum(MM, 1));
        yyyy.addKeyListener(KAonlyNum(yyyy, 3));

        yyyy.addKeyListener(search(dd, MM, yyyy, ID, TYPE, messagesPanel, connection, fontSize));
        MM.addKeyListener(search(dd, MM, yyyy, ID, TYPE, messagesPanel, connection, fontSize));
        dd.addKeyListener(search(dd, MM, yyyy, ID, TYPE, messagesPanel, connection, fontSize));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 2);

        gbc.gridx = -1;
        add(dd, gbc);

        add(MM, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        add(yyyy, gbc);

        setOpaque(false);
        setBorder(new EmptyBorder(2, 2, 2, 2));
    }

    private static JTextField textField(Dimension d, String placeHolder, int fontSize) {
        JTextField ta = new JTextField();
        ta.setPreferredSize(d);
        ta.setBorder(new EmptyBorder(0, 1, 0, 1));
        ta.setFont(new Font("", Font.PLAIN, fontSize));
        ta.setTransferHandler(null); // disables pasting input = ctrl + v 
        ta.setText(placeHolder);
        ta.setForeground(Color.gray);
        ta.setFocusable(false);

        ta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ta.setFocusable(true);
                ta.requestFocus();
            }
        });

        ta.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (ta.getForeground().getRGB() == Color.gray.getRGB()) {
                    ta.setText("");
                    ta.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ta.getText().isBlank()) {
                    ta.setText(placeHolder);
                    ta.setForeground(Color.gray);
                }
            }
        });

        return ta;
    }

    private static KeyAdapter KAonlyNum(JTextField tf, int max) {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // if it's not a number, ignore the event
                }

                String input = tf.getText();

                if (input.length() > max) {
                    e.consume();
                }
            }
        };
    }

    private static KeyListener search(JTextField dd, JTextField MM, JTextField yyyy, String tableName, String TYPE, JPanel messagesPanel, Connection connection, int fontSize) {
        return new KeyAdapter() {

            Calendar cal = Calendar.getInstance();
            String YIL = String.valueOf(cal.get(Calendar.YEAR));
            ArrayList list = new ArrayList();
            ArrayList list2 = new ArrayList();

            boolean update = false;
            boolean firstTime = true;

            @Override
            public void keyReleased(KeyEvent e) {

                String DAY = isNumber(dd.getText()) ? dd.getText() : "";
                String MONTH = isNumber(MM.getText()) ? MM.getText() : "";
                String YEAR = isNumber(yyyy.getText()) ? yyyy.getText() : "";

                if (DAY.length() == 2 && MONTH.length() == 2 && YEAR.length() == 4) {
                    list = getData("SELECT * FROM " + tableName + " WHERE KUN= '" + DAY + "' AND OY='" + MONTH + "' AND YIL='" + YEAR + "' AND TYPE='" + TYPE + "'", connection);
                    update = true;

                } else if (MONTH.length() == 2 && YEAR.length() == 4) {
                    list = getData("SELECT * FROM " + tableName + " WHERE OY='" + MONTH + "' AND YIL='" + YEAR + "' AND TYPE='" + TYPE + "'", connection);
                    update = true;

                } else if (YEAR.length() == 4) {
                    list = getData("SELECT * FROM " + tableName + " WHERE YIL='" + YEAR + "' AND TYPE='" + TYPE + "'", connection);
                    update = true;

                } else if (DAY.length() == 0 && MONTH.length() == 0 && YEAR.length() == 0) {
                    list = getData("SELECT * FROM " + tableName + " WHERE YIL='" + YIL + "' AND TYPE='" + TYPE + "'", connection);
                    update = true;
                }

                if (update && !list2.equals(list) || firstTime) {
                    firstTime = false;
                    update = false;
                    list2 = list;

                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                    gbc.weightx = 0.5;
                    gbc.gridx = 0;
                    gbc.gridy = -1;

                    gbc.insets = new Insets(5, 10, 5, 15);
                    messagesPanel.removeAll();
                    for (Object p : list) {
                        Map penaltyMap = (Map) p;

                        String DATE = valueOf(penaltyMap, "KUN") + "." + valueOf(penaltyMap, "OY") + "." + valueOf(penaltyMap, "YIL");
                        messagesPanel.add(message(DATE, valueOf(penaltyMap, "TEXT"), valueOf(penaltyMap, "ID"), tableName, valueOf(penaltyMap, "FILE"), connection, fontSize), gbc);
                    }
                    messagesPanel.revalidate();
                    messagesPanel.repaint();

                }

            }

        };
    }

    private static boolean isNumber(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
