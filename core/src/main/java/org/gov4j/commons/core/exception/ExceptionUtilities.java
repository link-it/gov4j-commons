package org.gov4j.commons.core.exception;

public class ExceptionUtilities {

	public static String readFirstErrorValidMessageFromException(Throwable e) {
		if(e instanceof NullPointerException) {
			 return "NullPointerException";
		}else {
			Throwable inner = ExceptionUtilities.getInnerNotEmptyMessageException(e);
			if(inner!=null) {
				return inner.getMessage();
			}
			else {
				if(ExceptionUtilities.isEmpytMessageException(e)) {
					return e.toString();
				}
				else {
					return e.getMessage();
				}
			}
		}
	}
	
	public static boolean isEmpytMessageException(Throwable e){
		if(e.getMessage()==null ||
				"".equals(e.getMessage()) || 
				"null".equalsIgnoreCase(e.getMessage()) ){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean existsInnerException(Throwable e,Class<?> found){
		return ExceptionUtilities.existsInnerException(e,found.getName());
	}
	public static boolean existsInnerException(Throwable e,String found){
		//System.out.println("ANALIZZO ["+e.getClass().getName()+"] ("+found+")");
		if(e.getClass().getName().equals(found) ){
			//System.out.println("OK ["+e.getClass().getName()+"]");
			return true;
		}else{
			if(e.getCause()!=null){
				//System.out.println("INNER ["+e.getClass().getName()+"]...");
				return ExceptionUtilities.existsInnerException(e.getCause(), found);
			}
			else{
				//System.out.println("ESCO ["+e.getClass().getName()+"]");
				return false;
			}
		}
	}

	public static Throwable getInnerException(Throwable e,Class<?> found){
		return ExceptionUtilities.getInnerException(e,found.getName());
	}
	public static Throwable getInnerException(Throwable e,String found){
		//System.out.println("ANALIZZO ["+e.getClass().getName()+"] ("+found+")");
		if(e.getClass().getName().equals(found) ){
			//System.out.println("OK ["+e.getClass().getName()+"]");
			return e;
		}else{
			if(e.getCause()!=null){
				//System.out.println("INNER ["+e.getClass().getName()+"]...");
				return ExceptionUtilities.getInnerException(e.getCause(), found);
			}
			else{
				//System.out.println("ESCO ["+e.getClass().getName()+"]");
				return null;
			}
		}
	}

	public static Throwable getLastInnerException(Throwable e){
		if(e.getCause()==null){
			return e;
		}
		else{
			return ExceptionUtilities.getLastInnerException(e.getCause());
		}
	}

	public static boolean existsInnerMessageException(Throwable e,String msg,boolean contains){
		//System.out.println("ANALIZZO ["+e.getClass().getName()+"] ("+found+")");
		boolean search = false;
		if(contains){
			search = e.getMessage()!=null && e.getMessage().contains(msg);
		}else{
			search = e.getMessage()!=null && e.getMessage().equals(msg);
		}
		if( search ){
			//System.out.println("OK ["+e.getClass().getName()+"]");
			return true;
		}else{
			if(e.getCause()!=null){
				//System.out.println("INNER ["+e.getClass().getName()+"]...");
				return ExceptionUtilities.existsInnerMessageException(e.getCause(), msg, contains);
			}
			else{
				//System.out.println("ESCO ["+e.getClass().getName()+"]");
				return false;
			}
		}
	}

	public static Throwable getInnerMessageException(Throwable e,String msg,boolean contains){
		//System.out.println("ANALIZZO ["+e.getClass().getName()+"] ("+found+")");
		boolean search = false;
		if(contains){
			search = e.getMessage()!=null && e.getMessage().contains(msg);
		}else{
			search = e.getMessage()!=null && e.getMessage().equals(msg);
		}
		if( search ){
			//System.out.println("OK ["+e.getClass().getName()+"]");
			return e;
		}else{
			if(e.getCause()!=null){
				//System.out.println("INNER ["+e.getClass().getName()+"]...");
				return ExceptionUtilities.getInnerMessageException(e.getCause(), msg, contains);
			}
			else{
				//System.out.println("ESCO ["+e.getClass().getName()+"]");
				return null;
			}
		}
	}


	public static Throwable getInnerNotEmptyMessageException(Throwable e){
		//System.out.println("ANALIZZO ["+e.getClass().getName()+"] ("+e.getMessage()+")");
		if(e.getMessage()!=null && !"".equals(e.getMessage()) && !"null".equalsIgnoreCase(e.getMessage())){
			return e;
		}

		if(e.getCause()!=null){
			//System.out.println("INNER ["+e.getClass().getName()+"]...");
			return ExceptionUtilities.getInnerNotEmptyMessageException(e.getCause());
		}
		else{
			return e; // sono nella foglia, ritorno comunque questa eccezione
		}
	}


	public static boolean isExceptionInstanceOf(Class<?> c,Throwable t){
		return isExceptionInstanceOf(c.getName(), t);
	}
	public static boolean isExceptionInstanceOf(String className,Throwable t){
		if(t.getClass().getName().equals(className)){
			return true;
		}
//		else if(t.getClass().getSuperclass()!=null && t.getClass().getSuperclass().equals(className)){
//			return true;
//		}
		else{
			try{
				Class<?> c = Class.forName(className); 
				return c.isInstance(t);
			}catch(Throwable tException){
				return false;
			}
		}
	}
	
}
