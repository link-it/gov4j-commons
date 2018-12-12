#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.core.orm.vo;

import java.io.IOException;

public class SqlGenerator {

	public static void main(String[] args) {
		String folder = "../../src/main/resources/database/sql/";
		try {
			org.gov4j.commons.orm.vo.SqlGenerator.generate("petstore", folder);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
}
