package gui_panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.JTableHeader;

/// <summary>
/// AppUI is a utility class that provides consistent styling and common UI components for the application.
/// It includes methods to create styled buttons, text fields, tables, and layout panels, as
/// well as a method to style the main application frame. This class is designed to centralize all UI-related code,    
/// making it easier to maintain a cohesive look and feel across the application and to update the UI design in one place.
/// </summary>


public final class AppUI {

    private static final Color PAGE_BACKGROUND = new Color(243,246,251);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color MUTED_TEXT = new Color(100, 116, 139);
    private static final Color BORDER_COLOR = new Color(203, 213, 225);
    private static final Color INPUT_BACKGROUND = new Color(248, 250, 252);
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private static final Color SECONDARY_COLOR = new Color(226, 232, 240);

    private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 28);
    private static final Font SUBTITLE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
    private static final Font LABEL_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 13);
    private static final Font BODY_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 13);

    private AppUI() {
    }

    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(PAGE_BACKGROUND);
    }

    public static JPanel createScreenShell(String title, String subtitle, JComponent content) {
        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(PAGE_BACKGROUND);
        page.setBorder(new EmptyBorder(32, 32, 32, 32));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(28, 28, 28, 28)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(MUTED_TEXT);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(22));
        card.add(content);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        page.add(card, gbc);
        return page;
    }

    public static JPanel createBodyPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    public static JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return formPanel;
    }

    public static void addFormRow(JPanel formPanel, int row, String labelText, JComponent field) {
        JPanel rowPanel = new JPanel(new BorderLayout(0, 6));
        rowPanel.setOpaque(false);

        JLabel label = new JLabel(labelText, SwingConstants.LEFT);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);

        rowPanel.add(label, BorderLayout.NORTH);
        rowPanel.add(field, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 16, 0);
        formPanel.add(rowPanel, gbc);
    }

    public static JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        styleInput(field);
        return field;
    }

    public static JPasswordField createPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        styleInput(field);
        return field;
    }

    public static JTextField createReadOnlyField(int columns) {
        JTextField field = createTextField(columns);
        field.setEditable(false);
        return field;
    }

    public static <T> JComboBox<T> createComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setFont(BODY_FONT);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBackground(INPUT_BACKGROUND);
        comboBox.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(4, 8, 4, 8)));
        return comboBox;
    }

    public static JTextArea createOutputArea(int rows, int columns, String placeholderText) {
        JTextArea area = new JTextArea(rows, columns);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setText(placeholderText);
        area.setFont(BODY_FONT);
        area.setForeground(TEXT_COLOR);
        area.setBackground(INPUT_BACKGROUND);
        area.setBorder(new EmptyBorder(12, 12, 12, 12));
        return area;
    }

    public static JScrollPane wrapTextArea(JTextArea area) {
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(INPUT_BACKGROUND);
        scrollPane.setPreferredSize(new Dimension(860, 220));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        return scrollPane;
    }

    public static JTable createTable(javax.swing.table.TableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setGridColor(BORDER_COLOR);
        table.setShowVerticalLines(false);
        table.setFont(BODY_FONT);
        table.setForeground(TEXT_COLOR);
        table.setBackground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(LABEL_FONT);
        header.setForeground(TEXT_COLOR);
        header.setBackground(INPUT_BACKGROUND);
        header.setBorder(new LineBorder(BORDER_COLOR, 1, true));

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(140);
        }

        return table;
    }

    public static JScrollPane wrapTable(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(860, 260));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, PRIMARY_COLOR, Color.WHITE, PRIMARY_COLOR.darker());
        return button;
    }

    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, SECONDARY_COLOR, TEXT_COLOR, BORDER_COLOR);
        return button;
    }

    public static JPanel createButtonRow(JButton... buttons) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (JButton button : buttons) {
            row.add(button);
        }

        return row;
    }

    public static JPanel createMenuButtonStack(JButton... buttons) {
        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
        stack.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setPreferredSize(new Dimension(280, 42));
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            stack.add(button);

            if (i < buttons.length - 1) {
                stack.add(Box.createVerticalStrut(10));
            }
        }

        return stack;
    }

    public static JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SUBTITLE_FONT);
        label.setForeground(MUTED_TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private static void styleInput(JTextField field) {
        field.setFont(BODY_FONT);
        field.setForeground(TEXT_COLOR);
        field.setBackground(INPUT_BACKGROUND);
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(10, 12, 10, 12)));
    }

    private static void styleButton(JButton button, Color background, Color foreground, Color borderColor) {
        button.setUI(new BasicButtonUI());
        button.setFont(BUTTON_FONT);
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorderPainted(true);
        button.setBorder(new CompoundBorder(
                new LineBorder(borderColor, 1, true),
                new EmptyBorder(10, 16, 10, 16)));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }
}
