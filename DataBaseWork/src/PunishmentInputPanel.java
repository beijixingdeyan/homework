import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PunishmentInputPanel extends JPanel {
    private JTextField studentIDField, levelsField, recTimeField, descriptionField, enableField;

    public PunishmentInputPanel() {
        setLayout(new GridLayout(6, 2));

        add(new JLabel("学号:"));
        studentIDField = new JTextField();
        add(studentIDField);

        add(new JLabel("处罚级别:"));
        levelsField = new JTextField();
        add(levelsField);

        add(new JLabel("记录时间:"));
        recTimeField = new JTextField();
        add(recTimeField);

        add(new JLabel("描述:"));
        descriptionField = new JTextField();
        add(descriptionField);

        add(new JLabel("是否生效:"));
        enableField = new JTextField();
        add(enableField);

        JButton submitButton = new JButton("提交处罚");
        add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitPunishmentInfo();
            }
        });
    }

    private void submitPunishmentInfo() {
        String studentID = studentIDField.getText();
        String levels = levelsField.getText();
        String recTime = recTimeField.getText();
        String description = descriptionField.getText();
        String enable = enableField.getText();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO PUNISH (STUDENTID, LEVELS, REC_TIME, ENABLE, DESCRIPTION) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, studentID);
                stmt.setString(2, levels);
                stmt.setDate(3, java.sql.Date.valueOf(recTime));
                stmt.setString(4, enable);
                stmt.setString(5, description);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "处罚信息提交成功！");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "提交失败: " + e.getMessage());
        }
    }
}