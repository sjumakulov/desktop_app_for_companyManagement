package yutong.BODY;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.sql.Connection;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import static yutong.BODY.EMPLOYEE.dataCell;
import static yutong.BODY.EMPLOYEE.dataCellProgress;
import static yutong.BODY.mouseAdapters.mouseAdapterForCarTools;
import static yutong.BODY.mouseAdapters.mouseAdapterForCarCollapsible;
import static yutong.tools.DBManager.valueOf;
import static yutong.tools.buttons.icon;

public class CAR {

    public static JPanel CAR(Map carMap, String number, String iconName, Connection connection, int fontSize) {

        JPanel head = head(valueOf(carMap, "NUM"), iconName, fontSize);

        JPanel body = body(carMap, connection, fontSize);

        JPanel CAR = new JPanel(new GridBagLayout());
        CAR.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;

        JLabel numLabel = new JLabel(number);
        numLabel.setForeground(Color.decode("#bdbdbd"));
        numLabel.setFont(new Font(null, Font.PLAIN, fontSize));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5);
        CAR.add(numLabel, gbc);

        gbc.gridx = 1;
        CAR.add(head, gbc);

        gbc.insets = new Insets(0, 9, 0, 0);
        gbc.gridy = 1;
        CAR.add(body, gbc);

        return CAR;
    }

    public static JPanel head(String carNum, String iconName, int fontSize) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenSize.getWidth() * 0.2145);

        JLabel line = new JLabel();
        line.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#bdbdbd")));

        JPanel lineContainer = new JPanel(new GridBagLayout());
        lineContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridy = 0;
        gbc.gridx = 0;
        lineContainer.add(line, gbc);

        JPanel head = new JPanel(new BorderLayout());
        head.setPreferredSize(new Dimension(w, 30));
        head.setOpaque(false);

        JLabel icon = icon(iconName, "Очиш", mouseAdapterForCarCollapsible(head, fontSize), fontSize);
        icon.setText(carNum + " ");
        icon.setFont(new Font(null, Font.BOLD, fontSize));
        icon.setForeground(Color.decode("#545454"));

        head.add(icon, BorderLayout.LINE_START);
        head.add(lineContainer, BorderLayout.CENTER);

        return head;
    }

    public static JPanel body(Map carMap, Connection connection, int fontSize) {
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.white);
        body.setVisible(false);
        body.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.decode("#dbdbdb")));

        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setBackground(Color.white);
        dataPanel.setBorder(new EmptyBorder(0, 0, 5, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 15, 15, 60);
        gbc.weightx = 0.5;

        gbc.gridx = 0;
        gbc.gridy = 0;
        dataPanel.add(dataCell("Давлат рақами", valueOf(carMap, "NUM"), "", fontSize), gbc);

        gbc.gridy = 1;
        dataPanel.add(dataCell("Русуми", valueOf(carMap, "BRAND"), "", fontSize), gbc);

        gbc.gridy = 2;
        dataPanel.add(dataCell("Фирма номи", valueOf(carMap, "COMPANY"), "", fontSize), gbc);

        gbc.gridy = 3;
        dataPanel.add(dataCell("1 - масул шахс", valueOf(carMap, "RESP1"), "", fontSize), gbc);

        gbc.gridy = 4;
        dataPanel.add(dataCell("2 - масул шахс", valueOf(carMap, "RESP2"), "", fontSize), gbc);

        gbc.gridy = 5;
        dataPanel.add(dataCellProgress("Тахогров хужжати ва муддати", valueOf(carMap, "TAXOGROVEXP"), valueOf(carMap, "TAXOGROVSTART"), valueOf(carMap, "TAXOGROVFILE"), fontSize), gbc);

        gbc.gridy = 6;
        gbc.gridwidth = 2;
        dataPanel.add(dataCell("Йўналиш номи, харитаси ва паспорти", valueOf(carMap, "YUNALISH"), valueOf(carMap, "YUNALISHFILE"), fontSize), gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        dataPanel.add(dataCell("Хитой завод хужжатлари", "", valueOf(carMap, "XITOYFILE"), fontSize), gbc);

        gbc.gridy = 1;
        dataPanel.add(dataCell("Умумий холат", valueOf(carMap, "UX"), valueOf(carMap, "UXFILE"), fontSize), gbc);

        gbc.gridy = 2;
        dataPanel.add(dataCell("Ходовой холати ва ремонти", valueOf(carMap, "XODOVOY"), valueOf(carMap, "XODOVOYFILE"), fontSize), gbc);

        gbc.gridy = 3;
        dataPanel.add(dataCell("Балон", valueOf(carMap, "BALON"), valueOf(carMap, "BALONFILE"), fontSize), gbc);

        gbc.gridy = 4;
        dataPanel.add(dataCell("Салон", valueOf(carMap, "SALON"), valueOf(carMap, "SALONFILE"), fontSize), gbc);

        gbc.gridy = 5;
        dataPanel.add(dataCell("Богажник", valueOf(carMap, "BOGAJ"), valueOf(carMap, "BOGAJFILE"), fontSize), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        dataPanel.add(dataCell("Боковой ойна", valueOf(carMap, "BOKOVOY"), valueOf(carMap, "BOKOVOYFILE"), fontSize), gbc);

        gbc.gridy = 1;
        dataPanel.add(dataCell("Лобовой ойна", valueOf(carMap, "LOBOVOY"), valueOf(carMap, "LOBOVOYFILE"), fontSize), gbc);

        gbc.gridy = 2;
        dataPanel.add(dataCell("Кузов", valueOf(carMap, "KUZOV"), valueOf(carMap, "KUZOVFILE"), fontSize), gbc);

        gbc.gridy = 3;
        dataPanel.add(dataCell("Лицензия АТ №", valueOf(carMap, "ATNUM"), valueOf(carMap, "ATFILE"), fontSize), gbc);

        gbc.gridy = 4;
        dataPanel.add(dataCellProgress("Тех. кўрик муддати", valueOf(carMap, "TEXKEXP"), valueOf(carMap, "TEXKSTART"), valueOf(carMap, "TEXKFILE"), fontSize), gbc);

        gbc.gridy = 5;
        dataPanel.add(dataCellProgress("Газбалон муддати", valueOf(carMap, "GAZEXP"), valueOf(carMap, "GAZSTART"), valueOf(carMap, "GAZFILE"), fontSize), gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        dataPanel.add(dataCellProgress("Посажир суғурта муддати", valueOf(carMap, "POSAJIRSEXP"), valueOf(carMap, "POSAJIRSSTART"), valueOf(carMap, "POSAJIRSFILE"), fontSize), gbc);

        gbc.gridy = 1;
        dataPanel.add(dataCellProgress("GPS рухсатнома муддати", valueOf(carMap, "GPSEXP"), valueOf(carMap, "GPSSTART"), valueOf(carMap, "GPSFILE"), fontSize), gbc);

        gbc.gridy = 2;
        dataPanel.add(dataCellProgress("Ижара шартнома муддати", valueOf(carMap, "IJARASHEXP"), valueOf(carMap, "IJARASHSTART"), valueOf(carMap, "IJARASHFILE"), fontSize), gbc);

        gbc.gridy = 3;
        dataPanel.add(dataCellProgress("Литсензия АТ муддати", valueOf(carMap, "ATEXP"), valueOf(carMap, "ATSTART"), "", fontSize), gbc);

        gbc.gridy = 4;
        dataPanel.add(dataCellProgress("СП муддати", valueOf(carMap, "SPEXP"), valueOf(carMap, "SPSTART"), valueOf(carMap, "SPFILE"), fontSize), gbc);

        gbc.gridy = 5;
        dataPanel.add(dataCellProgress("Двигател мойи муддати", valueOf(carMap, "ENGINEEXP"), valueOf(carMap, "ENGINESTART"), "", fontSize), gbc);

        body.add(dataPanel, BorderLayout.CENTER);
        body.add(toolsPanel(valueOf(carMap, "ID"), connection, fontSize), BorderLayout.LINE_END);

        return body;
    }

    public static JPanel toolsPanel(String NUM, Connection connection, int fontSize) {
        JPanel toolsPanel = new JPanel(new GridLayout(1, 4, 15, 0));

        toolsPanel.add(icon("pencil", "Ўзгартириш", mouseAdapterForCarTools(connection), fontSize));
        toolsPanel.add(icon("download", "Хужжатларни юклаб олиш", mouseAdapterForCarTools(connection), fontSize));
        toolsPanel.add(icon("bin", "Ўчириш", mouseAdapterForCarTools(connection), fontSize));
        toolsPanel.setBackground(Color.white);
        toolsPanel.setName(NUM);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.white);
        mainPanel.add(toolsPanel, BorderLayout.PAGE_START);

        return mainPanel;
    }

}
