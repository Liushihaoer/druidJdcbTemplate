package com.dcsoft.aims.service;

import java.util.List;
import java.util.Map;

import com.dcsoft.aims.utils.Params;


public interface CommonService {
	public static final String SERVICE_NAME = "CommonService";
	
	List<Map<String, Object>> getResultList(Params params) throws Exception;

}
