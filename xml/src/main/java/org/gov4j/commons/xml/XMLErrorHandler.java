package org.gov4j.commons.xml;

import java.io.PrintStream;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLErrorHandler implements ErrorHandler {

	private boolean throwWarningException = true;
	private boolean throwErrorException = true;
	private boolean throwFatalErrorException = true;
	private boolean printStackTraceWarningException = false;
	private boolean printStackTraceErrorException = false;
	private boolean printStackTraceFatalErrorException = false;
	
	private PrintStream warningOutputStream = System.out;
	private PrintStream errorOutputStream = System.err;
	private PrintStream fatalErrorOutputStream = System.err;
	
	XMLErrorHandler(){}
	XMLErrorHandler(boolean throwWarningException,boolean throwErrorException,boolean throwFatalErrorException){
		this(throwWarningException,throwErrorException,throwFatalErrorException,false,false,false);
	}
	XMLErrorHandler(boolean throwWarningException,boolean throwErrorException,boolean throwFatalErrorException,
			boolean printStackTraceWarningException,boolean printStackTraceErrorException, boolean printStackTraceFatalErrorException){
		this.throwWarningException = throwWarningException;
		this.throwErrorException = throwErrorException;
		this.throwFatalErrorException = throwFatalErrorException;
		this.printStackTraceWarningException = printStackTraceWarningException;
		this.printStackTraceErrorException = printStackTraceErrorException;
		this.printStackTraceFatalErrorException = printStackTraceFatalErrorException;
	}
	
	@Override
	public void warning(SAXParseException exception) throws SAXException {
		if(this.printStackTraceWarningException){
			this.warningOutputStream.append("WARNING: "+exception.getMessage());
			exception.printStackTrace(this.warningOutputStream);
		}
		if(this.throwWarningException)
			throw exception;
	}

	@Override
	public void error(SAXParseException exception) throws SAXException {
		if(this.printStackTraceErrorException){
			this.errorOutputStream.append("ERROR: "+exception.getMessage());
			exception.printStackTrace(this.errorOutputStream);
		}
		if(this.throwErrorException)
			throw exception;
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		if(this.printStackTraceFatalErrorException){
			this.fatalErrorOutputStream.append("FATAL ERROR: "+exception.getMessage());
			exception.printStackTrace(this.fatalErrorOutputStream);
		}
		if(this.throwFatalErrorException)
			throw exception;
	}

}
