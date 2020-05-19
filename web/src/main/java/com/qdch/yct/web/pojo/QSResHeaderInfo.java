package com.qdch.yct.web.pojo;

import qs.common.yct.InnerPojo;

/*
 * 清算响应头信息
 * */
public class QSResHeaderInfo {
    private String securityId;
    private String tranCode;
    private String resCode;
    private String resMsg;
    private String serialNo;
    private String serverTime;
    private String bankRtnInfo;

    public QSResHeaderInfo(InnerPojo innerPojo, String arg1) {
        initInnerPojo(innerPojo);
        this.securityId = arg1;
    }

    public String getSecurityId() {
        return securityId;
    }

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }

    private void initInnerPojo(InnerPojo innerPojo) {
        this.tranCode = innerPojo.getTranCode();
        this.resCode = innerPojo.getResCode();
        this.resMsg = innerPojo.getResMsg();
        this.serialNo = innerPojo.getSerialNo();
        this.serverTime = innerPojo.getServerTime();
        this.bankRtnInfo = innerPojo.getBankRtnInfo();
    }

    public String getBankRtnInfo() {
        return bankRtnInfo;
    }

    public void setBankRtnInfo(String bankRtnInfo) {
        this.bankRtnInfo = bankRtnInfo;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}
