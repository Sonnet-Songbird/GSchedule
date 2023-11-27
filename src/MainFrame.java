import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private DefaultListModel<Object> todoListModel;
    private DefaultListModel<Object> gameListModel;
    private JList<Object> todoJList;
    private JList<Object> gameJList;
    private InfoPanel todoInfo;
    private InfoPanel gameInfo;
    private JTabbedPane tabbedPane;

    private ArrayList<Game> games;
    private ArrayList<Todo> todos;

    public MainFrame() {
        setTitle("GSchedule");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(700, 500);

        todoListModel = new DefaultListModel<>();
        gameListModel = new DefaultListModel<>();
        todoJList = new JList<>(todoListModel);
        todoJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameJList = new JList<>(gameListModel);
        gameJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane todoList = new JScrollPane(todoJList);
        todoList.setPreferredSize(new Dimension(100, 150));
        JScrollPane gameList = new JScrollPane(gameJList);
        gameList.setPreferredSize(new Dimension(100, 150));

        todoInfo = new TodoInfo();
        gameInfo = new GameInfo();
        todoInfo.setPreferredSize(new Dimension(200, 150));
        gameInfo.setPreferredSize(new Dimension(200, 150));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeItem();
            }
        });

        JButton countButton = new JButton("Count");
        countButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countItems();
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetItem();
            }
        });

        JButton resetAllButton = new JButton("Reset All");
        resetAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetAllItems();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(countButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(resetAllButton);

        add(buttonPanel, BorderLayout.EAST);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Game", createGamePanel());
        tabbedPane.addTab("TODO", createTodoPanel());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(resetAllButton, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        games = new ArrayList<>();
        todos = new ArrayList<>();
    }

    private JPanel createGamePanel() {
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.add(gameInfo.getInfoPanel(), BorderLayout.WEST);
        gamePanel.add(gameJList, BorderLayout.CENTER);
        return gamePanel;
    }
    private JPanel createTodoPanel() {
        JPanel todoPanel = new JPanel(new BorderLayout());
        todoPanel.add(todoInfo.getInfoPanel(), BorderLayout.WEST);
        todoPanel.add(todoJList, BorderLayout.CENTER);
        return todoPanel;
    }

    private void addItem() {
        String itemName = (String) JOptionPane.showInputDialog(
                this, "아이템을 입력하세요:", "아이템 추가", JOptionPane.PLAIN_MESSAGE, null, null, "");
        if (itemName != null && !itemName.isEmpty()) {
            if (getSelectedTabIndex() == 0) {
                Game newGame = new Game(itemName);
                games.add(newGame);
                gameListModel.addElement(newGame.getName());
            } else if (getSelectedTabIndex() == 1) {
                todos.add(newTodo);
                todoListModel.addElement(newTodo.getTask());
            }
        }
    }


    public void addGame(Game newGame) {
        games.add(newGame);
        gameListModel.addElement(newGame.getName());
    }

    private void removeItem() {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex != -1) {
            int confirm = JOptionPane.showConfirmDialog(
                    this, "이 아이템을 삭제하시겠습니까?", "아이템 삭제", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (getSelectedTabIndex() == 0) {
                    games.remove(selectedIndex);
                    gameListModel.remove(selectedIndex);
                } else if (getSelectedTabIndex() == 1) {
                    todos.remove(selectedIndex);
                    todoListModel.remove(selectedIndex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "삭제할 아이템을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void countItems() {
        int itemCount;
        if (getSelectedTabIndex() == 0) {
            itemCount = gameListModel.getSize();
        } else {
            itemCount = todoListModel.getSize();
        }
        JOptionPane.showMessageDialog(this, "아이템 수: " + itemCount, "아이템 수 세기", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetItem() {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex != -1) {
            if (getSelectedTabIndex() == 0) {
                gameListModel.setElementAt("리셋", selectedIndex);
            } else if (getSelectedTabIndex() == 1) {
                todoListModel.setElementAt("리셋", selectedIndex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "리셋할 아이템을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetAllItems() {
        int confirm = JOptionPane.showConfirmDialog(
                this, "모든 아이템을 리셋하시겠습니까?", "모두 리셋", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // 모든 item의 객체에 순회하며 reset()
        }
    }

    private int getSelectedTabIndex() {
        return tabbedPane.getSelectedIndex();
    }

    private int getSelectedIndex() {
        if (getSelectedTabIndex() == 0) {
            return gameJList.getSelectedIndex();
        } else {
            return todoJList.getSelectedIndex();
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
