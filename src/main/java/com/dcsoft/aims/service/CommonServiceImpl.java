package com.dcsoft.aims.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.dcsoft.aims.utils.DataSourceUtils;
import com.dcsoft.aims.utils.Params;


@Service(CommonService.SERVICE_NAME)
public class CommonServiceImpl implements CommonService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public List<Map<String, Object>> getResultList(Params params) throws Exception {
		List<Map<String, Object>> resultList = null;
		if (params == null || params.getDbParams() == null) {
			return resultList;
		}
		logger.info("CommonServiceImpl-getResultList() params : " + params.toString());
		JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource(params));
		String sql = params.getSql();
		Object[] sqlParams = params.getSqlParams();
		resultList = jdbcTemplate.queryForList(sql, sqlParams);
		return resultList;
	}

}
