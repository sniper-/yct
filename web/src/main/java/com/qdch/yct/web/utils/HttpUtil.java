package com.qdch.yct.web.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qdch.yct.web.pojo.QSReqHeader;
import com.qdch.yct.web.pojo.QSReqHeaderInfo;
import com.qdch.yct.web.pojo.QSResHeader;
import com.qdch.yct.web.pojo.QSResHeaderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import qs.common.CommonUtil;
import qs.common.yct.InnerPojo;
import qs.common.yct.ResMsg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

@Component
public class HttpUtil {

    //region client
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //    获取request对象
    public static HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes())
                    .getRequest();
        } catch (Exception e) {
            return null;
        }
    }
    //endregion

    //region request

    /**
     * @Description: 发送json格式的请求数据
     * @Param: [urlString, param]
     * @return: java.lang.String
     * @Author: wallace.yin
     * @Date: 2018/7/24
     */
    public String sendJsonReq(String urlStr, String data) throws Exception {
        boolean flag = CommonUtil.veriUrlConnect(urlStr);
        if (!flag) {
            CommonUtil.writeLog(logger, "连接失败", urlStr);
            return null;
        }
        String resp = "";
        URL url;
        PrintWriter out = null;
        BufferedReader in = null;
        if (StringUtils.isEmpty(urlStr) || StringUtils.isEmpty(data)) {
            return "";
        }
        try {
            StringBuilder result = new StringBuilder();
            url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            out = new PrintWriter(connection.getOutputStream());
            out.print(data);
            out.flush();

            in = new BufferedReader(new InputStreamReader(connection
                    .getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

            resp = result.toString();

        } catch (Exception e) {
            logger.error(CommonUtil.getStackTrace(e));
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return resp;
    }

    //endregion
    //region 初始化请求头信息
    public QSReqHeader initRequestHead(String body) {
        QSReqHeader request = null;
        try {
            body = getHeader(body);
            request = JSON.parseObject(body, QSReqHeader.class);
        } catch (Exception ex) {
            JSONObject json = JSON.parseObject(body);
            JSONObject jsonHeader = json.getJSONObject("reqHdr");
            request = new QSReqHeader();
            QSReqHeaderInfo header = new QSReqHeaderInfo();
            header.setTranCode(jsonHeader.getString("tranCode"));
            header.setResCode(jsonHeader.getString("resCode"));
            header.setResMsg(jsonHeader.getString("resMsg"));
            header.setSerialNo(jsonHeader.getString("serialNo"));
            header.setServerTime(jsonHeader.getString("serverTime"));
            /**
             * 构造resHdr使用toJSONString,构造resBody使用toJSONString,最后一起拼接
             */
            request.setReqHdr(header);
        }
        return request;
    }

    //region 初始化应答头信息
    public String initResponseHead(QSReqHeaderInfo headerInfo, ResMsg resMsg) {
        StringBuilder msg = new StringBuilder();
        msg.append("\"resHdr\":{");
        msg.append("\"securityId\":\"" + headerInfo.getSecurityId() + "\",");
        msg.append("\"tranCode\":\"" + headerInfo.getTranCode() + "\",");
        msg.append("\"resCode\":\"" + resMsg.getRetCode() + "\",");
        msg.append("\"resMsg\":\"" + resMsg.getRetMsg() + "\",");
        msg.append("\"serialNo\":\"\",");
        msg.append("\"serverTime\":\"" + CommonUtil.getCurrentDateString() + "\"}");
        return msg.toString();
    }
    //endregion

    public String getHeader(String body) {
        String endTag = "reqBody";
        StringBuilder header = new StringBuilder();
        try {
            int index = body.indexOf(endTag);
            body = body.substring(0, index);
            //找最后一个逗号
            index = body.lastIndexOf(",");
            body = body.substring(0, index);
            header.append(body);
        } catch (Exception e) {
            logger.error(CommonUtil.getStackTrace(e));
        }
        header.append("}");
        return header.toString();
    }

    public String buildNoSecretResponse(QSReqHeaderInfo headerInfo, InnerPojo pojo) {
        QSResHeaderInfo resHdr = new QSResHeaderInfo(pojo, headerInfo.getSecurityId());
        QSResHeader response = new QSResHeader();
        response.setResHdr(resHdr);
        String responseTmp = JSON.toJSONString(response);
        return responseTmp;
    }

    public String buildResponse(QSReqHeaderInfo headerInfo, InnerPojo innerPojo) {
        String resBody = "{}";
        if (innerPojo.getExpan() != null) {
            resBody = JSONObject.toJSONString(innerPojo.getExpan());
        }
        StringBuilder msg = new StringBuilder();
        msg.append("{\"resHdr\":{");
        msg.append("\"securityId\":\"" + headerInfo.getSecurityId() + "\",");
        msg.append("\"tranCode\":\"" + headerInfo.getTranCode() + "\",");
        msg.append("\"resCode\":\"" + innerPojo.getResCode() + "\",");
        msg.append("\"resMsg\":\"" + innerPojo.getResMsg() + "\",");
        msg.append("\"serialNo\":\"" + CommonUtil.formateString(innerPojo.getSerialNo()) + "\",");
        msg.append("\"bankRtnInfo\":\"" + CommonUtil.formateString(innerPojo.getBankRtnInfo()) + "\",");
        msg.append("\"serverTime\":\"" + CommonUtil.getCurrentDateString() + "\"},");
        msg.append("\"resBody\":" + resBody);
        msg.append("}");
        return msg.toString();
    }

    public String buildFailRes(QSReqHeaderInfo headerInfo, InnerPojo innerPojo) {
        return buildResponse(headerInfo, innerPojo);
    }

    /**
     * @Description: 响应回前端
     * @Param: [response, json]
     * @return: void
     * @Author: wangsw
     * @Date: 2018/8/8
     */
    public void returnJson(HttpServletResponse response, String json) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
