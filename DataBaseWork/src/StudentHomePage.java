import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentHomePage extends JFrame {
    private StudentInfo studentInfo;  // 存储学生信息

    // 构造函数，接受学号作为参数
    public StudentHomePage(String studentId) {
        setTitle("学生主页");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 居中显示

        // 设置背景图
        setContentPane(new BackgroundPanel("/picture/test5.jpg")); // 设置背景图片

        // 查询学生信息
        queryStudentInfo(studentId);

        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu studentMenu = new JMenu("学生功能");
        JMenuItem viewInfoItem = new JMenuItem("查看个人信息");
        JMenuItem modifyInfoItem = new JMenuItem("修改个人信息");
        JMenuItem viewChangeItem = new JMenuItem("查看学籍变动");
        JMenuItem viewRewardItem = new JMenuItem("查看奖励情况");
        JMenuItem viewPunishItem = new JMenuItem("查看处罚情况");
        JMenuItem logoutItem = new JMenuItem("退出");

        // 将菜单项添加到菜单
        studentMenu.add(viewInfoItem);
        studentMenu.add(modifyInfoItem);
        studentMenu.addSeparator();
        studentMenu.add(viewChangeItem);
        studentMenu.add(viewRewardItem);
        studentMenu.add(viewPunishItem);
        studentMenu.addSeparator();
        studentMenu.add(logoutItem);

        // 将菜单添加到菜单栏
        menuBar.add(studentMenu);
        setJMenuBar(menuBar);

        // 创建面板并将按钮居中
        JPanel panel = new JPanel();
        panel.setOpaque(false);  // 使面板透明，这样背景图片可以显示
        panel.setLayout(new GridBagLayout());  // 使用 GridBagLayout 来布局

        // 创建约束对象
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);  // 给组件设置间距

        JButton viewInfoButton = new JButton("查看个人信息");
        JButton modifyInfoButton = new JButton("修改个人信息");
        JButton viewChangeButton = new JButton("查看学籍变动");
        JButton viewRewardButton = new JButton("查看奖励情况");
        JButton viewPunishButton = new JButton("查看处罚情况");
        JButton logoutButton = new JButton("退出");

        // 将组件添加到面板
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(viewInfoButton, constraints);

        constraints.gridy = 1;
        panel.add(modifyInfoButton, constraints);

        constraints.gridy = 2;
        panel.add(viewChangeButton, constraints);

        constraints.gridy = 3;
        panel.add(viewRewardButton, constraints);

        constraints.gridy = 4;
        panel.add(viewPunishButton, constraints);

        constraints.gridy = 5;
        panel.add(logoutButton, constraints);

        // 将面板添加到窗口
        add(panel);

        // 按钮事件处理
        viewInfoButton.addActionListener(e -> showStudentInfoView());
        modifyInfoButton.addActionListener(e -> showStudentInfoEdit());
        viewChangeButton.addActionListener(e -> showChangeInfo());
        viewRewardButton.addActionListener(e -> showRewardInfo());
        viewPunishButton.addActionListener(e -> showPunishmentInfo());
        logoutButton.addActionListener(e -> System.exit(0));  // 退出系统

        // 菜单栏事件处理
        viewInfoItem.addActionListener(e -> showStudentInfoView());
        modifyInfoItem.addActionListener(e -> showStudentInfoEdit());
        viewChangeItem.addActionListener(e -> showChangeInfo());
        viewRewardItem.addActionListener(e -> showRewardInfo());
        viewPunishItem.addActionListener(e -> showPunishmentInfo());
        logoutItem.addActionListener(e -> System.exit(0));  // 退出系统
    }

    private void queryStudentInfo(String studentId) {
        String sql = "SELECT * FROM student WHERE sno = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("sname");
                    String gender = rs.getString("ssex");
                    String birthday = rs.getString("sbir");
                    String nativePlace = rs.getString("birpla");
                    String department = rs.getString("sdept");
                    String className = rs.getString("class");
                    studentInfo = new StudentInfo(studentId, name, gender, birthday, nativePlace, department, className);
                } else {
                    JOptionPane.showMessageDialog(null, "未找到学生信息");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "查询失败: " + ex.getMessage());
        }
    }

    // 弹出查看个人信息的界面
    private void showStudentInfoView() {
        if (studentInfo != null) {
            String info = "姓名: " + studentInfo.getName() + "\n"
                    + "学号: " + studentInfo.getStudentId() + "\n"
                    + "性别: " + studentInfo.getGender() + "\n"
                    + "生日: " + studentInfo.getBirthday() + "\n"
                    + "籍贯: " + studentInfo.getNativePlace() + "\n"
                    + "专业: " + studentInfo.getDepartment() + "\n"
                    + "班级: " + studentInfo.getClassName();
            JOptionPane.showMessageDialog(this, info, "查看个人信息", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "学生信息未加载");
        }
    }

    // 弹出修改个人信息的界面
    private void showStudentInfoEdit() {
        if (studentInfo != null) {
            // 创建表单窗口
            JDialog editDialog = new JDialog(this, "修改个人信息", true);
            editDialog.setSize(400, 300);
            editDialog.setLayout(new GridLayout(7, 2, 10, 10)); // 7行2列网格布局

            // 添加表单字段
            JTextField nameField = new JTextField(studentInfo.getName());
            JTextField genderField = new JTextField(studentInfo.getGender());
            JTextField birthdayField = new JTextField(studentInfo.getBirthday());
            JTextField nativePlaceField = new JTextField(studentInfo.getNativePlace());
            JTextField departmentField = new JTextField(studentInfo.getDepartment());
            JTextField classNameField = new JTextField(studentInfo.getClassName());

            editDialog.add(new JLabel("姓名:"));
            editDialog.add(nameField);
            editDialog.add(new JLabel("性别:"));
            editDialog.add(genderField);
            editDialog.add(new JLabel("生日:"));
            editDialog.add(birthdayField);
            editDialog.add(new JLabel("籍贯:"));
            editDialog.add(nativePlaceField);
            editDialog.add(new JLabel("专业:"));
            editDialog.add(departmentField);
            editDialog.add(new JLabel("班级:"));
            editDialog.add(classNameField);

            // 添加确认按钮和取消按钮
            JButton confirmButton = new JButton("确认");
            JButton cancelButton = new JButton("取消");
            editDialog.add(confirmButton);
            editDialog.add(cancelButton);

            // 按钮事件
            confirmButton.addActionListener(e -> {
                if (!nameField.getText().isEmpty() && !genderField.getText().isEmpty() &&
                        !birthdayField.getText().isEmpty() && !nativePlaceField.getText().isEmpty() &&
                        !departmentField.getText().isEmpty() && !classNameField.getText().isEmpty()) {

                    updateStudentInfo(nameField.getText(), genderField.getText(),
                            birthdayField.getText(), nativePlaceField.getText(),
                            departmentField.getText(), classNameField.getText());

                    editDialog.dispose(); // 关闭窗口
                } else {
                    JOptionPane.showMessageDialog(editDialog, "所有字段必须填写！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener(e -> editDialog.dispose());

            editDialog.setLocationRelativeTo(this); // 窗口居中
            editDialog.setVisible(true);
        }
    }
    private void updateStudentInfo(String name, String gender, String birthday, String nativePlace, String department, String className) {
        String sql = "UPDATE student SET sname=?, ssex=?, sbir=?, birpla=?, sdept=?, class=? WHERE sno=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, gender);
            stmt.setString(3, birthday);
            stmt.setString(4, nativePlace);
            stmt.setString(5, department);
            stmt.setString(6, className);
            stmt.setString(7, studentInfo.getStudentId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "个人信息更新成功！");
                // 更新内存中的信息
                studentInfo.setName(name);
                studentInfo.setGender(gender);
                studentInfo.setBirthday(birthday);
                studentInfo.setNativePlace(nativePlace);
                studentInfo.setDepartment(department);
                studentInfo.setClassName(className);
            } else {
                JOptionPane.showMessageDialog(this, "个人信息更新失败！");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "更新失败: " + ex.getMessage());
        }
    }
    // 弹出查看学籍变动的界面
    private void showChangeInfo() {
        if (studentInfo != null) {
            String studentId = studentInfo.getStudentId();
            String sql = "SELECT * FROM `change` WHERE sno = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, studentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    JTextArea textArea = new JTextArea(10, 30);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    if (rs.next()) {
                        String id = rs.getString("id");
                        String name = rs.getString("sname");
                        String change = rs.getString("code");
                        String recTime = rs.getString("rec_time");
                        String description = rs.getString("des");
                        textArea.append("学籍变动信息:\n");
                        textArea.append("id号:"+id + "\n");
                        textArea.append("姓名:"+name + "\n");
                        textArea.append("变更代码: " + change + "\n");
                        textArea.append("记录时间: " + recTime + "\n");
                        textArea.append("描述: " + description + "\n");
                    } else {
                        textArea.setText("未找到学籍变动信息");
                    }
                    JOptionPane.showMessageDialog(this, scrollPane, "查看学籍变动", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "查询失败: " + ex.getMessage());
            }
        }
    }

    // 弹出查看奖励情况的界面
    private void showRewardInfo() {
        if (studentInfo != null) {
            String studentId = studentInfo.getStudentId();
            String sql = "SELECT * FROM reward WHERE sno = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, studentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    JTextArea textArea = new JTextArea(10, 30);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    if (rs.next()) {
                        String id = rs.getString("id");
                        String name = rs.getString("sname");
                        String levels = rs.getString("lev");
                        String recTime = rs.getString("rec_time");
                        String description = rs.getString("des");
                        textArea.append("奖励信息:\n");
                        textArea.append("id号:"+id + "\n");
                        textArea.append("姓名:"+name + "\n");
                        textArea.append("奖励级别: " + levels + "\n");
                        textArea.append("记录时间: " + recTime + "\n");
                        textArea.append("描述: " + description + "\n");
                    } else {
                        textArea.setText("未找到奖励信息");
                    }
                    JOptionPane.showMessageDialog(this, scrollPane, "查看奖励情况", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "查询失败: " + ex.getMessage());
            }
        }
    }

    // 弹出查看处罚情况的界面
    private void showPunishmentInfo() {
        if (studentInfo != null) {
            String studentId = studentInfo.getStudentId();
            String sql = "SELECT * FROM punish WHERE sno = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, studentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    JTextArea textArea = new JTextArea(10, 30);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    if (rs.next()) {
                        String id = rs.getString("id");
                        String name = rs.getString("sname");
                        String levels = rs.getString("lev");
                        String recTime = rs.getString("rec_time");
                        String enable = rs.getString("enab");
                        String description = rs.getString("des");
                        textArea.append("处罚信息:\n");
                        textArea.append("id号:"+id + "\n");
                        textArea.append("姓名:"+name + "\n");
                        textArea.append("处罚级别: " + levels + "\n");
                        textArea.append("记录时间: " + recTime + "\n");
                        textArea.append("是否生效: " + enable + "\n");
                        textArea.append("描述: " + description + "\n");
                    } else {
                        textArea.setText("未找到处罚信息");
                    }
                    JOptionPane.showMessageDialog(this, scrollPane, "查看处罚情况", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "查询失败: " + ex.getMessage());
            }
        }
    }
}