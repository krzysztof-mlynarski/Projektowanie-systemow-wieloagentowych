package lab2;

public class Matrix 
{
	private int row;
	private int column;
	private double[][] value;
	
	public Matrix(int row, int column) {
		this.row = row;
		this.column = column;
		this.value = new double[row][column];
	}
	
	public Matrix(int row, int column, double[][] value) {
		this.row = row;
		this.column = column;
		this.value = value;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public double[][] getValue() {
		return value;
	}

	public void setValue(int row, int col, double value) {
		this.value[row][col] = value;
	}
}
