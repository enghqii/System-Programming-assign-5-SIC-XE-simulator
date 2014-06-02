package simulator.impls;

import java.io.File;

import simulator.interfaces.ResourceManager;

public class MemoryManager implements ResourceManager {

	public MemoryManager() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initializeMemory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initializeRegister() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialDevice(String devName) {
		// TODO 파일 열어놓기. Map에 저장.
		File device = new File(devName);
	}

	@Override
	public void writeDevice(String devName, byte[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] readDevice(String devName, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMemory(int locate, byte[] data, int size) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] getMemory(int locate, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRegister(int regNum, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getRegister(int regNum) {
		// TODO Auto-generated method stub
		return 0;
	}

}
