package textanalyzer.gui.components;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * A felület alján megjelenő információs sor.
 * 
 * @author Istvánfi Zsolt
 */
public class StatusBar extends JPanel {

    private static final long serialVersionUID = 4676467245707954480L;

    private final JTextField textField;

    public StatusBar(int height) {
	super();

	setBorder(new BevelBorder(BevelBorder.LOWERED));
	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	setPreferredSize(new Dimension(getPreferredSize().width, height));

	textField = new JTextField();
	textField.setFont(new Font("Arial", Font.BOLD, 12));
	textField.setEditable(false);
	textField.setHorizontalAlignment(SwingConstants.LEFT);

	add(textField);
    }

    public void getText() {
	textField.getText();
    }

    public void setText(String text) {
	textField.setText(text);
    }
}
