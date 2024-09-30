package modules.views.components;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import modules.views.utils.ImageLoader;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import modules.views.utils.Colors;

public class CustomButton extends JButton {
    private Icon icon;
    private boolean pressed;
    
    public CustomButton (String imagePath, int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.icon = ImageLoader.setImageLabel(imagePath, width, height);
        
        setIcon(this.icon);
        setContentAreaFilled(false);
        setBorderPainted(false);
        
        addMouseListener(new MouseAdapter() {
           
            @Override
           public void mouseEntered(MouseEvent e) {
               setBorder(BorderFactory.createLineBorder(Colors.PURPLE));
           }
           
           @Override
           public void mouseExited(MouseEvent e) {
               
           }
        });
    }  
}
