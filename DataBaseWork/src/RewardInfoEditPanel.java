import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RewardInfoEditPanel extends JPanel {
    private JTextField idField;
    private JButton searchButton;

    public RewardInfoEditPanel() {
        setLayout(new GridLayout(2, 2));

        add(new JLabel("记录号:"));
        idField = new JTextField(10);
        add(idField);

        searchButton = new JButton("查询");
        add(searchButton);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchRecordById();
            }
        });
    }

    private void searchRecordById() {
        String id = idField.getText();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入记录号！");
            return;
        }

        String sql = "SELECT * FROM REWARD WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // 获取当前记录信息
                String studentID = rs.getString("sno");
                String sname = rs.getString("sname");
                String levels = rs.getString("lev");
                String recTime = rs.getString("rec_time");
                String description = rs.getString("des");

                // 弹出对话框编辑信息
                JTextField studentIDField = new JTextField(studentID, 10);
                JTextField snameField = new JTextField(sname, 10);
                JTextField levelsField = new JTextField(levels, 10);
                JTextField recTimeField = new JTextField(recTime, 10);
                JTextField descriptionField = new JTextField(description, 30);

                JPanel panel = new JPanel(new GridLayout(6, 2));
                panel.add(new JLabel("学号:"));
                panel.add(studentIDField);
                panel.add(new JLabel("姓名:"));
                panel.add(snameField);
                panel.add(new JLabel("级别代码:"));
                panel.add(levelsField);
                panel.add(new JLabel("记录时间:"));
                panel.add(recTimeField);
                panel.add(new JLabel("描述:"));
                panel.add(descriptionField);

                int result = JOptionPane.showConfirmDialog(null, panel, "编辑奖励信息", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    // 更新记录
                    updateRecord(id, studentIDField.getText(), snameField.getText(), levelsField.getText(),
                            recTimeField.getText(), descriptionField.getText());
                }
            } else {
                JOptionPane.showMessageDialog(null, "未找到记录号为 " + id + " 的记录！");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "查询失败: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "记录号应为数字！");
        }
    }

    private void updateRecord(String id, String studentID, String sname, String levels, String recTime, String description) {
        String sql = "UPDATE REWARD SET sno=?, sname=?, lev=?, rec_time=?, des=? WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentID);
            stmt.setString(2, sname);
            stmt.setString(3, levels);
            stmt.setDate(4, java.sql.Date.valueOf(recTime));
            stmt.setString(5, description);
            stmt.setInt(6, Integer.parseInt(id));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "奖励信息更新成功！");
            } else {
                JOptionPane.showMessageDialog(null, "奖励信息更新失败！");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "更新失败: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "记录时间格式错误，请使用 YYYY-MM-DD 格式！");
        }
    }
}
