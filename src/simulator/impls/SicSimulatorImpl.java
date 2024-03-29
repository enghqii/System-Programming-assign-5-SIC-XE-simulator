package simulator.impls;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import simulator.interfaces.ResourceManager;
import simulator.interfaces.SicSimulator;
import util.Pair;

public class SicSimulatorImpl implements SicSimulator {
	
	private ResourceManager rmgr = null;
	
	private Map<Byte, Pair<Integer, OperatorTypeAll>> opTable = null;
	
	private byte[] op = null;

	public SicSimulatorImpl() {
		
		// get opCode Table
		opTable = new TreeMap<Byte, Pair<Integer, OperatorTypeAll>>();

		opTable.put((byte) 0x14, new Pair<Integer, OperatorTypeAll>(3,new STL()));
		opTable.put((byte) 0x48, new Pair<Integer, OperatorTypeAll>(3,new JSUB()));
		opTable.put((byte) 0x00, new Pair<Integer, OperatorTypeAll>(3,new LDA()));
		opTable.put((byte) 0x28, new Pair<Integer, OperatorTypeAll>(3,new COMP()));
		opTable.put((byte) 0x30, new Pair<Integer, OperatorTypeAll>(3,new JEQ()));
		opTable.put((byte) 0x3C, new Pair<Integer, OperatorTypeAll>(3,new J()));
		opTable.put((byte) 0x0C, new Pair<Integer, OperatorTypeAll>(3,new STA()));
		opTable.put((byte) 0x08, new Pair<Integer, OperatorTypeAll>(3,new LDL()));
		opTable.put((byte) 0x4C, new Pair<Integer, OperatorTypeAll>(3,new RSUB()));
		opTable.put((byte) 0x04, new Pair<Integer, OperatorTypeAll>(3,new LDX()));
		opTable.put((byte) 0xE0, new Pair<Integer, OperatorTypeAll>(3,new TD()));
		opTable.put((byte) 0xD8, new Pair<Integer, OperatorTypeAll>(3,new RD()));
		opTable.put((byte) 0x54, new Pair<Integer, OperatorTypeAll>(3,new STCH()));
		opTable.put((byte) 0x2C, new Pair<Integer, OperatorTypeAll>(3,new TIX()));
		opTable.put((byte) 0x10, new Pair<Integer, OperatorTypeAll>(3,new STX()));
		opTable.put((byte) 0x38, new Pair<Integer, OperatorTypeAll>(3,new JLT()));
		opTable.put((byte) 0x50, new Pair<Integer, OperatorTypeAll>(3,new LDCH()));
		opTable.put((byte) 0xDC, new Pair<Integer, OperatorTypeAll>(3,new WD()));
		opTable.put((byte) 0x74, new Pair<Integer, OperatorTypeAll>(3,new LDT()));

		opTable.put((byte) 0xB4, new Pair<Integer, OperatorTypeAll>(2,new CLEAR()));
		opTable.put((byte) 0xA0, new Pair<Integer, OperatorTypeAll>(2,new COMPR()));
		opTable.put((byte) 0xB8, new Pair<Integer, OperatorTypeAll>(2,new TIXR()));
		
	}

	@Override
	public void initialize(File objFile, ResourceManager rMgr) {
		this.rmgr = rMgr;
		
		// set PC zero
		//this.PC = 0;//0x1033;
		rmgr.setRegister(8, 0);
		
		rmgr.setRegister(2, -1); // LDL -1
	}

	@Override
	public void oneStep() {
		// TODO : PC가 가리키는 메모리에서 명령어 꺼내옴, 실행.
		
		// if (PC == -1) exit;
		
		// get opcode
		op = rmgr.getMemory(rmgr.getRegister(8), 2);
		byte opcode = (byte) (op[0] & 0xFC); // n,i 버림.

		// get size
		int size = opTable.get(opcode).getFirst();
		if ((size == 3) && ((op[1] & 0x10) != 0)) {
			// type_3 && extended
			size = 4;
		}

		// get full instruction
		op = rmgr.getMemory(rmgr.getRegister(8), size);
		{
			// print it
			for (byte b : op) {
				System.out.print(String.format("%02X", b));
			}

			System.out.println("");
		}
		
		// AND, go to next step - 이거 사이즈 계산 하고 나서 바로 해야하는거 아님?
			// PC = PC + size;
		rmgr.setRegister(8, rmgr.getRegister(8) + size);

		// Do step
		switch (size) {
		case 1:
			// do op
			break;
		case 2: {
			// get r1, r2

			int r1 = (op[1] >> 4);
			int r2 = (op[1] & 0x0F);

			OperatorType2 operator = (OperatorType2) opTable.get(opcode).getSecond();
			operator.operate(rmgr, r1, r2);

			break;
		}

		case 4:
		case 3: {
			// get disp, nixbpe.

			NIXBPE nixbpe = new NIXBPE();

			nixbpe.n = ((op[0] & 0x02) != 0 ? true : false);
			nixbpe.i = ((op[0] & 0x01) != 0 ? true : false);

			nixbpe.x = ((op[1] & 0x80) != 0 ? true : false);
			nixbpe.b = ((op[1] & 0x40) != 0 ? true : false);
			nixbpe.p = ((op[1] & 0x20) != 0 ? true : false);
			nixbpe.e = ((op[1] & 0x10) != 0 ? true : false);
			
			// disp BEGIN TODO generalize

			int disp = 0;
		
			if(size == 3){
				
				disp |= ((op[1] & 0x0F) << 8);
				disp |= ((op[2] & 0xFF) << 0);
				
			}else if(size == 4){

				disp |= ((op[1] & 0x0F) << 8*2);
				disp |= ((op[2] & 0xFF) << 8*1);
				disp |= ((op[3] & 0xFF) << 8*0);
				
			}
			
			if ((op[1] & 0x08) != 0) {
				if(size == 3){
					disp |= (0xFFFFF000);
				}else if(size ==4){
					disp |= (0xFFF00000);
				}
			}
			
			// disp END

			// 이제 명령어랑 어드레싱 모드, 피연산자(disp)까지 얻음.

			int targetAddr = disp;

			if (nixbpe.x) {
				targetAddr += rmgr.getRegister(1);
			}
			if (nixbpe.b) {
				targetAddr += rmgr.getRegister(3);
			}
			if (nixbpe.p) {
				targetAddr += rmgr.getRegister(8);
			}
			
			// indirect
			if(nixbpe.n == true && nixbpe.i == false){
				
				byte[] byteVal = rmgr.getMemory(targetAddr, 3);
				int value = 0;

				value |= (byteVal[0] << 8 * 2);
				value |= (byteVal[1] << 8 * 1);
				value |= (byteVal[2] << 8 * 0);
				
				targetAddr = value;
			}

			OperatorType3 operator = (OperatorType3) opTable.get(opcode).getSecond();
			operator.operate(rmgr, nixbpe, disp, targetAddr);

			break;
		}
		}
	}

	@Override
	public void allStep() {
		// TODO : until "END" repeat onStep;
		
		while(rmgr.getRegister(8) != -1){
			oneStep();
		}
	}
	
	public byte[] getOp(){
		return op;
	}

}