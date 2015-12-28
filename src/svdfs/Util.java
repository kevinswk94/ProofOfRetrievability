package svdfs;

import java.util.Random;

import svdfs.GaloisField;
import svdfs.IDAException;

public class Util {
	public static final long serialVersionUID = 1L;
	
	private static final int MASK = 0xFF;
	
	public static void printFloatArray(float[] fa)
	{
		System.out.println("Float Array:");
		for (int i = 0; i < fa.length; i++) {
			System.out.print(" " + fa[i]);
		}
		System.out.println();
	}
	
	public static byte[] integerToBytes(int value)
	{
		return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
	}
	
	public static int bytesToInteger(byte[] ba)
	{
		return (ba[0] << 24)
        + ((ba[1] & MASK) << 16)
        + ((ba[2] & MASK) << 8)
        + (ba[3] & MASK);
	}

	public static byte[] shortToBytes(short value)
	{
		return new byte[] {
                (byte)(value >>> 8),
                (byte)value};
	}
	
	public static short bytesToShort(byte[] ba)
	{
		return (short)((ba[0] << 8) + (ba[1] & MASK));
	}

	public static byte[] floatToBytes(float f)
	{
		int i = Float.floatToRawIntBits(f);
		return integerToBytes(i);
	}
	public static float bytesToFloat(byte[] ba)
	{
		int bits = 0;

		bits |= ((int) ba[0] & MASK) << 24;
		bits |= ((int) ba[1] & MASK) << 16;
		bits |= ((int) ba[2] & MASK) << 8;
		bits |= ((int) ba[3] & MASK);
		
        return Float.intBitsToFloat(bits);
	}

	
	private static int getNextUnique(int[] a)
	{
		java.util.Random ran = new Random();
		int next;
		
		while(true)
		{
			//Generates a number between 1 to 255 (cannot be zero)
			next = ran.nextInt(254) + 1; // 0 to 255
			
			//check if unique
			for (int i = 0; i < a.length; i++) {
				if(a[i] == next)
					continue; //not unique
			}
			
			return next; //unique
		}
	}
	                            
	/**
	 * Generates an independence matrix.
	 * @param row No of rows of the generated matrix
	 * @param col No of columns of the generated matrix
	 * @return A matrix that has rows that are independences of each other
	 */
	public static int[][] generateIndependenceMatrix(int row, int col)
	{
		//Conditions for independence
		//xi + yi <> 0 (we can satisfy this if x and y are positive and non-zero
		//for i <> j, xi <> xj and yi <> yj (ie, unique) 
		
		int[] x = new int[row];
		int[] y = new int[col];
		
		for (int i = 0; i < x.length; i++) {
			x[i] = Util.getNextUnique(x);
		}
		for (int i = 0; i < y.length; i++) {
			y[i] = Util.getNextUnique(y);
		}
		
//			for (int i = 0; i < x.length; i++) {
//				x[i] = i*2 + 1;
//			}
//			for (int i = 0; i < y.length; i++) {
//				y[i] = i*2;
//			}
		
		int[][] mat = new int[row][col];
		GaloisField gf = GaloisField.getInstance();
		//int n;
		for (int r = 0; r < row; r++) {
			mat[r] = new int[col];
			for (int c = 0; c < col; c++) {
				mat[r][c] = (int)(gf.gf2_inv(8, x[r] ^ y[c]));
			}
		}
		
		return mat;
	}
	
