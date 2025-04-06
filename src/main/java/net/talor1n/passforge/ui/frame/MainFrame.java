package net.talor1n.passforge.ui.frame;

import net.talor1n.passforge.FileManager;
import net.talor1n.passforge.Main;
import net.talor1n.passforge.ui.panel.ScrollAccountPanel;
import net.talor1n.passforge.ui.panel.SidebarPanel;
import net.talor1n.passforge.ui.util.SearchTextField;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private static MainFrame instance;
    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        instance.toFront();
        instance.requestFocus();
        if (instance.getState() == JFrame.ICONIFIED) {
            instance.setState(JFrame.NORMAL);
        }
        return instance;
    }

    SearchTextField filter = new SearchTextField();
    ScrollAccountPanel scrollAccountPanel = new ScrollAccountPanel(Main.getAccounts());
    private MainFrame() {
        setSize(1280, 720);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        filter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String searchText = filter.getText().trim().toLowerCase();
                scrollAccountPanel.filterAccounts(searchText);
            }
        });

        var backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(backgroundPanel);

        var rightpanel = new JPanel(new BorderLayout());
        var mainPanel = new JPanel(new BorderLayout());

        backgroundPanel.add(rightpanel, BorderLayout.WEST);
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        backgroundPanel.setBackground(new Color(0x292F36));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rightpanel, mainPanel);
        splitPane.setDividerLocation(250);
        splitPane.setDividerSize(0);
        splitPane.setOpaque(true);

        BasicSplitPaneDivider divider = ((BasicSplitPaneUI) splitPane.getUI()).getDivider();
        divider.setBackground(null);
        divider.setBorder(null);

        backgroundPanel.add(splitPane, BorderLayout.CENTER);

        ImageIcon originalIcon = new ImageIcon(MainFrame.class.getResource("/img/icon.png"));

        var titleLabel = new JLabel(originalIcon);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        var titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0x303030)));
        titlePanel.setBackground(new Color(0x181A1F));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(12,0,11, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);


        rightpanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
        rightpanel.add(SidebarPanel.getInstance(), BorderLayout.CENTER);
        rightpanel.add(titlePanel, BorderLayout.NORTH);

        JPanel topbarPanel = new JPanel(new BorderLayout(7, 7));
        JButton button = new JButton("Добавить запись");
        button.setBackground(new Color(0x282C34));

        button.addActionListener(e -> {
            new ActionFrame(null);
        });

        mainPanel.add(topbarPanel, BorderLayout.NORTH);
        mainPanel.setBackground(new Color(0x1C1F25));
        mainPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));

        JPanel topbarContainer = new JPanel(new BorderLayout());
        topbarContainer.setBackground(new Color(0x181A1F)); // Цвет фона

        topbarPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        topbarPanel.setBackground(new Color(0x181A1F));
        topbarPanel.add(button, BorderLayout.WEST);

        var filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, -4, 0));
        filter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        filter.setPreferredSize(new Dimension(Integer.MAX_VALUE, 32));
        filterPanel.add(filter);
        filterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        filterPanel.setBackground(new Color(0x181A1F));

        topbarPanel.add(filterPanel, BorderLayout.CENTER);

        JPanel dividerPanel = new JPanel();
        dividerPanel.setPreferredSize(new Dimension(getWidth(), 1));
        dividerPanel.setBackground(new Color(0x303030));

        topbarContainer.add(topbarPanel, BorderLayout.CENTER);
        topbarContainer.add(dividerPanel, BorderLayout.SOUTH);

        mainPanel.add(topbarContainer, BorderLayout.NORTH);
        mainPanel.add(scrollAccountPanel, BorderLayout.CENTER);

        button.setFocusPainted(false);
        button.setFont(new Font("Roboto", Font.BOLD, 14));

        button.setMargin(new Insets(5, 25, 5, 25));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showExitConfirmation();
            }
        });

        setVisible(true);
    }

    private void showExitConfirmation() {
        if (FileManager.hasUnsavedChanges(Main.getAccounts(), Main.getTags())) { // Проверяем изменения
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Сохранить изменения перед выходом?",
                    "Подтверждение выхода",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            switch (result) {
                case JOptionPane.YES_OPTION:
                    FileManager.saveData(Main.getAccounts(), Main.getTags());
                    System.exit(0);
                    break;
                case JOptionPane.NO_OPTION:
                    System.exit(0);
                    break;
                case JOptionPane.CANCEL_OPTION:
                case JOptionPane.CLOSED_OPTION:
                    break;
            }
        } else {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Выйти из приложения?",
                    "Подтверждение выхода",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    @Override
    public void repaint(long time, int x, int y, int width, int height) {
        super.repaint(time, x, y, width, height);
        scrollAccountPanel.repaint(time, x, y, width, height);
        scrollAccountPanel.updateTable();
    }
}
