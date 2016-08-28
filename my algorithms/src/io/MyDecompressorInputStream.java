package io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyDecompressorInputStream extends InputStream {

	private InputStream in;
	
	public MyDecompressorInputStream(InputStream in) {
		this.in = in;
	}
	
	@Override
	public int read() throws IOException {
		
		return 0;
	}
	
	
	public int read(byte[] array) throws IOException{
		
		DataInputStream data = new DataInputStream(in);
		
		byte num;
		int i = 0;
		int index;
		
		while (data.available() > 4 && i < array.length){
			num = data.readByte();
			index = data.readInt();
			index += i;

			for (;i < index; i++) {
				array[i] = num ;

			}

		}

		
		return array.length;
	}

}