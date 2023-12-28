import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.util.Optional;

public class GameAddPopup extends JDialog {
    private JTextField nameField;
    private JComboBox<DayOfWeek> dayOfWeekComboBox;
    private JSpinner resetHourSpinner;

    public GameAddPopup(MainFrame parentFrame) {
        super(parentFrame, "게임 추가", true);
        setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("게임 이름:");
        nameField = new JTextField();

        JLabel dayOfWeekLabel = new JLabel("리셋 요일:");
        dayOfWeekComboBox = new JComboBox<>(DayOfWeek.values());

        JLabel resetHourLabel = new JLabel("리셋 시간:");
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 23, 1);
        resetHourSpinner = new JSpinner(model);

        add(nameLabel);
        add(nameField);
        add(dayOfWeekLabel);
        add(dayOfWeekComboBox);
        add(resetHourLabel);
        add(resetHourSpinner);

        JButton addButton = new JButton("게임 추가");
        JButton cancelButton = new JButton("취소");

        addButton.addActionListener(e -> getGame().ifPresent(newGame -> {
            parentFrame.addGame(newGame);
            dispose();
        }));
        cancelButton.addActionListener(e -> dispose());

        add(addButton);
        add(cancelButton);

        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        setVisible(true);
    }

    public Optional<Game> getGame() {
        String name = nameField.getText();
        DayOfWeek resetDoW = (DayOfWeek) dayOfWeekComboBox.getSelectedItem();
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름은 필수 항목입니다.");
            return Optional.empty();
        }
        int resetHour = (int) resetHourSpinner.getValue();

        return Optional.of(new Game(name, resetDoW, resetHour));
    }
}
