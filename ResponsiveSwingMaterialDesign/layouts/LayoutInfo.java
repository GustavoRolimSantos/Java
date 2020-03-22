package br.com.maxtercreations.responsiveswing.layouts;

public class LayoutInfo {

	private double columns, rows, skipColumns;

	public LayoutInfo(double columns, double rows, double skipColumns) {
		this.columns = columns;
		this.rows = rows;
		this.skipColumns = skipColumns;
	}

	public double getColumns() {
		return columns;
	}

	public double getRows() {
		return rows;
	}

	public double getSkipColumns() {
		return skipColumns;
	}
	
}
