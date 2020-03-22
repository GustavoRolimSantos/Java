package br.com.maxtercreations.responsiveswing.components.input;

import java.awt.Color;
import java.awt.Dimension;
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
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.sun.org.glassfish.gmbal.Description;

import br.com.maxtercreations.responsiveswing.ResponsiveSwingMaterialDesign;
import br.com.maxtercreations.responsiveswing.animation.AnimateComponent;
import br.com.maxtercreations.responsiveswing.animation.Animation;
import br.com.maxtercreations.responsiveswing.callback.Callback;
import br.com.maxtercreations.responsiveswing.components.utils.RoundedBorder;
import br.com.maxtercreations.responsiveswing.layouts.ResponsiveLayout;
import br.com.maxtercreations.responsiveswing.utils.Utils;

public class TextArea {

	private JLabel label, errorLabel;
	private Font labelFont = Utils.getMainFont(25);
	private ResponsiveLayout panel;
	private Color caretColor, lastBorderColor;

	private String caption;

	private Callback rules;

	private String error;

	private boolean firstInstance = true, hasUpdatedColors = false;
	private long forcedFocus = System.currentTimeMillis();
	private final int leftMargin = 25;

	private JTextArea textArea;
	private JScrollPane scrollPane;

	@SuppressWarnings("serial")
	public TextArea(String caption, ResponsiveLayout rl) {
		this.panel = rl;
		this.caption = caption;

		textArea = new JTextArea();
		textArea.setOpaque(false);

		textArea.addComponentListener(new ComponentAdapter() {
			@SuppressWarnings("deprecation")
			public void componentResized(ComponentEvent componentEvent) {
				textArea.requestFocusInWindow();
				label.requestFocusInWindow();
				scrollPane.requestFocusInWindow();
				rl.requestDefaultFocus();
				forcedFocus = System.currentTimeMillis();
			}
		});

		scrollPane = new JScrollPane() {
			@Override
			public void repaint() {
				super.repaint();
				updateColors();
			}
		};
	}

	public TextArea setCaption(String caption) {
		this.caption = caption;
		return this;
	}

	public String getCaption() {
		return caption;
	}

	@Description("Get the component propertly")
	public JScrollPane getComponent() {
		if (caption == null)
			caption = "";

		label = new JLabel(caption);
		errorLabel = new JLabel();

		panel.add(label);

		new Thread(() -> {
			build();
		}).start();

		scrollPane.setViewportView(textArea);

		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		scrollPane.setOpaque(false);

		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getViewport().setOpaque(false);

		return scrollPane;
	}

	@Description("Get all intern components from InputField.")
	public JComponent[] getComponents() {
		return new JComponent[] { textArea, label, errorLabel };
	}

	@Description("Set callback rules to InputField.")
	public void setRules(Callback callback) {
		this.rules = callback;
	}
	
	@Description("Get InputField text")
	public String getText() {
		return textArea.getText();
	}

	@Description("Set a text to InputField")
	public void setText(String text) {
		textArea.setText(text);
	}
	
	/* Utilitary */

