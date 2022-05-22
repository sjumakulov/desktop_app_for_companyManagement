package yutong.BODY;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import static yutong.tools.MyScrollBarUI.myScrollBarUI;
import static yutong.tools.buttons.icon;
import static yutong.BODY.EMPLOYEE.EMPLOYEE;
import static yutong.BODY.mouseAdapters.mouseAdapterForDepartmentCollapsible;
import static yutong.tools.DBManager.getData;

public class EMPLOYEES {

    public static JScrollPane EMPLOYEES(Connection connection, int fontSize) {

        ArrayList ishdanolinganlar = getData("SELECT * FROM EMPLOYEES WHERE DEPARTMENT='ishdanolingan'", connection);
        ArrayList prikazichiqmaganlar = getData("SELECT * FROM EMPLOYEES WHERE DEPARTMENT='prikazichiqmagan'", connection);
        ArrayList bazaishchilari = getData("SELECT * FROM EMPLOYEES WHERE DEPARTMENT='bazaishchisi'", connection);
        ArrayList haydovchilar = getData("SELECT * FROM EMPLOYEES WHERE DEPARTMENT='haydovchi'", connection);
        ArrayList boshqaruvchilar = getData("SELECT * FROM EMPLOYEES WHERE DEPARTMENT='boshqaruvchi'", connection);
        ArrayList dispetcherlar = getData("SELECT * FROM EMPLOYEES WHERE DEPARTMENT='dispetcher'", connection);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenSize.getWidth() * 0.05);

        JPanel EMPLOYEES = new JPanel(new GridBagLayout());
        EMPLOYEES.setBorder(new EmptyBorder(15, 0, 5, 0));
        EMPLOYEES.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, w, 0, 0);
        gbc.weightx = 0.5;

        gbc.gridx = 0;
        gbc.gridy = -1;

        EMPLOYEES.add(DEPARTMENT("Ҳайдовчилар", haydovchilar, connection, fontSize), gbc);
        EMPLOYEES.add(DEPARTMENT("Диспетчерлар", dispetcherlar, connection, fontSize), gbc);
        EMPLOYEES.add(DEPARTMENT("База ишчилари", bazaishchilari, connection, fontSize), gbc);
        EMPLOYEES.add(DEPARTMENT("Бошқарувчи ҳодимлар", boshqaruvchilar, connection, fontSize), gbc);
        EMPLOYEES.add(DEPARTMENT("Прикази чиқмаганлар", prikazichiqmaganlar, connection, fontSize), gbc);
        EMPLOYEES.add(DEPARTMENT("Ишдан бўшатилганлар", ishdanolinganlar, connection, fontSize), gbc);

        JPanel panelForScrollPane = new JPanel(new BorderLayout());
        panelForScrollPane.add(EMPLOYEES, BorderLayout.PAGE_START);
        panelForScrollPane.setBackground(Color.white);

        JScrollPane scrollPane = new JScrollPane(panelForScrollPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(26);

        scrollPane.getVerticalScrollBar().setUI(myScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(myScrollBarUI());

        return scrollPane;
    }

    private static JPanel DEPARTMENT(String departmentLabel, ArrayList list, Connection connection, int fontSize) {

        JPanel D = new JPanel(new GridBagLayout());
        D.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 0;

        gbc.gridy = 0;
        D.add(departmentHEAD(departmentLabel, fontSize), gbc);

        gbc.insets = new Insets(0, 9, 0, 0);
        gbc.gridy = 1;
        D.add(departmentBODY(list, connection, fontSize), gbc);

        return D;
    }

    private static JPanel departmentHEAD(String departmentLabel, int fontSize) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenSize.getWidth() * 0.22);

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

        JPanel H = new JPanel(new BorderLayout());
        H.setPreferredSize(new Dimension(w, 30));
        H.setOpaque(false);

        JLabel icon = icon("arrowright", "Очиш", mouseAdapterForDepartmentCollapsible(H, fontSize), fontSize);
        icon.setText(departmentLabel + " ");
        icon.setFont(new Font(null, Font.BOLD, fontSize));
        icon.setForeground(Color.decode("#545454"));

        H.add(icon, BorderLayout.LINE_START);
        H.add(lineContainer, BorderLayout.CENTER);

        return H;
    }

    private static JPanel departmentBODY(ArrayList list, Connection connection, int fontSize) {
        JPanel departmentBODY = new JPanel(new GridBagLayout());
        departmentBODY.setOpaque(false);
        departmentBODY.setVisible(false);
        departmentBODY.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.decode("#dbdbdb")));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 0, 0);
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = -1;

        for (int i = 0; i < list.size(); i++) {
            departmentBODY.add(EMPLOYEE((Map) list.get(i), String.valueOf(i + 1), "arrowright", connection, fontSize), gbc);
        }

        departmentBODY.setName("departmentBody");
        return departmentBODY;
    }

}
