package net.talor1n.passforge.ui.frame;

import net.talor1n.passforge.entity.Account;
import net.talor1n.passforge.Main;
import net.talor1n.passforge.entity.Tag;
import net.talor1n.passforge.ui.tablemodels.TagsTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class TagsFrame extends JFrame {

    private Map<JCheckBox, Tag> checkboxTagMap;

    public TagsFrame(Account account) {
        super("Редактирование Тэгов");

        checkboxTagMap = loadTagsForAccount(account);
        setIconImage(new ImageIcon(TagsFrame.class.getResource("/img/key_icon.png")).getImage());
        setMinimumSize(new Dimension(540, 370));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(28, 31, 37));
        add(mainPanel);

        // Создаём JTable с моделью данных
        JTable table = new JTable(new TagsTableModel(checkboxTagMap));

        // Создаём JScrollPane для прокрутки
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Кнопка для сохранения
        JPanel saveButtonContainer = new JPanel(new BorderLayout());
        mainPanel.add(saveButtonContainer, BorderLayout.SOUTH);
        saveButtonContainer.setBackground(new Color(28, 31, 37));

        JButton saveButton = new JButton("Сохранить");
        saveButton.setBackground(new Color(0x3D4856));
        saveButton.setForeground(Color.WHITE);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButtonContainer.add(saveButton, BorderLayout.EAST);

        saveButton.addActionListener(e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    for (Map.Entry<JCheckBox, Tag> entry : checkboxTagMap.entrySet()) {
                        JCheckBox checkBox = entry.getKey();
                        Tag tag = entry.getValue();
                        int index = Main.getTags().indexOf(tag);

                        if (checkBox.isSelected()) {
                            if (!tag.getAccounts().contains(account)) {
                                tag.addAccount(account);
                            }
                        } else {
                            if (tag.getAccounts().contains(account)) {
                                tag.removeAccount(account);
                            }
                        }
                        Main.getTags().set(index, tag);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    MainFrame.getInstance().repaint();
                    dispose();
                }
            };

            worker.execute();
        });

        setVisible(true);
    }

    private Map<JCheckBox, Tag> loadTagsForAccount(Account account) {
        Map<JCheckBox, Tag> result = new HashMap<>();
        for (Tag tag : Main.getTags()) {
            result.put(new JCheckBox((Icon) null, tag.getAccounts() != null && tag.getAccounts().contains(account)), tag);
        }
        return result;
    }
}

