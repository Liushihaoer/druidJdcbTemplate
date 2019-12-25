package com.dcsoft.aims.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dcsoft.aims.common.JSONResponse;
import com.dcsoft.aims.service.CommonService;
import com.dcsoft.aims.utils.Params;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/db")
public class CommonController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	CommonService commonservice;
	
	/* Params参数类demo 必须用双引号包裹,否则解析报错
    {
	    "dbParams":{
	        "target_db_host":"192.168.0.213",
	        "target_db_port":"1521",
	        "target_db_user":"xzykdxfsyy_case_history",
	        "target_db_password":"xzykdxfsyy_case_history",
	        "target_db_type":"oracle",
	        "target_db_name":"orcl"
	    },
	    "sql":"select * from v_his_pk where dept_code = ? and rownum <= ?",
	    "sqlParams":["616", 20]
	}
	*/
	
	@RequestMapping(value = "query", method = RequestMethod.POST)
	public String query(@RequestBody Params params) throws JsonProcessingException {
		try {
			List<Map<String, Object>> resultList = commonservice.getResultList(params);
			return JSONResponse.success(resultList);
		} catch (Exception ex) {
			logger.error("error : CommonController-init()", ex);
			ex.printStackTrace();
			return JSONResponse.fail(ex);
		}
	}

}
