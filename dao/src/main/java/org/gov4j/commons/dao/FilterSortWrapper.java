package org.gov4j.commons.dao;

public class FilterSortWrapper {

	private String joinTable;
	private String field;
	private Boolean sortOrderAsc;
	public String getJoinTable() {
		return this.joinTable;
	}
	public void setJoinTable(String joinTable) {
		this.joinTable = joinTable;
	}
	public String getField() {
		return this.field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Boolean getSortOrderAsc() {
		return this.sortOrderAsc;
	}
	public void setSortOrderAsc(Boolean sortOrderAsc) {
		this.sortOrderAsc = sortOrderAsc;
	}
	
}
