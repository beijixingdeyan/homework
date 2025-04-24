import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherLogin extends JFrame {

    public TeacherLogin() {
        setTitle("教师登录");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 居中显示

        // 设置背景图
        setContentPane(new BackgroundPanel("/picture/test1.jpg")); // 设置背景图片

        // 创建面板和组件
        JPanel panel = new JPanel();
        panel.setOpaque(false);  // 使面板透明，这样背景图片可以显示
        panel.setLayout(new GridBagLayout());  // 使用 GridBagLayout 来布局

        // 创建约束对象
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);  // 给组件设置间距

        JLabel labelUsername = new JLabel("教师号:");
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

                // 验证教师账号和密码
                if (validateTeacherLogin(username, password)) {
                    JOptionPane.showMessageDialog(TeacherLogin.this, "登录成功！");
                    // 跳转到教师主页
                    new TeacherHomePage().setVisible(true);
                    dispose();  // 关闭登录窗口
                } else {
                    JOptionPane.showMessageDialog(TeacherLogin.this, "用户名或密码错误");
                }
            }
        });
    }

    private boolean validateTeacherLogin(String username, String password) {
        String sql = "SELECT * FROM teacher WHERE id = ? AND password = ?";
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TeacherLogin().setVisible(true);
            }
        });
    }
}