	/***
	 * Invert a matrix via Gaussian Jordan Elimination method.
	 * Operation using GF2
	 * @param mat
	 * @return
	 */
	public static int[][] invertMatrixGf2(int[][] matIn)
		throws IDAException
	{

		//Generate the matrix to be used
		int size = matIn.length;
		
		//mat = [ matIn | I ] //index 0 not used
		int[][] mat = new int[size + 1][size*2 + 1]; //+1 because index 0 is not used
		for (int i = 1; i < size+1; i++) {
			//Put in the matIn matrix
			for (int j = 1; j < size+1; j++) {
				mat[i][j] = matIn[i-1][j-1]; //-1 because matIn is 0 index based
			}
			//Put in the Identity matrix
			for (int j = 1; j < size+1; j++) {
				if(j == i) {//diagonal
					mat[i][j+size] = 1;
				}
				else {
					mat[i][j+size] = 0;
				}
			}
		}
		
		//System.out.println(Util.matrixToString(mat));

		
		int i = 1;
		int j = 1;
		int n = mat.length-1; //row
		int m = mat[0].length-1; //col
		int k;
		
		while( i<=n && j<=m ){

			//look for a non-zero entry in col j at or below row i
			k = i;
			while( k<=n && mat[k][j]==0 ) 
				k++;

			// if such an entry is found at row k
			if( k<=n ){

				//  if k is not i, then swap row i with row k
				if( k!=i ) {
					swap(mat, i, k, j);
					//System.out.println(matrixToString(mat));
				}

				// if A[i][j] is not 1, then divide row i by A[i][j]
				if(mat[i][j]!=1 ){
					divide(mat, i, j);
					//System.out.println(matrixToString(mat));
				}

				// eliminate all other non-zero entries from col j by subtracting from each
				// row (other than i) an appropriate multiple of row i
				eliminate(mat, i, j);
				//System.out.println(matrixToString(mat));
				i++;
			}
			j++;
		}
		
		int[][] result = new int[n][n];
		for (int r = 0; r < n; r++) {
			for (int c = 0; c < n; c++) {
				result[r][c] = mat[r+1][c+n+1]; //for some reason 0 index is not used.
			}
		}
		
		//System.out.println(matrixToString(result));
		
		return result;
	}
	
	public static String matrixToString(int[][] values)
	{
		if(values == null)
			return "Empty matrix";
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				sb.append(values[i][j]);
				sb.append(" ");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}

	
	/**
	 * swap row i with row k
	 * @param Input matrix
	 * @param i
	 * @param k
	 * @param j
	 */
	private static void swap(int[][] A, int i, int k, int j){
		int m = A[0].length - 1;
		int temp;
		for(int q=j; q<=m; q++){
			temp = A[i][q];
			A[i][q] = A[k][q];
			A[k][q] = temp;
		}
	}

	/**
	 * divide row i by A[i][j]
	 * pre: A[i][j]!=0, A[i][q]==0 for 1<=q<j
	 * post: A[i][j]==1;
	 * @param A
	 * @param i
	 * @param j
	 */
	private static void divide(int[][] A, int i, int j){
		int m = A[0].length - 1;
		for(int q = j+1; q <= m; q++) {
			//A[i][q] /= A[i][j];
			A[i][q] = (int)GaloisField.getInstance().gf2_div(8, A[i][q], A[i][j]);
		}
		A[i][j] = 1;
	}
	
	/**
	 * subtract an appropriate multiple of row i from every other row
	 * pre: A[i][j]==1, A[i][q]==0 for 1<=q<j
	 * post: A[p][j]==0 for p!=i
	 * @param A
	 * @param i
	 * @param j
	 */
	private static void eliminate(int[][] A, int i, int j){
		
		int n = A.length - 1;
		int m = A[0].length - 1;
		
		for(int p=1; p<=n; p++){
			if( p!=i && A[p][j]!=0 ){
				for(int q=j+1; q<=m; q++){
					//A[p][q] -= A[p][j]*A[i][q];
					A[p][q] = A[p][q] ^ (int)(GaloisField.getInstance().gf2_mul(8, A[p][j], A[i][q])); 
					
				}
				A[p][j] = 0;
			}
		}
	}


	@SuppressWarnings("unused")
	private static int getDeterminant(int[][] values) throws IDAException
	{
		if(values.length == 2) {
			return values[0][0] * values[1][1] - values[0][1] * values[1][0];
		}
		
		int rows = values.length;
		int sign = 1;
		int col = 0;
		
		//using the first column
		int acc = 0;
		for (int i = 0; i < rows; i++) {
			acc += sign * values[i][col] * Util.getDeterminant(Util.getCofactorElement(values, i, col));
			sign *= -1; //toggle sign
		}
		
		return acc;
	}

	/**
	 * Gets the cofactor of a matrix
	 * @param values The input matrix
	 * @param row The row
	 * @param col The col
	 * @return Cofactor
	 * @throws IDAException
	 */
	private static int[][] getCofactorElement(int[][] values, int row, int col)
	throws IDAException
	{
		if(values == null || values.length < 1)
			throw new IDAException("Matrix::getSubOrderMarix:Error in values parameter");

		int RowCount = values.length;
		int ColCount = values[0].length;

		int matRow = 0, matCol = 0;
		int[][] mat = new int[RowCount-1][ColCount-1];
		for (int i = 0; i < RowCount; i++) {
			if(i == row)
				continue;
			matCol = 0;
			for (int j = 0; j < ColCount; j++) {
				if(j == col)
					continue;
				mat[matRow][matCol++] = values[i][j];
			}
			matRow++;
		}

		return mat;
	}
}
