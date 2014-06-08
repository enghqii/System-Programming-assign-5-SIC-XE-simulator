package simulator.impls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import simulator.interfaces.ResourceManager;
import simulator.interfaces.SicLoader;

public class SicLoaderImpl implements SicLoader {

	private class ModRecord {
		public int 		addr 		= 0;
		public int 		offset 		= 0;	//Length of the filed to be modified
		public boolean 	flag 		= true; // true for '+', false for '-'
		public String 	symbol 		= "";
	}

	private ArrayList<ModRecord> modRecords = null;
	private Map<String, Integer> symbolTable = null;
	
	private String programName = null;
	private int startAddr = 0;
	private int sectionSize = 0;

	public SicLoaderImpl() {

	}

	@Override
	public void load(File objFile, ResourceManager rMgr) {

		try {

			int sectionOffset = 0;
			
			modRecords = new ArrayList<ModRecord>();
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
					sectionName = sectionName.replaceAll("\\s+",""); // remove white spaces

					String startAddrStr = line.substring(7, 7 + 6);
					int startAddr = Integer.parseInt(startAddrStr, 16);

					String sectionSizeStr = line.substring(13, 13 + 6);
					sectionSize = Integer.parseInt(sectionSizeStr, 16);

					symbolTable.put(sectionName, sectionOffset + startAddr);
					
					if(programName == null){
						this.programName = sectionName;
						this.startAddr = startAddr;
						this.sectionSize = sectionSize;
					}
					
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
					
				case 'M' : {
					ModRecord record = new ModRecord();
					
					String 	addrStr = line.substring(1, 7);
					int 	addr = Integer.parseInt(addrStr, 16);
					
					String offsetStr = line.substring(7, 9);
					int offset = Integer.parseInt(offsetStr, 16);
					
					if(line.charAt(9) == '+')
						record.flag = true;
					else if(line.charAt(9) == '-')
						record.flag = false;
					
					record.addr = sectionOffset + addr;
					record.offset = offset;
					record.symbol = line.substring(10).replaceAll("\\s+","");
					
					this.modRecords.add(record);
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
			
			for(ModRecord record : modRecords){
				System.out.println(String.format("%06X %02X %c %s %s", record.addr, record.offset, record.flag?'+':'-', record.symbol, symbolTable.containsKey(record.symbol)));
				
				int size = (int)Math.ceil(record.offset / 2.0f);
				
				int 	val = symbolTable.get(record.symbol);
				byte[] 	valByte = ByteBuffer.allocate(4).putInt(val).array();
				
				byte[] 	buf = rMgr.getMemory(record.addr, size);
				
				for(int i = 1; i <= size; i++){
					if(record.flag == true)
						buf[buf.length - i] += valByte[valByte.length - i];
					else
						buf[buf.length - i] -= valByte[valByte.length - i];
				}
				
				System.out.println(buf);
				rMgr.setMemory(record.addr, buf);
			}
			modRecords.clear();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getProgramName(){
		return this.programName;
	}
	
	public int getStartAddr(){
		return this.startAddr;
	}

	public int getSectionSize(){
		return this.sectionSize;
	}
}
