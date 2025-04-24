import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//无用模块
public class StudentChangeInputPanel extends JPanel {
    private JTextField studentIDField, changeCodeField, recTimeField, descriptionField;

    public StudentChangeInputPanel() {
        setLayout(new GridLayout(5, 2));

        add(new JLabel("学号:"));
        studentIDField = new JTextField();
        add(studentIDField);

        add(new JLabel("变更代码:"));
        changeCodeField = new JTextField();
        add(changeCodeField);

        add(new JLabel("记录时间:"));
        recTimeField = new JTextField();
        add(recTimeField);

        add(new JLabel("描述:"));
        descriptionField = new JTextField();
        add(descriptionField);

        JButton submitButton = new JButton("提交变更");
        add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitChangeInfo();
            }
        });
    }

    private void submitChangeInfo() {
        String studentID = studentIDField.getText();
        String changeCode = changeCodeField.getText();
        String recTime = recTimeField.getText();
        String description = descriptionField.getText();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO CHANGE (STUDENTID, CHANGE, REC_TIME, DESCRIPTION) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, studentID);
                stmt.setString(2, changeCode);
                stmt.setString(3, recTime);
                stmt.setString(4, description);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "学籍变更信息提交成功！");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "提交失败: " + e.getMessage());
        }
    }
}

