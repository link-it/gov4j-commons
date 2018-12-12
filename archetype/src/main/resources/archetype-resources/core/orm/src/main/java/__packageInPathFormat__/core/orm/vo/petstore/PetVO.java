#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.core.orm.vo.petstore;

import org.gov4j.commons.orm.vo.AbstractBasicVO;
import ${package}.core.orm.vo.petstore.constants.PetAnimal;


public class PetVO extends AbstractBasicVO {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	private String tag;
	private PetAnimal animal;
	
	@Override
	public Long getId() {
		return this.id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTag() {
		return this.tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public PetAnimal getAnimal() {
		return this.animal;
	}
	public void setAnimal(PetAnimal animal) {
		this.animal = animal;
	}
}
