import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentInfoEditPanel extends JPanel {
    private JTextField studentIDField;
    private JButton searchButton;

    public StudentInfoEditPanel() {
        setLayout(new GridLayout(2, 2));

        add(new JLabel("学号:"));
        studentIDField = new JTextField(10);
        add(studentIDField);

        searchButton = new JButton("查询");
        add(searchButton);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudentByID();
            }
        });
    }

    private void searchStudentByID() {
        String studentID = studentIDField.getText();
        if (studentID.isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入学号！");
            return;
        }

        String sql = "SELECT * FROM STUDENT WHERE sno = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // 获取当前记录信息
                String name = rs.getString("sname");
                String sex = rs.getString("ssex");
                String birthday = rs.getString("sbir");
                String nativePlace = rs.getString("birpla");
                String department = rs.getString("sdept");
                String className = rs.getString("class");

                // 弹出对话框编辑信息
                JTextField nameField = new JTextField(name, 10);
                JTextField sexField = new JTextField(sex, 10);
                JTextField birthdayField = new JTextField(birthday, 10);
                JTextField nativePlaceField = new JTextField(nativePlace, 10);
                JTextField departmentField = new JTextField(department, 10);
                JTextField classField = new JTextField(className, 10);

                JPanel panel = new JPanel(new GridLayout(7, 2));
                panel.add(new JLabel("姓名:"));
                panel.add(nameField);
                panel.add(new JLabel("性别:"));
                panel.add(sexField);
                panel.add(new JLabel("生日:"));
                panel.add(birthdayField);
                panel.add(new JLabel("籍贯:"));
                panel.add(nativePlaceField);
                panel.add(new JLabel("院系编号:"));
                panel.add(departmentField);
                panel.add(new JLabel("班级编号:"));
                panel.add(classField);

                int result = JOptionPane.showConfirmDialog(null, panel, "编辑学生信息", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    // 更新记录
                    updateStudentInfo(studentID, nameField.getText(), sexField.getText(), birthdayField.getText(),
                            nativePlaceField.getText(), departmentField.getText(), classField.getText());
                }
            } else {
                JOptionPane.showMessageDialog(null, "未找到学号为 " + studentID + " 的学生记录！");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "查询失败: " + ex.getMessage());
        }
    }

    private void updateStudentInfo(String studentID, String name, String sex, String birthday, String nativePlace, String department, String className) {
        String sql = "UPDATE STUDENT SET sname=?, ssex=?, sbir=?, birpla=?, sdept=?, class=? WHERE sno=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, sex);
            stmt.setString(3, birthday);
            stmt.setString(4, nativePlace);
            stmt.setString(5, department);
            stmt.setString(6, className);
            stmt.setString(7, studentID);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "学生信息更新成功！");
            } else {
                JOptionPane.showMessageDialog(null, "学生信息更新失败！");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "更新失败: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "生日格式错误，请使用 YYYY-MM-DD 格式！");
        }
    }
}
