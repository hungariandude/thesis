package textanalyzer.gui;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Rádiógombokat vízszintesen vagy függõlegesen megjelenítõ panel.
 * 
 * @author Istvánfi Zsolt
 */
public class RadioPanel extends JPanel {
    private static final long serialVersionUID = 7960107096632067124L;

    private final JRadioButton[] buttons;

    public RadioPanel(String[] values, boolean horizontalAlignment) {
	if (values.length == 0) {
	    throw new IllegalArgumentException(
		    "String array parameter cannot be empty.");
	}

	BoxLayout layout = new BoxLayout(this,
		horizontalAlignment ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS);
	setLayout(layout);

	ButtonGroup group = new ButtonGroup();
	buttons = new JRadioButton[values.length];
	for (int i = 0; i < values.length; ++i) {
	    JRadioButton button = new JRadioButton(values[i]);
	    buttons[i] = button;
	    group.add(button);
	    add(button);
	}

	buttons[0].setSelected(true);
    }

    public String getSelectedValue() {
	for (JRadioButton button : buttons) {
	    if (button.isSelected()) {
		return button.getText();
	    }
	}
	return null;
    }

    @Override
    public void setEnabled(boolean enabled) {
	super.setEnabled(enabled);
	for (JRadioButton button : buttons) {
	    button.setEnabled(enabled);
	}
    }

}
