package yutong;

import java.awt.Color;
import java.awt.FlowLayout;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static yutong.tools.DBManager.getFontSize;
import yutong.tools.SearchBar;
import static yutong.tools.buttons.menuButton;

public class Header extends JPanel {

    public Header(JPanel BODY, Connection connection) {
        int fontSize = getFontSize(connection);
        
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        setBackground(Color.decode("#f2f2f2"));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#005780")));

        SearchBar SB = new SearchBar(getComponent(BODY, 0), getComponent(BODY, 1), fontSize);

        add(menuButton("Ходимлар", "EMPLOYEES", SB, connection, fontSize));
        add(menuButton("Автобуслар", "CARS", SB, connection, fontSize));
        add(menuButton("Хатлар", "LETTERS", SB, connection, fontSize));
        add(menuButton("Склад", "STOCK", SB, connection, fontSize));

        add(menuButton("Маълумот қўшиш", "add", null, connection, fontSize));
        add(menuButton("Маълумотларни 'excelda' да олиш", "excel", null, connection, fontSize));
        add(menuButton("Масштабни ўзгартириш", "size", null, connection, fontSize));
        add(SB);

    }

    private static JPanel getComponent(JPanel BODY, int index) {
        JScrollPane pane = (JScrollPane) BODY.getComponent(index);
        JPanel panelForScrollPane = (JPanel) pane.getViewport().getComponent(0);
        JPanel p = (JPanel) panelForScrollPane.getComponent(0);

        return p;
    }
}
