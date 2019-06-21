#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.vo;

import java.io.IOException;

import org.openspcoop2.utils.sql.JavaxPersistenceSqlGenerator;

public class SqlGenerator {

	public static void main(String[] args) {
		String folder = "../../src/main/resources/database/sql/";
		try {
			JavaxPersistenceSqlGenerator.generate("config", folder);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
}
