package simulator.impls;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import simulator.interfaces.Device;
import simulator.interfaces.ResourceManager;

public class MemoryManager implements ResourceManager {
	
	private int[] 	registers 	= null;
	private byte[] 	memory 		= null;
	
	private Map<String, Device> deviceMap = null;

	public MemoryManager() {
		
		initializeRegister();
		initializeMemory();
		
		deviceMap = new TreeMap<String, Device>();
	}

	@Override
	public void initializeMemory() {
		
		memory = new byte[8192];
	}

	@Override
	public void initializeRegister() {
		
		registers = new int[10];
		
		for(int i = 0; i < registers.length; i++){
			registers[i] = 0;
		}
	}

	@Override
	public void initialDevice(String devName) {
		
		Device dev = new FileDevice();
		dev.initialize(devName);
		
		deviceMap.put(devName, dev);
	}

	@Override
	public void writeDevice(String devName, byte[] data) {
		
		Device dev = deviceMap.get(devName);
		dev.write(data);

	}

	@Override
	public byte[] readDevice(String devName, int size) {
		
		Device dev = deviceMap.get(devName);
		byte[] buffer = dev.read(size);
		
		return buffer;
	}

	@Override
	public void setMemory(int locate, byte[] data) {
		
		System.arraycopy(data, 0, memory, locate, data.length);
	}

	@Override
	public byte[] getMemory(int locate, int size) {
		
		byte[] subArr = Arrays.copyOfRange(memory, locate, locate + size);
		return subArr;
	}

	@Override
	public void setRegister(int regNum, int value) {
		
		this.registers[regNum] = value;
	}

	@Override
	public int getRegister(int regNum) {
		
		int val = registers[regNum];
		return val;
	}

}
