import javax.swing.*;
import java.awt.*;

public class StudentInfoHomePage extends JFrame {
    public StudentInfoHomePage() {
        setTitle("学生信息管理");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 创建背景面板
        BackgroundPanel backgroundPanel = new BackgroundPanel("/picture/test4.jpg");
        backgroundPanel.setLayout(new BorderLayout());

        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

        // 设置内容面板
        setContentPane(backgroundPanel);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false); // 使面板透明

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 设置组件间距
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // 居中对齐

        JButton inputButton = new JButton("插入");
        inputButton.addActionListener(e -> showFunctionPanel(new StudentInfoInputPanel()));
        panel.add(inputButton, gbc);

        gbc.gridy++;
        JButton editButton = new JButton("修改");
        editButton.addActionListener(e -> showFunctionPanel(new StudentInfoEditPanel()));
        panel.add(editButton, gbc);

        gbc.gridy++;
        JButton searchButton = new JButton("查询");
        searchButton.addActionListener(e -> showFunctionPanel(new StudentInfoSearchPanel()));
        panel.add(searchButton, gbc);

        gbc.gridy++;
        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> showFunctionPanel(new StudentInfoDeletePanel()));
        panel.add(deleteButton, gbc);

        return panel;
    }

    private void showFunctionPanel(JPanel functionPanel) {
        JDialog dialog = new JDialog(this, "Function Panel", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(functionPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentInfoHomePage().setVisible(true));
    }
}