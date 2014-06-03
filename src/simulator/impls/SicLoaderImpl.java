package simulator.impls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import simulator.interfaces.ResourceManager;
import simulator.interfaces.SicLoader;

public class SicLoaderImpl implements SicLoader {

	private Map<String, Integer> symbolTable = null;

	public SicLoaderImpl() {

	}

	@Override
	public void load(File objFile, ResourceManager rMgr) {

		try {

			int sectionOffset = 0;

			symbolTable = new TreeMap<String, Integer>();
			BufferedReader objReader = new BufferedReader(new FileReader(objFile));
			
			int sectionSize = 0;

			while (true) {

				String line = objReader.readLine();
				
				if (line == null)
					break;
				
				if(line.length() < 1)
					continue;

				switch (line.charAt(0)) {
				
				case 'H': {
					// get section name (symbol), start addr (and actual addr),
					// section size
					String sectionName = line.substring(1, 1 + 6);

					String startAddrStr = line.substring(7, 7 + 6);
					int startAddr = Integer.parseInt(startAddrStr, 16);

					String sectionSizeStr = line.substring(13, 13 + 6);
					sectionSize = Integer.parseInt(sectionSizeStr, 16);

					symbolTable.put(sectionName, sectionOffset + startAddr);
				}
					break;

				case 'D': {
					// set symbol and its addr
					for (int i = 1; i < line.length(); i += 12) {

						String symbol = line.substring(i, i + 6);

						String symAddrStr = line.substring(i + 6, i + 12);
						int symAddr = Integer.parseInt(symAddrStr, 16);

						symbolTable.put(symbol, sectionOffset + symAddr);
					}
				}
					break;

				case 'R':

					// umm.... it's external refs

					break;

				case 'T': {
					
					// Text Record, load 'em up
					String recordStartAddrStr = line.substring(1, 1 + 6);
					int recordStartAddr = Integer.parseInt(recordStartAddrStr, 16);

					String recordSizeStr = line.substring(7, 7 + 2);
					int recordSize = Integer.parseInt(recordSizeStr, 16);

					String recordStr = line.substring(9, 9 + recordSize * 2);
					byte[] record = new byte[recordSize];
					
					for(int i = 0; i < recordStr.length(); i++){
						
						if(i%2 == 0){
							record[i/2] |= ( Integer.parseInt("" + recordStr.charAt(i),16) << 4 );
						}else{
							record[i/2] |= Integer.parseInt("" + recordStr.charAt(i),16);
						}
					}

					rMgr.setMemory(sectionOffset + recordStartAddr, record);
				}
					break;

				case 'E' : {
					sectionOffset += sectionSize;
				}
				
					break;
				}
				
				System.out.println(line);

			}

			objReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
