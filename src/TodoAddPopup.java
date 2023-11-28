import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TodoAddPopup extends JDialog {
    private final JTextField nameField;
    private final JComboBox<Object> gameComboBox;
    private final JComboBox<Todo.ResetType> resetTypeComboBox;
    private final JTextField goalField;

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
        goalField = new JTextField();

        add(nameLabel);
        add(nameField);
        add(gameLabel);
        add(gameComboBox);
        add(resetTypeLabel);
        add(resetTypeComboBox);
        add(goalLabel);
        add(goalField);

        JButton addButton = new JButton("게임 추가");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Todo newTodo = getTodo();
                if (newTodo != null) {
                    parentFrame.addTodo(newTodo);
                    dispose();
                }
            }
        });

        add(addButton);

        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        setVisible(true);
    }

    public Todo getTodo() {
        String name = nameField.getText();
        Game selectedGame = (Game) gameComboBox.getSelectedItem();
        Todo.ResetType resetType = (Todo.ResetType) resetTypeComboBox.getSelectedItem();
        int goal = 1; // 기본값
        String goalText = goalField.getText();

        if (name != null && !name.trim().isEmpty() && selectedGame != null) {
            if (!goalText.isEmpty()) {
                if (isInteger(goalText)) {
                    goal = Integer.parseInt(goalText);
                } else {
                    JOptionPane.showMessageDialog(this, "유효하지 않은 숫자입니다.");
                    return null;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "이름과 게임은 필수 항목입니다.");
            return null;
        }

        return new Todo(selectedGame, name, goal, resetType);
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
                new MainFrame();
            }
        });
    }
}
