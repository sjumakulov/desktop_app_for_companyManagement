package yutong.BODY;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import static yutong.tools.MyScrollBarUI.myScrollBarUI;
import static yutong.BODY.CAR.CAR;
import static yutong.tools.DBManager.getData;

public class CARS {

    public static JScrollPane CARS(Connection connection, int fontSize) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenSize.getWidth() * 0.05);

        JPanel CARS = new JPanel(new GridBagLayout());
        CARS.setBorder(new EmptyBorder(15, 0, 5, 0));
        CARS.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, w, 0, 0);
        gbc.weightx = 0.5;

        gbc.gridx = 0;
        gbc.gridy = -1;

        ArrayList carsList = getData("SELECT * FROM CARS", connection);

        for (int i = 0; i < carsList.size(); i++) {
            CARS.add(CAR((Map) carsList.get(i), String.valueOf(i + 1), "arrowright", connection, fontSize), gbc);
        }

        JPanel panelForScrollPane = new JPanel(new BorderLayout());
        panelForScrollPane.add(CARS, BorderLayout.PAGE_START);
        panelForScrollPane.setBackground(Color.white);

        JScrollPane scrollPane = new JScrollPane(panelForScrollPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(26);
        scrollPane.getVerticalScrollBar().setUI(myScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(myScrollBarUI());

        return scrollPane;
    }

}