	private void updateColors() {

		if (scrollPane == null)
			return;

		Color color = scrollPane.hasFocus() ? ResponsiveSwingMaterialDesign.PRIMARY_COLOR : ResponsiveSwingMaterialDesign.SECONDARY_COLOR;

		if (errorLabel != null && !hasUpdatedColors && (lastBorderColor == null || !lastBorderColor.equals(color))) {
			hasUpdatedColors = true;
			errorLabel.setForeground(ResponsiveSwingMaterialDesign.ERROR_COLOR);
			label.setBackground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);

			responsiveUpdate();

			if (error != null && canValidate())
				color = ResponsiveSwingMaterialDesign.ERROR_COLOR;

			textArea.setForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
			label.setForeground(color);

			RoundedBorder border = new RoundedBorder(20, color);
			border.setDefaultInsets(new Insets(0, leftMargin, 0, leftMargin));
			border.setStroke(textArea.hasFocus() ? 2 : 1);
			scrollPane.setBorder(border);
			hasUpdatedColors = false;

			lastBorderColor = color;
		}
	}

	private void responsiveUpdate() {
		errorLabel.setBounds(scrollPane.getX() + leftMargin, scrollPane.getY() + (scrollPane.getHeight() / 2) + 15, scrollPane.getWidth(), scrollPane.getHeight());
	}

	private void build() {

		textArea.setLineWrap(true);
		textArea.setOpaque(false);

		RoundedBorder border = new RoundedBorder(20, ResponsiveSwingMaterialDesign.PRIMARY_COLOR);
		border.setDefaultInsets(new Insets(leftMargin, leftMargin, leftMargin + 5, leftMargin));
		border.setStroke(2);
		scrollPane.setBorder(border);

		textArea.setForeground(ResponsiveSwingMaterialDesign.PRIMARY_COLOR);
		textArea.setFont(Utils.getMainFont(30));

		label.setForeground(ResponsiveSwingMaterialDesign.PRIMARY_COLOR);
		label.setBackground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);
		label.setFont(labelFont);
		label.setBorder(null);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setOpaque(true);
		label.setBounds(scrollPane.getBounds());

		errorLabel.setFont(Utils.getMainFont(15));
		errorLabel.setForeground(Color.RED);
		errorLabel.setBounds(scrollPane.getX() + leftMargin, scrollPane.getY() + (scrollPane.getHeight() / 2) + 15, scrollPane.getWidth(), scrollPane.getHeight());
		errorLabel.setBackground(Color.BLACK);
		errorLabel.setVisible(false);

		textArea.requestFocus();
		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				update();
			}
		});

		caretColor = textArea.getCaretColor();

		textArea.addFocusListener(new FocusListener() {

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

		textArea.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				update();
			}
		});
	}
	
	private void update() {

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

		Color color = textArea.hasFocus() ? ResponsiveSwingMaterialDesign.PRIMARY_COLOR : ResponsiveSwingMaterialDesign.SECONDARY_COLOR;

		if (error != null && canValidate())
			color = Color.RED;

		textArea.setForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
		label.setForeground(color);

		RoundedBorder border = new RoundedBorder(20, color);
		border.setDefaultInsets(new Insets(leftMargin, leftMargin, leftMargin + 5, leftMargin));
		border.setStroke(textArea.hasFocus() ? 2 : 1);
		scrollPane.setBorder(border);

		if (textArea.hasFocus() || !textArea.getText().isEmpty()) {

			Font shurenkenFont = getShurenkenFont();

			double labelW = label.getFontMetrics(shurenkenFont).stringWidth(label.getText()), labelH = label.getFontMetrics(shurenkenFont).getMaxAscent();

			int lastX = scrollPane.getX(), lastY = scrollPane.getY();
			double nextX = scrollPane.getX() + leftMargin, nextY = scrollPane.getY() - (labelH / 2);

			if (scrollPane.getY() == label.getY()) {
				if (canValidate()) {
					label.setBounds(scrollPane.getX() + leftMargin, scrollPane.getY() + ((label.getHeight() - (int) labelH) / 2), (int) labelW + ((int) labelW / 5), (int) labelH);

					AnimateComponent ac = new AnimateComponent();

					ArrayList<Animation> animations = new ArrayList<Animation>();

					Animation animation = new Animation(label, new Point(lastX, lastY), new Point((int) nextX, (int) nextY));

					animation.setCallback(new Callback() {
						@Override
						public String done() {

							return null;
						}
					});

					animations.add(animation);

					ac.execute(1, animations);
				}

				label.setFont(labelFont);
			} else {
				label.setBounds(scrollPane.getX() + leftMargin, label.getY(), (int) labelW + ((int) labelW / 5), (int) labelH);
				label.setFont(shurenkenFont);
			}

			label.setOpaque(true);
			label.setHorizontalAlignment(SwingConstants.CENTER);

			textArea.setCaretColor(caretColor);
		} else {
			label.setBounds(scrollPane.getX() + leftMargin, scrollPane.getY(), scrollPane.getWidth() - (leftMargin * 2), scrollPane.getHeight());
			label.setFont(labelFont);
			label.setVerticalAlignment(SwingConstants.CENTER);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setOpaque(false);

			textArea.setCaretColor(panel.getBackground());
		}
	}

	private boolean canValidate() {
		return System.currentTimeMillis() - forcedFocus > 500;
	}

	private Font getShurenkenFont() {
		return Utils.getMainFont(label.getFont().getSize() - (label.getFont().getSize() / 3));
	}

}
