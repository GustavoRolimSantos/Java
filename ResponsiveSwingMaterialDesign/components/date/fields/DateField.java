package br.com.maxtercreations.responsiveswing.components.date.fields;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import com.sun.org.glassfish.gmbal.Description;

import br.com.maxtercreations.responsiveswing.ResponsiveSwingMaterialDesign;
import br.com.maxtercreations.responsiveswing.animation.AnimateComponent;
import br.com.maxtercreations.responsiveswing.animation.Animation;
import br.com.maxtercreations.responsiveswing.callback.Callback;
import br.com.maxtercreations.responsiveswing.callback.CallbackT;
import br.com.maxtercreations.responsiveswing.components.date.DatePicker;
import br.com.maxtercreations.responsiveswing.components.date.picker.DarkDatePicker;
import br.com.maxtercreations.responsiveswing.components.date.picker.LightDatePicker;
import br.com.maxtercreations.responsiveswing.components.utils.RoundedBorder;
import br.com.maxtercreations.responsiveswing.constants.Constants;
import br.com.maxtercreations.responsiveswing.layouts.ResponsiveLayout;
import br.com.maxtercreations.responsiveswing.utils.Utils;

public class DateField {

	private JLabel label, errorLabel, calendarIcon;
	private Font labelFont = Utils.getMainFont(22);
	private ResponsiveLayout panel;
	private Color caretColor, iconColor;

	private String caption, error;

	private Callback rules;

	private boolean firstInstance = true, required = false, hasUpdatedColors = false;
	private long forcedFocus = System.currentTimeMillis();
	private final int leftMargin = 25;
	
	private MaskFormatter maskData;
	private JFormattedTextField textField;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	private DatePicker datePicker;
	
