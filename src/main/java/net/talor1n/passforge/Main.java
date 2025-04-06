package net.talor1n.passforge;

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import lombok.Getter;
import lombok.Setter;
import net.talor1n.passforge.entity.Account;
import net.talor1n.passforge.entity.Tag;
import net.talor1n.passforge.ui.frame.MainFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    @Getter
    private static final List<Account> accounts = new ArrayList<>();
    @Getter
    private static final List<Tag> tags = new ArrayList<>();
    @Getter
    @Setter
    private static Tag selectedTag = null;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatOneDarkIJTheme());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        SwingUtilities.invokeLater(() -> {
            loadData();
            MainFrame.getInstance();
        });
    }

    public static void saveData() {
        FileManager.saveData(accounts, tags);
    }

    public static void saveDataAs() {
        FileManager.saveData(accounts, tags);
    }

    public static void loadData() {
        FileManager.DataContainer data = FileManager.loadData();
        if (data != null) {
            accounts.clear();
            tags.clear();
            accounts.addAll(data.getAccounts());
            tags.addAll(data.getTags());
            MainFrame.getInstance().repaint();
        }
    }

    public static void addAccount(Account account) {
        accounts.add(account);
        saveData();
    }
}