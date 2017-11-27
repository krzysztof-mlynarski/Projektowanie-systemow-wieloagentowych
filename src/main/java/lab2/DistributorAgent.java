package lab2;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import lab2.Behaviour.DistributeBehaviour;
import lab2.Helpers.DFServiceHelper;

@SuppressWarnings("serial")
public class DistributorAgent extends Agent
{
	private List<MatrixFragment> matrixFragments;
	private Matrix resultMatrix;
	private List<Agents> agents;
	
	@Override
	protected void setup()
	{
		DFServiceHelper.Register(this, "distributor", "distributor-matrix");
		
		Matrix matrixA = new Matrix(6, 7, new double[][] 
		{
			{18, 2 ,66, 4, 22, 34, 212}, 
			{12, 25, 3, 2, 85, 150, 215},
			{888, 22 ,66, 42, 32, 34, 2}, 
			{12, 215, 653, 62, 85, 150, 215},
			{108, 2 ,616, 4, 22, 34, 212}, 
			{121, 125, 13, 2, 585, 150, 215}
		});
		Matrix matrixB = new Matrix(7, 6, new double[][] 
		{
			{18, 2 ,66, 4, 22, 34}, 
			{12, 25, 3, 2, 85, 150},
			{888, 22 ,66, 42, 32, 34}, 
			{12, 215, 653, 62, 85, 150},
			{108, 2 ,616, 4, 22, 34}, 
			{121, 125, 13, 2, 585, 150},
			{108, 2 ,616, 4, 22, 34}
		});
		resultMatrix = new Matrix(matrixA.getRow(), matrixB.getColumn());
		
		agents = new ArrayList<>();
		matrixFragments = GenerateFragments(matrixA, matrixB);
				
		addBehaviour(new DistributeBehaviour(this));
	}
	
	public List<MatrixFragment> GenerateFragments(Matrix a, Matrix b) 
	{
		List<MatrixFragment> matrixFragments = new ArrayList<>();
		
		for (int i = 0; i < a.getRow(); i++) {
			for (int j = 0; j < b.getColumn(); j++) {
				double[] transpValues = new double[b.getRow()];
						
				for (int k = 0; k < b.getRow(); k++) {
					 transpValues[k] = b.getValue()[k][j];
				}
				
				matrixFragments.add(new MatrixFragment(i, j, a.getValue()[i], transpValues));
			}
		}
		
		return matrixFragments;
	}

	public List<MatrixFragment> getMatrixFragments() {
		return matrixFragments;
	}

	public void setMatrixFragments(List<MatrixFragment> matrixFragments) {
		this.matrixFragments = matrixFragments;
	}

	public Matrix getResultMatrix() {
		return resultMatrix;
	}

	public void setResultMatrix(Matrix resultMatrix) {
		this.resultMatrix = resultMatrix;
	}

	public List<Agents> getAgents() {
		return agents;
	}

	public void setAgents(List<Agents> agents) {
		this.agents = agents;
	}
}
