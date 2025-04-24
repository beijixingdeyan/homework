import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("学生信息管理系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 居中显示

        // 使用自定义的背景面板
        BackgroundPanel backgroundPanel = new BackgroundPanel("picture/test9.png");

        // 设置背景面板为主界面内容面板
        setContentPane(backgroundPanel);

        // 创建和设置界面组件
        JPanel panel = new JPanel();
        panel.setOpaque(false);  // 设置面板透明，这样背景可以显示出来
        panel.setLayout(new GridBagLayout());

        // 设置布局约束
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        // 创建按钮或其他界面组件
        JButton studentLoginButton = new JButton("学生登录");
        JButton teacherLoginButton = new JButton("教师登录");

        // 将组件添加到面板中
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(studentLoginButton, constraints);

        constraints.gridy = 1;
        panel.add(teacherLoginButton, constraints);

        // 将面板添加到主界面
        add(panel);

        // 设置按钮的监听事件
        studentLoginButton.addActionListener(e -> {
            // 跳转到学生登录界面
            new StudentLogin().setVisible(true);
            this.dispose();
        });

        teacherLoginButton.addActionListener(e -> {
            // 跳转到教师登录界面
            new TeacherLogin().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}