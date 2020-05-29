package com.qdch.yct.web.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import qs.common.CommonUtil;
import qs.common.security.SecurityContants;
import qs.common.yct.InnerPojo;
import qs.common.yct.ResMsg;
import qs.dubbo.pay.security.dubboApi.SecurityService;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@ControllerAdvice(basePackages = "com.qdch.yct.web.controller")
public class RequestHandle implements RequestBodyAdvice {
    @Resource
    SecurityService securityService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends
            HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter
            methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter
            methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        boolean encode = false;
        if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
            //获取注解配置的包含和去除字段
            SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
            //入参是否需要解密
            encode = serializedField.inDecode();
        }
        if (encode) {
            return new CustomerHttpInputMessage(httpInputMessage);
        } else {
            return httpInputMessage;
        }
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter
            methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        //身份一致性验证
        Boolean veriResult = ciAuth.handle(String.valueOf(o));
        if (!veriResult) {
            QSReqHeader reqHdr = httpUtil.initRequestHead(String.valueOf(o));
            QSReqHeaderInfo reqHdrInfo = reqHdr.getReqHdr();
            String tranCode = reqHdrInfo.getTranCode();
            String securityId = reqHdrInfo.getSecurityId();
            QSReqHeaderInfo headerInfo = new QSReqHeaderInfo();
            InnerPojo innerPojo = new InnerPojo();
            headerInfo.setSecurityId(securityId);
            headerInfo.setResCode(ResMsg.IDENTITY_ERR.getRetCode());
            headerInfo.setResMsg(ResMsg.IDENTITY_ERR.getRetMsg());
            headerInfo.setTranCode(tranCode);
            headerInfo.setServerTime(CommonUtil.getCurrentDateString());
            innerPojo.setResCode(ResMsg.IDENTITY_ERR.getRetCode());
            innerPojo.setResMsg(ResMsg.IDENTITY_ERR.getRetMsg());
            throw new VerifyFailException(headerInfo, innerPojo);
        }

        veriResult = formatVeri.veri(String.valueOf(o));
        if (!veriResult) {
            QSReqHeader reqHdr = httpUtil.initRequestHead(String.valueOf(o));
            QSReqHeaderInfo reqHdrInfo = reqHdr.getReqHdr();
            String tranCode = reqHdrInfo.getTranCode();
            String securityId = reqHdrInfo.getSecurityId();
            QSReqHeaderInfo headerInfo = new QSReqHeaderInfo();
            InnerPojo innerPojo = new InnerPojo();
            headerInfo.setSecurityId(securityId);
            headerInfo.setResCode(ResMsg.VERI_ERR_DATA.getRetCode());
            headerInfo.setResMsg(ResMsg.VERI_ERR_DATA.getRetMsg());
            headerInfo.setTranCode(tranCode);
            headerInfo.setServerTime(CommonUtil.getCurrentDateString());
            innerPojo.setResCode(ResMsg.VERI_ERR_DATA.getRetCode());
            innerPojo.setResMsg(ResMsg.VERI_ERR_DATA.getRetMsg());
            throw new VerifyFailException(headerInfo, innerPojo);
        }

        Map veriRtnInfo = customerInterceptorControl.handle(methodParameter.getContainingClass().getName(),
                String.valueOf(o));
        ResMsg resMsg = (ResMsg) veriRtnInfo.get("0");
        String merchId = String.valueOf(veriRtnInfo.get("1"));
        String tranCode = String.valueOf(veriRtnInfo.get("2"));
        if (resMsg == null) {
            resMsg = ResMsg.SUCCESS;
        }
        veriResult = ResMsg.SUCCESS.equals(resMsg);
        if (veriResult) {
            return o;
        } else {
            QSReqHeaderInfo headerInfo = new QSReqHeaderInfo();
            InnerPojo innerPojo = new InnerPojo();
            headerInfo.setSecurityId(merchId);
            headerInfo.setResCode(resMsg.getRetCode());
            headerInfo.setResMsg(resMsg.getRetMsg());
            headerInfo.setTranCode(tranCode);
            headerInfo.setServerTime(CommonUtil.getCurrentDateString());
            innerPojo.setResCode(resMsg.getRetCode());
            innerPojo.setResMsg(resMsg.getRetMsg());

            throw new VerifyFailException(headerInfo, innerPojo);
        }
    }

    class CustomerHttpInputMessage implements HttpInputMessage {

        private HttpHeaders headers;

        private InputStream body;

        public CustomerHttpInputMessage(HttpInputMessage inputMessage) {
            this.headers = inputMessage.getHeaders();
            String securityId = null;
            Boolean toThrow = true;
            try {
                String reqBody = IOUtils.toString(inputMessage.getBody(), SecurityContants.ENCODING);
                JSONObject reqBodyJson = JSON.parseObject(reqBody);
                if (reqBodyJson.containsKey("securityId") && reqBodyJson.containsKey("securityData")) {
                    // 商户编号
                    securityId = reqBodyJson.getString(SecurityContants.SECURITY_ID);
                    //jdk版本号
                    String jdkVersion = reqBodyJson.getString(SecurityContants.JDK_VERSION);
                    // 需要解密的密文
                    String cryptograph = reqBodyJson.getString(SecurityContants.SECURITY_DATA);
                    // rsa私钥解密
                    String data = securityService.rsaDecryptByPrivateKey(securityId, cryptograph);
                    if (isSign(jdkVersion)) {
                        if (StringUtils.isNotBlank(data)) {
                            // 获取签名
                            JSONObject dataJson = JSON.parseObject(data);
                            String sign = dataJson.getString(SecurityContants.SIGN);
                            if (StringUtils.isNotBlank(sign)) {
                                // 验签
                                dataJson.remove(SecurityContants.SIGN);
                                data = dataJson.toJSONString();
                                if (securityService.verify(securityId, data, sign)) {
                                    //验签成功
                                    this.body = IOUtils.toInputStream(data, SecurityContants.ENCODING);
                                    toThrow = false;
                                }
                            }
                        }
                    } else {
                        this.body = IOUtils.toInputStream(data, SecurityContants.ENCODING);
                        toThrow = false;
                    }
                } else if (reqBodyJson.containsKey("reqHdr") && reqBodyJson.containsKey("reqBody")) {
                    this.body = IOUtils.toInputStream(reqBody, SecurityContants.ENCODING);
                    toThrow = false;
                }
            } catch (Exception e) {
                logger.error(CommonUtil.getStackTrace(e));
            }
            if (toThrow) {
                QSReqHeaderInfo headerInfo = new QSReqHeaderInfo();
                InnerPojo innerPojo = new InnerPojo();
                headerInfo.setSecurityId(securityId);
                headerInfo.setResCode(ResMsg.SIGN_ERR.getRetCode());
                headerInfo.setResMsg(ResMsg.SIGN_ERR.getRetMsg());
                headerInfo.setTranCode("");
                headerInfo.setServerTime(CommonUtil.getCurrentDateString());
                innerPojo.setResCode(ResMsg.SIGN_ERR.getRetCode());
                innerPojo.setResMsg(ResMsg.SIGN_ERR.getRetMsg());
                throw new VerifyFailException(headerInfo, innerPojo);
            }
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        private boolean isSign(String jdkVersion) {
            //目前仅支持到JDK7
            return SecurityContants.JDK_VERSION_7.equals(jdkVersion) ? FALSE : TRUE;
        }
    }
}

