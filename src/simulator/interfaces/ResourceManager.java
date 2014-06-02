package simulator.interfaces;

public interface ResourceManager {

	public void 	initializeMemory();
	public void 	initializeRegister();
	
	public void 	initialDevice(String devName);
	public void 	writeDevice(String devName, byte[] data);
	public byte[] 	readDevice(String devName, int size);
	
	public void		setMemory(int locate, byte[] data);
	public byte[]	getMemory(int locate, int size);
	
	public void		setRegister(int regNum, int value);
	public int		getRegister(int regNum);
}
