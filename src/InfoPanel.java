import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private JPanel info;

    public InfoPanel() {
        info = new JPanel();
        info.setPreferredSize(new Dimension(200, 150));
        setLayout(new BorderLayout());
        add(info, BorderLayout.WEST);
    }

    public JPanel getInfoPanel() {
        return info;
    }
}
class GameInfo extends InfoPanel {
    public GameInfo() {
        super();
    }
}
class TodoInfo extends InfoPanel {
    public TodoInfo() {
        super();
    }
}
