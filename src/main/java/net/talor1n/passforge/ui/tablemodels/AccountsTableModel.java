package net.talor1n.passforge.ui.tablemodels;

import net.talor1n.passforge.entity.Account;
import net.talor1n.passforge.Main;

import javax.swing.table.AbstractTableModel;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AccountsTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Сайт", "Почта", "Пароль"};
    private final List<Account> allAccounts;
    private List<Account> filteredAccounts;

    public AccountsTableModel(List<Account> accounts) {
        this.allAccounts = accounts != null ? accounts : new ArrayList<>();
        this.filteredAccounts = new ArrayList<>(this.allAccounts);
    }

    @Override
    public int getRowCount() {
        return filteredAccounts.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= filteredAccounts.size()) {
            return null;
        }
        Account account = filteredAccounts.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> getSiteName(account.getUrl());
            case 1 -> account.getEmail();
            case 2 -> account.getPassword();
            default -> null;
        };
    }

    public void updateData() {
        List<Account> baseList = (Main.getSelectedTag() == null) ?
                allAccounts : Main.getSelectedTag().getAccounts();

        filteredAccounts = new ArrayList<>(baseList != null ? baseList : allAccounts);
        fireTableDataChanged();
    }

    public void filter(String searchText) {
        List<Account> baseList = (Main.getSelectedTag() == null) ?
                allAccounts : Main.getSelectedTag().getAccounts();

        baseList = (baseList != null) ? baseList : allAccounts; // Защита от null

        filteredAccounts = new ArrayList<>();

        if (searchText == null || searchText.trim().isEmpty()) {
            filteredAccounts.addAll(baseList);
        } else {
            String searchLower = searchText.trim().toLowerCase();
            for (Account account : baseList) {
                if (account != null && (
                        getSiteName(account.getUrl()).toLowerCase().contains(searchLower) ||
                                (account.getEmail() != null && account.getEmail().toLowerCase().contains(searchLower)) ||
                                (account.getPassword() != null && account.getPassword().toLowerCase().contains(searchLower)))) {
                    filteredAccounts.add(account);
                }
            }
        }
        fireTableDataChanged();
    }

    public void refresh() {
        updateData(); // Полное обновление данных
    }

    public static String getSiteName(String urlString) {
        if (urlString == null) {
            return "Сайт недоступен";
        }
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            String domainName = host.replaceFirst("^www\\.", "");
            return capitalize(domainName);
        } catch (Exception e) {
            return "Сайт недоступен";
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}