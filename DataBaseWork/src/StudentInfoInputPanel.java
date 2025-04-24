import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentInfoInputPanel extends JPanel {
    private JTextField studentIDField, nameField, sexField, birthdayField, nativePlaceField, departmentField, classField;

    public StudentInfoInputPanel() {
        setLayout(new GridLayout(8, 2));

        add(new JLabel("学号:"));
        studentIDField = new JTextField(10);
        add(studentIDField);

        add(new JLabel("姓名:"));
        nameField = new JTextField(10);
        add(nameField);

        add(new JLabel("性别:"));
        sexField = new JTextField(10);
        add(sexField);

        add(new JLabel("生日:"));
        birthdayField = new JTextField(10);
        add(birthdayField);

        add(new JLabel("籍贯:"));
        nativePlaceField = new JTextField(10);
        add(nativePlaceField);

        add(new JLabel("院系编号:"));
        departmentField = new JTextField(10);
        add(departmentField);

        add(new JLabel("班级编号:"));
        classField = new JTextField(10);
        add(classField);

        JButton submitButton = new JButton("提交");
        add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitStudentInfo();
            }
        });
    }

    private void submitStudentInfo() {
        String studentID = studentIDField.getText();
        String name = nameField.getText();
        String sex = sexField.getText();
        String birthday = birthdayField.getText();
        String nativePlace = nativePlaceField.getText();
        String departmentID = departmentField.getText();
        String classID = classField.getText();

        String sql = "INSERT INTO STUDENT (sno, sname, ssex, sbir, birpla,sdept, class) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentID);
            stmt.setString(2, name);
            stmt.setString(3, sex);
            stmt.setString(4, birthday);
            stmt.setString(5, nativePlace);
            stmt.setString(6, departmentID);
            stmt.setString(7, classID);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "学生信息提交成功！");
            } else {
                JOptionPane.showMessageDialog(null, "学生信息提交失败！");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "提交失败: " + ex.getMessage());
        }
    }
}