package com.dcsoft.aims.utils;

import java.util.Arrays;
import java.util.Map;

/**
 * 定义从手麻程序传递过来的参数
 *
 */
public class Params {
	
	// 数据库连接信息
	private Map<String, String> dbParams;
	// 查询sql
	private String sql;
	// sql语句查询条件
	private Object[] sqlParams;
	
	public Map<String, String> getDbParams() {
		return dbParams;
	}
	public void setDbParams(Map<String, String> dbParams) {
		this.dbParams = dbParams;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Object[] getSqlParams() {
		return sqlParams;
	}
	public void setSqlParams(Object[] sqlParams) {
		this.sqlParams = sqlParams;
	}
	
	@Override
	public String toString() {
		return "Params [dbParams=" + dbParams + ", sql=" + sql + ", sqlParams=" + Arrays.toString(sqlParams) + "]";
	}
}
