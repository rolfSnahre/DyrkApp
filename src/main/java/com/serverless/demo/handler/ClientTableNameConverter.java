package com.serverless.demo.handler;

import java.util.Map;

public interface ClientTableNameConverter {
	public Map clientToTable(Map map);
	public Map tableToClient(Map map);
}
