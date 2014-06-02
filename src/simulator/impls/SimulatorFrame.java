package simulator.impls;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JFrame;

import simulator.interfaces.ResourceManager;
import simulator.interfaces.VisualSimulator;

public class SimulatorFrame extends JFrame implements VisualSimulator {

	private static final long serialVersionUID = 626819653852183458L;

	public SimulatorFrame() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

	public SimulatorFrame(GraphicsConfiguration arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public SimulatorFrame(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public SimulatorFrame(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(File objFile, ResourceManager rMgr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void oneStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void allStep() {
		// TODO Auto-generated method stub

	}

}
