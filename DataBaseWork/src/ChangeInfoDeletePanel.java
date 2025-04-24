import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ChangeInfoDeletePanel extends JPanel {
    private JTextField idField;

    public ChangeInfoDeletePanel() {
        setLayout(new GridLayout(2, 2));

        add(new JLabel("记录号:"));
        idField = new JTextField(10);
        add(idField);

        JButton deleteButton = new JButton("删除");
        add(deleteButton);
        deleteButton.addActionListener(e -> deleteChangeInfo());
    }

    private void deleteChangeInfo() {
        String id = idField.getText();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入记录号");
            return;
        }

        String sql = "DELETE FROM `change` WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "学籍变动信息删除成功！");
            } else {
                JOptionPane.showMessageDialog(this, "删除失败，未找到对应记录。");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "删除失败: " + ex.getMessage());
        }
    }
}