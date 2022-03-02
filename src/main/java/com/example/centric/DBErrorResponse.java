package com.example.centric;
import java.lang.Exception;

public class DBErrorResponse extends Exception{
	private String response = "";
	public DBErrorResponse(String error)
	{
		this.response = error;
	}
}
