package es.uca.webservices.testgen.metamorphic.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.WSDLException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.xmlbeans.XmlException;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCondition;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TIf;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSource;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSources;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import es.uca.webservices.bpel.InvalidProcessException;
import es.uca.webservices.testgen.autoseed.reader.constants.BPELConstReaderVisitor;
import es.uca.webservices.testgen.autoseed.source.BPELSource;

public class BPELActivityTest {
	
	//BPEL Process
	private BPELSource bpel;
	
	//Path
	private String path;
	
	//ELIMINAR
	//Ruta donde escribirá los archivos auxiliares
	private String path2 = "/home/kevin/Colaboracion/actividades/";
	
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
	public void visitActivities() throws IOException, SAXException, ParserConfigurationException
	{
		//ELIMINAR
		int i = 0;
		List<TActivity> activities = this.bpel.getActivities();
		for(TActivity actividad : activities)
		{
			/*System.out.println("Encontrada actividad "+i);
			i++;*/
			
			//TEST PURPOSES
			/*TSources sources = actividad.getSources();
			if(sources != null) {
				for(TSource source : sources.getSourceArray())
				{
					TCondition expr = source.getTransitionCondition();
					System.out.println("aaa");
					if(expr != null)
					{
						System.out.println("aaa");
						System.out.println(expr);
					}
				}
			}*/
			
			System.out.println("Me llamo "+actividad.getName());
			
			File aux = new File(this.path2 + actividad.getName() + ".txt");
			BufferedWriter imp = new BufferedWriter(new FileWriter(aux));
			imp.write(actividad.toString());
			imp.close();
			
			i++;
			
			accept(actividad);			
		}
	}
	
	//Funciones para visitar cada actividad individualmente
	public void accept(TActivity actividad) throws IOException, SAXException, ParserConfigurationException
	{
		if(actividad instanceof TIf)
		{
			visit((TIf) actividad);
		}
	}
	
	//Funciones para cada tipo
	public void visit(TIf actividad) throws IOException, SAXException, ParserConfigurationException
	{
		//Vamos a imprimir cosas en el fichero
		File auxIf = new File(this.path2 + actividad.getName() + "Condition.txt");
		BufferedWriter impIf = new BufferedWriter(new FileWriter(auxIf));
		impIf.write(actividad.getCondition().xmlText());
		impIf.close();
		
		convert(actividad);
	}
	
	//IDEA LOCA: CONVERTIR EL XML-FRAGMENT A DOCUMENT PARA OBTENER SOLO LA CONDICION
	public void convert(TIf actividad) throws SAXException, IOException, ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		
		Document doc = factory.newDocumentBuilder().parse(new File(this.path2 + actividad.getName() + "Condition.txt"));
		String fragment = actividad.getCondition().toString();
		
		factory = DocumentBuilderFactory.newInstance();
	    Document d = factory.newDocumentBuilder().parse(new InputSource(new StringReader(fragment)));
	    
	    Node node = doc.importNode(d.getDocumentElement(), true);
	    
	    DocumentFragment docfrag = doc.createDocumentFragment();
	    
	    while(node.hasChildNodes())
	    {
	    	docfrag.appendChild(node.removeChild(node.getFirstChild()));
	    }
	    
	    Element element = doc.getDocumentElement();
	    element.appendChild(docfrag);
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
