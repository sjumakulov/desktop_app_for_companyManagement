package yutong.BODY.messagesPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class header extends JPanel {

    Color headerColor;
    String headerLabel;

    public header(String TYPE, String tableName, JPanel messagesPanel, Connection connection, int fontSize) {
        if (TYPE.equals("IN")) {
            headerColor = Color.decode("#248a3f");
            headerLabel = "Кирим";
        } else {
            headerColor = Color.decode("#7d0606");
            headerLabel = "Чиқим";
        }

        JLabel label = new JLabel(headerLabel);
        label.setForeground(Color.white);
        label.setOpaque(false);
        label.setFont(new Font(null, Font.BOLD, fontSize));
        label.setBorder(new EmptyBorder(0, 5, 0, 0));

        setLayout(new BorderLayout());
        setBackground(headerColor);

        add(label, BorderLayout.LINE_START);
        add(new DateInput(tableName, TYPE, messagesPanel, connection, fontSize), BorderLayout.LINE_END);
    }

}
