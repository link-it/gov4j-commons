#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config.${parentArtifactId}.${artifactId};

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

public class Helper {
	
	@FunctionalInterface
	public interface ThrowingSupplier<T> {
		
	    T get() throws Exception;
	}
	
	
	public interface ThrowingRunnable {
		
		void run() throws Exception;
	}
	
	
	public static final<T> T dropnull(ThrowingSupplier<T> r) {
		try {
			return r.get();
		} catch (NullPointerException e) {
			return null;
		} catch( Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static final <T> T evalnull(ThrowingSupplier<T> r) {
		try {
			return r.get();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public static final <T> T evalorElse(ThrowingSupplier<T> r, T orElse) {
		T ret = null;
		try {
			ret = r.get();	
		} catch (Exception e) {
			// Ignoring Exception
		}
		if (ret != null) return ret;
		else return orElse;
	}

	
	public static final void runNull(ThrowingRunnable r) {
		try {
			r.run();
		} catch (NullPointerException e) {
			// Ignore
		} catch ( Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static <T> Optional<T> findFirst(Iterable<? extends T> collection, Predicate<? super T> test) {
	    T value = null;
	    if (collection != null ) {
		    for (Iterator<? extends T> it = collection.iterator(); it.hasNext();)
		        if (test.test(value = it.next())) {
		            return Optional.of(value);
		        }
	    }
	    return Optional.empty();
	}
	

	public static <T> T findAndRemoveFirst(Iterable<? extends T> collection, Predicate<? super T> test) {
	    T value = null;
	    for (Iterator<? extends T> it = collection.iterator(); it.hasNext();)
	        if (test.test(value = it.next())) {
	            it.remove();
	            return value;
	        }
	    return null;
	}
	

}
