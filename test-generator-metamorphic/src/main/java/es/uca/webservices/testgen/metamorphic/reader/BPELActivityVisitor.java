package es.uca.webservices.testgen.metamorphic.reader;

import es.uca.webservices.testgen.autoseed.source.BPELSource;
import es.uca.webservices.testgen.autoseed.source.BPELSourceUtils;
import es.uca.webservices.xpath.parser.ParseException;
import es.uca.webservices.testgen.autoseed.reader.constants.BPELConstReader;
import es.uca.webservices.bpel.InvalidProcessException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.wsdl.WSDLException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.xmlbeans.XmlException;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TElseif;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TIf;
import org.xml.sax.SAXException;

import java.lang.*;
import java.util.*;


public class BPELActivityVisitor {
	
	public BPELSource bpel;
	public String path = "/home/kevin/Colaboracion/wsbpel-comp-repo/LoanApprovalDoc/LoanApprovalProcess.bpel";
	public String ruta = "/home/kevin/Colaboracion/pruebas.txt";
	public String ruta2 = "/home/kevin/Colaboracion/actividades/pruebas";
	
	public void openFile() throws XPathExpressionException, XmlException, IOException, ParserConfigurationException, SAXException, InvalidProcessException, WSDLException
	{
		BPELActivityVisitor test = new BPELActivityVisitor();
		bpel = new BPELSource(test.path);
	}
	
	public static void main(String[] args) throws XPathExpressionException, XmlException, IOException, ParserConfigurationException, SAXException, InvalidProcessException, WSDLException, ParseException, DatatypeConfigurationException {
		BPELActivityVisitor test = new BPELActivityVisitor();
		test.openFile();
		BPELConstReader lector = new BPELConstReader(test.bpel);
		
		//System.out.println(test.bpel.getActivities());
		//File fic = new File(test.ruta2);
		//BufferedWriter imp = new BufferedWriter(new FileWriter(fic));
		List<TActivity> act = test.bpel.getActivities();
		int i = 0;
		for(TActivity actividad : act)
		{
			File fic = new File(test.ruta2 + i + ".txt");
			BufferedWriter imp = new BufferedWriter(new FileWriter(fic));
			imp.write(actividad.toString());
			//imp.write("---------------TEST--------------");
			imp.close();
			
			
			//Pruebas para comentar
			if(actividad instanceof TIf)
			{
				System.out.println("He encontrado un If!!!!!!!!!!!!!!");
				File cond = new File(test.ruta2 + "If" + i);
				BufferedWriter impcond = new BufferedWriter(new FileWriter(cond));
				impcond.write(((TIf) actividad).getCondition().toString());
				impcond.close();
				
				if(!((TIf) actividad).getElse().isNil()){
					System.out.println("He encontrado un Else!!!!!!!!!!!!!!");
					File cond2 = new File(test.ruta2 + "Else" + i);
					BufferedWriter impcond2 = new BufferedWriter(new FileWriter(cond2));
					impcond2.write(((TIf) actividad).getElse().toString());
					impcond2.close();
				}

			}
			
			i++;
		}
		
		
		
		
		File fichero = new File(test.ruta);
		BufferedWriter impresora;
		impresora = new BufferedWriter(new FileWriter(fichero));
		impresora.write(lector.getConstants().toString());
		impresora.close();
		
		
		System.out.println("Comprobar fichero en "+test.ruta+" y en "+test.ruta2);
	}

}