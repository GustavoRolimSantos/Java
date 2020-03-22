package br.com.maxtercreations.responsiveswing.layouts;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;

import com.sun.org.glassfish.gmbal.Description;

import br.com.maxtercreations.responsiveswing.ResponsiveSwingMaterialDesign;
import br.com.maxtercreations.responsiveswing.components.utils.filters.BlackFilter;

@SuppressWarnings("serial")
public class ResponsiveLayout extends JLayeredPane {

	private final BlackFilter blackFilter = new BlackFilter();
	private final Insets heightInsets = new Insets(10, 10, 10, 10), margin = new Insets(50, 50, 50, 50);

	private JFrame frame;

	private JScrollPane scrollPane;

	private HashMap<JComponent, LayoutInfo> componentsInfo = new HashMap<>();

	private int columnWidth, rowHeight, x, y, columns, width, height, currentColumnn;

	private double maxHeightInRow = 0;

	private boolean addRow = false;

	public enum ResponsiveConstants {
		RIGHT, LEFT, CENTER
	}

	public ResponsiveLayout(int rows, int columns, JFrame frame) {
		this.frame = frame;

		this.columns = columns;
		this.width = frame.getWidth();
		this.height = frame.getHeight();

		setBounds(0, 0, width, height);
		setLayout(null);
		setOpaque(true);

		columnWidth = width / columns;
		rowHeight = height / rows;

		rowHeight -= heightInsets.top;

		x = margin.left / 2;
		y = margin.top;

		blackFilter.build(frame);

		build();
	}

	@Description("Add a new component into your Responsive Layout.")
	public void add(JComponent component, double columns, double rows, double skipColumns, ResponsiveConstants responsiveConstants) {
		
		if (addRow) {
			skipColumns = (ResponsiveLayout.this.columns - currentColumnn);
			currentColumnn = 0;
		}
		
		if (responsiveConstants != null) {
			if (responsiveConstants.equals(ResponsiveConstants.CENTER)) {
				double necessaryColumns = (ResponsiveLayout.this.columns / 2) - (columns / 2);

				skipColumns += necessaryColumns;

				currentColumnn += necessaryColumns;
			} else if (responsiveConstants.equals(ResponsiveConstants.RIGHT)) {
				double necessaryColumns = (ResponsiveLayout.this.columns / 2) - (columns / 2);

				skipColumns = necessaryColumns + (columns * 2);
			} else if (responsiveConstants.equals(ResponsiveConstants.LEFT)) {
				double necessaryColumns = (ResponsiveLayout.this.columns / 2) - (columns / 2);

				skipColumns = necessaryColumns - (columns * 2);
			}
		}
		
		LayoutInfo info = new LayoutInfo(columns, rows, skipColumns);

		displayComponent(component, info);

		componentsInfo.put(component, info);

		add(component);

		currentColumnn += columns;
		addRow = false;

		updateScroll();
	}

	@Description("Add a new component into your Responsive Layout. Use ResponsiveConstants as LEFT, CENTER or RIGHT")
	public void add(JComponent component, double columns, double rows, ResponsiveConstants responsiveConstants) {
		add(component, columns, rows, 0, responsiveConstants);
	}

	@Description("Add a new component into your Responsive Layout. Use skipColumns as the number of columns you want to skip.")
	public void add(JComponent component, double columns, double rows, double skipColumns) {
		add(component, columns, rows, skipColumns, null);
	}

	@Description("Add a new component into your Responsive Layout.")
	public void add(JComponent component, double columns, double rows) {
		add(component, columns, rows, 0);
	}

	@Description("Make the Responsive Layout screen update all components bounds and sizes.")
	public void update() {
		this.width = frame.getWidth();

		width = width + 7 - margin.left / 2;
		columnWidth = width / columns;

		x = margin.left / 2;
		y = margin.top;

		maxHeightInRow = 0;

		for (Component component : getComponents()) {
			if (componentsInfo.get(component) == null)
				continue;

			LayoutInfo info = componentsInfo.get(component);

			if (!component.getBackground().equals(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR)) {
				component.setBackground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);

			}

			displayComponent((JComponent) component, info);
		}

		scrollPane.getViewport().setBackground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);

	}

	// Display the component based on responsive info
	private void displayComponent(JComponent component, LayoutInfo layoutInfo) {
		skip(layoutInfo);
	
		double w = layoutInfo.getColumns() * columnWidth, h = layoutInfo.getRows() * rowHeight;

		w -= margin.left;
		h += heightInsets.top + heightInsets.bottom;

		if (x + (margin.left * 2) >= width || x + w > width) {

			y += (maxHeightInRow == 0 ? h : maxHeightInRow) + margin.bottom;

			x = margin.left / 2;
			currentColumnn = 0;
			maxHeightInRow = 0;
		}

		component.setBounds(x, y, (int) w, (int) h);

		x += w + margin.left;

		if (h > maxHeightInRow) {
			maxHeightInRow = h;
		}

	}

	// Skip rows and columns based on responsive info
	private void skip(LayoutInfo layoutInfo) {

		for (int i = 0; i < layoutInfo.getSkipColumns(); i++) {
			double w = columnWidth, h = layoutInfo.getRows() * rowHeight;

			h += heightInsets.top + heightInsets.bottom;

			x += w;
			
			if (x > width) {
				y += h + margin.bottom;
				x = x - width;
			}
		}
	}

	// Add the responsive layout to a scrollpane and add frame events to update
	// components
	private void build() {
		scrollPane = new JScrollPane(this);

		scrollPane.setOpaque(true);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getViewport().setOpaque(true);
		scrollPane.getViewport().setBackground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);

		frame.setContentPane(scrollPane);
		frame.getContentPane().setBackground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);

		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				update();
			}
		});

		frame.addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent arg0) {
				update();
			}
		});

		frame.repaint();
	}

	// Update scrollbar height based on most height component
	private void updateScroll() {
		int maxHeight = 0;

		for (Component component : this.getComponents()) {
			if (component.getY() + component.getHeight() > maxHeight)
				maxHeight = component.getY() + component.getHeight();
		}

		this.setPreferredSize(new Dimension(this.getWidth(), maxHeight));
	}

	@Description("Add a new row in your Responsive Layout.")
	public void addRow() {
		addRow = true;
	}

	@Description("Get the row height based on frame height and number of rows.")
	public int getRowHeight() {
		return rowHeight;
	}

	@Description("Get the column width based on frame width and number of columns.")
	public int getColumWidth() {
		return columnWidth;
	}

	@Description("Get the black filter to apply/dispose in your frame.")
	public BlackFilter getBlackFilter() {
		return blackFilter;
	}

	public JFrame getFrame() {
		return frame;
	}

}