package org.gov4j.commons.xml;

import java.io.PrintStream;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

public class XMLErrorListener implements ErrorListener {

	private boolean throwWarningException = true;
	private boolean throwErrorException = true;
	private boolean throwFatalErrorException = true;
	private boolean printStackTraceWarningException = false;
	private boolean printStackTraceErrorException = false;
	private boolean printStackTraceFatalErrorException = false;
	
	private PrintStream warningOutputStream = System.out;
	private PrintStream errorOutputStream = System.err;
	private PrintStream fatalErrorOutputStream = System.err;
	
	XMLErrorListener(){}
	XMLErrorListener(boolean throwWarningException,boolean throwErrorException,boolean throwFatalErrorException){
		this(throwWarningException,throwErrorException,throwFatalErrorException,false,false,false);
	}
	XMLErrorListener(boolean throwWarningException,boolean throwErrorException,boolean throwFatalErrorException,
			boolean printStackTraceWarningException,boolean printStackTraceErrorException, boolean printStackTraceFatalErrorException){
		
	}
	
	@Override
	public void warning(TransformerException exception)
			throws TransformerException {
		
		if(this.printStackTraceWarningException){
			this.warningOutputStream.append("WARNING: "+exception.getMessage());
			exception.printStackTrace(this.warningOutputStream);
		}
		if(this.throwWarningException)
			throw exception;	
		
	}
	@Override
	public void error(TransformerException exception)
			throws TransformerException {
		
		if(this.printStackTraceErrorException){
			this.errorOutputStream.append("ERROR: "+exception.getMessage());
			exception.printStackTrace(this.errorOutputStream);
		}
		if(this.throwErrorException)
			throw exception;
		
	}
	@Override
	public void fatalError(TransformerException exception)
			throws TransformerException {
		
		if(this.printStackTraceFatalErrorException){
			this.fatalErrorOutputStream.append("FATAL ERROR: "+exception.getMessage());
			exception.printStackTrace(this.fatalErrorOutputStream);
		}
		if(this.throwFatalErrorException)
			throw exception;
		
	}

}
