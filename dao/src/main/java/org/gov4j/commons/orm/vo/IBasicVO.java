package org.gov4j.commons.orm.vo;

public interface IBasicVO {

	public Long getId();
	public void setId(Long id);
	
	public void updateTableIdsFrom(IBasicVO vo);
	
}
