package simulator.interfaces;

public interface Device {
	
	public void 	initialize(String devName);
	public void 	write(byte[] data);
	public byte[] 	read(int size);
	
}
