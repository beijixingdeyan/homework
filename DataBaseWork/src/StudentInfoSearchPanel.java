import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentInfoSearchPanel extends JPanel {
    private JTextField searchField;
    private JTextArea resultArea;

    public StudentInfoSearchPanel() {
        setLayout(new GridLayout(2, 2));

        add(new JLabel("搜索学号:"));
        searchField = new JTextField(10);
        add(searchField);

        JButton searchButton = new JButton("查询");
        add(searchButton);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudentInfo();
            }
        });

        resultArea = new JTextArea(10, 30);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane);
    }

    private void searchStudentInfo() {
        String studentID = searchField.getText();

        String sql = "SELECT * FROM student WHERE sno=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                resultArea.setText("");
                if (rs.next()) {
                    String name = rs.getString("sname");
                    String sex = rs.getString("ssex");
                    String birthday = rs.getString("sbir");
                    String nativePlace = rs.getString("birpla");
                    String department = rs.getString("sdept");
                    String classID = rs.getString("class");
                    resultArea.append("姓名: " + name + "\n");
                    resultArea.append("性别: " + sex + "\n");
                    resultArea.append("生日: " + birthday + "\n");
                    resultArea.append("籍贯: " + nativePlace + "\n");
                    resultArea.append("院系编号: " + department + "\n");
                    resultArea.append("班级编号: " + classID + "\n");
                } else {
                    resultArea.setText("未找到学生信息");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "查询失败: " + ex.getMessage());
        }
    }
}