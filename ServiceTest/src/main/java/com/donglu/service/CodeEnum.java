package com.donglu.service;

public enum CodeEnum {
	setServiceIp((byte)0xf9,4);
	private byte code;
	private int length;

	CodeEnum(byte code,int length){
		this.code = code;
		this.length = length;
	}

	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
