package popups;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import static popups.AddCarPopup.showAddCarPopup;
import static popups.AddEmployeePopup.showEmployeePopup;

public class addIconMenu {

    public static void showAddMenu(JComponent iconLabel, MouseEvent e, Connection connection) {
        JFrame FRAME = (JFrame) SwingUtilities.getRoot(iconLabel);
        
        
        JPopupMenu menu = new JPopupMenu();
        menu.removeAll();
        menu.setBorder(BorderFactory.createLineBorder(Color.gray));
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JMenuItem carMenuItem = menuItem("+ Автомобил");
        JMenuItem employeeMenuItem = menuItem("+ Ходим");

        carMenuItem.addActionListener((ActionEvent e1) -> {
            showAddCarPopup(FRAME, connection);
        });

        employeeMenuItem.addActionListener((ActionEvent e1) -> {
            showEmployeePopup(FRAME, connection);
        });

        menu.add(employeeMenuItem);
        menu.add(carMenuItem);
        menu.show(iconLabel, e.getX() - 150, e.getY());
    }

    public static JMenuItem menuItem(String name) {
        JMenuItem item = new JMenuItem(name);
        item.setBackground(Color.white);
        item.setOpaque(true);

        return item;
    }
}
