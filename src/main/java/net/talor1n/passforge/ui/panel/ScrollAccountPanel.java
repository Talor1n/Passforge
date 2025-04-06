package net.talor1n.passforge.ui.panel;

import net.talor1n.passforge.entity.Account;
import net.talor1n.passforge.Main;
import net.talor1n.passforge.ui.frame.ActionFrame;
import net.talor1n.passforge.ui.frame.MainFrame;
import net.talor1n.passforge.ui.frame.TagsFrame;
import net.talor1n.passforge.ui.tablemodels.AccountsTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ScrollAccountPanel extends JPanel {
    AccountsTableModel model;
    public ScrollAccountPanel(List<Account> accounts) {
        super(new BorderLayout());

        model = new AccountsTableModel(accounts);
        JTable table = new JTable(model);

        // Настройка внешнего вида таблицы
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(60, 60, 60));

        table.setForeground(Color.WHITE);
        table.setBackground(new Color(0x1C1F25));
        table.setSelectionBackground(new Color(0x2A2F38));
        table.setSelectionForeground(Color.WHITE);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Редактировать");
        JMenuItem deleteItem = new JMenuItem("Удалить");
        JMenuItem moreItem = new JMenuItem("Добавить в тэги");

        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        popupMenu.add(moreItem);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()) {
                    table.setRowSelectionInterval(row, row);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        editItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            Account selected = accounts.get(selectedRow);
            new ActionFrame(selected);
            MainFrame.getInstance().repaint();
        });

        deleteItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            Account selected = accounts.get(selectedRow);
            Main.getTags().forEach(tag ->
            {
                tag.getAccounts().removeIf(acc -> acc.equals(selected));
            });
            Main.getAccounts().remove(selected);
            MainFrame.getInstance().repaint();
        });

        moreItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            Account selected = accounts.get(selectedRow);
            new TagsFrame(selected);
        });

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0x2B2F36));
        header.setForeground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD));

        // Центрирование текста в ячейках (опционально)
        TableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
            }
        };
        for (int i = 0; i < model.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(0x1C1F25));

        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateTable() {
        model.updateData();
    }
    public void filterAccounts(String searchText) {
        model.filter(searchText);
    }
}
