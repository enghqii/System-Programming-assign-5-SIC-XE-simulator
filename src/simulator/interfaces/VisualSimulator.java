package simulator.interfaces;
import java.io.File;


public interface VisualSimulator {
	
	// 시뮬레이터를 동작시키기 위한 세팅을 수행한다.
	// sic 시뮬레이터를 통해 로더를 수행시키고, 로드된 값들을 읽어 보여주어
	// 스텝을 진행할 수 있는 상태로 만들어 놓는다.
	public void initialize(File objFile, ResourceManager rMgr);
	
	// 하나의 명령어만 수행하는 메소드로써, sic 시뮬레이터에게 작업을 전달한다.
	public void oneStep();
	
	// 남은 명령어를 모두 수행하는 메소드로써, sic 시뮬레이터에게 작업을 전달.
	public void allStep();
	
	/* 이외에 GUI관련 메소드들 */
}
