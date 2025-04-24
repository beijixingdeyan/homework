import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//失去作用的代码
public class ChangeInfoPanel extends JPanel {
    private JTextArea changeInfoArea;

    public ChangeInfoPanel(String studentId) {
        setLayout(new BorderLayout());
        changeInfoArea = new JTextArea();
        changeInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(changeInfoArea);
        add(scrollPane, BorderLayout.CENTER);

        // 查询学籍变动信息并显示
        queryChangeInfo(studentId);
    }

    private void queryChangeInfo(String studentId) {
        String sql = "SELECT * FROM change WHERE studentid = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                changeInfoArea.setText("");
                while (rs.next()) {
                    changeInfoArea.append("变更类型: " + rs.getString("change") + "\n");
                    changeInfoArea.append("变更时间: " + rs.getDate("rec_time") + "\n");
                    changeInfoArea.append("描述: " + rs.getString("description") + "\n\n");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "查询失败: " + ex.getMessage());
        }
    }
}
