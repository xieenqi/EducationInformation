package com.cdeducation.service;

public interface IDataCallBack {

	public void handleHttpResultSucc(int succCode,int what, Object data);
	public void handleHttpResultErr(int errCode, int what, Object data);
}