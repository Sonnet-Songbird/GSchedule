//Todo: 인포 패널 구현
//Todo: 메인프레임 사이즈 수정(인포패널을 크게)
//Todo: 이하 구현 완료 후 개선사항
//Todo: JList에 출력하는 이름을
//Todo: 정렬,필터 기능, 미완료만 보기 등. 현재 JList 인덱스를 참조하는 방식도 수정해야 함.
//Todo:
//Todo: 다중 선택 및 다중 선택 기반의 기능 구현
/*Todo: --게임--
        ----Todo----
        ----Todo----
        --게임--
        ----Todo---*/
//Todo: 게임 더블 클릭 시 해당 게임 탭을 생성하여 해당 게임의 Todo 모아 보기
//Todo: 로거 사용
//Todo: 미완료 상태로 초기화된 경우 알려주기?

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private static MainFrame mainFrame;
    private final DefaultListModel<Object> todoListModel;
    private final DefaultListModel<Object> gameListModel;
    private final JList<Object> todoJList;
    private final JList<Object> gameJList;
    private final InfoPanel todoInfo;
    private final InfoPanel gameInfo;
    private final JTabbedPane tabbedPane;

    private MainFrame() {
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
        PersistenceHandler ph = PersistenceHandler.getInstance(this);
        ph.loadItems();

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
                if (getSelectedTabIndex() == 0) {
                    if (gameListModel.getSize() == 0) JOptionPane.showMessageDialog(mainFrame, "먼저 게임을 추가 해 주세요.");
                    else new TodoAddPopup(MainFrame.this, gameListModel);
                } else {
                    new GameAddPopup(MainFrame.this);
                }
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
        tabbedPane.addTab("TODO", createTodoPanel());
        tabbedPane.addTab("Game", createGamePanel());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(resetAllButton, BorderLayout.SOUTH);

        add(mainPanel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ph.saveItems(getGames(), getTodos());
            }
        });
        setVisible(true);
    }

    public static MainFrame getMainFrame() {
        if (mainFrame == null) {
            mainFrame = new MainFrame();
        }
        return mainFrame;
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

    public void addGame(Game newGame) {
        gameListModel.addElement(newGame);
    }

    public void addTodo(Todo newTodo) {
        todoListModel.addElement(newTodo);
    }

    private void removeItem() {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "이 아이템을 삭제하시겠습니까?", "아이템 삭제", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (getSelectedTabIndex() == 0) {
                    todoListModel.remove(selectedIndex);
                } else if (getSelectedTabIndex() == 1) {
                    gameListModel.remove(selectedIndex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "삭제할 아이템을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    } //todo: 탭별 작동 수정, Game 삭제시 하위 전부 날리기

    private void countItems() {
        int itemCount;
        if (getSelectedTabIndex() == 0) {
            itemCount = todoListModel.getSize();
        } else {
            itemCount = gameListModel.getSize();
        }
        JOptionPane.showMessageDialog(this, "아이템 수: " + itemCount, "아이템 수 세기", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetItem() {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex != -1) {
            if (getSelectedTabIndex() == 0) {
                todoListModel.setElementAt("리셋", selectedIndex);
            } else if (getSelectedTabIndex() == 1) {
                gameListModel.setElementAt("리셋", selectedIndex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "리셋할 아이템을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Todo:기간반복 item의 경우 초기화를 시도하면 자동 reset 조건 클리어 여부에 따라 확인 메세지 출력.
    // Todo:잘못 초기화 한 경우 취소 기능. Count가 0일시 취소 버튼으로 바뀌고 Todo 클래스의 메소드에서 멤버변수로 리셋 전 count저장해둬서 롤백.
    // Todo:선택된 item이 게임인 경우 뭔가 다른 버튼으로 바꿔볼까?

    private void resetAllItems() {
        int confirm = JOptionPane.showConfirmDialog(this, "모든 아이템을 리셋하시겠습니까?", "모두 리셋", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            //Todo: 모든 item의 객체에 순회하며 resetAll()
        }
    }

    private int getSelectedTabIndex() {
        return tabbedPane.getSelectedIndex();
    }

    private int getSelectedIndex() {
        if (getSelectedTabIndex() == 0) {
            return todoJList.getSelectedIndex();
        } else {
            return gameJList.getSelectedIndex();
        }
    }

    public List<Game> getGames() {
        List<Game> games = new ArrayList<>();
        for (int i = 0; i < gameListModel.getSize(); i++) {
            Object element = gameListModel.getElementAt(i);
            games.add((Game) element);
        }
        return games;
    }

    public List<Todo> getTodos() {
        List<Todo> todos = new ArrayList<>();
        for (int i = 0; i < todoListModel.getSize(); i++) {
            Object element = todoListModel.getElementAt(i);
            todos.add((Todo) element);
        }
        return todos;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::getMainFrame);
    }
}
