import javax.swing.*;
import java.awt.*;
//暂无用
public class StudentInfoViewPanel extends JPanel {
    public StudentInfoViewPanel(StudentInfo studentInfo) {
        setLayout(new GridLayout(0, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("个人信息"));

        JLabel nameLabel = new JLabel("姓名: " + studentInfo.getName());
        JLabel studentIdLabel = new JLabel("学号: " + studentInfo.getStudentId());
        JLabel genderLabel = new JLabel("性别: " + studentInfo.getGender());
        JLabel majorLabel = new JLabel("专业: " + studentInfo.getDepartment());

        add(nameLabel);
        add(studentIdLabel);
        add(genderLabel);
        add(majorLabel);
    }
}