package net.talor1n.passforge.ui.panel;

import net.talor1n.passforge.Main;
import net.talor1n.passforge.entity.Tag;
import net.talor1n.passforge.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SidebarPanel extends JPanel {
    private static SidebarPanel instance;

    private final List<PanelObject> panelObjects = new ArrayList<>();
    private final JPanel tagsContainer;

    public static SidebarPanel getInstance() {
        if (instance == null) {
            instance = new SidebarPanel();
        }
        return instance;
    }

    private static final ImageIcon originalIcon = new ImageIcon(MainFrame.class.getResource("/img/tags/all.png"));
    private static final Image scaledImage = originalIcon.getImage().getScaledInstance(20, -1, Image.SCALE_SMOOTH);
    private static final int MAX_TAG_LENGTH = 15;

    private SidebarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        // "All" элемент
        var allButton = new PanelObject(new Tag("All"), new ImageIcon(scaledImage));
        add(allButton);

        // Текст "Тэги:"
        JPanel tagsTextPanel = new JPanel(new BorderLayout());
        tagsTextPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tagsTextPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        tagsTextPanel.setOpaque(false);

        JLabel tagsText = new JLabel("Тэги:");
        tagsText.setFont(new Font("Roboto", Font.PLAIN, 13));
        tagsText.setVerticalAlignment(SwingConstants.CENTER);
        tagsTextPanel.add(tagsText, BorderLayout.CENTER);

        add(tagsTextPanel);

        // Контейнер тэгов
        tagsContainer = new JPanel();
        tagsContainer.setLayout(new BoxLayout(tagsContainer, BoxLayout.Y_AXIS));
        tagsContainer.setOpaque(false);
        add(tagsContainer);

        loadTags();

        // Кнопка добавления
        JButton addTagButton = new JButton("+ Добавить тэг");
        addTagButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addTagButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        addTagButton.setFont(new Font("Roboto", Font.PLAIN, 12));
        addTagButton.setFocusPainted(false);
        addTagButton.setBackground(new Color(0x21252B));
        addTagButton.setForeground(Color.WHITE);
        addTagButton.addActionListener((e) -> showAddTagDialog(e, "", true));

        add(Box.createRigidArea(new Dimension(0, 5)));
        add(addTagButton);
    }

    private void loadTags() {
        for (Tag tag : Main.getTags()) {
            addTag(tag);
        }
    }

    private String showAddTagDialog(ActionEvent e, String text, boolean flag) {
        String tagName = JOptionPane.showInputDialog(SidebarPanel.getInstance(),
                "Введите новое имя тэга:", text);

        if (tagName != null) {
            tagName = tagName.trim();
            if (tagName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Имя тэга не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else if (tagName.length() > MAX_TAG_LENGTH) {
                JOptionPane.showMessageDialog(this, "Имя тэга не должно превышать 15 символов.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else if (tagName.equals("All")) {
                JOptionPane.showMessageDialog(this, "Имя тэга 'All' уже существует.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else {
                if (flag == true) {
                    var tag = new Tag();
                    tag.setName(tagName);
                    Main.getTags().add(tag);
                    addTag(tag);
                    repaint();
                }
            }
        }
        return tagName;
    }

    public void addTag(Tag tag) {
        PanelObject newTag = new PanelObject(tag, new ImageIcon(scaledImage));
        panelObjects.add(newTag);
        tagsContainer.add(newTag);
        tagsContainer.revalidate();
        tagsContainer.repaint();
    }

    public void clearTags() {
        panelObjects.clear();
        tagsContainer.removeAll();
        tagsContainer.revalidate();
        tagsContainer.repaint();
    }

    private static class PanelObject extends JButton {
        private static final int HEIGHT = 40;

        public PanelObject(Tag tag, ImageIcon icon) {
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setLayout(new BorderLayout());
            setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT));
            setPreferredSize(new Dimension(0, HEIGHT));
            setBackground(new Color(0x21252B));
            setBorder(null);

            JLabel label = new JLabel(tag.getName());
            label.setFont(new Font("Roboto", Font.PLAIN, 12));
            label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem editItem = new JMenuItem("Редактировать");
            JMenuItem deleteItem = new JMenuItem("Удалить");
            popupMenu.add(editItem);
            popupMenu.add(deleteItem);
            if (!tag.getName().equals("All"))
                setComponentPopupMenu(popupMenu); // Устанавливаем меню

            if (icon != null) {
                JLabel iconLabel = new JLabel(icon);
                iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                add(iconLabel, BorderLayout.WEST);
            }

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (tag.getName().equals("All")) {
                            Main.setSelectedTag(null);
                            MainFrame.getInstance().repaint();
                            return;
                        }
                        Main.setSelectedTag(tag);
                        MainFrame.getInstance().revalidate();
                        MainFrame.getInstance().repaint();
                    }
                }
            });

            editItem.addActionListener(e -> {
                String newName = getInstance().showAddTagDialog(e, tag.getName(), false);
                if (newName != null && !newName.trim().isEmpty() && newName.length() <= MAX_TAG_LENGTH) {
                    tag.setName(newName.trim());
                    label.setText(newName.trim());
                    repaint();
                }
            });

            deleteItem.addActionListener(e -> {
                Main.getTags().remove(tag);
                SidebarPanel.getInstance().clearTags();
                SidebarPanel.getInstance().loadTags();
            });

            add(label, BorderLayout.CENTER);
        }
    }
}
