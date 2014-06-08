import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import simulator.impls.SimulatorFrame;

public class SimulatorMain {
	
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		SimulatorFrame vSim = new SimulatorFrame();
		vSim.setVisible(true);
	}

}