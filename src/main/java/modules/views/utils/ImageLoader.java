package modules.views.utils;

import java.awt.Image;
import javax.swing.Icon;

import javax.swing.ImageIcon;

public class ImageLoader {
    public static Icon setImageLabel(String root, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(ImageLoader.class.getResource(root));
        Image image = imageIcon.getImage();
        
        if (image == null) {
            System.out.println("No se pudo cargar la imagen correctamente");
        }
        return new ImageIcon(image.getScaledInstance(width - 20, height - 20, Image.SCALE_SMOOTH));
    }
}
