package simulator.impls;

import java.nio.ByteBuffer;

import simulator.interfaces.ResourceManager;

public class Operators {
	// NULL CLASS
}

interface OperatorTypeAll {
	
}

interface OperatorType2 extends OperatorTypeAll {
	public void operate(ResourceManager rmgr, int r1, int r2);
}

interface OperatorType3 extends OperatorTypeAll {
	public void operate(ResourceManager rmgr, NIXBPE nixbpe, int disp,
			int targetAddr);
}

class STL implements OperatorType3 {

	@Override
	public void operate(ResourceManager rmgr, NIXBPE nixbpe, int disp,
			int targetAddr) {

		int val = rmgr.getRegister(2);
		byte[] tmpArr = ByteBuffer.allocate(4).putInt(val).array();

		// 상위 1바이트 절삭
		byte[] valArr = new byte[3];
		System.arraycopy(tmpArr, 1, valArr, 0, valArr.length);

		rmgr.setMemory(targetAddr, valArr);
	}

}

class JSUB implements OperatorType3 {

	@Override
	public void operate(ResourceManager rmgr, NIXBPE nixbpe, int disp,
			int targetAddr) {

		int pc = rmgr.getRegister(8);
		rmgr.setRegister(2, pc);

		rmgr.setRegister(8, targetAddr);

		// 현재 PC를 L에 저장. 타겟 어드레스로 점프

	}

}

class LDT implements OperatorType3 {

	@Override
	public void operate(ResourceManager rmgr, NIXBPE nixbpe, int disp,
			int targetAddr) {
		
		byte[] byteVal = rmgr.getMemory(targetAddr, 3);
		int value = 0;

		value |= (byteVal[0] << 8*2);
		value |= (byteVal[1] << 8*1);
		value |= (byteVal[2] << 8*0);
		
		rmgr.setRegister(5, value);
	}

}

class TD implements OperatorType3{

	@Override
	public void operate(ResourceManager rmgr, NIXBPE nixbpe, int disp,
			int targetAddr) {
		
		// read a byte
		Byte dev = rmgr.getMemory(targetAddr, 1)[0];
		
		rmgr.initialDevice(dev.toString());
		rmgr.setRegister(9, 1);
	}
	
}

class JEQ implements OperatorType3{

	@Override
	public void operate(ResourceManager rmgr, NIXBPE nixbpe, int disp,
			int targetAddr) {
		int sw = rmgr.getRegister(9);
		
		if(sw == 0){
			// jump to targetAddr
			rmgr.setRegister(8, targetAddr);
		}
	}
	
}

class RD implements OperatorType3{

	@Override
	public void operate(ResourceManager rmgr, NIXBPE nixbpe, int disp,
			int targetAddr) {
		// 바이트 하나 읽어서 A에 저장.
		Byte dev = rmgr.getMemory(targetAddr, 1)[0];
		
		byte data = rmgr.readDevice(dev.toString(), 1)[0];
		
		rmgr.setRegister(0, data);
	}
	
}

class STCH implements OperatorType3{

	@Override
	public void operate(ResourceManager rmgr, NIXBPE nixbpe, int disp,
			int targetAddr) {

		byte[] ch = new byte[1];
		ch[0] = (byte) rmgr.getRegister(0);

		rmgr.setMemory(targetAddr, ch);
	}

}

class JLT implements OperatorType3{

	@Override
	public void operate(ResourceManager rmgr, NIXBPE nixbpe, int disp,
			int targetAddr) {
		
		// less than
		if(rmgr.getRegister(9) < 0){
			rmgr.setRegister(8, targetAddr);
		}
	}
	
}

class STX implements OperatorType3{

	@Override
	public void operate(ResourceManager rmgr, NIXBPE nixbpe, int disp,
			int targetAddr) {
		// x의 값을 targetAddr에
		int val = rmgr.getRegister(1);
		byte[] tmpArr = ByteBuffer.allocate(4).putInt(val).array();

		// 상위 1바이트 절삭
		byte[] valArr = new byte[3];
		System.arraycopy(tmpArr, 1, valArr, 0, valArr.length);

		rmgr.setMemory(targetAddr, valArr);
	}
	
}

class RSUB implements OperatorType3{

	@Override
	public void operate(ResourceManager rmgr, NIXBPE nixbpe, int disp,
			int targetAddr) {
		int retAddr = rmgr.getRegister(2);
		rmgr.setRegister(8, retAddr);
	}
	
}

class CLEAR implements OperatorType2 {

	@Override
	public void operate(ResourceManager rmgr, int r1, int r2) {
		rmgr.setRegister(r1, 0);
	}

}

class COMPR implements OperatorType2 {

	@Override
	public void operate(ResourceManager rmgr, int r1, int r2) {
		int v1 = rmgr.getRegister(r1);
		int v2 = rmgr.getRegister(r2);

		if (v1 == v2) {
			rmgr.setRegister(9, 0);
		} else if (v1 < v2){
			rmgr.setRegister(9, -1);
		} else {
			rmgr.setRegister(9, +1);
		}
	}
	
}

class TIXR implements OperatorType2{

	@Override
	public void operate(ResourceManager rmgr, int r1, int r2) {
		// x = x + 1;
		rmgr.setRegister(1, rmgr.getRegister(1) + 1);
		
		int v1 = rmgr.getRegister(r1);
		int X = rmgr.getRegister(1);
		
		if (v1 == X) {
			rmgr.setRegister(9, 0);
		} else if (v1 < X){
			rmgr.setRegister(9, -1);
		} else {
			rmgr.setRegister(9, +1);
		}
	}
	
}