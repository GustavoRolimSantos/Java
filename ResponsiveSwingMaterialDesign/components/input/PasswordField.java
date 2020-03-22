package br.com.maxtercreations.responsiveswing.components.input;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import com.sun.org.glassfish.gmbal.Description;

import br.com.maxtercreations.responsiveswing.ResponsiveSwingMaterialDesign;
import br.com.maxtercreations.responsiveswing.animation.AnimateComponent;
import br.com.maxtercreations.responsiveswing.animation.Animation;
import br.com.maxtercreations.responsiveswing.callback.Callback;
import br.com.maxtercreations.responsiveswing.components.utils.RoundedBorder;
import br.com.maxtercreations.responsiveswing.constants.Constants;
import br.com.maxtercreations.responsiveswing.layouts.ResponsiveLayout;
import br.com.maxtercreations.responsiveswing.utils.Utils;

public class PasswordField {

	private JLabel label, errorLabel, eyeIcon;
	private Font labelFont = Utils.getMainFont(22);
	private ResponsiveLayout panel;
	private Color caretColor, transparent = new Color(255, 255, 255, 0), iconColor;

	private String caption;

	private Callback rules;

	private String error;

	private boolean firstInstance = true, required = false, hasUpdatedColors = false;

	private final int leftMargin = 25;

	private MaskFormatter maskData;
	private JPasswordField textField;

	private long forcedFocus = System.currentTimeMillis();

