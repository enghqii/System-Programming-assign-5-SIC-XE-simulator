package simulator.impls;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import simulator.interfaces.ResourceManager;
import simulator.interfaces.SicSimulator;

public class SicSimulatorImpl implements SicSimulator {
	
	private ResourceManager rmgr = null;
	private int 			PC = 0;
	
	private Map<Byte, Integer> opTable = null;

	public SicSimulatorImpl() {
		
		// get opCode Table
		opTable = new TreeMap<Byte, Integer>();

		opTable.put((byte) 0x14, 3);
		opTable.put((byte) 0x48, 3);
		opTable.put((byte) 0x00, 3);
		opTable.put((byte) 0x28, 3);
		opTable.put((byte) 0x30, 3);
		opTable.put((byte) 0x3C, 3);
		opTable.put((byte) 0x0C, 3);
		opTable.put((byte) 0x08, 3);
		opTable.put((byte) 0x4C, 3);
		opTable.put((byte) 0x04, 3);
		opTable.put((byte) 0xE0, 3);
		opTable.put((byte) 0xD8, 3);
		opTable.put((byte) 0x54, 3);
		opTable.put((byte) 0x2C, 3);
		opTable.put((byte) 0x10, 3);
		opTable.put((byte) 0x38, 3);
		opTable.put((byte) 0x50, 3);
		opTable.put((byte) 0xDC, 3);
		opTable.put((byte) 0x74, 3);

		opTable.put((byte) 0xB4, 2);
		opTable.put((byte) 0xA0, 2);
		opTable.put((byte) 0xB8, 2);
	}

	@Override
	public void initialize(File objFile, ResourceManager rMgr) {
		this.rmgr = rMgr;
		this.PC = 0x1033;
	}

	@Override
	public void oneStep() {
		// TODO : PC가 가리키는 메모리에서 명령어 꺼내옴, 실행.
		
		// if (PC == -1) exit;
		
		// get opcode
		byte[] op = rmgr.getMemory(PC, 2);
		byte opcode = (byte) (op[0] & 0xFC); // n,i 버림.

		// get size
		int size = opTable.get(opcode);
		if ((size == 3) && ((op[1] & 0x10) != 0)) {
			// type_3 && extended
			size = 4;
		}

		// get full instruction
		op = rmgr.getMemory(PC, size);
		
		
		// print it
		for(byte b : op){
			System.out.print(String.format("%02X", b));
		}
		
		System.out.println("");
		
		// go to next step
		PC = PC + size;
	}

	@Override
	public void allStep() {
		// TODO : until "END" repeat onStep;
		
		while(PC != -1){
			oneStep();
		}
	}

}