package util;


import javax.swing.*;
import java.awt.*;

public class Utils {

    /**
     * Define o ícone do JFrame com uma imagem redimensionada.
     *
     * @param frame O JFrame que terá o ícone definido.
     * @param imagePath O caminho da imagem no classpath.
     * @param width A largura desejada para o ícone.
     * @param height A altura desejada para o ícone.
     */
    public static void setFrameIcon(JFrame frame, String imagePath, int width, int height) {
        try {
            // Carrega a imagem do classpath
            ImageIcon logoIcon = new ImageIcon(Utils.class.getClassLoader().getResource(imagePath));
            // Redimensiona a imagem
            Image logoImage = logoIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            // Define o ícone do JFrame
            frame.setIconImage(logoImage);
        } catch (Exception e) {
            System.err.println("Erro ao carregar o ícone: " + e.getMessage());
        }
    }
}