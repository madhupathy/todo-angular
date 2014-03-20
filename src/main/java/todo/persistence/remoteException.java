package todo.persistence;

// we are cheating here to maintain future compability with EJB
public class remoteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public remoteException(String m) {
		super(m);
	}
}