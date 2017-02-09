package com.libray.tools;

/**
 * 服务器函数执行返回false的Exception
 * <p>Title: ExecuteResultException</p>
 * <p>Description: </p>
 * <p>Company: bsj</p>
 * @author zhaolin
 * @date 2013-6-13
 */
public class RemoteExecuteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 构造函数
	 * @param msg 异常信息
	 */
	public RemoteExecuteException(String msg) {
		super(msg);
	}
	
	public RemoteExecuteException() {
		
	}

}
