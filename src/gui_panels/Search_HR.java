package gui_panels;
import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.*;

public class Search_HR extends JPanel {
    private final JTextArea resultArea = AppUI.createOutputArea(
            7,
            28,
            "Search results will appear here after you connect the HR lookup logic.");
    private final JTextField empIdField = AppUI.createTextField(22);

    public Search_HR(MainFrame frame) {
        JButton searchBtn = AppUI.createPrimaryButton("Search");
        JButton clearBtn = AppUI.createSecondaryButton("Clear");
        JButton backBtn = AppUI.createSecondaryButton("Back to Menu");

        clearBtn.addActionListener(e -> {
            empIdField.setText("");
            resultArea.setText("Search results will appear here after you connect the HR lookup logic.");
        });
        backBtn.addActionListener(e -> frame.returnToMenu());

        JPanel formPanel = AppUI.createFormPanel();
        AppUI.addFormRow(formPanel, 0, "Employee ID", empIdField);

        JPanel body = AppUI.createBodyPanel();
        body.add(formPanel);
        body.add(Box.createVerticalStrut(6));
        body.add(AppUI.createButtonRow(searchBtn, clearBtn, backBtn));
        body.add(Box.createVerticalStrut(18));
        body.add(AppUI.createInfoLabel("Results"));
        body.add(Box.createVerticalStrut(8));
        body.add(AppUI.wrapTextArea(resultArea));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Search Employee",
                "Find and view employee information.",
                body));
    }
}
