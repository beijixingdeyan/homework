import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangeInfoSearchPanel extends JPanel {
    private JTextField searchField;
    private JTextArea resultArea;

    public ChangeInfoSearchPanel() {
        setLayout(new GridLayout(2, 2));

        add(new JLabel("搜索学号:"));
        searchField = new JTextField(10);
        add(searchField);

        JButton searchButton = new JButton("查询");
        add(searchButton);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchChangeInfo();
            }
        });

        resultArea = new JTextArea(10, 30);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane);
    }

    private void searchChangeInfo() {
        String studentID = searchField.getText();

        String sql = "SELECT * FROM `change` WHERE sno = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                resultArea.setText("");
                while (rs.next()) {
                    String id = rs.getString("id");
                    String sno= rs.getString("sno");
                    String name = rs.getString("sname");
                    String change = rs.getString("code");
                    String recTime = rs.getString("rec_time");
                    String description = rs.getString("des");
                    resultArea.append("id号:"+ id + "\n");
                    resultArea.append("学号:"+sno + "\n");
                    resultArea.append("姓名:"+name + "\n");
                    resultArea.append("变更代码: " + change + "\n");
                    resultArea.append("记录时间: " + recTime + "\n");
                    resultArea.append("描述: " + description + "\n\n");
                }
                if (resultArea.getText().isEmpty()) {
                    resultArea.setText("未找到学籍变更信息");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "查询失败: " + ex.getMessage());
        }
    }
}