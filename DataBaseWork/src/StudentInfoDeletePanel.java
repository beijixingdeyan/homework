import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentInfoDeletePanel extends JPanel {
    private JTextField studentIdField;

    public StudentInfoDeletePanel() {
        setLayout(new GridLayout(2, 2));

        add(new JLabel("学号:"));
        studentIdField = new JTextField(10);
        add(studentIdField);

        JButton deleteButton = new JButton("删除");
        add(deleteButton);
        deleteButton.addActionListener(e -> deleteStudentInfo());
    }

    private void deleteStudentInfo() {
        String studentId = studentIdField.getText();
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入学号");
            return;
        }

        String sql = "DELETE FROM student WHERE sno=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "学生信息删除成功！");
            } else {
                JOptionPane.showMessageDialog(this, "删除失败，未找到对应记录。");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "删除失败: " + ex.getMessage());
        }
    }
}