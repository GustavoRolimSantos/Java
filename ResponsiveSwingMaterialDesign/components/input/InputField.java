package br.com.maxtercreations.responsiveswing.components.input;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import com.sun.org.glassfish.gmbal.Description;

import br.com.maxtercreations.responsiveswing.ResponsiveSwingMaterialDesign;
import br.com.maxtercreations.responsiveswing.animation.AnimateComponent;
import br.com.maxtercreations.responsiveswing.animation.Animation;
import br.com.maxtercreations.responsiveswing.callback.Callback;
import br.com.maxtercreations.responsiveswing.components.utils.RoundedBorder;
import br.com.maxtercreations.responsiveswing.layouts.ResponsiveLayout;
import br.com.maxtercreations.responsiveswing.utils.Utils;

public class InputField {
	
	private ResponsiveLayout panel;
	private JLabel label, errorLabel;
	private Font labelFont = Utils.getMainFont(22);
	private Color caretColor, lastBorderColor;

	private String caption, error;

	private Callback rules;

	private MaskFormatter maskData;
	private JFormattedTextField textField;

	private long forcedFocus = System.currentTimeMillis();

	private final int leftMargin = 25;
	
	private boolean firstInstance = true, hasUpdatedColors = false, required = false;
	
	private NumberFormatter formatter;

	public InputField setCaption(String caption) {
		this.caption = caption;
		return this;
	}

	public String getCaption() {
		return caption;
	}

	@Description("Set the field as required, validating empty answers.")
	public InputField setRequired(boolean required) {
		this.required = required;
		return this;
	}
	

	@Description("Get unmasked value (It removes all symbols and non-alphanumeric digits)")
	public String getUnmaskedValue() {
		String value = Utils.removeAccents(textField.getText().replaceAll("[\\s()-]", "").trim());

		return value;
	}

	@Description("Get InputField text")
	public String getText() {
		return textField.getText();
	}

	@Description("Set a text to InputField")
	public void setText(String text) {
		textField.setText(text);
	}