	@SuppressWarnings("serial")
	public DateField(String caption, ResponsiveLayout responsiveLayout) {
		this.panel = responsiveLayout;
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

		calendarIcon = new JLabel();
		try {
			calendarIcon.setIcon(Utils.convertImageColors(Constants.RESOURCES_DIRECTORY + "/components/icons/calendar-icon.png", ResponsiveSwingMaterialDesign.SECONDARY_COLOR));
		} catch (Exception e) {
			e.printStackTrace();
		}
		calendarIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				responsiveLayout.getBlackFilter().setVisible();

				Date date = null;

				try {
					date = dateFormat.parse(textField.getText());
				} catch (ParseException e1) {
					date = new Date();
				}

				if (!ResponsiveSwingMaterialDesign.getThemesManager().getCurrentModel().getName().equalsIgnoreCase("Light")) {
					datePicker = new DarkDatePicker(date, new CallbackT() {
						@Override
						public void done(Object s) {
							focusUpdate(false);
							responsiveLayout.getBlackFilter().dispose();
							textField.setValue(dateFormat.format(datePicker.getValue()));
							maskData.install(textField);
						}
					});
				} else {
					datePicker = new LightDatePicker(date, new CallbackT() {
						@Override
						public void done(Object s) {
							focusUpdate(false);
							responsiveLayout.getBlackFilter().dispose();
							textField.setValue(dateFormat.format(datePicker.getValue()));
							maskData.install(textField);
						}
					});
				}
			}
		});
		responsiveLayout.add(calendarIcon);
		
		setMask("##/##/####");

		setRules(new Callback() {
			@Override
			public String done() {

				String value = textField.getText().replace("/", "").replace(" ", "").trim();

				if (value.isEmpty() && required)
					return "Informe uma Data";

				textField.setText(value);

				return null;
			}
		});
	}
	
	@Description("Set the field as required, validating empty answers.")
	public DateField setRequired(boolean required) {
		this.required = required;
		return this;
	}

	public DateField setCaption(String caption) {
		this.caption = caption;
		return this;
	}

	public String getCaption() {
		return caption;
	}
	
	@Description("Get formatted date.")
	public String getDateText() {
		return textField.getText();
	}
	
	@Description("Get selected as Date")
	public Date getDate() {
		try {
			return dateFormat.parse(textField.getValue().toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Description("Set current Date")
	public void setDate(Date date) {
		textField.setValue(dateFormat.format(date));
	}
	
	@Description("Set date format")
	public void setDateFormat(String format) {
		this.dateFormat = new SimpleDateFormat(format);
		this.setMask(format.toLowerCase().replaceAll("d", "#").replaceAll("m", "#").replaceAll("y", "#").replaceAll("h", "#").replaceAll("s", "#"));
	}

	@Description("Get the component propertly")
	public JFormattedTextField getComponent() {
		if (caption == null)
			caption = "";

		label = new JLabel(caption);
		errorLabel = new JLabel();

		panel.add(label);

		new Thread(() -> {
			build();
		}).start();

		return textField;
	}

	@Description("Get all intern components from DateField.")
	public JComponent[] getComponents() {
		return new JComponent[] { textField, label, errorLabel };
	}
	
	@Description("Set callback rules to DateField.")
	public void setRules(Callback callback) {
		this.rules = callback;
	}
	
	/* Utilitary */
	
	private String getUnmaskedValue() {
		String value = textField.getText() == null ? "" : textField.getText().replace("/", "").replace(" ", "").trim();

		return value;
	}
	
	private void setMask(String mask) {
		try {
			maskData = new MaskFormatter(mask);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void responsiveUpdate() {
		errorLabel.setBounds(textField.getX() + leftMargin, textField.getY() + (textField.getHeight() / 2) + 15, textField.getWidth(), textField.getHeight());
	}

	private void build() {

		textField.setOpaque(false);

		RoundedBorder border = new RoundedBorder(20, ResponsiveSwingMaterialDesign.PRIMARY_COLOR);
		border.setDefaultInsets(new Insets(0, leftMargin, 0, 0));
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

		calendarIcon.setBounds((textField.getWidth() - 64) + textField.getX(), textField.getY() + (textField.getHeight() / 2) - 16, 32, 32);

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
				
				if (!textField.getText().contains("/")) {
					maskData.install(textField);
				}
			}
		});

		if (caption == null)
			setCaption("Data");
	}

	private void update() {
		calendarIcon.setVisible(textField.getWidth() > 160);

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
		border.setDefaultInsets(new Insets(0, leftMargin, 0, 0));
		border.setStroke(textField.hasFocus() ? 2 : 1);
		textField.setBorder(border);

		if (textField.hasFocus() || !getUnmaskedValue().isEmpty()) {
			focusUpdate(true);

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

			calendarIcon.setBounds((textField.getWidth() - 64) + textField.getX(), textField.getY() + (textField.getHeight() / 2) - 16, 32, 32);
		}
	}

	private void focusUpdate(boolean shurenken) {
		Font shurenkenFont = getShurenkenFont(shurenken);

		double labelW = label.getFontMetrics(shurenkenFont).stringWidth(label.getText()), labelH = label.getFontMetrics(shurenkenFont).getMaxAscent();

		int lastX = textField.getX(), lastY = textField.getY();
		double nextX = textField.getX() + leftMargin, nextY = textField.getY() - (labelH / 2);

		if (textField.getY() == label.getY()) {
			if (canValidate()) {
				label.setBounds(textField.getX() + leftMargin, textField.getY() + ((label.getHeight() - (int) labelH) / 2), (int) labelW + ((int) labelW / 5), (int) labelH);

				calendarIcon.setBounds((textField.getWidth() - 64) + textField.getX(), textField.getY() + (textField.getHeight() / 2) - 16, 32, 32);

				AnimateComponent ac = new AnimateComponent();

				ArrayList<Animation> animations = new ArrayList<Animation>();

				Animation animation = new Animation(label, new Point(lastX, lastY), new Point((int) nextX, (int) nextY));

				animation.setCallback(new Callback() {
					@Override
					public String done() {

						if (maskData != null) {
							maskData.install(textField);
						}

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

			calendarIcon.setBounds((textField.getWidth() - 64) + textField.getX(), textField.getY() + (textField.getHeight() / 2) - 16, 32, 32);
			
			if (!textField.getText().contains("/")) {
				maskData.install(textField);
			}
		}

		label.setOpaque(true);
		label.setHorizontalAlignment(SwingConstants.CENTER);

		textField.setCaretColor(caretColor);

		calendarIcon.setBounds((textField.getWidth() - 64) + textField.getX(), textField.getY() + (textField.getHeight() / 2) - 16, 32, 32);
	}

	private boolean canValidate() {
		return System.currentTimeMillis() - forcedFocus > 500;
	}

	private Font getShurenkenFont(boolean shurenken) {
		return shurenken ? Utils.getMainFont(label.getFont().getSize() - (label.getFont().getSize() / 3)) : label.getFont();
	}
	
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
				calendarIcon.setIcon(Utils.convertImageColors(Constants.RESOURCES_DIRECTORY + "/components/icons/calendar-icon.png", color));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		
		return color;
	}


}
