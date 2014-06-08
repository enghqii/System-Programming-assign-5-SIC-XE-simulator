package simulator.impls;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import simulator.interfaces.ResourceManager;
import simulator.interfaces.VisualSimulator;

public class SimulatorFrame extends JFrame implements VisualSimulator {

	private static final long serialVersionUID = 626819653852183458L;
	
	private File objFile = null;

	private JFileChooser 	fileChooser 	= new JFileChooser();
	private ResourceManager rmgr 			= new MemoryManager();
	private SicLoaderImpl 		loader 			= new SicLoaderImpl();
	private SicSimulatorImpl 	simulator 		= new SicSimulatorImpl();
	
	private JTextArea ProgramInfo;
	private JTextArea RegisterInfo;
	private JTextArea opInfo;

	public SimulatorFrame() throws HeadlessException {

		this.setSize(400, 500);

		this.setTitle("SIC/XE simulator");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initUI();
	}

	private void initUI() {
		setLayout(null);

		{
			JLabel filename = new JLabel("file name : ");
			filename.setBounds(10, 10, 100, 25);
			this.add(filename);

			final JTextField fileField = new JTextField();
			fileField.setBounds(100 - 10, 10, 150, 25);
			this.add(fileField);

			JButton fileOpen = new JButton("open");
			fileOpen.setBounds(250, 10, 65, 25);

			fileOpen.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					int result = fileChooser
							.showOpenDialog(SimulatorFrame.this);

					if (result == JFileChooser.APPROVE_OPTION) {

						File selectedFile = fileChooser.getSelectedFile();
						fileField.setText(""+selectedFile);
						initialize(selectedFile, rmgr);
					}

				}
			});
			this.add(fileOpen);
		}

		{
			ProgramInfo = new JTextArea();
			ProgramInfo.setBounds(10, 60, 350, 70);
			ProgramInfo.setEditable(false);
			this.add(ProgramInfo);
		}
		
		JButton oneStepBtn = new JButton("oneStep");
		oneStepBtn.setBounds(10, 140, 100, 25);
		oneStepBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				oneStep();
			}
		});
		this.add(oneStepBtn);
		
		JButton allStepBtn = new JButton("AllStep");
		allStepBtn.setBounds(135, 140, 100, 25);
		allStepBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				allStep();
			}
		});
		this.add(allStepBtn);
		
		JButton abortBtn = new JButton("abort");
		abortBtn.setBounds(260, 140, 100, 25);
		abortBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//allStep();
				initialize(objFile, rmgr);
				opInfo.setText("");
			}
		});
		this.add(abortBtn);
		
		{
			RegisterInfo = new JTextArea();
			RegisterInfo.setEditable(false);
			RegisterInfo.setBounds(10, 190, 350, 70);
			this.add(RegisterInfo);
		}
		
		{
			opInfo = new JTextArea();
			opInfo.setEditable(false);
			opInfo.setLineWrap(true);
			JScrollPane pane = new JScrollPane(opInfo);
			pane.setBounds(10, 290, 350, 73*2);
			this.add(pane);
		}
		
	}

	@Override
	public void initialize(File objFile, ResourceManager rMgr) {
		
		this.objFile = objFile;

		loader.load(objFile, rmgr);
		System.out.println(">>Load done.");

		simulator.initialize(objFile, rmgr);
		System.out.println(">>Simulator initialised.");

		{
			String info = "Program Name : " + loader.getProgramName() + "\n";
			info += "Start Addr : " + loader.getStartAddr() + "\n";
			info += "Size : " + loader.getSectionSize();
			ProgramInfo.setText(info);
		}
		updateRegisterInfo();
	}

	@Override
	public void oneStep() {
		if (rmgr.getRegister(8) != -1){
			simulator.oneStep();
		}
		updateRegisterInfo();
		updateOpInfo();
	}

	@Override
	public void allStep() {
		simulator.allStep();
		updateRegisterInfo();
		updateOpInfo();
	}
	
	private void updateRegisterInfo(){
		
		String info = "";
		
		info += "A : " + String.format("%06X",rmgr.getRegister(0)) + " | ";
		info += "X : " + String.format("%06X",rmgr.getRegister(1)) + " | ";
		info += "L : " + String.format("%06X",rmgr.getRegister(2)) + " | \n";
		info += "B : " + String.format("%06X",rmgr.getRegister(3)) + " | ";
		info += "S : " + String.format("%06X",rmgr.getRegister(4)) + " | ";
		info += "T : " + String.format("%06X",rmgr.getRegister(5)) + " | \n";
		info += "F : " + String.format("%06X",rmgr.getRegister(6)) + " | ";
		info += "PC : " + String.format("%06X",rmgr.getRegister(8)) + " | ";
		info += "SW : " + String.format("%06X",rmgr.getRegister(9)) + " | \n";
		
		RegisterInfo.setText(info);
	}

	private void updateOpInfo() {

		int pc = rmgr.getRegister(8);
		byte[] mem0 = rmgr.getMemory(Math.max(pc - 32, 0), 32);
		byte[] mem1 = rmgr.getMemory(Math.max(pc, 0), 4);
		byte[] mem2 = rmgr.getMemory(Math.max(pc + 4, 0), 32);
		
		String memStr = "";
		
		for(byte b : mem0){
			memStr += String.format("%02X", b);
		}
		
		memStr += "\n\nPC -> ";
		
		for(byte b : mem1){
			memStr += String.format("%02X", b);
		}
		
		memStr += "\n\n";
		for(byte b : mem2){
			memStr += String.format("%02X", b);
		}

		opInfo.setText(memStr);
	}

}
