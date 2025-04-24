import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//暂无用
public class RewardInputPanel extends JPanel {
    private JTextField studentIDField, levelsField, recTimeField, descriptionField;

    public RewardInputPanel() {
        setLayout(new GridLayout(5, 2));

        add(new JLabel("学号:"));
        studentIDField = new JTextField();
        add(studentIDField);

        add(new JLabel("奖励级别:"));
        levelsField = new JTextField();
        add(levelsField);

        add(new JLabel("记录时间:"));
        recTimeField = new JTextField();
        add(recTimeField);

        add(new JLabel("描述:"));
        descriptionField = new JTextField();
        add(descriptionField);

        JButton submitButton = new JButton("提交奖励");
        add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitRewardInfo();
            }
        });
    }

    private void submitRewardInfo() {
        String studentID = studentIDField.getText();
        String levels = levelsField.getText();
        String recTime = recTimeField.getText();
        String description = descriptionField.getText();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO REWARD (STUDENTID, LEVELS, REC_TIME, DESCRIPTION) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, studentID);
                stmt.setString(2, levels);
                stmt.setDate(3, java.sql.Date.valueOf(recTime));
                stmt.setString(4, description);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "奖励信息提交成功！");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "提交失败: " + e.getMessage());
        }
    }
}
