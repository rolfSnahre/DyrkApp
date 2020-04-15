package com.serverless.demo.handler;

import java.util.Map;

public class ElementFormat implements ClientTableNameConverter{

	@Override
	public Map clientToTable(Map map) {
		map.put("sort", map.get("date"));
		map.remove("date");
		return map;
	}

	@Override
	public Map tableToClient(Map map) {
		map.put("date", map.get("sort"));
		map.remove("sort");
		return map;
	}
}
