import java.io.File;

import simulator.impls.MemoryManager;
import simulator.impls.SicLoaderImpl;
import simulator.impls.SicSimulatorImpl;
import simulator.interfaces.ResourceManager;
import simulator.interfaces.SicLoader;
import simulator.interfaces.SicSimulator;


public class SimulatorMain {
	
	public static void main(String[] args) {
		
		File objFile = new File("obj.txt");
		ResourceManager rmgr = new MemoryManager();
		
		SicLoader loader = new SicLoaderImpl();
		loader.load(objFile, rmgr);
		
		SicSimulator simulator = new SicSimulatorImpl();
		simulator.initialize(objFile, rmgr);
		simulator.allStep();
	}

}