package simulator.interfaces;

// An abstractive interface of external devices
public interface Device {
	
	public void 	initialize(String devName);
	public void 	write(byte[] data);
	public byte[] 	read(int size);
	
}
