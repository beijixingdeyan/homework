import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PunishmentInfoInputPanel extends JPanel {
    private JTextField idField,studentIDField,snameField, levelsField, descriptionField;
    private JTextField recTimeField, enableField;

    public PunishmentInfoInputPanel() {
        setLayout(new GridLayout(8, 2));

        add(new JLabel("ID"));
        idField = new JTextField(10);
        add(idField);

        add(new JLabel("学号:"));
        studentIDField = new JTextField(10);
        add(studentIDField);

        add(new JLabel("姓名"));
        snameField = new JTextField(10);
        add(snameField);

        add(new JLabel("级别代码:"));
        levelsField = new JTextField(10);
        add(levelsField);

        add(new JLabel("记录时间:"));
        recTimeField = new JTextField(10);
        add(recTimeField);

        add(new JLabel("是否生效:"));
        enableField = new JTextField(10);
        add(enableField);

        add(new JLabel("描述:"));
        descriptionField = new JTextField(30);
        add(descriptionField);

        JButton submitButton = new JButton("提交");
        add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitPunishmentInfo();
            }
        });
    }

    private void submitPunishmentInfo() {
        String id = idField.getText();
        String studentID = studentIDField.getText();
        String sname = snameField.getText();
        String levels = levelsField.getText();
        String recTime = recTimeField.getText();
        String enable = enableField.getText();
        String description = descriptionField.getText();

        String sql = "INSERT INTO punish (id,sno,sname, lev, rec_time, enab, des) VALUES (?,?, ?,?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, studentID);
            stmt.setString(3, sname);
            stmt.setString(4, levels);
            stmt.setDate(5, java.sql.Date.valueOf(recTime));
            stmt.setString(6, enable);
            stmt.setString(7, description);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "处罚信息提交成功！");
            } else {
                JOptionPane.showMessageDialog(null, "处罚信息提交失败！");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "提交失败: " + ex.getMessage());
        }
    }
}