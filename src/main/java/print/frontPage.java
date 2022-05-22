package print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import static print.backPage.cell;
import static yutong.tools.DBManager.getData;
import static yutong.tools.DBManager.valueOf;

public class frontPage {

    public static JPanel frontPage(String monthIndex, String ID, Connection connection) {
        JPanel frontPage = new JPanel(new GridLayout(1, 2, 10, 0));
        frontPage.setPreferredSize(new Dimension(813, 561));
        frontPage.setBackground(Color.white);
        frontPage.setName("Путёвка олд тараф");

        frontPage.add(left(monthIndex));
        frontPage.add(right(monthIndex, ID, connection));

        return frontPage;
    }

    private static JPanel right(String monthIndex, String ID, Connection connection) {
        ArrayList driverList = getData("SELECT * FROM EMPLOYEES WHERE ID=" + "'" + ID + "'", connection);
        Map driverMap = (Map) driverList.get(0);

        ArrayList otherList = getData("SELECT * FROM OTHER WHERE ID = 'PUTYOVKANUM'", connection);
        Map otherMap = (Map) otherList.get(0);
        int putyovkaNum = Integer.parseInt(otherMap.get("VALUE").toString()) + 1;

        JPanel right = new JPanel(new GridBagLayout());
        right.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 5, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        right.add(borderlessLabel("«ERKIN EMINA EXPRESS TOUR» МЧЖ", 15), gbc);

        gbc.gridy = 1;

        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        right.add(borderlessLabel("Йўл Варақаси № " + putyovkaNum, 13), gbc);

        gbc.insets = new Insets(0, 5, 10, 0);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        right.add(borderlessLabel(dateText(monthIndex), 13), gbc);

        gbc.insets = new Insets(0, 5, 5, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 3;
        right.add(image(), gbc);

        // bottom table part:
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 4;
        right.add(borderLabel("Йўналиш номи", "", 1, 1, 0, 1, connection), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        right.add(borderLabel("Ҳайдовчи", valueOf(driverMap, "ISM"), 1, 1, 0, 1, connection), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        right.add(borderLabel("Автомобил русуми", "", 1, 1, 0, 1, connection), gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        right.add(borderLabel("Давлат рақами", "", 1, 1, 0, 1, connection), gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        right.add(borderLabel("Ҳайдовчи гувоҳномаси", valueOf(driverMap, "HGNUMBER"), 1, 1, 0, 1, connection), gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        right.add(borderLabel("Тоифаси", valueOf(driverMap, "HGTOIFA"), 1, 1, 0, 1, connection), gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        right.add(borderLabel("Лицензия АТ №", "", 1, 1, 0, 1, connection), gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 1;
        gbc.gridy = 10;
        right.add(borderLabel("Муддати", "", 1, 0, 0, 1, connection), gbc);

        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        right.add(borderLabel("Ижара шартнома муддати", "", 1, 1, 0, 1, connection), gbc);

        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 1;
        right.add(borderLabel("СП", "", 1, 1, 0, 1, connection), gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 1;
        gbc.gridy = 12;
        right.add(borderLabel("ГАЗ", "", 1, 0, 0, 1, connection), gbc);

        gbc.insets = new Insets(0, 5, 50, 0);
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        right.add(borderLabel("Тех. Курик", "", 1, 1, 1, 1, connection), gbc);

        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy = 14;
        right.add(borderlessLabel("Диспетчер: _____________________ М.П", 15), gbc);

        return right;
    }

    private static JPanel borderLabel(String labelText, String contentText, int top, int left, int bottom, int right, Connection connection) {

        JLabel label = new JLabel(labelText + ":");
        label.setFont(new Font("Calibri", Font.BOLD, 12));

        JTextArea dataPoint = new JTextArea(contentText);
        dataPoint.setFont(new Font("Calibri", Font.PLAIN, 12));
        dataPoint.setBorder(null);

        dataPoint.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

                String NUM = dataPoint.getText();

                if (!NUM.isBlank()) {
                    NUM = NUM.toUpperCase().replaceAll(" ", "");
                    dataPoint.setText(NUM);

                    NUM = NUM.replaceAll(" ", "");

                    ArrayList list = getData("SELECT * FROM CARS WHERE NUM = '" + NUM + "'", connection);

                    if (!list.isEmpty()) {
                        Map carMap = (Map) list.get(0);

                        JPanel container = (JPanel) dataPoint.getParent();
                        JPanel right = (JPanel) container.getParent();

                        setBorderLabelContent(right, valueOf(carMap, "YUNALISH"), 4);
                        setBorderLabelContent(right, valueOf(carMap, "BRAND"), 6);
                        setBorderLabelContent(right, valueOf(carMap, "ATNUM"), 10);
                        setBorderLabelContent(right, valueOf(carMap, "ATEXP"), 11);
                        setBorderLabelContent(right, valueOf(carMap, "IJARASHEXP"), 12);
                        setBorderLabelContent(right, valueOf(carMap, "SPEXP"), 13);
                        setBorderLabelContent(right, valueOf(carMap, "GAZEXP"), 14);
                        setBorderLabelContent(right, valueOf(carMap, "TEXKEXP"), 15);
                    } else {

                        JPanel container = (JPanel) dataPoint.getParent();
                        JPanel right = (JPanel) container.getParent();

                        setBorderLabelContent(right, "", 4);
                        setBorderLabelContent(right, "", 6);
                        setBorderLabelContent(right, "", 10);
                        setBorderLabelContent(right, "", 11);
                        setBorderLabelContent(right, "", 12);
                        setBorderLabelContent(right, "", 13);
                        setBorderLabelContent(right, "", 14);
                        setBorderLabelContent(right, "", 15);

                    }
                }

            }
        });

        if (labelText.equals("Давлат рақами")) {
            dataPoint.setPreferredSize(new Dimension(100, 12));
        } else {
            dataPoint.setEditable(false);
        }

        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEADING));
        container.setOpaque(false);

        container.add(label);
        container.add(dataPoint);

        container.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.black));

        return container;
    }

    private static void setBorderLabelContent(JPanel right, String content, int index) {
        JPanel borderLabel = (JPanel) right.getComponent(index);
        JTextArea dataPoint = (JTextArea) borderLabel.getComponent(1);
        dataPoint.setText(content);
    }

    private static JTextArea borderlessLabel(String text, int fontSize) {
        JTextArea label = new JTextArea();
        label.setFont(new Font("Calibri", Font.PLAIN, fontSize));
        label.setBorder(null);
        label.setText(text);

        return label;
    }

    private static JLabel image() {
        ImageIcon carPNG = new ImageIcon("./resources/image.PNG");

        carPNG = new ImageIcon(carPNG.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH));

        JLabel label = new JLabel();
        label.setIcon(carPNG);

        return label;
    }

    private static JPanel left(String monthIndex) {
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);

        Calendar cal = Calendar.getInstance();
        int month = Integer.parseInt(monthIndex);

        cal.set(Calendar.MONTH, month);
        int numOfDays = cal.getActualMaximum(Calendar.DATE);
        SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        ArrayList dates = new ArrayList();

        for (int day = 25; day <= numOfDays; day++) {
            cal.set(Calendar.DATE, day);
            dates.add(dateFormater.format(cal.getTime()));
        }

        left.add(table(dates), BorderLayout.PAGE_START);
        left.add(bottom(), BorderLayout.PAGE_END);

        return left;
    }

