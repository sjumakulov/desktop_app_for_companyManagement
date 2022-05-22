package yutong.BODY;

import yutong.BODY.messagesPanel.messagesPanel;
import java.awt.Color;
import java.sql.Connection;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static yutong.tools.MyScrollBarUI.myScrollBarUI;
import yutong.tools.ScrollablePanel;

public class MESSENGER {

    public static JScrollPane MESSENGER(String tableName, Connection connection, int fontSize) {

        JPanel MESSENGER = new ScrollablePanel();
        MESSENGER.setLayout(new ScrollablePanel.BetterFlowLayout());
        MESSENGER.setOpaque(false);

        MESSENGER.add(new messagesPanel(tableName, "IN", connection, fontSize));
        MESSENGER.add(new messagesPanel(tableName, "OUT", connection, fontSize));

        JScrollPane scrollPane = new JScrollPane(MESSENGER);
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.getVerticalScrollBar().setUnitIncrement(26);

        scrollPane.getVerticalScrollBar().setUI(myScrollBarUI());

        return scrollPane;
    }
}
