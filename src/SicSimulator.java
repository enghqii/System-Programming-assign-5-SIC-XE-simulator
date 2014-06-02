import java.io.File;


public interface SicSimulator {

	// �ùķ����͸� ���۽�Ű�� ���� ������ �����Ѵ�.
	// �޸� �۾� �� �������� �ʱ�ȭ �۾��� �����Ѵ�.
	public void initialize(File objFile, ResourceManager rMgr);

	// �ϳ��� ��ɾ �����Ѵ�, �ش� ��ɾ ����ǰ� �� ���� ��ȭ�� 
	// �����ְ�, ���� ��ɾ �������Ѵ�
	// �������� ������ �����ϴ� �޼ҵ�
	public void oneStep();

	// ���� ��ɾ ��� �����ϴ� �޼ҵ�.
	// ���� �ڵ带 ��� �����ϰ� �� ���� ��ȭ�� �����ش�.
	// �������� ������ �����ϴ� �޼ҵ�.
	public void allStep();
}
