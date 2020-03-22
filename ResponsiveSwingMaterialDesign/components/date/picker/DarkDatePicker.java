package br.com.maxtercreations.responsiveswing.components.date.picker;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.maxtercreations.responsiveswing.ResponsiveSwingMaterialDesign;
import br.com.maxtercreations.responsiveswing.callback.CallbackT;
import br.com.maxtercreations.responsiveswing.components.buttons.RoundedFillButton;
import br.com.maxtercreations.responsiveswing.components.date.DatePicker;
import br.com.maxtercreations.responsiveswing.components.utils.RoundedBorder;
import br.com.maxtercreations.responsiveswing.components.utils.RoundedPane;
import br.com.maxtercreations.responsiveswing.constants.Constants;
import br.com.maxtercreations.responsiveswing.utils.Utils;

public class DarkDatePicker extends DatePicker {

	public DarkDatePicker(Date currentDate, CallbackT callbackT) {
		super(currentDate, callbackT);
		// TODO Auto-generated constructor stub
	}

	public DarkDatePicker(CallbackT callbackT) {
		super(callbackT);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void build() {
		frame = new JWindow();

		panel = new RoundedPane(new Dimension(480, 500), new RoundRectangle2D.Float(0, 0, 480, 500, 30, 30));

		JLabel yearLabel = new JLabel("<html><div style='text-align: center;'><strong>" + getYear() + "</strong><br>" + months[getMonth()] + "</div></html>");
		yearLabel.setHorizontalAlignment(SwingConstants.CENTER);
		yearLabel.setFont(Utils.getMainFont(30));
		yearLabel.setBounds(0, 30, 390, 80);
		yearLabel.setForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
		panel.add(yearLabel);

		panel.setBackground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);

		ArrayList<String> yearsList = new ArrayList<>();

		for (int i = 0; i < 14; i++) {
			yearsList.add(String.valueOf(getYear() - i));
		}

		for (int i = 0; i < 14; i++) {
			yearsList.add(String.valueOf(getYear() + i + 1));
		}

		Collections.sort(yearsList);
		Collections.reverse(yearsList);

		ArrayList<String> formmatedList = new ArrayList<>();

		for (int i = 0; i < yearsList.size(); i++) {
			formmatedList.add(yearsList.get(i));
		}

		JLabel bar1 = new JLabel("<html><div style='text-align: center;'><font color='#bcbcbc'>——————</font></div><html>");
		bar1.setFont(Utils.getMainFont(20));
		bar1.setBounds(420, 133, 30, 5);
		panel.add(bar1);

		JLabel bar2 = new JLabel("<html><div style='text-align: center;'><font color='#bcbcbc'>——————</font></div><html>");
		bar2.setFont(Utils.getMainFont(20));
		bar2.setBounds(420, 266, 30, 5);
		panel.add(bar2);

		RoundedFillButton ready = new RoundedFillButton("", null, ResponsiveSwingMaterialDesign.TEXT_COLOR, ResponsiveSwingMaterialDesign.PRIMARY_COLOR, new Rectangle(398, 410, 80, 80), 80, 1, panel, true, new CallbackT() {
			@Override
			public void done(Object s) {
				frame.setVisible(false);
				callbackT.done(s.toString());
			}
		});

		try {
			ready.getTextLabel().setIcon(Utils.convertImageColors(Constants.RESOURCES_DIRECTORY + "/components/icons/check-icon.png", ResponsiveSwingMaterialDesign.TEXT_COLOR));
		} catch (Exception e) {
			e.printStackTrace();
		}

		JList<String> list = new JList<String>(formmatedList.toArray(new String[formmatedList.size()]));

		list.setFont(Utils.getBoldFont(22));
		list.setForeground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);
		list.setOpaque(false);
		list.setBorder(null);
		list.setBackground(new Color(243, 243, 243));
		list.setFixedCellHeight(133);

		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			@SuppressWarnings("deprecation")
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				if (Utils.isNumber(list.getSelectedValue())) {
					if (Integer.valueOf(list.getSelectedValue()) > getYear()) {
						list.ensureIndexIsVisible(list.getSelectedIndex() - 1);
					} else {
						list.ensureIndexIsVisible(list.getSelectedIndex() - 1);
					}
					
					currentDate.setYear(Integer.valueOf(list.getSelectedValue()) - 1900);

					displayDays();
					
					if (next != null && last != null) {
						next.setText(getNextMonthText());
						last.setText(getLastMonthText());
					}
					yearLabel.setText("<html><div style='text-align: center;'><strong>" + getYear() + "</strong><br>" + months[getMonth()] + "</div></html>");

					bar1.repaint();
					bar2.repaint();
				}
			}
		};
		list.addListSelectionListener(listSelectionListener);

		@SuppressWarnings("serial")
		class SelectedListCellRenderer extends DefaultListCellRenderer {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (isSelected) {

					if (Utils.isNumber(value.toString())) {
						c.setForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
						lastYear = value.toString();
					} else {
						list.setSelectedValue(lastYear.toString(), true);
						list.ensureIndexIsVisible(list.getSelectedIndex() - 2);
						c.setForeground(ResponsiveSwingMaterialDesign.SECONDARY_COLOR);
					}

					
				}
				
				c.setBackground(ResponsiveSwingMaterialDesign.PRIMARY_COLOR);

				JLabel label = ((JLabel) c);

				label.setHorizontalAlignment(SwingConstants.CENTER);

				return c;
			}
		}

		list.setCellRenderer(new SelectedListCellRenderer());

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(list);

		list.setLayoutOrientation(JList.VERTICAL);
		scrollPane.setBounds(400, 0, 70, 400);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		scrollPane.setBorder(null);
		scrollPane.setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);

		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane);

		JLabel yearsBar = new JLabel();
		yearsBar.setBorder(new RoundedBorder(30, ResponsiveSwingMaterialDesign.PRIMARY_COLOR, true));
		yearsBar.setBounds(390, 0, 91, 505);
		panel.add(yearsBar);

		list.setSelectedValue(String.valueOf(getYear()), true);
		list.ensureIndexIsVisible(list.getSelectedIndex() - 2);

		for (int i = 0; i < days.length; i++) {
			JLabel dayLabel = new JLabel(days[i]);
			dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
			dayLabel.setFont(Utils.getMainFont(18));
			dayLabel.setForeground(ResponsiveSwingMaterialDesign.SECONDARY_COLOR);
			dayLabel.setBounds(i * 55, 140, 70, 30);
			panel.add(dayLabel);
		}

		displayDays();

		last = new JLabel(getLastMonthText());
		last.setForeground(ResponsiveSwingMaterialDesign.PRIMARY_COLOR);
		last.setFont(Utils.getBoldFont(5));
		last.setBounds(21, 63, 155, 50);
		last.setHorizontalAlignment(SwingConstants.CENTER);
		last.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {

				if (getMonth() > 0) {
					currentDate.setMonth(currentDate.getMonth() - 1);
				} else {
					currentDate.setYear(currentDate.getYear() - 1);
					currentDate.setMonth(11);
				}

				displayDays();

				yearLabel.setText("<html><div style='text-align: center;'><strong>" + getYear() + "</strong><br>" + months[getMonth()] + "</div></html>");
				last.setText(getLastMonthText());
			}
		});
		panel.add(last);

		next = new JLabel(getNextMonthText());
		next.setForeground(ResponsiveSwingMaterialDesign.PRIMARY_COLOR);
		next.setFont(Utils.getBoldFont(5));
		next.setBounds(210, 63, 165, 50);
		next.setHorizontalAlignment(SwingConstants.CENTER);
		next.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {

				if (getMonth() < 11) {
					currentDate.setMonth(currentDate.getMonth() + 1);
				} else {
					currentDate.setYear(currentDate.getYear() + 1);
					currentDate.setMonth(0);
				}

				displayDays();
				next.setText(getNextMonthText());
				yearLabel.setText("<html><div style='text-align: center;'><strong>" + getYear() + "</strong><br>" + months[getMonth()] + "</div></html>");
			}
		});
		panel.add(next);

		frame.setBackground(new Color(0, 0, 0, 0));
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void displayDays() {

		for (RoundedFillButton button : buttons) {
			button.dispose();
		}

		buttons.clear();

		CallbackT callbackT = new CallbackT() {
			@Override
			public void done(Object s) {
				if (s.toString().trim().isEmpty())
					return;
				
				currentDate.setDate(Integer.valueOf(s.toString()));
				
				RoundedFillButton next = null, last = null;

				for (RoundedFillButton roundedButton : buttons) {
					if (roundedButton.getTextLabel().getForeground().equals(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR)) {
						last = roundedButton;
					}
					if (roundedButton.getTextLabel().getText().equalsIgnoreCase(s.toString())) {
						next = roundedButton;
					}
				}

				next.getBackgroundButton().recolor(ResponsiveSwingMaterialDesign.PRIMARY_COLOR, 50, true);
				next.getTextLabel().setForeground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);
				next.getTextLabel().setFont(Utils.getBoldFont(22));

				if (last != null) {
					last.getBackgroundButton().recolor(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR, 50, true);
					last.getTextLabel().setForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
					last.getTextLabel().setFont(Utils.getMainFont(22));
				}

			}
		};

		Calendar cal = getCalendar();

		int currentDay = cal.get(Calendar.DAY_OF_MONTH);

		cal.set(Calendar.DAY_OF_MONTH, 1);
		int maxDayNo = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int dayNo = 2 - cal.get(Calendar.DAY_OF_WEEK);

		int dayCount = 0, x = 10, y = 175;

		for (int l = 0; l < 6; l++) {
			for (int c = 0; c < 7; c++) {

				if (dayCount % 7 == 0 && dayCount != 0) {
					x = 10;
					y += 50;
				}

				String s = "";
				if (dayNo >= 1 && dayNo <= maxDayNo) {
					s = String.valueOf(dayNo);
				}
				dayNo++;

				RoundedFillButton button = null;

				if (s.equalsIgnoreCase(String.valueOf(currentDay))) {
					button = new RoundedFillButton(s, Utils.getBoldFont(22), ResponsiveSwingMaterialDesign.BACKGROUND_COLOR, ResponsiveSwingMaterialDesign.PRIMARY_COLOR, new Rectangle(x, y, 50, 50), 50, 1, panel, true, callbackT);
				} else {
					button = new RoundedFillButton(s, Utils.getMainFont(22), ResponsiveSwingMaterialDesign.TEXT_COLOR, ResponsiveSwingMaterialDesign.BACKGROUND_COLOR, new Rectangle(x, y, 50, 50), 50, 1, panel, true, callbackT);
				}

				button.ajustText(-3);
				buttons.add(button);

				x += 55;
				dayCount++;
			}
		}
	}

}
