package net.talor1n.passforge.ui.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SearchTextField extends JTextField {
    private final Icon searchIcon;
    private final Icon scaledSearchIcon;
    private final int cornerRadius = 5; // Радиус скругления углов

    public SearchTextField() {
        super(10);
        searchIcon = new ImageIcon(SearchTextField.class.getResource("/img/search.png"));

        Image img = ((ImageIcon) searchIcon).getImage();
        Image scaledImg = img.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        scaledSearchIcon = new ImageIcon(scaledImg);

        // Устанавливаем прозрачный бордер и отступы
        setBorder(new EmptyBorder(0, 25, 0, 10));
        setForeground(Color.GRAY);
        setFont(new Font("Arial", Font.PLAIN, 14));
        setOpaque(false); // Делаем фон прозрачным, чтобы рисовать его вручную

        // Добавляем placeholder
        setText("Фильтр...");
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (getText().equals("Фильтр...")) {
                    setText("");
                    setForeground(Color.WHITE);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (getText().isEmpty()) {
                    setForeground(Color.GRAY);
                    setText("Фильтр...");
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Включаем сглаживание для более плавных углов
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Рисуем скругленный фон
        g2.setColor(getBackground() != null ? getBackground() : new Color(60, 63, 65)); // Цвет фона по умолчанию
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

        // Рисуем иконку
        int iconX = 7;
        int iconY = (getHeight() - scaledSearchIcon.getIconHeight()) / 2;
        scaledSearchIcon.paintIcon(this, g2, iconX, iconY);

        // Рисуем текст поверх фона
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Рисуем скругленную границу
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(70,76,85, 255)); // Цвет границы
        g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));
        g2.dispose();
    }

    @Override
    public Insets getInsets() {
        Insets insets = super.getInsets();
        insets.left += scaledSearchIcon.getIconWidth() - 10;
        insets.top += 2;
        return insets;
    }

    // Устанавливаем форму компонента для корректного взаимодействия
    @Override
    public boolean contains(int x, int y) {
        return new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius)
                .contains(x, y);
    }
}