import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;

public class GameAddPopup extends JPanel {
    private JTextField nameField;
    private JComboBox<DayOfWeek> dayOfWeekComboBox;
    private JTextField resetHourField;

    public GameAddPopup() {
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

        JFrame frame = new JFrame("게임 추가");
        JButton addButton = new JButton("게임 추가");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game newGame = getGame();
                if (newGame != null) {

                }
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.add(addButton, BorderLayout.SOUTH);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

    }

    public Game getGame() {
        String name = nameField.getText();
        DayOfWeek resetDoW = (DayOfWeek) dayOfWeekComboBox.getSelectedItem();

        int resetHour = 0;
        String resetHourText = resetHourField.getText();

        if (name != null && !name.trim().isEmpty()) {
            if (!resetHourText.isEmpty()) {
                if (isInteger(resetHourText)) {
                    resetHour = Integer.parseInt(resetHourText);
                } else {
                    JOptionPane.showMessageDialog(this, "유효하지 않은 리셋 시간입니다. 유효한 숫자를 입력하세요.");
                    return null;
                }
            } else {
                JOptionPane.showMessageDialog(this, "리셋 시간을 입력하세요.");
                return null;
            }
        } else {
            JOptionPane.showMessageDialog(this, "게임 이름을 입력하세요.");
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
        new GameAddPopup();
    }
}
