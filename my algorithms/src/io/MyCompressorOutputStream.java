package io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MyCompressorOutputStream  extends OutputStream{

	
	private OutputStream out;
	
	public MyCompressorOutputStream(OutputStream out) {
		this.out = out;
	}
	
	@Override
	public void write(int b) throws IOException {
		
	}
	
	public void write(byte[] array) throws IOException{
		DataOutputStream data = new DataOutputStream(out);
		int counter = 0;
		byte num = array[0];
	
	for (byte b : array) {
		if(b == num){
		   counter++;	
		}
		else{	
		data.writeByte(num);
		data.writeInt(counter);
		num = b;
		counter = 1;	
		}
		
	}
	
	data.writeByte(num);
	data.writeInt(counter);
		
	}
	
}