	@SuppressWarnings("serial")
	public InputField(String caption, ResponsiveLayout rl) {
		this.panel = rl;
		this.caption = caption;

		textField = new JFormattedTextField() {
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
	}

	@Description("Get the component propertly")
	public JFormattedTextField getComponent() {

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

	@Description("Get all intern components from InputField.")
	public JComponent[] getComponents() {
		return new JComponent[] { textField, label, errorLabel };
	}

	@Description("Set callback rules to InputField.")
	public void setRules(Callback callback) {
		this.rules = callback;
	}

	@Description("Apply regex mask on Input Field")
	public void setMask(String mask) {
		try {
			maskData = new MaskFormatter(mask);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/* Validations */

	@Description("Apply e-mail validation")
	public InputField email() {
		if (caption == null)
			setCaption("E-mail");

		setRules(new Callback() {
			@Override
			public String done() {

				String value = Utils.removeAccents(textField.getText());

				if (value.isEmpty() && required)
					return "Digite um e-mail";

				if (!value.isEmpty() && !value.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"))
					return "Informe um e-mail válido!";

				textField.setText(value);

				return null;
			}
		});

		return this;
	}

	@Description("Apply phone validation")
	public InputField phone() {
		if (caption == null)
			setCaption("Telefone");

		setMask("(##) #####-####");
		setRules(null);

		if (required) {
			setRules(new Callback() {
				@Override
				public String done() {

					String value = getUnmaskedValue().replaceAll("\\d+", "").trim();

					if (value.isEmpty() && required)
						return "Digite um telefone";

					return null;
				}
			});
		}

		return this;
	}

	@Description("Apply money validation")
	public InputField money() {
		if (caption == null)
			setCaption("Valor");

		maskData = null;
		setRules(null);

		DecimalFormat dFormat = new DecimalFormat("#,###,###.00");
		dFormat.setMinimumIntegerDigits(1);
		formatter = new NumberFormatter(dFormat);
		formatter.setFormat(dFormat);
		formatter.setAllowsInvalid(false);

		textField.setFormatterFactory(new DefaultFormatterFactory(formatter));

		if (required) {
			setRules(new Callback() {
				@Override
				public String done() {

					String value = textField.getText();

					if (value.isEmpty() && required)
						return "Digite um valor";
					return null;
				}
			});
		}

		return this;
	}
	
	/* Utilitary */
	
	private void updateColors() {
		if (textField == null)
			return;

		if (errorLabel != null && !hasUpdatedColors) {
			errorLabel.setForeground(ResponsiveSwingMaterialDesign.ERROR_COLOR);
			label.setBackground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);
		}

		Color dynamicColor = ResponsiveSwingMaterialDesign.SECONDARY_COLOR;

		Color color = error != null && canValidate() ? ResponsiveSwingMaterialDesign.ERROR_COLOR : dynamicColor;

		if (errorLabel != null && !hasUpdatedColors && (lastBorderColor == null || !lastBorderColor.equals(color))) {
			hasUpdatedColors = true;

			responsiveUpdate();

			textField.setForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
			label.setForeground(color);

			RoundedBorder border = new RoundedBorder(20, color);
			border.setDefaultInsets(new Insets(0, leftMargin, 0, leftMargin));
			border.setStroke(textField.hasFocus() ? 2 : 1);
			textField.setBorder(border);
			hasUpdatedColors = false;

			lastBorderColor = color;
		}
	}
	

	private void responsiveUpdate() {
		errorLabel.setBounds(textField.getX() + leftMargin, textField.getY() + (textField.getHeight() / 2) + 15, textField.getWidth(), textField.getHeight());
	}

	private void build() {
		textField.setOpaque(false);

		RoundedBorder border = new RoundedBorder(20, ResponsiveSwingMaterialDesign.PRIMARY_COLOR);
		border.setDefaultInsets(new Insets(0, leftMargin, 0, leftMargin));
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
	}

	private boolean canValidate() {
		return System.currentTimeMillis() - forcedFocus > 500;
	}

	private Font getShurenkenFont() {
		return Utils.getMainFont(label.getFont().getSize() - (label.getFont().getSize() / 3));
	}
	
	private void update() {
		if (getUnmaskedValue().equalsIgnoreCase(",00"))
			textField.setText("0,00");

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

		Color color = textField.hasFocus() ? ResponsiveSwingMaterialDesign.PRIMARY_COLOR : ResponsiveSwingMaterialDesign.SECONDARY_COLOR;

		if (error != null && canValidate())
			color = ResponsiveSwingMaterialDesign.ERROR_COLOR;

		textField.setForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
		label.setForeground(color);

		RoundedBorder border = new RoundedBorder(20, color);
		border.setDefaultInsets(new Insets(0, leftMargin, 0, leftMargin));
		border.setStroke(textField.hasFocus() ? 2 : 1);
		textField.setBorder(border);

		if (textField.hasFocus() || !getUnmaskedValue().isEmpty()) {

			Font shurenkenFont = getShurenkenFont();

			double labelW = label.getFontMetrics(shurenkenFont).stringWidth(label.getText()), labelH = label.getFontMetrics(shurenkenFont).getMaxAscent();

			int lastX = textField.getX(), lastY = textField.getY();
			double nextX = textField.getX() + leftMargin, nextY = textField.getY() - (labelH / 2);

			if (textField.getY() == label.getY()) {
				if (canValidate()) {
					label.setBounds(textField.getX() + leftMargin, textField.getY() + ((label.getHeight() - (int) labelH) / 2), (int) labelW + ((int) labelW / 5), (int) labelH);

					AnimateComponent ac = new AnimateComponent();

					ArrayList<Animation> animations = new ArrayList<Animation>();

					Animation animation = new Animation(label, new Point(lastX, lastY), new Point((int) nextX, (int) nextY));

					animation.setCallback(new Callback() {
						@Override
						public String done() {
							if (maskData != null) {
								maskData.install(textField);
							}
							textField.setFormatterFactory(new DefaultFormatterFactory(formatter));

							return null;
						}
					});

					animations.add(animation);

					ac.execute(1, animations);
				}

				label.setFont(labelFont);
			} else {
				label.setBounds(textField.getX() + leftMargin, label.getY(), (int) labelW + ((int) labelW / 5), (int) labelH);
				label.setFont(shurenkenFont);
			}

			label.setOpaque(true);
			label.setHorizontalAlignment(SwingConstants.CENTER);

			textField.setCaretColor(caretColor);
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

			textField.setCaretColor(panel.getBackground());
		}
	}
	

}
