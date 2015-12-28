package svdfs;

/**
 * Metadata for IDA transformation of a file
 * 
 * @author Law Chee Yong, Victoria Chin
 * @version %I% %G%
 */
public class IdaInfo {
	public static final long serialVersionUID = 2L;
	
	/**
	 * Names for the IDA properties when transmitting and receiving data to and from the master application.
	 * 
	 * @author Victoria Chin
	 * @version %I% %G%
	 */
	public enum PropName{
		/**
		 * Number of slices to split a file to
		 */
		SHARES ("idaShares"),
		/**
		 * Number of slices required to combine to get back the original file
		 */
		QUORUM ("idaQuorum"),
		/**
		 * Matrix used in the IDA transformation
		 */
		MATRIX ("idaMatrix");
		
		private String name;
		PropName(String name){ this.name=name; }
		/**
		 * Gets the property name.
		 * 
		 * @return Name of the property
		 */
		public String value(){ return name; }
	}
	
	private long dataSize; //N, aka the file size
	private int shares; //n
	private int lostThreshold; //k
	private int quorum;	//m
	
	//so that the combiner can know when to remove the padding
	private long dataOffset=0;
	
	private int[][] matrix;
	private String strMatrix;
	
	/**
	 * Creates a IDA info object using the given shares and quorum
	 * 
	 * @param shares Number of slices to split a file to
	 * @param quorum Number of slices required to combine to get back the original file
	 */
	public IdaInfo(int shares, int quorum){
		this.shares=shares;
		this.quorum=quorum;
		this.lostThreshold=shares-quorum;
		
		matrix=new int[this.shares][this.quorum];
	}
	
	/**
	 * Creates a IDA info object using the given shares, quorum and matrix
	 * 
	 * @param shares Number of slices to split a file to
	 * @param quorum Number of slices required to combine to get back the original file
	 * @param matrix Matrix used in the IDA transformation
	 */
	public IdaInfo(int shares, int quorum, int matrix[][]){
		this.shares=shares;
		this.quorum=quorum;
		this.lostThreshold=shares-quorum;
		setMatrix(matrix);
	}
	
	/**
	 * Creates a IDA info object using the given shares, quorum and matrix
	 * 
	 * @param shares Number of slices to split a file to
	 * @param quorum Number of slices required to combine to get back the original file
	 * @param strMatrix Matrix used in the IDA transformation in the following string format X,X,X|Y,Y,Y|Z,Z,Z
	 */
	public IdaInfo(int shares, int quorum, String strMatrix){
		this.shares=shares;
		this.quorum=quorum;
		this.lostThreshold=shares-quorum;	
		this.matrix=new int[shares][quorum];
		
		setStrMatrix(strMatrix);
	}

	/**
	 * Gets the number of slices to split a file to
	 * 
	 * @return Number of slices to split a file to
	 */
	public int getShares() {
		return shares;
	}
	/**
	 * Gets the number of slices that can be lost before combination cannot happen. The value is obtain by subtracting the value of quorum with the value of shares
	 * 
	 * @return Number of slices that can be lost before combination cannot happen
	 */
	public int getLostThreshold() {
		return lostThreshold;
	}
	/**
	 * Gets the number of slices that is required to combine to get back the original file
	 * 
	 * @return Number of slices that is required for combination
	 */
	public int getQuorum() {
		return quorum;
	}
	
	/**
	 * Gets the size of the file
	 * 
	 * @return Size of the file
	 */
	public long getDataSize() {
		return dataSize;
	}
	/**
	 * Sets the size of the file
	 * 
	 * @param dataSize Size of the file in bytes
	 */
	public void setDataSize(long dataSize) {
		this.dataSize = dataSize;
	}

	/**
	 * Gets the size of each slice of the file. Calculated by dividing the size of the file with the value of quorum. 
	 * 
	 * If the size of the file is a odd number, then add 1 byte to each slice.
	 * 
	 *  @return Size of each slice of the file
	 */
	public int getSliceLength()
	{
		/*
		int remainder = (int)(getDataSize() % getQuorum());
		int length = (int)(getDataSize()/getQuorum()); 
		if(remainder!=0)
			return length+1; //takes care of padding
		return length;
		*/
		return (int) (dataSize%getQuorum()>0? (dataSize/getQuorum())+1 : dataSize/getQuorum());
	}
	/**
	 * Gets the size of each slice of the file using the given size of the file. 
	 * Calculated by dividing the given size of the file with the value of quorum. 
	 * 
	 * If the size of the file is a odd number, then add 1 byte to each slice.
	 * 
	 * @param dataSize Size of the file in bytes
	 * @return Size of each slice of the file
	 */
	public long getSliceLength(long dataSize){
		return (dataSize%getQuorum()>0? (dataSize/getQuorum())+1 : dataSize/getQuorum());
	}
	
	/**
	 * Sets the matrix used for the IDA transformation
	 * 
	 * @param matrix Matrix used for IDA transformation
	 */
	public void setMatrix(int[][] matrix){
		this.matrix=matrix;
		
		StringBuilder str=new StringBuilder();
		
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[0].length; j++){
				str.append(matrix[i][j]+",");
			}
			str.insert(str.length()-1, "|");
		}
		str.deleteCharAt(str.length()-1);
		strMatrix=str.toString();
	}
	/**
	 * Gets the matrix used for the IDA transformation
	 * 
	 * @return Matrix used for the IDA transformation
	 */
	public int[][] getMatrix(){
		return matrix;
	}
	
	public void setStrMatrix(String matrix){
		this.strMatrix=matrix;
		
		int row=0, col;
		for(String matrixRow: matrix.split("\\|")){
			col=0;
			for(String matrixCol: matrixRow.split(",")){
				this.matrix[row][col]=Integer.parseInt(matrixCol);
				col++;
			}
			row++;
		}
	}
	
	public String getStrMatrix(){
		return strMatrix;
	}

	/**
	 * Sets the offset within the file to start the IDA transformation
	 * 
	 * @param dataOffset Offset within the file to start the IDA transformation
	 */
	public void setDataOffset(long dataOffset) {
		this.dataOffset = dataOffset;
	}
	/**
	 * Gets the offset within the file to start the IDA transformation
	 * 
	 * @return Offset within the file to start the IDA transformation
	 */
	public long getDataOffset() {
		return dataOffset;
	}
}
