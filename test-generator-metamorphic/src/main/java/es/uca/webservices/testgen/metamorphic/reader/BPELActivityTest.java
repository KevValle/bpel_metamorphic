package es.uca.webservices.testgen.metamorphic.reader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.WSDLException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.xmlbeans.XmlException;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.xml.sax.SAXException;

import es.uca.webservices.bpel.InvalidProcessException;
import es.uca.webservices.testgen.autoseed.reader.constants.BPELConstReaderVisitor;
import es.uca.webservices.testgen.autoseed.source.BPELSource;

public class BPELActivityTest {
	
	//BPEL Process
	private BPELSource bpel;
	
	//Path
	private String path;
	
	//Raw constants
	//private Map<String, Set<String>> rawConstants;

	public BPELActivityTest(String path)
	{
		this.path = path;
	}
	
	public BPELSource getBPELSource()
	{
		return bpel;
	}
	
	//Función que abre el proyecto bpel dada una ruta
	public void openFile() 
		throws XPathExpressionException, XmlException, IOException, ParserConfigurationException, 
		SAXException, InvalidProcessException, WSDLException
	{
		this.bpel = new BPELSource(this.path);
	}
	
	//Obtiene las actividades del fichero bpel que está abierto en la propia clase
	public void visitActivities()
	{
		//ELIMINAR
		int i = 0;
		List<TActivity> activities = this.bpel.getActivities();
		for(TActivity actividad : activities)
		{
			System.out.println("Encontrada actividad "+i);
			i++;
		}
	}
	
	public static void main(String[] args) 
			throws XPathExpressionException, XmlException, IOException, 
			ParserConfigurationException, SAXException, InvalidProcessException, WSDLException
	{
		//Este main está aquí solo para probar
		String ruta = "/home/kevin/Colaboracion/wsbpel-comp-repo/LoanApprovalDoc/LoanApprovalProcess.bpel";
		BPELActivityTest test = new BPELActivityTest(ruta);
		test.openFile();
		
		test.visitActivities();
	}
	
}
