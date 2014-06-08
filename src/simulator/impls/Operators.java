package simulator.impls;

import java.nio.ByteBuffer;

import simulator.interfaces.ResourceManager;

public class Operators {
	// NULL CLASS
}

interface OperatorTypeAll{
}

interface OperatorType2 extends OperatorTypeAll{
	public void operate(ResourceManager rmgr,int r1, int r2);
}

interface OperatorType3 extends OperatorTypeAll{
	public void operate(ResourceManager rmgr,NIXBPE nixbpe, int disp, int targetAddr);
}


class STL implements OperatorType3{

	@Override
	public void operate(ResourceManager rmgr, NIXBPE nixbpe, int disp, int targetAddr) {
		
		int val = rmgr.getRegister(2);
		byte[] tmpArr = ByteBuffer.allocate(4).putInt(val).array();
		
		// 상위 1바이트 절삭
		byte[] valArr = new byte[3];
		System.arraycopy(tmpArr, 1, valArr, 0, valArr.length);
		
		rmgr.setMemory(targetAddr, valArr);
	}
	
}

class CLEAR implements OperatorType2{

	@Override
	public void operate(ResourceManager rmgr, int r1, int r2) {
		rmgr.setRegister(r1, 0);
	}
	
}