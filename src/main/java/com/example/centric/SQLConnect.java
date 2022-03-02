package com.example.centric;

import java.util.ArrayList;

public class SQLConnect {
	public SQLConnect() {}
	
	public void insertValue(ArrayList<String> vals) {
		String q = String.format("insert into Products Values(\r\n"
				+ "{}"
				+ "{}"
				+ "{}"
				+ "{}"
				+ "{}"
				+ "{}"
				+ ");",vals);
	}
	
	public void getValue(String item, String table)
	{
		String q = String.format("select {} into {}", item,table);
	}
}
