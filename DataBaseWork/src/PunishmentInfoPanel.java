import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//时代的眼泪
public class PunishmentInfoPanel extends JPanel {
    private JTextArea punishmentInfoArea;

    public PunishmentInfoPanel(String studentId) {
        setLayout(new BorderLayout());
        punishmentInfoArea = new JTextArea();
        punishmentInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(punishmentInfoArea);
        add(scrollPane, BorderLayout.CENTER);

        // 查询处罚情况信息并显示
        queryPunishmentInfo(studentId);
    }

    private void queryPunishmentInfo(String studentId) {
        String sql = "SELECT * FROM punishment WHERE studentid = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                punishmentInfoArea.setText("");
                while (rs.next()) {
                    punishmentInfoArea.append("处罚级别: " + rs.getString("levels") + "\n");
                    punishmentInfoArea.append("处罚时间: " + rs.getDate("rec_time") + "\n");
                    punishmentInfoArea.append("是否生效: " + rs.getString("enable") + "\n");
                    punishmentInfoArea.append("描述: " + rs.getString("description") + "\n\n");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "查询失败: " + ex.getMessage());
        }
    }
}
