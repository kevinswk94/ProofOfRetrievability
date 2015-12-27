

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import sg.edu.nyp.sit.svds.client.ida.Util;
import sg.edu.nyp.sit.svds.exception.IDAException;
import sg.edu.nyp.sit.svds.metadata.IdaInfo;

public class TestRabinImpl {

	/*  use dynamically generated matrix to do the split */
	//@Test
	//public void testSplit2() {
	public static void main(String args[])  {
		
		int rowsize = 15;
		int colsize = 12;
		//int rowsize = 20;
		//int colsize = 13;
		//int rowsize = 10;
		//int colsize = 7;
				
		int[][] mat = Util.generateIndependenceMatrix(rowsize,colsize);
		StringBuffer sb = new StringBuffer();
		// convert the matrix into a string 
		for (int row=0;row<rowsize;row++) {
			for (int col=0; col < colsize;col++) {
				sb.append(mat[row][col]);
				if (col < colsize-1)
				sb.append(",");
			}
			if (row < rowsize-1) 
				sb.append("|");
		}
		String matrix = sb.toString();
		System.out.println(matrix);
		
		IInfoDispersal iid = new RabinImpl2();
		
		IdaInfo info=new IdaInfo(rowsize, colsize,matrix );
		
		// open a file 
		//File f = new File("c:/temp/test.doc");
		File f = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg");
		//File f = new File("/Users/markk/Documents/in.txt");
		
		info.setDataSize(f.length());
		
		FileInputStream fis;
	
		try {
			fis = new FileInputStream(f);
		
			long start = System.currentTimeMillis();
			List<InputStream> lsSegmented = iid.split(fis, info);
			long end = System.currentTimeMillis();
			System.out.println("total time taken = " + (end-start));
			System.out.println("Output Segments (" + lsSegmented.size() + "):");
			
			List<InputStream> lsSegmentedWithId = new ArrayList<InputStream>();
			byte index = 0;
			//Needs to add sequence no to each sequence
			for (InputStream inputStream : lsSegmented) {
				lsSegmentedWithId.add(new SequenceInputStream(new ByteArrayInputStream(new byte[] {index++}), inputStream));
			}

			//Rearrange InputStreams
			List<InputStream> lsShuffle = new ArrayList<InputStream>();
			lsShuffle.add(lsSegmentedWithId.get(0)); //random
			lsShuffle.add(lsSegmentedWithId.get(1));
			lsShuffle.add(lsSegmentedWithId.get(2));
			lsShuffle.add(lsSegmentedWithId.get(3)); //random
			lsShuffle.add(lsSegmentedWithId.get(4));
			lsShuffle.add(lsSegmentedWithId.get(5));
			lsShuffle.add(lsSegmentedWithId.get(6));
			lsShuffle.add(lsSegmentedWithId.get(7));
			lsShuffle.add(lsSegmentedWithId.get(8));
			lsShuffle.add(lsSegmentedWithId.get(9));
			lsShuffle.add(lsSegmentedWithId.get(10));
			lsShuffle.add(lsSegmentedWithId.get(11));
			/*
			lsShuffle.add(lsSegmentedWithId.get(12));
			lsShuffle.add(lsSegmentedWithId.get(13));
			lsShuffle.add(lsSegmentedWithId.get(14));
			lsShuffle.add(lsSegmentedWithId.get(15));
			lsShuffle.add(lsSegmentedWithId.get(16)); 
			lsShuffle.add(lsSegmentedWithId.get(17));
			lsShuffle.add(lsSegmentedWithId.get(18));
			lsShuffle.add(lsSegmentedWithId.get(19)); */
			//fis.reset();

			//InputStream isCombined = impl.combine(lsSegmented);
			InputStream isCombined = iid.combine(lsShuffle, info);
			//isCombined.mark(0);
			FileOutputStream fout = new FileOutputStream("C:\\Users\\Public\\Pictures\\Sample Pictures\\Jellyfish2.jpg");
			//FileOutputStream fout = new FileOutputStream("/Users/markk/Documents/out.txt");
			//ByteArrayOutputStream out=new ByteArrayOutputStream();
			int d;
			while((d=isCombined.read())!=-1) {
				//System.out.print(d);
				fout.write(d);
			}
			//System.out.println("Read col combined data len: " + out.size());
			
			fout.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IDAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	@Test
	public void testSplit() {
		
		System.out.println("---- Test Split ---- ");

		int N = 55;
		
		IInfoDispersal iid = new RabinImpl2();
		byte[] ba = new byte[N];
		int nSize = 0;
		int value = 0;
		while(nSize < N) {
			if(value >= 256)
				value = 0;
			ba[nSize++] = (byte)(value++);
		}
		
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(ba);
			bais.mark(0);
			//Print input
			System.out.println("Input:");
			printInputStream(bais);
			bais.reset();

			//IdaInfo info=new IdaInfo(5,3, "1,1,1|1,2,4|1,3,9|1,4,16|1,5,25");
			//IdaInfo info=new IdaInfo(5,3, "40,6,10|80,191,82|8,5,0|81,207,17|149,44,175");
			IdaInfo info=new IdaInfo(10, 7, "127,226,192,22,196,81,132,|85,121,231,228,161,125,44,|42,27,180,208,11,245,247,|231,156,85,9,70,133,142,|204,9,50,156,103,212,199,|64,100,216,28,49,163,79,|202,209,196,224,192,2,105,|139,2,237,111,126,209,252,|199,240,190,80,191,7,204,|83,137,103,193,50,153,144");
			info.setDataSize(N);
			List<InputStream> lsSegmented = iid.split(bais, info);
			System.out.println("Output Segments (" + lsSegmented.size() + "):");
			
			List<InputStream> lsSegmentedWithId = new ArrayList<InputStream>();
			byte index = 0;
			//Needs to add sequence no to each sequence
			for (InputStream inputStream : lsSegmented) {
				lsSegmentedWithId.add(new SequenceInputStream(new ByteArrayInputStream(new byte[] {index++}), inputStream));
			}

			//Rearrange InputStreams
			List<InputStream> lsShuffle = new ArrayList<InputStream>();
			lsShuffle.add(lsSegmentedWithId.get(7)); //random
			lsShuffle.add(lsSegmentedWithId.get(8));
			lsShuffle.add(lsSegmentedWithId.get(9));
			lsShuffle.add(lsSegmentedWithId.get(0));
			lsShuffle.add(lsSegmentedWithId.get(1));
			lsShuffle.add(lsSegmentedWithId.get(2));
			lsShuffle.add(lsSegmentedWithId.get(3));
			
			bais.reset();

			//InputStream isCombined = impl.combine(lsSegmented);
			InputStream isCombined = iid.combine(lsShuffle, info);
			isCombined.mark(0);
			
			System.out.println("Recombined:");
			printInputStream(isCombined);

			//Assert
			isCombined.reset();
			bais.reset();
			Assert.assertTrue(isSame(isCombined, bais));
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	//@Test
	public void testSplit3() {
		System.out.println("---- Test Split ---- ");

		//int N = 55;
		int N = 10;
		
		IInfoDispersal iid = new RabinImpl2();
		byte[] ba = new byte[N];
		int nSize = 0;
		int value = 128;
		while(nSize < (N-1)) {
			if(value >= 256)
				value = 128;
			ba[nSize++] = (byte)(value++);
		}
		System.out.println("data len: "+nSize);
		
		try {
			//Print input
			System.out.println("Input:");
			printByteArray(ba);

			IdaInfo info=new IdaInfo(5,3, "1,1,1|1,2,4|1,3,9|1,4,16|1,5,25");
			//IdaInfo info=new IdaInfo(5,3, "40,6,10|80,191,82|8,5,0|81,207,17|149,44,175");
			//IdaInfo info=new IdaInfo(10, 7, "127,226,192,22,196,81,132,|85,121,231,228,161,125,44,|42,27,180,208,11,245,247,|231,156,85,9,70,133,142,|204,9,50,156,103,212,199,|64,100,216,28,49,163,79,|202,209,196,224,192,2,105,|139,2,237,111,126,209,252,|199,240,190,80,191,7,204,|83,137,103,193,50,153,144");
			info.setDataSize(N);
			
			//prepare the array to pass in
			byte[][] sout=new byte[5][info.getSliceLength()];
			int sliceLen=iid.split(ba, 0, nSize, info, sout, 0, null);
			
			//addition one byte to write the sequence no in front
			byte[][] sin=new byte[info.getQuorum()][sliceLen+1];
			sin[0][0]=0;
			System.arraycopy(sout[0], 0, sin[0], 1, sliceLen);
			sin[1][0]=1;
			System.arraycopy(sout[1], 0, sin[1], 1, sliceLen);
			sin[2][0]=2;
			System.arraycopy(sout[2], 0, sin[2], 1, sliceLen);
			
			System.out.println("Slice 0:");
			printByteArray(sin[0]);
			System.out.println("Slice 1:");
			printByteArray(sin[1]);
			System.out.println("Slice 2:");
			printByteArray(sin[2]);
			
			byte[] result=new byte[N];
			info.setDataOffset(0);
			int dataLen=iid.combine(sin, 0, sliceLen, info, result,0);
			System.out.println("Combined data len: " + dataLen);
			
			System.out.println("Recombined:");
			printByteArray(result);
			
			for(int i=0; i<dataLen; i++){
				if(ba[i]!=result[i])
					fail("Combined result not the same at index " + i);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	private void printByteArray(byte[] is){
		for(byte b: is)
			System.out.print((b  & 0xff) + " ");
		System.out.println();
	}

	@SuppressWarnings("unused")
	private void printFloatInputStream(InputStream is) throws IOException
	{
		System.out.println();
		byte[] ba = new byte[4];
		int read = is.read(ba);
		while(read != -1)
		{
			System.out.print(Util.bytesToFloat(ba) + " ");
			read = is.read(ba);
		}
		System.out.println();
	}
	
	@SuppressWarnings("unused")
	private void printShortInputStream(InputStream is) throws IOException
	{
		System.out.println();
		byte[] ba = new byte[2];
		int read = is.read(ba);
		while(read != -1)
		{
			System.out.print(Util.bytesToShort(ba) + " ");
			read = is.read(ba);
		}
		System.out.println();
	}

	private void printInputStream(InputStream is) throws IOException
	{
		System.out.println();
		int read = is.read();
		while(read != -1)
		{
			System.out.print(read + " ");
			read = is.read();
		}
		System.out.println();
	}
	
	private boolean isSame(InputStream is1, InputStream is2) throws IOException
	{
		int n2;
		int n1 = is1.read();
		while(n1 != -1) {
			n2 = is2.read();
			if(n2 == -1){
				return false;
			}
			
			if(n1 != n2){
				return false;
			}
			
			n1 = is1.read();
		}
		
		return true;
	}
	
//	private void print(InputStream is) throws IOException 
//	{
//		int read = is.read();
//		while(read != -1) {
//			System.out.print(" " + read);
//			read = is.read();
//		}
//		System.out.println();
//	}
}
