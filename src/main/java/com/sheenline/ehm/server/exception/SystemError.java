package com.sheenline.ehm.server.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Shoddon
 *
 */
public enum SystemError implements IError {
	// system start
	// prefix encoding is '00'
	SYSTEM_INNER_ERROR(SystemError.SYSTEM_PREFIX + "000000","服务中心内部错误"), 
	SYSTEM_REQUEST_METHOD_ERROR(SystemError.SYSTEM_PREFIX + "000001","请求方法错误"), 
	SYSTEM_DATA_INSERT_ERROR(SystemError.SYSTEM_PREFIX + "000002","数据插入失败"),
	SYSTEM_OTHER(SystemError.SYSTEM_PREFIX + "999999", "... ..."),
	
	// system end
	// prefix encoding is '00'
	
	// business start
	// prefix encoding is '01'
	BUSINESS_DEVICENO_ISNULL_ERROR(SystemError.BUSINESS_PREFIX + "000100","设备编号为空"),
	BUSINESS_DEVICE_NOEXIST_ERROR(SystemError.BUSINESS_PREFIX + "000101","设备不存在"),
	BUSINESS_STATUSCODE_ISNULL_ERROR(SystemError.BUSINESS_PREFIX + "000102","状态码为空"),
	BUSINESS_STATUS_NOEXIST_ERROR(SystemError.BUSINESS_PREFIX + "000103","状态码为空"),
	BUSINESS_DATA_NOEXIST_ERROR(SystemError.SYSTEM_PREFIX + "000104","数据不存在"),
	BUSINESS_PARAM_ERROR(SystemError.SYSTEM_PREFIX + "000105","参数非法"),
	BUSINESS_OTHER(SystemError.BUSINESS_PREFIX + "999998", "... ...");
	
	// business end
	// prefix encoding is '01'

	///////////////////////////////////////////////////////////////////////////
	public static final String SYSTEM_PREFIX = "00-00-";
	public static final String BUSINESS_PREFIX = "00-01-";

	private String code;

	private String desc;

	private String extDesc;

	private static Map<String, IError> errorMap = new HashMap<String, IError>();

	static {
		IError[] e = SystemError.values();
		for (IError error : e) {
			errorMap.put(error.getCode(), error);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(errorMap);
	}

	private SystemError(String code, String desc){
		this.code = code;
		this.desc = desc;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the extDesc
	 */
	public String getExtDesc() {
		return extDesc;
	}

	/**
	 * @param extDesc
	 *            the extDesc to set
	 */
	public void setExtDesc(String extDesc) {
		this.extDesc = extDesc;
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static IError getErrorByCode(String code) {
		return SystemError.errorMap.get(code);
	}

	/**
	 * 
	 * @param code
	 */
	public static void addErrors(IError error) {
		if (error != null) {
			errorMap.put(error.getCode(), error);
		}
	}

	/**
	 * 
	 * @param errors
	 */
	public static void addErrors(IError[] errors) {
		if (errors == null) {
			return;
		}
		for (IError error : errors) {
			if (error != null) {
				errorMap.put(error.getCode(), error);
			}
		}
	}
}
