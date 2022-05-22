package yutong.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchBar extends JTextField {

    public SearchBar(JPanel EMPLOYEES, JPanel CARS, int fontSize) {

        setPreferredSize(new Dimension(200, 30));
        setFont(new Font("Verdana", Font.PLAIN, fontSize));
        setFocusable(false);
        setForeground(Color.gray);
        setText("Излаш...");
        setName("EMPLOYEES");

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals("Излаш...")) {
                    setForeground(Color.black);
                    setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isBlank()) {
                    setForeground(Color.gray);
                    setText("Излаш...");
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setFocusable(true);
                requestFocus();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                switch (getName()) {
                    case "EMPLOYEES" -> {
                        searchInEmployees(EMPLOYEES, getText().replaceAll(" ", "").toLowerCase());
                        break;
                    }

                    case "CARS" -> {
                        searchInCars(CARS, getText().replaceAll(" ", "").toLowerCase());
                        break;
                    }
                }
            }
        });
    }

    private static void searchInEmployees(JPanel EMPLOYEES, String inputText) {

        for (Object dep : EMPLOYEES.getComponents()) {
            JPanel currentDepartment = (JPanel) (JPanel) dep;
            JPanel departmentBody = (JPanel) (currentDepartment).getComponent(1);

            for (Object emp : departmentBody.getComponents()) {
                JPanel currentEmployee = (JPanel) (JPanel) emp;
                JPanel employeeHead = (JPanel) (currentEmployee).getComponent(1);

                JLabel nameLabel = (JLabel) employeeHead.getComponent(0);
                String currentEmployeeName = nameLabel.getText().replaceAll(" ", "").toLowerCase();

                if (currentEmployeeName.contains(inputText)) {
                    currentEmployee.setVisible(true);

                    currentDepartment.setVisible(true);
                } else {
                    currentEmployee.setVisible(false);
                    currentDepartment.setVisible(false);
                }
            }
        }
    }

    private static void searchInCars(JPanel CARS, String inputText) {

        for (Object emp : CARS.getComponents()) {
            JPanel currentCar = (JPanel) (JPanel) emp;
            JPanel employeeHead = (JPanel) (currentCar).getComponent(1);

            JLabel numLabel = (JLabel) employeeHead.getComponent(0);
            String currentCarNum = numLabel.getText().replaceAll(" ", "").toLowerCase();

            if (currentCarNum.contains(inputText)) {
                currentCar.setVisible(true);

            } else {
                currentCar.setVisible(false);
            }
        }
    }
}
