package print.OneDayPutyovka;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import static print.backPage.cell;

public class backPage {

    public static JPanel backPage() {
        JPanel frontPage = new JPanel(new GridLayout(1, 2, 10, 0));
        frontPage.setPreferredSize(new Dimension(813, 561));
        frontPage.setBackground(Color.white);
        frontPage.setName("Путёвка олд тараф");

        JPanel whiteSpace = new JPanel();
        whiteSpace.setOpaque(false);

        frontPage.add(left());
        frontPage.add(whiteSpace);

        return frontPage;
    }

    private static JPanel left() {
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);

        left.add(table(), BorderLayout.PAGE_START);
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

    private static JPanel table() {

        String[] dates = dates();
        System.out.println(Arrays.toString(dates));

        JPanel table = new JPanel(new GridBagLayout());
        table.setBackground(Color.white);
        table.setBorder(BorderFactory.createLineBorder(Color.black));

        GridBagConstraints gbc = new GridBagConstraints();
        String[] headers = {"Сана", "Хайдовчи Ахволи", "Транспорт ҳолати", "Кунлик тўлов", "Чиқиш вақти", "Кириш вақти",};

        for (int row = 0; row <= 4; row++) {
            for (int col = 0; col < 6; col++) {
                gbc.gridx = col;
                gbc.gridy = row;
                if (row == 0) {
                    table.add(cell(headers[col], "bold", col), gbc);
                } else if (row != 4) {
                    table.add(cellTextField("plain", col, dates[row - 1]), gbc);
                } else {
                    table.add(cellTextField("lastRow", col, dates[row - 1]), gbc);
                }
            }
        }

        return table;
    }

    public static JTextField cellTextField(String type, int colNumber, String date) {
        JTextField cell = new JTextField(SwingConstants.CENTER);

        if (type.equals("lastRow")) {
            if (colNumber != 5) {
                cell.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
            } else {
                cell.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
            }
            if (colNumber == 0) {
                cell.setText(date);
            }
        } else {
            if (colNumber == 5) {
                cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
            } else {
                cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));

            }
            if (colNumber == 0) {
                cell.setText(date);
            }
        }

        cell.setFont(new Font("Calibri", Font.PLAIN, 11));

        int width = 0;
        int height = 43;

        switch (colNumber) {
            case 0 -> {
                width = 55;
                break;
            }
            case 1, 2, 3 -> {
                width = 88;
                break;
            }
            case 4, 5 -> {
                width = 40;
                break;
            }
        }

        cell.setPreferredSize(new Dimension(width, height));

        return cell;
    }

    public static String[] dates() {
        String[] dates = new String[4];

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);

        for (int i = 0; i < 4; i++) {
            cal.set(2022, cal.get(Calendar.MONTH), day + i, 0, 0);

            dates[i] = df.format(cal.getTime());
        }

        return dates;
    }

}
