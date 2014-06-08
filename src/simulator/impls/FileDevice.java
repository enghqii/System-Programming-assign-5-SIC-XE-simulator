package simulator.impls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import simulator.interfaces.Device;

public class FileDevice implements Device {
	
	private File 				file = null;
	private FileInputStream 	input = null;
	private FileOutputStream 	output = null;

	@Override
	public void initialize(String devName) {
		
		file = new File(devName);
		
		try {

			output = new FileOutputStream(file, true);
			input = new FileInputStream(file);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(byte[] data) {
		
		try {
			output.write(data);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public byte[] read(int size) {
		
		byte[] buffer = new byte[size];
		
		try {
			input.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return buffer;
	}

}
