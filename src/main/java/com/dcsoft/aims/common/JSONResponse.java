package com.dcsoft.aims.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONResponse {

	public static String success(Object data) throws JsonProcessingException {

		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		map.put("version", String.valueOf("5.0.2"));
		map.put("date", Tools.dateformat(new Date(), null));
		map.put("data", data);
		String result = new ObjectMapper().writeValueAsString(map);

		return result;
	}

	public static String success(Object data,String messageId) throws JsonProcessingException {

		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		map.put("version", String.valueOf("5.0.2"));
		map.put("date", Tools.dateformat(new Date(), null));
		map.put("data", data);
		if(messageId!=null) {
			map.put("messageId", messageId);
		}
		String result = new ObjectMapper().writeValueAsString(map);

		return result;
	}
	
	public static String fail(Exception err) {

		Map<String, Object> map = new HashMap<>();
		map.put("success", false);
		map.put("version", String.valueOf("5.0.2"));
		map.put("date", Tools.dateformat(new Date(), null));
		map.put("errorStack", err.getStackTrace());

		String result = "";
		try {
			result = new ObjectMapper().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
