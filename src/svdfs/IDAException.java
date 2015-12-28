package svdfs;

/**
 * Exception that occurred during the splitting and/or combining of the data.
 * 
 * @author Law Chee Yong
 * @version %I% %G%
 */
public class IDAException extends Exception {
	public static final long serialVersionUID = 1L;
	
	/**
	 * Creates a exception with a message.
	 * 
	 * @param msg The message for the exception.
	 */
	public IDAException(String str) {
		super(str);
	}
	
	/**
	 * Creates a empty exception
	 */
	public IDAException() {
		
	}

	/**
	 * Creates a exception with a inner/base exception.
	 * 
	 * @param innerException
	 */
	public IDAException(Exception innerException) {
		super(innerException);
	}
}
