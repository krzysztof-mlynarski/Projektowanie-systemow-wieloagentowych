package lab2;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MatrixFragment implements Serializable
{
	private int rowIndex;
	private int colIndex;
	private double[] row;
	private double[] col;
	
	private double result;
	private int state; //0 - queue 1 - sent 2 - calculated
		
	public MatrixFragment(int rowIndex, int colIndex, double[] row, double[] col) {
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
		this.row = row;
		this.col = col;
		this.state = 0;
	}
	
	public int getSize() {
		if (row.length == col.length)
			return row.length;
		
		return 0;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	public double[] getRow() {
		return row;
	}

	public void setRow(double[] row) {
		this.row = row;
	}

	public double[] getCol() {
		return col;
	}

	public void setCol(double[] col) {
		this.col = col;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
