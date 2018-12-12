#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.core.dao.petstore.filters;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.gov4j.commons.dao.filters.AbstractWriter;
import ${package}.core.orm.vo.petstore.PetVO;
import ${package}.core.orm.vo.petstore.constants.PetAnimal;
import ${package}.core.orm.vo.petstore.model.PetModel;

public class PetFilter extends AbstractWriter<PetVO> {

	public PetFilter() {
		super(PetVO.class);	
	}

	private String name;
	private String tag;
	private PetAnimal animal;

	@Override
	protected List<Predicate> toWhere(CriteriaBuilder criteriaBuilder, Root<PetVO> from) {
		
		List<Predicate> predLst = new ArrayList<Predicate>();
				
		if(this.id != null) {
			predLst.add(criteriaBuilder.equal(from.get(PetModel.ID), this.id));
		}
		
		if(this.name != null) {
			predLst.add(criteriaBuilder.equal(from.get(PetModel.NAME), this.name));
		}
		
		if(this.tag != null) {
			predLst.add(criteriaBuilder.equal(from.get(PetModel.TAG), this.tag));
		}
		
		if(this.animal != null) {
			predLst.add(criteriaBuilder.equal(from.get(PetModel.ANIMAL), this.animal));
		}
		
		return predLst;
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
