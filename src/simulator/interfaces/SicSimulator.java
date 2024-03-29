package simulator.interfaces;
import java.io.File;


public interface SicSimulator {

	// 시뮬레이터를 동작시키기 위한 세팅을 수행한다.
	// 메모리 작업 등 실질적인 초기화 작업을 수행한다.
	public void initialize(File objFile, ResourceManager rMgr);

	// 하나의 명령어만 수행한다, 해당 명령어가 수행되고 난 값의 변화를 
	// 보여주고, 다음 명령어를 포인팅한다
	// 실질적인 동작을 수행하는 메소드
	public void oneStep();

	// 남은 명령어를 모두 수행하는 메소드.
	// 목적 코드를 모두 수행하고 난 값의 변화를 보여준다.
	// 실질적인 동작을 수행하는 메소드.
	public void allStep();
}
