import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StudentLogin extends JFrame {

    public StudentLogin() {
        setTitle("学生登录");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 居中显示

        // 设置背景图
        setContentPane(new BackgroundPanel("/picture/test3.jpg")); // 设置背景图片

        // 创建面板和组件
        JPanel panel = new JPanel();
        panel.setOpaque(false);  // 使面板透明，这样背景图片可以显示
        panel.setLayout(new GridBagLayout());  // 使用 GridBagLayout 来布局

        // 创建约束对象
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);  // 给组件设置间距

        JLabel labelUsername = new JLabel("学号:");
        JTextField textUsername = new JTextField(20);
        JLabel labelPassword = new JLabel("密码:");
        JPasswordField textPassword = new JPasswordField(20);
        JButton loginButton = new JButton("登录");

        // 将组件添加到面板
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(labelUsername, constraints);

        constraints.gridx = 1;
        panel.add(textUsername, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(labelPassword, constraints);

        constraints.gridx = 1;
        panel.add(textPassword, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(loginButton, constraints);  // 按钮放在最下方居中

        // 将面板添加到窗口
        add(panel);

        // 登录按钮事件处理
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textUsername.getText();
                String password = new String(textPassword.getPassword());

                // 验证学生账号和密码
                if (validateStudentLogin(username, password)) {
                    JOptionPane.showMessageDialog(StudentLogin.this, "登录成功！");
                    // 跳转到学生主页，并传递学生信息
                    new StudentHomePage(username).setVisible(true);
                    dispose();  // 关闭登录窗口
                } else {
                    JOptionPane.showMessageDialog(StudentLogin.this, "学号或密码错误");
                }
            }
        });
    }

    private boolean validateStudentLogin(String username, String password) {
        String sql = "SELECT * FROM user WHERE sno = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "数据库查询失败: " + ex.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentLogin().setVisible(true));
    }
}