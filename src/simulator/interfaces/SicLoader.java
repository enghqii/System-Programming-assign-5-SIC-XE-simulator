package simulator.interfaces;
import java.io.File;


public interface SicLoader {
	
	// �����ڵ带 �о� �޸𸮿� �ε��Ѵ�.
	// �����ڵ��� �� ���(H, T, M)���� �о� ������ �����Ѵ�.
	public void load(File objFile, ResourceManager rMgr);
	
}
