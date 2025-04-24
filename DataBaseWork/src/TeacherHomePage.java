import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeacherHomePage extends JFrame {
    public TeacherHomePage() {
        setTitle("教师主页");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // 居中显示

        // 创建背景面板
        BackgroundPanel backgroundPanel = new BackgroundPanel("/picture/test2.jpg"); // 指定教师主页的背景图片
        backgroundPanel.setLayout(new BorderLayout());

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new GridBagLayout()); // 使用 GridBagLayout 使按钮居中
        buttonPanel.setOpaque(false); // 使按钮面板透明

        // 创建按钮并添加到面板
        JButton studentButton = new JButton("学生信息管理");
        JButton changeButton = new JButton("学籍变动管理");
        JButton rewardButton = new JButton("奖励信息管理");
        JButton punishmentButton = new JButton("处罚信息管理");

        // 使用 GridBagConstraints 使按钮面板居中对齐
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 设置按钮之间的间距
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(studentButton, gbc);
        gbc.gridy++;
        buttonPanel.add(changeButton, gbc);
        gbc.gridy++;
        buttonPanel.add(rewardButton, gbc);
        gbc.gridy++;
        buttonPanel.add(punishmentButton, gbc);

        // 添加按钮事件处理
        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 跳转到学生信息管理主页
                new StudentInfoHomePage().setVisible(true);
            }
        });
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 跳转到学籍变动管理主页
                new ChangeInfoHomePage().setVisible(true);
            }
        });
        rewardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 跳转到奖励信息管理主页
                new RewardInfoHomePage().setVisible(true);
            }
        });
        punishmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 跳转到处罚信息管理主页
                new PunishmentInfoHomePage().setVisible(true);
            }
        });

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        setContentPane(backgroundPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TeacherHomePage().setVisible(true));
    }
}