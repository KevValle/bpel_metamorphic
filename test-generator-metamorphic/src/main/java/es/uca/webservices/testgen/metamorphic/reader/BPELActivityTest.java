package es.uca.webservices.testgen.metamorphic.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.wsdl.WSDLException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.xmlbeans.XmlException;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TBooleanExpr;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCondition;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCopy;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TElseif;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TForEach;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFrom;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TIf;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSource;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSources;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TTo;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWhile;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import es.uca.webservices.bpel.InvalidProcessException;
import es.uca.webservices.testgen.autoseed.reader.constants.BPELConstReaderVisitor;
import es.uca.webservices.testgen.autoseed.source.BPELSource;
import es.uca.webservices.testgen.autoseed.source.XMLUtils;

public class BPELActivityTest {
	
	//BPEL Process
	private BPELSource bpel;
	
	//Path
	private String path;
	
	//ELIMINAR
	//Ruta donde escribirá los archivos auxiliares
	private String path2 = "/home/kevin/Colaboracion/actividades/";
	
	//Fichero donde guardo todas las condiciones
	private File conditionsAux = new File(this.path2 + "Condiciones.txt");
	
	//Raw constants
	//private Map<String, Set<String>> rawConstants;

	public BPELActivityTest(String path)
	{
		this.path = path;
		if(conditionsAux.exists())
		{
			conditionsAux.delete();
		}
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
			}
			
			System.out.println("Me llamo "+actividad.getName());
			
			File aux = new File(this.path2 + actividad.getName() + ".txt");
			BufferedWriter imp = new BufferedWriter(new FileWriter(aux));
			imp.write(actividad.toString());
			imp.close();
			
			i++;
			*/
			accept(actividad);			
		}
	}
	
	//Funciones para visitar cada actividad individualmente
	public void accept(TActivity actividad) throws IOException, SAXException, ParserConfigurationException
	{
		if(actividad instanceof TAssign)
		{
			visit((TAssign) actividad);
		}
		
		if(actividad instanceof TWhile)
		{
			visit((TWhile) actividad);
		}
		
		if(actividad instanceof TIf)
		{
			visit((TIf) actividad);
		}
	}
	
	private void visit(TAssign actividad) throws IOException
	{
		for(TCopy cop : actividad.getCopyArray())
		{
			TFrom from = cop.getFrom();
			TTo to = cop.getTo();
			
			BufferedWriter impCond = new BufferedWriter(new FileWriter(conditionsAux, true));
			impCond.write("Copy from "+XMLUtils.getExpression(from)+" to "+to.getVariable()+"\t"+actividad.getName()+"\n");
			impCond.close();

		}
	}

	public void visit(TWhile actividad) throws IOException 
	{
		getConditionExpression(actividad.getCondition(), actividad.getName());
	}

	//Funciones para cada tipo
	public void visit(TIf actividad) throws IOException, SAXException, ParserConfigurationException
	{
		getConditionExpression(actividad.getCondition(), actividad.getName());
		for (TElseif ei : actividad.getElseifArray()) {
			getConditionExpression(ei.getCondition(), actividad.getName());
		}
	}
	
	
	
	public void getConditionExpression(TBooleanExpr expr, String act) throws IOException
	{
		BufferedWriter impCond = new BufferedWriter(new FileWriter(conditionsAux, true));
		impCond.write(XMLUtils.getExpression(expr)+"\t"+act+"\n");
		impCond.close();
	}
	
	public static void main(String[] args) 
			throws XPathExpressionException, XmlException, IOException, 
			ParserConfigurationException, SAXException, InvalidProcessException, WSDLException
	{
		//Main para probar
		
		//Array con rutas
		String rutas[] = {"/home/kevin/Colaboracion/wsbpel-comp-repo/LoanApprovalDoc/LoanApprovalProcess.bpel",
				"/home/kevin/Colaboracion/triangle/Triangle/Triangle.bpel",
				"/home/kevin/Colaboracion/wsbpel-comp-repo/squaresSum_2/squaresSum_2.bpel"
		};
		
		int r = 0;
		
		//Switch para pruebas, se pueden añadir facilmente rutas
		Scanner s = new Scanner(System.in);
		System.out.println("Rutas disponibles:");
		
		for(int i = 0; i < rutas.length; i++)
		{
			System.out.println("Numero "+i+": "+rutas[i]);
		}
		
		System.out.print("Selecciona el numero de la ruta: ");
		r = s.nextInt();
		
		
		
		BPELActivityTest test = new BPELActivityTest(rutas[r]);
		test.openFile();
		
		test.visitActivities();
		
		System.out.println("Completado");
	}
	
}
