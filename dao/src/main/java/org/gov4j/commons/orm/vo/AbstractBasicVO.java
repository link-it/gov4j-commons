package org.gov4j.commons.orm.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class AbstractBasicVO implements Serializable,IBasicVO {
	
	private static final long serialVersionUID = 1L;
	
	public AbstractBasicVO() {}
	
	@Override
	public void updateTableIdsFrom(IBasicVO vo) {
		if(vo==null) {
			return;
		}
		this.setId(vo.getId());
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE, false);
	}
}