	@SuppressWarnings("serial")
	public PasswordField(String caption, ResponsiveLayout rl) {
		this.panel = rl;
		this.caption = caption;

		textField = new JPasswordField() {
			@Override
			public void repaint() {
				super.repaint();
				updateColors();
			}
		};

		textField.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				textField.requestFocusInWindow();
				label.requestFocusInWindow();
				forcedFocus = System.currentTimeMillis();
			}
		});

		eyeIcon = new JLabel();
		try {
			eyeIcon.setIcon(Utils.convertImageColors(Constants.RESOURCES_DIRECTORY + "/components/icons/eye-icon.png", ResponsiveSwingMaterialDesign.SECONDARY_COLOR));
		} catch (Exception e) {
			e.printStackTrace();
		}

		JTextField passwordSeeingField = new JTextField();
		passwordSeeingField.setOpaque(false);
		passwordSeeingField.setBorder(null);
		passwordSeeingField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				textField.setText(passwordSeeingField.getText());
			}
		});
		rl.add(passwordSeeingField);

		eyeIcon.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			public void mouseClicked(MouseEvent e) {

				updateDefault();

				if (passwordSeeingField.getText().trim().isEmpty()) {
					updateInFocus();

					passwordSeeingField.setVisible(true);
					passwordSeeingField.setBounds(textField.getX() + leftMargin, textField.getY(), textField.getWidth() - (leftMargin * 2) - 80, textField.getHeight());
					passwordSeeingField.setText(textField.getText());
					passwordSeeingField.setFont(textField.getFont());
					passwordSeeingField.setForeground(textField.getForeground());

					textField.setForeground(transparent);
					eyeIcon.requestDefaultFocus();
					eyeIcon.requestFocus();
				} else {
					textField.requestFocusInWindow();
					passwordSeeingField.requestFocusInWindow();
				}
			};
		});
		passwordSeeingField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				passwordSeeingField.setVisible(false);
				passwordSeeingField.setText("");

			}
		});
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				passwordSeeingField.setVisible(false);
				passwordSeeingField.setText("");
			}
		});

		rl.add(eyeIcon);
	}

	public PasswordField setCaption(String caption) {
		this.caption = caption;
		return this;
	}

	public String getCaption() {
		return caption;
	}

	@Description("Set the field as required, validating empty answers.")
	public PasswordField setRequired(boolean required) {
		this.required = required;
		return this;
	}

	@Description("Get all intern components from InputField.")
	public JComponent[] getComponents() {
		return new JComponent[] { textField, label, errorLabel };
	}

	@Description("Set callback rules to InputField.")
	public void setRules(Callback callback) {
		this.rules = callback;
	}

	@Description("Get the password")
	@SuppressWarnings("deprecation")
	public String getPassword() {
		return textField.getText();
	}

	@Description("Set the password.")
	public void setPassword(String text) {
		textField.setText(text);
	}

	@Description("Get the component propertly")
	public JPasswordField getComponent() {
		if (caption == null) {
			caption = "";
		}

		label = new JLabel(caption);
		errorLabel = new JLabel();

		panel.add(label);

		new Thread(() -> {
			build();
		}).start();

		return textField;
	}

	/* Validations */

	public PasswordField validatePassword(String regex) {
		if (caption == null)
			setCaption("Senha");

		setRules(new Callback() {
			@Override
			public String done() {

				@SuppressWarnings("deprecation")
				String value = textField.getText();

				if (value.isEmpty())
					return "Digite uma senha!";

				if (!value.matches(regex))
					return "Informe uma senha válida!";

				textField.setText(value);

				return null;
			}
		});

		return this;
	}
	
	/* Utilitary */

	private void updateColors() {
		if ((iconColor == null || !iconColor.equals(ResponsiveSwingMaterialDesign.SECONDARY_COLOR)) && errorLabel != null && !hasUpdatedColors) {
			hasUpdatedColors = true;
			errorLabel.setForeground(ResponsiveSwingMaterialDesign.ERROR_COLOR);
			label.setBackground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);
			responsiveUpdate();

			Color color = getColor();

			textField.setForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
			label.setForeground(color);

			RoundedBorder border = new RoundedBorder(20, color);
			border.setDefaultInsets(new Insets(0, leftMargin, 0, leftMargin));
			border.setStroke(textField.hasFocus() ? 2 : 1);
			textField.setBorder(border);
			hasUpdatedColors = false;

			iconColor = ResponsiveSwingMaterialDesign.SECONDARY_COLOR;
		}
	}

	private Color getColor() {
		Color dynamicColor = textField.hasFocus() ? ResponsiveSwingMaterialDesign.PRIMARY_COLOR : ResponsiveSwingMaterialDesign.SECONDARY_COLOR;

		final Color color = error != null && canValidate() ? ResponsiveSwingMaterialDesign.ERROR_COLOR : dynamicColor;

		new Thread(() -> {
			try {
				Color c2 = color.equals(ResponsiveSwingMaterialDesign.PRIMARY_COLOR) ? ResponsiveSwingMaterialDesign.SECONDARY_COLOR : color;
				eyeIcon.setIcon(Utils.convertImageColors(Constants.RESOURCES_DIRECTORY + "/components/icons/eye-icon.png", c2));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return color;
	}

	private void responsiveUpdate() {
		errorLabel.setBounds(textField.getX() + leftMargin, textField.getY() + (textField.getHeight() / 2) + 15, textField.getWidth(), textField.getHeight());
	}

	private void build() {

		textField.setOpaque(false);

		RoundedBorder border = new RoundedBorder(20, ResponsiveSwingMaterialDesign.PRIMARY_COLOR);
		border.setDefaultInsets(new Insets(0, leftMargin, 0, (leftMargin * 2) + 80));
		border.setStroke(2);
		textField.setBorder(border);

		textField.setForeground(ResponsiveSwingMaterialDesign.PRIMARY_COLOR);
		textField.setFont(Utils.getMainFont(30));

		label.setForeground(ResponsiveSwingMaterialDesign.PRIMARY_COLOR);
		label.setBackground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);
		label.setFont(labelFont);
		label.setBorder(null);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setOpaque(true);
		label.setBounds(textField.getBounds());

		eyeIcon.setBounds((textField.getWidth() - 64) + textField.getX(), textField.getY() + (textField.getHeight() / 2) - 16, 32, 32);
		errorLabel.setFont(Utils.getMainFont(15));
		errorLabel.setForeground(ResponsiveSwingMaterialDesign.ERROR_COLOR);
		errorLabel.setBounds(textField.getX() + leftMargin, textField.getY() + (textField.getHeight() / 2) + 15, textField.getWidth(), textField.getHeight());
		errorLabel.setBackground(Color.BLACK);
		errorLabel.setVisible(false);

		textField.requestFocus();
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				update();
			}
		});

		caretColor = textField.getCaretColor();

		textField.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				update();
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});

		update();

		panel.add(errorLabel);

		textField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				update();
			}
		});

		if (required) {
			setRules(new Callback() {
				@SuppressWarnings("deprecation")
				@Override
				public String done() {
					if (textField.getText().isEmpty() && required)
						return "Digite uma senha.";

					return null;
				}
			});
		}
	}

	@SuppressWarnings("deprecation")
	private void update() {
		updateDefault();

		if (textField.hasFocus() || !textField.getText().isEmpty()) {
			updateInFocus();

		} else {

			if (maskData != null) {
				maskData.uninstall();
				textField.setText("");
			}
			label.setBounds(textField.getX() + leftMargin, textField.getY(), textField.getWidth() - (leftMargin * 2), textField.getHeight());
			label.setFont(labelFont);
			label.setVerticalAlignment(SwingConstants.CENTER);
			label.setHorizontalAlignment(SwingConstants.LEFT);
			label.setOpaque(false);

			eyeIcon.setBounds((textField.getWidth() - 64) + textField.getX(), textField.getY() + (textField.getHeight() / 2) - 16, 32, 32);

			textField.setCaretColor(panel.getBackground());
		}
	}

	private void updateDefault() {
		eyeIcon.setVisible(textField.getWidth() > 160);

		label.setFont(labelFont);

		if (rules != null && !firstInstance) {
			error = rules.done();

			if (error != null && canValidate()) {
				errorLabel.setText(error);
				errorLabel.setVisible(true);
			} else {
				errorLabel.setVisible(false);
			}
		}

		firstInstance = false;

		Color color = getColor();

		textField.setForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
		label.setForeground(color);

		RoundedBorder border = new RoundedBorder(20, color);
		border.setDefaultInsets(new Insets(0, leftMargin, 0, (leftMargin * 2) + 80));
		border.setStroke(textField.hasFocus() ? 2 : 1);
		textField.setBorder(border);
	}

	private void updateInFocus() {

		Font shurenkenFont = getShurenkenFont();

		double labelW = label.getFontMetrics(shurenkenFont).stringWidth(label.getText()), labelH = label.getFontMetrics(shurenkenFont).getMaxAscent();

		int lastX = textField.getX(), lastY = textField.getY();
		double nextX = textField.getX() + leftMargin, nextY = textField.getY() - (labelH / 2);

		if (textField.getY() == label.getY()) {
			if (canValidate()) {
				label.setBounds(textField.getX() + leftMargin, textField.getY() + ((label.getHeight() - (int) labelH) / 2), (int) labelW + ((int) labelW / 5), (int) labelH);

				eyeIcon.setBounds((textField.getWidth() - 64) + textField.getX(), textField.getY() + (textField.getHeight() / 2) - 16, 32, 32);

				AnimateComponent ac = new AnimateComponent();

				ArrayList<Animation> animations = new ArrayList<Animation>();

				Animation animation = new Animation(label, new Point(lastX, lastY), new Point((int) nextX, (int) nextY));

				animations.add(animation);

				ac.execute(1, animations);
			}

			label.setFont(labelFont);
		} else {
			label.setBounds(textField.getX() + leftMargin, label.getY(), (int) labelW + ((int) labelW / 5), (int) labelH);

			eyeIcon.setBounds((textField.getWidth() - 64) + textField.getX(), textField.getY() + (textField.getHeight() / 2) - 16, 32, 32);

			label.setFont(shurenkenFont);
		}

		label.setOpaque(true);
		label.setHorizontalAlignment(SwingConstants.CENTER);

		textField.setCaretColor(caretColor);
	}

	private boolean canValidate() {
		return System.currentTimeMillis() - forcedFocus > 500;
	}

	private Font getShurenkenFont() {
		return Utils.getMainFont(label.getFont().getSize() - (label.getFont().getSize() / 3));
	}

}
