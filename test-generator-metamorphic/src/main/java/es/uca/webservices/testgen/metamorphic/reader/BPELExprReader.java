package es.uca.webservices.testgen.metamorphic.reader;

import java.io.*;

public class BPELExprReader {
	public static void main(String[] args) {
		File archivo = null;
		FileReader fBPEL = null;
		BufferedReader br = null;
		
		try {
			//Se abre el fichero y se crea el buffer para leerlo
			archivo = new File("/home/kevin/Colaboracion/LoanApprovalProcess.bpel");
			fBPEL = new FileReader(archivo);
			br = new BufferedReader(fBPEL);
			
			//Lectura
			String linea;
			while((linea=br.readLine()) != null)
				System.out.println(linea);
		} catch(Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if( null != fBPEL ){
					fBPEL.close();
				}
			}catch (Exception e2){
				e2.printStackTrace();
			}
		}
	}
}