package es.uca.webservices.testgen.metamorphic.reader;

import java.util.Map;
import java.util.Set;

import es.uca.webservices.testgen.autoseed.reader.constants.BPELConstReaderVisitor;
import es.uca.webservices.testgen.autoseed.source.BPELSource;

public class BPELActivityTest {
	
	//BPEL Process
	private BPELSource bpel;
	
	//Raw constants
	private Map<String, Set<String>> rawConstants;

	public BPELActivityTest(BPELSource bpel)
	{
		this.bpel = bpel;
	}
	
	public BPELSource getBPELSource()
	{
		return bpel;
	}
	
}
