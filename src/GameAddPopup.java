//Todo: 선택 입력 사항 폰트 수정

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;

public class GameAddPopup extends JDialog {
    private JTextField nameField;
    private JComboBox<DayOfWeek> dayOfWeekComboBox;
    private JTextField resetHourField;

    public GameAddPopup(MainFrame parentFrame) {
        super(parentFrame, "게임 추가", true);
        setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("게임 이름:");
        nameField = new JTextField();

        JLabel dayOfWeekLabel = new JLabel("리셋 요일:");
        dayOfWeekComboBox = new JComboBox<>(DayOfWeek.values());

        JLabel resetHourLabel = new JLabel("리셋 시간:");
        resetHourField = new JTextField();

        add(nameLabel);
        add(nameField);
        add(dayOfWeekLabel);
        add(dayOfWeekComboBox);
        add(resetHourLabel);
        add(resetHourField);

        JButton addButton = new JButton("게임 추가");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game newGame = getGame();
                if (newGame != null) {
                    parentFrame.addGame(newGame);
                    dispose();
                }
            }
        });

        add(addButton);

        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        setVisible(true);
    }

    public Game getGame() {
        String name = nameField.getText();
        DayOfWeek resetDoW = (DayOfWeek) dayOfWeekComboBox.getSelectedItem();

        int resetHour = 0;// 기본값
        String resetHourText = resetHourField.getText();

        if (name != null && !name.trim().isEmpty()) {
            if (!resetHourText.isEmpty()) {
                if (isInteger(resetHourText)) {
                    resetHour = Integer.parseInt(resetHourText);
                } else {
                    JOptionPane.showMessageDialog(this, "유효하지 않은 숫자입니다.");
                    return null;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "이름은 필수 항목입니다.");
            return null;
        }

        return new Game(name, resetDoW, resetHour);
    }

    private boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame.getInstance();
            }
        });
    }
}
