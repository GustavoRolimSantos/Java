package br.com.maxtercreations.responsiveswing.components.date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import br.com.maxtercreations.responsiveswing.callback.CallbackT;
import br.com.maxtercreations.responsiveswing.components.buttons.RoundedFillButton;

public abstract class DatePicker {

	protected JWindow frame;
	protected Date currentDate;

	protected String[] months = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" };
	protected String[] days = { "Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb" };

	protected ArrayList<RoundedFillButton> buttons = new ArrayList<>();

	protected JPanel panel;

	protected CallbackT callbackT;

	public DatePicker(CallbackT callbackT) {
		this.currentDate = new Date();
		this.callbackT = callbackT;

		build();
	}

	public DatePicker(Date currentDate, CallbackT callbackT) {
		this.currentDate = currentDate;
		this.callbackT = callbackT;
		
		build();
	}

	@SuppressWarnings("deprecation")
	public int getYear() {
		return currentDate.getYear() + 1900;
	}

	@SuppressWarnings("deprecation")
	public int getDay() {
		return currentDate.getDay();
	}

	@SuppressWarnings("deprecation")
	public int getMonth() {
		return currentDate.getMonth();
	}

	public int getDayOfWeek() {
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		return dayOfWeek;
	}

	public Date getValue() {
		return currentDate;
	}

	public Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		return calendar;
	}

	public JLabel next, last;

	public String lastYear = new String();

	public abstract void build();

	public String getLastMonthText() {
		return "<html> <div style='text-align: center;'> <font size='5'>←</font></div></html>";
	}

	public String getNextMonthText() {
		return "<html> <div style='text-align: center;'><font size='5'>→</font></div></html>";
	}
	
	public abstract void displayDays();

}