    private static JPanel bottom() {
        Font font = new Font("Calibri", Font.PLAIN, 15);
        String warningText1 = "Ижара солиғини ўз вақтида тўлашни унутманг!";
        String warningText2 = "Алохида белгилар ва огоҳлантиришлар:";

        JLabel warningLabel1 = new JLabel(warningText1, SwingConstants.CENTER);
        JLabel warningLabel2 = new JLabel(warningText2, SwingConstants.CENTER);

        warningLabel1.setFont(font);
        warningLabel2.setFont(font);

        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setOpaque(false);
        bottom.setPreferredSize(new Dimension(100, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        bottom.add(warningLabel1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        bottom.add(warningLabel2, gbc);

        gbc.weightx = 0.5;
        gbc.insets = new Insets(20, 0, 0, 0);
        for (int y = 2; y < 5; y++) {
            JLabel line = new JLabel();
            line.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.gray));
            line.setPreferredSize(new Dimension(100, 20));

            gbc.gridx = 0;
            gbc.gridy = y;
            bottom.add(line, gbc);
        }

        return bottom;
    }

    private static JPanel table(ArrayList dates) {
        int numOfRows = dates.size();

        JPanel table = new JPanel(new GridBagLayout());
        table.setBackground(Color.white);
        table.setBorder(BorderFactory.createLineBorder(Color.black));

        GridBagConstraints gbc = new GridBagConstraints();
        String[] headers = {"Сана", "Хайдовчи Ахволи", "Транспорт ҳолати", "Кунлик тўлов", "Чиқиш вақти", "Кириш вақти",};

        for (int row = 0; row <= numOfRows; row++) {
            for (int col = 0; col < 6; col++) {
                gbc.gridx = col;
                gbc.gridy = row;
                if (row == 0) {
                    table.add(cell(headers[col], "bold", col), gbc);
                } else if (row != numOfRows) {
                    if (col == 0) {
                        table.add(cell(dates.get(row - 1).toString(), "plain", col), gbc);
                    } else {
                        table.add(cell("", "plain", col), gbc);
                    }

                } else {
                    if (col == 0) {
                        table.add(cell(dates.get(row - 1).toString(), "lastRow", col), gbc);
                    } else {
                        table.add(cell("", "lastRow", col), gbc);
                    }

                }
            }
        }

        return table;
    }

    private static String dateText(String monthIndex) {
        int month = Integer.parseInt(monthIndex);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        String[] monthNames = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

        String date = "«" + monthNames[month] + "» " + year + " йил";

        return date;
    }

}
