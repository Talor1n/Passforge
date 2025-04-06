package net.talor1n.passforge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import net.talor1n.passforge.EncryptionUtil;
import net.talor1n.passforge.entity.Account;
import net.talor1n.passforge.entity.Tag;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileManager {
    @Getter
    private static File currentFile = null;
    private static String masterPassword = null;

    static class DataContainer {
        private List<Account> accounts;
        private List<Tag> tags;

        public DataContainer(List<Account> accounts, List<Tag> tags) {
            this.accounts = accounts;
            this.tags = tags;
        }

        public List<Account> getAccounts() {
            return accounts;
        }

        public List<Tag> getTags() {
            return tags;
        }
    }

    public static void setMasterPassword(String password) {
        masterPassword = password;
    }

    public static String getMasterPassword() {
        return masterPassword;
    }

    public static void saveData(List<Account> accounts, List<Tag> tags) {
        if (masterPassword == null) {
            masterPassword = promptForNewMasterPassword();
            if (masterPassword == null || masterPassword.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Мастер-пароль не введен. Сохранение отменено.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (currentFile == null) {
            saveDataAs(accounts, tags);
            return;
        }

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            DataContainer data = new DataContainer(accounts, tags);
            String json = gson.toJson(data);
            String encryptedData = EncryptionUtil.encrypt(json, masterPassword);

            try (FileWriter writer = new FileWriter(currentFile)) {
                writer.write(encryptedData);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при сохранении файла: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void saveDataAs(List<Account> accounts, List<Tag> tags) {
        String passforgeDirPath = System.getProperty("user.home") + File.separator + "passforge";
        File passforgeDir = new File(passforgeDirPath);

        // Создаем директорию passforge, если она не существует
        if (!passforgeDir.exists()) {
            try {
                boolean created = passforgeDir.mkdirs();
                if (!created) {
                    JOptionPane.showMessageDialog(null,
                            "Не удалось создать директорию: " + passforgeDirPath,
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (SecurityException e) {
                JOptionPane.showMessageDialog(null,
                        "Нет прав доступа для создания директории: " + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        File file = new File(passforgeDirPath + File.separator + "pass.enc");

        try {
            // Если файл не существует, создаем его
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    JOptionPane.showMessageDialog(null,
                            "Не удалось создать файл: " + file.getAbsolutePath(),
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            currentFile = file;
            saveData(accounts, tags);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при создании файла: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static DataContainer loadData() {
        String passforgeDirPath = System.getProperty("user.home") + File.separator + "passforge";
        File passforgeDir = new File(passforgeDirPath);
        File file = new File(passforgeDirPath + File.separator + "pass.enc");

        if (file.exists() & file.isFile()) {
            if (masterPassword == null) {
                masterPassword = promptForPassword("Введите мастер-пароль для расшифровки:");
                if (masterPassword == null || masterPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Мастер-пароль не введен. Загрузка отменена.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }

            try {
                String encryptedData = new String(Files.readAllBytes(file.toPath()));
                String json;
                try {
                    json = EncryptionUtil.decrypt(encryptedData, masterPassword);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Неверный мастер-пароль или поврежденный файл.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    masterPassword = null;
                    return null;
                }

                Gson gson = new Gson();
                DataContainer data = gson.fromJson(json, new TypeToken<DataContainer>(){}.getType());
                if (data == null || data.getAccounts() == null || data.getTags() == null) {
                    data = new DataContainer(new ArrayList<>(), new ArrayList<>());
                }
                currentFile = file;
                return data;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Ошибка при загрузке файла: " + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return new DataContainer(new ArrayList<>(), new ArrayList<>());
            }
        }
        return null;
    }

    public static boolean hasUnsavedChanges(List<Account> currentAccounts, List<Tag> currentTags) {
        if (currentFile == null || !currentFile.exists()) {
            return !currentAccounts.isEmpty() || !currentTags.isEmpty();
        }

        try {
            String encryptedData = new String(Files.readAllBytes(currentFile.toPath()));
            String savedJson = EncryptionUtil.decrypt(encryptedData, masterPassword);
            Gson gson = new Gson();
            DataContainer savedData = gson.fromJson(savedJson, new TypeToken<DataContainer>(){}.getType());

            if (savedData == null) {
                return !currentAccounts.isEmpty() || !currentTags.isEmpty();
            }

            List<Account> savedAccounts = savedData.getAccounts() != null ? savedData.getAccounts() : new ArrayList<>();
            List<Tag> savedTags = savedData.getTags() != null ? savedData.getTags() : new ArrayList<>();

            if (savedAccounts.size() != currentAccounts.size() || savedTags.size() != currentTags.size()) {
                return true;
            }

            for (int i = 0; i < savedAccounts.size(); i++) {
                Account saved = savedAccounts.get(i);
                Account current = currentAccounts.get(i);
                if (!Objects.equals(saved.getUrl(), current.getUrl()) ||
                        !Objects.equals(saved.getEmail(), current.getEmail()) ||
                        !Objects.equals(saved.getPassword(), current.getPassword())) {
                    return true;
                }
            }

            for (int i = 0; i < savedTags.size(); i++) {
                Tag saved = savedTags.get(i);
                Tag current = currentTags.get(i);
                if (!Objects.equals(saved.getName(), current.getName()) ||
                        !Objects.equals(saved.getAccounts(), current.getAccounts())) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private static String promptForPassword(String message) {
        JPasswordField passwordField = new JPasswordField(20);
        JPanel panel = new JPanel();
        panel.add(new JLabel(message));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Ввод мастер-пароля",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            return new String(passwordField.getPassword());
        }
        return null;
    }

    private static String promptForNewMasterPassword() {
        JPasswordField passwordField = new JPasswordField(20);
        JPasswordField confirmField = new JPasswordField(20);
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Введите новый мастер-пароль:"));
        panel.add(passwordField);
        panel.add(new JLabel("Подтвердите мастер-пароль:"));
        panel.add(confirmField);

        while (true) {
            passwordField.setText("");
            confirmField.setText("");

            int result = JOptionPane.showConfirmDialog(null, panel, "Создание мастер-пароля",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String password = new String(passwordField.getPassword());
                String confirm = new String(confirmField.getPassword());

                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Мастер-пароль не может быть пустым.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                } else if (password.length() < 8) {
                    JOptionPane.showMessageDialog(null,
                            "Мастер-пароль должен содержать не менее 8 символов.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                } else if (!password.equals(confirm)) {
                    JOptionPane.showMessageDialog(null,
                            "Пароли не совпадают.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    return password;
                }
            } else {
                return null;
            }
        }
    }
}