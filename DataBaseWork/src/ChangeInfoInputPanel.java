import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangeInfoInputPanel extends JPanel {
    private JTextField idField,studentIDField,snameField, changeField, descriptionField;
    private JTextField recTimeField;

    public ChangeInfoInputPanel() {
        setLayout(new GridLayout(7, 2));

        add(new JLabel("id"));
        idField = new JTextField(10);
        add(idField);

        add(new JLabel("学号:"));
        studentIDField = new JTextField(10);
        add(studentIDField);

        add(new JLabel("姓名:"));
        snameField = new JTextField(10);
        add(snameField);

        add(new JLabel("变更代码:"));
        changeField = new JTextField(10);
        add(changeField);

        add(new JLabel("记录时间:"));
        recTimeField = new JTextField(10);
        add(recTimeField);

        add(new JLabel("描述:"));
        descriptionField = new JTextField(30);
        add(descriptionField);

        JButton submitButton = new JButton("提交");
        add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitChangeInfo();
            }
        });
    }

    private void submitChangeInfo() {
        String id = idField.getText();
        String studentID = studentIDField.getText();
        String sname = snameField.getText();
        String changeCode = changeField.getText();
        String recTime = recTimeField.getText();
        String description = descriptionField.getText();

        String sql = "INSERT INTO `change` (id,sno,sname, code, rec_time, des) VALUES (?,?,?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, studentID);
            stmt.setString(3, sname);
            stmt.setString(4, changeCode);
            stmt.setDate(5, java.sql.Date.valueOf(recTime));
            stmt.setString(6, description);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "学籍变更信息提交成功！");
            } else {
                JOptionPane.showMessageDialog(null, "学籍变更信息提交失败！");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "提交失败: " + ex.getMessage());
        }
    }
}