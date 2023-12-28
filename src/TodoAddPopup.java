import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class TodoAddPopup extends JDialog {
    private final JTextField nameField;
    private final JComboBox<Object> gameComboBox;
    private final JComboBox<Todo.ResetType> resetTypeComboBox;
    private final JSpinner goalSpinner;

    public TodoAddPopup(MainFrame parentFrame, DefaultListModel<Object> games) {
        super(parentFrame, "할 일 추가", true);
        setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("할 일 이름:");
        nameField = new JTextField();

        JLabel gameLabel = new JLabel("게임:");
        gameComboBox = new JComboBox<>(games.toArray());

        JLabel resetTypeLabel = new JLabel("리셋 타입:");
        resetTypeComboBox = new JComboBox<>(Todo.ResetType.values());

        JLabel goalLabel = new JLabel("목표 횟수:");
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        goalSpinner = new JSpinner(model);

        add(nameLabel);
        add(nameField);
        add(gameLabel);
        add(gameComboBox);
        add(resetTypeLabel);
        add(resetTypeComboBox);
        add(goalLabel);
        add(goalSpinner);

        JButton addButton = new JButton("게임 추가");
        JButton cancelButton = new JButton("취소");

        addButton.addActionListener(e -> getTodo().ifPresent(newTodo -> {
            parentFrame.addTodo(newTodo);
            dispose();
        }));
        cancelButton.addActionListener(e -> dispose());

        add(addButton);

        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        setVisible(true);
    }

    public Optional<Todo> getTodo() {
        String name = nameField.getText();
        Game selectedGame = (Game) gameComboBox.getSelectedItem();
        Todo.ResetType resetType = (Todo.ResetType) resetTypeComboBox.getSelectedItem();
        int goal = (int) goalSpinner.getValue();

        if (name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름은 필수 항목입니다.");
            return Optional.empty();
        }
        if (selectedGame == null) {
            JOptionPane.showMessageDialog(this, "게임은 필수 항목입니다.");
            return Optional.empty();
        }

        return Optional.of(new Todo(selectedGame, name, goal, resetType));
    }

}
