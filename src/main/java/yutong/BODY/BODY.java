package yutong.BODY;

import java.awt.CardLayout;
import java.sql.Connection;
import javax.swing.JPanel;
import static yutong.BODY.CARS.CARS;
import static yutong.BODY.EMPLOYEES.EMPLOYEES;
import static yutong.BODY.MESSENGER.MESSENGER;
import static yutong.tools.DBManager.getFontSize;

public class BODY {

    public static JPanel BODY(Connection connection) {
        
        int fontSize = getFontSize(connection);

        JPanel cards = new JPanel(new CardLayout());

        cards.add(EMPLOYEES(connection, fontSize), "EMPLOYEES");
        cards.add(CARS(connection, fontSize), "CARS");
        cards.add(MESSENGER("LETTERS", connection, fontSize), "LETTERS");
        cards.add(MESSENGER("STOCK", connection, fontSize), "STOCK");

        return cards;
    }
}
