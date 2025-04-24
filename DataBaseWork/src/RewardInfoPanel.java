import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//暂不知何用
public class RewardInfoPanel extends JPanel {
    private JTextArea rewardInfoArea;

    public RewardInfoPanel(String studentId) {
        setLayout(new BorderLayout());
        rewardInfoArea = new JTextArea();
        rewardInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(rewardInfoArea);
        add(scrollPane, BorderLayout.CENTER);

        // 查询奖励情况信息并显示
        queryRewardInfo(studentId);
    }

    private void queryRewardInfo(String studentId) {
        String sql = "SELECT * FROM reward WHERE studentid = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                rewardInfoArea.setText("");
                while (rs.next()) {
                    rewardInfoArea.append("奖励级别: " + rs.getString("levels") + "\n");
                    rewardInfoArea.append("奖励时间: " + rs.getDate("rec_time") + "\n");
                    rewardInfoArea.append("描述: " + rs.getString("description") + "\n\n");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "查询失败: " + ex.getMessage());
        }
    }
}