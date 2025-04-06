package net.talor1n.passforge.ui.frame;

import net.talor1n.passforge.entity.Account;
import net.talor1n.passforge.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class ActionFrame extends JFrame {

    private static ActionFrame instance;
    private JTextField passwordTextField;
    private JProgressBar passwordProgressBar;
    private JLabel passwordQualityLabel;
    private JCheckBox uppercaseCheckBox;
    private JCheckBox lowercaseCheckBox;
    private JCheckBox numbersCheckBox;
    private JCheckBox specialCharsCheckBox;
    private JSpinner lengthSpinner;

    public ActionFrame(Account account) {
        super(account == null ? "Добавить учетную запись" : "Редактировать учетную запись");
        setIconImage(new ImageIcon(ActionFrame.class.getResource("/img/key_icon.png")).getImage());
        setMinimumSize(new Dimension(540, 370));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        // Основная панель с вертикальным BoxLayout и уменьшенными отступами
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(28, 31, 37));
        add(mainPanel);

        JPanel siteInfoPanel = new JPanel();
        siteInfoPanel.setBackground(new Color(0x1C1F25));
        JTextField siteInfoURI = new JTextField();
        if (account != null)
        {
            siteInfoURI.setText(account.getUrl());
        }

        siteInfoURI.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        JLabel siteInfoLabel = new JLabel("URL Сайта");
        JPanel siteInfoLabelPanel = new JPanel(new BorderLayout());
        siteInfoPanel.setLayout(new BoxLayout(siteInfoPanel, BoxLayout.Y_AXIS));
        siteInfoPanel.add(siteInfoLabelPanel);
        siteInfoLabelPanel.add(siteInfoLabel, BorderLayout.WEST);
        siteInfoLabelPanel.setBackground(new Color(0x1C1F25));
        siteInfoPanel.add(siteInfoURI);
        mainPanel.add(siteInfoPanel);

        JPanel emailInfoPanel = new JPanel();
        emailInfoPanel.setBackground(new Color(0x1C1F25));
        JTextField emailInfoField = new JTextField();
        if (account != null)
        {
            emailInfoField.setText(account.getEmail());
        }
        emailInfoField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        JLabel emailInfoLabel = new JLabel("Email");
        JPanel emailInfoLabelPanel = new JPanel(new BorderLayout());
        emailInfoPanel.setLayout(new BoxLayout(emailInfoPanel, BoxLayout.Y_AXIS));
        emailInfoPanel.add(emailInfoLabelPanel);
        emailInfoLabelPanel.add(emailInfoLabel, BorderLayout.WEST);
        emailInfoLabelPanel.setBackground(new Color(0x1C1F25));
        emailInfoPanel.add(emailInfoField);
        mainPanel.add(emailInfoPanel);

        // Панель генератора пароля

        JPanel passwordGeneratorPanel = new JPanel(new BorderLayout());
        passwordGeneratorPanel.setBackground(new Color(28, 31, 37));
        passwordGeneratorPanel.setPreferredSize(new Dimension(400, 18));

        JLabel passwordLabel = new JLabel("Пароль");
        JPanel passwordLabelPanel = new JPanel(new BorderLayout());
        siteInfoPanel.setLayout(new BoxLayout(siteInfoPanel, BoxLayout.Y_AXIS));
        siteInfoPanel.add(passwordLabelPanel);
        passwordLabelPanel.add(passwordLabel, BorderLayout.WEST);
        passwordLabelPanel.setBackground(new Color(0x1C1F25));
        mainPanel.add(passwordLabelPanel);

        mainPanel.add(passwordGeneratorPanel);
        mainPanel.add(Box.createVerticalStrut(5));

        // Панель проверки пароля
        JPanel passwordCheckerPanel = new JPanel(new BorderLayout());
        passwordCheckerPanel.setBackground(new Color(28, 31, 37));
        passwordCheckerPanel.setPreferredSize(new Dimension(400, 8));
        mainPanel.add(passwordCheckerPanel);
        mainPanel.add(Box.createVerticalStrut(5));

        // Панель настроек пароля
        JPanel passwordSettings = new JPanel(new BorderLayout());
        JPanel checkBoxContainer = new JPanel();
        checkBoxContainer.setLayout(new BoxLayout(checkBoxContainer, BoxLayout.Y_AXIS));
        checkBoxContainer.setBackground(new Color(28, 31, 37));
        checkBoxContainer.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        passwordSettings.setBackground(new Color(28, 31, 37));
        passwordSettings.setMinimumSize(new Dimension(400, 100));
        mainPanel.add(passwordSettings);
        passwordSettings.add(checkBoxContainer, BorderLayout.WEST);

        // Поле пароля
        passwordTextField = new JTextField();
        if (account != null)
        {
            passwordTextField.setText(account.getPassword());
        }
        passwordTextField.setEditable(true);
        passwordTextField.setPreferredSize(new Dimension(300, 40));
        passwordGeneratorPanel.add(passwordTextField, BorderLayout.CENTER);

        passwordTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (passwordTextField.getText() != null && !passwordTextField.getText().isEmpty()) {
                    updatePasswordStrength(passwordTextField.getText());
                }
            }
        });

        // Кнопка обновления
        ImageIcon originalIcon = new ImageIcon(MainFrame.class.getResource("/img/restart.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
        JButton reloadButton = new JButton(new ImageIcon(scaledImage));
        reloadButton.setMargin(new Insets(5, 5, 5, 5));
        passwordGeneratorPanel.add(reloadButton, BorderLayout.EAST);

        // Прогресс-бар
        passwordProgressBar = new JProgressBar();
        passwordProgressBar.setValue(100);
        passwordProgressBar.setPreferredSize(new Dimension(200, 15));
        passwordProgressBar.setForeground(new Color(40, 140, 80));
        passwordProgressBar.setLayout(new BorderLayout());
        passwordCheckerPanel.add(passwordProgressBar, BorderLayout.CENTER);

        // Метка с вопросом
        passwordQualityLabel = new JLabel("Хороший");
        passwordQualityLabel.setFont(new Font("Roboto", Font.BOLD, 10));
        passwordQualityLabel.setForeground(Color.WHITE);
        passwordQualityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordProgressBar.add(passwordQualityLabel, BorderLayout.CENTER);

        // Checkbox'ы
        uppercaseCheckBox = new JCheckBox("Заглавные буквы (A-Z)");
        lowercaseCheckBox = new JCheckBox("Строчные буквы (a-z)");
        numbersCheckBox = new JCheckBox("Цифры (0-9)");
        specialCharsCheckBox = new JCheckBox("Спецсимволы (!@#$%)");

        // Устанавливаем стили для checkbox'ов
        uppercaseCheckBox.setForeground(Color.WHITE);
        lowercaseCheckBox.setForeground(Color.WHITE);
        numbersCheckBox.setForeground(Color.WHITE);
        specialCharsCheckBox.setForeground(Color.WHITE);

        uppercaseCheckBox.setBackground(new Color(28, 31, 37));
        lowercaseCheckBox.setBackground(new Color(28, 31, 37));
        numbersCheckBox.setBackground(new Color(28, 31, 37));
        specialCharsCheckBox.setBackground(new Color(28, 31, 37));

        uppercaseCheckBox.setFont(new Font("Roboto", Font.PLAIN, 12));
        lowercaseCheckBox.setFont(new Font("Roboto", Font.PLAIN, 12));
        numbersCheckBox.setFont(new Font("Roboto", Font.PLAIN, 12));
        specialCharsCheckBox.setFont(new Font("Roboto", Font.PLAIN, 12));

        // Выравниваем checkbox'ы по левому краю
        uppercaseCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        lowercaseCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        numbersCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        specialCharsCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Панель для длины пароля
        JPanel lengthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lengthPanel.setBackground(new Color(28, 31, 37));
        lengthPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lengthLabel = new JLabel("Длина пароля: ");
        lengthLabel.setForeground(Color.WHITE);
        lengthLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        SpinnerNumberModel lengthModel = new SpinnerNumberModel(12, 4, 32, 1);
        lengthSpinner = new JSpinner(lengthModel);
        lengthSpinner.setPreferredSize(new Dimension(60, 20));
        lengthPanel.add(lengthLabel);
        lengthPanel.add(lengthSpinner);

        // По умолчанию включаем некоторые опции
        uppercaseCheckBox.setSelected(true);
        lowercaseCheckBox.setSelected(true);
        numbersCheckBox.setSelected(true);
        specialCharsCheckBox.setSelected(false);

        // Добавляем элементы в checkBoxContainer
        checkBoxContainer.add(uppercaseCheckBox);
        checkBoxContainer.add(lowercaseCheckBox);
        checkBoxContainer.add(numbersCheckBox);
        checkBoxContainer.add(specialCharsCheckBox);

        // Обработчик для кнопки генерации пароля
        reloadButton.addActionListener(e -> generatePassword());

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel buttonContainer = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel);
        buttonPanel.add(buttonContainer, BorderLayout.EAST);
        buttonPanel.add(lengthPanel, BorderLayout.WEST);
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));

        buttonPanel.setBackground(new Color(28, 31, 37));
        buttonContainer.setBackground(new Color(28, 31, 37));

        JButton okButton = new JButton("Ок");
        JButton cancelButton = new JButton("Отмена");
        cancelButton.setBackground(new Color(220, 53, 69));

        buttonContainer.add(okButton);
        buttonContainer.add(cancelButton);

        cancelButton.addActionListener(e -> {
            dispose();
        });

        okButton.addActionListener(e -> {
            if (siteInfoURI.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Укажите сайт!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (emailInfoField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Укажите вашу электронную почту!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (passwordTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Укажите пароль", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (account == null) {
                Main.getAccounts().add(Account.builder()
                        .email(emailInfoField.getText())
                        .url(siteInfoURI.getText())
                        .password(passwordTextField.getText())
                        .build());
                MainFrame.getInstance().repaint();
            }
            else {
                int id = Main.getAccounts().indexOf(account);
                if (id != -1) {
                    account.setEmail(emailInfoField.getText());
                    account.setUrl(siteInfoURI.getText());
                    account.setPassword(passwordTextField.getText());
                    Main.getAccounts().set(id, account);
                }
            }
            MainFrame.getInstance().repaint();
            dispose();
        });

        setVisible(true);
    }

    // Метод генерации пароля
    private void generatePassword() {
        boolean useUppercase = uppercaseCheckBox.isSelected();
        boolean useLowercase = lowercaseCheckBox.isSelected();
        boolean useNumbers = numbersCheckBox.isSelected();
        boolean useSpecialChars = specialCharsCheckBox.isSelected();
        int length = (int) lengthSpinner.getValue();

        // Проверяем, что выбрана хотя бы одна опция
        if (!useUppercase && !useLowercase && !useNumbers && !useSpecialChars) {
            JOptionPane.showMessageDialog(this, "Выберите хотя бы одну опцию для генерации пароля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder allowedChars = new StringBuilder();
        if (useUppercase) allowedChars.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        if (useLowercase) allowedChars.append("abcdefghijklmnopqrstuvwxyz");
        if (useNumbers) allowedChars.append("0123456789");
        if (useSpecialChars) allowedChars.append("!@#$%^&*()_+-=[]{}|;:,.<>?");

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // Генерируем пароль
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            password.append(allowedChars.charAt(index));
        }

        // Устанавливаем сгенерированный пароль в поле
        passwordTextField.setText(password.toString());

        // Оцениваем сложность пароля
        updatePasswordStrength(password.toString());
    }

    // Метод оценки сложности пароля
    private void updatePasswordStrength(String password) {
        int score = 0;

        // Критерии сложности
        if (password.length() >= 8) score += 20; // Длина 8+
        if (password.length() >= 12) score += 20; // Длина 12+
        if (password.matches(".*[A-Z].*") && uppercaseCheckBox.isSelected()) score += 20; // Есть заглавные
        if (password.matches(".*[a-z].*") && lowercaseCheckBox.isSelected()) score += 20; // Есть строчные
        if (password.matches(".*\\d.*") && numbersCheckBox.isSelected()) score += 20; // Есть цифры
        if (password.matches(".*[!@#$%^&*()_\\-+=\\[\\]{}|;:,.<>?].*") && specialCharsCheckBox.isSelected())
            score += 20;

        // Ограничиваем score в диапазоне 0-100
        score = Math.min(score, 100);

        // Обновляем прогресс-бар
        passwordProgressBar.setValue(score);

        // Обновляем цвет прогресс-бара
        if (score <= 40) {
            passwordProgressBar.setForeground(new Color(180, 40, 40)); // Тёмно-красный для слабого
        } else if (score <= 75) {
            passwordProgressBar.setForeground(new Color(180, 140, 40)); // Тёмно-жёлтый для среднего
        } else {
            passwordProgressBar.setForeground(new Color(40, 140, 80)); // Тёмно-зелёный для сильного
        }

        // Обновляем метку
        if (score <= 40) {
            passwordQualityLabel.setText("Слабый");
        } else if (score <= 75) {
            passwordQualityLabel.setText("Средний");
        } else {
            passwordQualityLabel.setText("Надёжный");
        }
    }

    @Override
    public void dispose() {
        instance = null;
        super.dispose();
    }
}