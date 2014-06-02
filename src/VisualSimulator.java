import java.io.File;


public interface VisualSimulator {
	
	// �ùķ����͸� ���۽�Ű�� ���� ������ �����Ѵ�.
	// sic �ùķ����͸� ���� �δ��� �����Ű��, �ε�� ������ �о� �����־�
	// ������ ������ �� �ִ� ���·� ����� ���´�.
	public void initialize(File objFile, ResourceManager rMgr);
	
	// �ϳ��� ��ɾ �����ϴ� �޼ҵ�ν�, sic �ùķ����Ϳ��� �۾��� �����Ѵ�.
	public void oneStep();
	
	// ���� ��ɾ ��� �����ϴ� �޼ҵ�ν�, sic �ùķ����Ϳ��� �۾��� ����.
	public void allStep();
	
	/* �̿ܿ� GUI���� �޼ҵ�� */
}
