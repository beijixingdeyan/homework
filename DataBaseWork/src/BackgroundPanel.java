import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        setPreferredSize(new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this)));
        setLayout(new GridBagLayout());
        setOpaque(false); // 使面板透明，以便显示背景图片
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}