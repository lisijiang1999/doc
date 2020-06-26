package com.payment.http.pay.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 支付 入款接口
 */
public class PayJavaDepositDemoUtil {

  // 购买币种 .json接口地址
  private static String rechargeUrlAddress = "https://tiantianpay.co/pay/payChannel/recharge.json?";
  // 应用ID ：去商户系统申请
  private static String appId = "d7024830f2294e92afd25a86149ed0e5";
  // 商户ID ：去商户系统查看
  private static String merchantId = "98000064";

  // 入款接口
  public static void sendRechargePost() {
    Map<String, String> urlParamMap = new HashMap<>();
    urlParamMap.put("appId", appId);
    urlParamMap.put("merchantId", merchantId);
    // 商户订单号
    urlParamMap.put("outTradeNo", "OrderId:91726178162");
    // 随机字符串 要求 4位数据
    urlParamMap.put("nonceStr", "hdfs");
    // 回调地址
    urlParamMap.put("notifyUrl", "http://www.baidu.com");
    // 下单数量
    urlParamMap.put("unitNameAmount", "100");
    // 要求支付类型
    urlParamMap.put("payType", "1");
    // 订单备注信息
    urlParamMap.put("remark", "会员ID");


    // 签名工具 签名参数
    String sign = generateSignature(urlParamMap, "23478dc22bdd6fdc718daa099f64d4a3");

    // 签名（HMAC-SHA256）
    urlParamMap.put("sign", sign);

    PrintWriter out = null;
    BufferedReader in = null;
    String result = "";
    try {
      URL realUrl = new URL(rechargeUrlAddress);
      // 打开和URL之间的连接
      URLConnection conn = realUrl.openConnection();
      // 设置通用的请求属性
      conn.setRequestProperty("accept", "*/*");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent",
          "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      // conn.setRequestProperty("Charset", "UTF-8");
      // 发送POST请求必须设置如下两行
      conn.setDoOutput(true);
      conn.setDoInput(true);
      // 获取URLConnection对象对应的输出流
      out = new PrintWriter(conn.getOutputStream());

      // 设置请求属性
      StringBuffer sb = new StringBuffer();
      if (urlParamMap != null && urlParamMap.size() > 0) {
        Iterator<String> ite = urlParamMap.keySet().iterator();
        while (ite.hasNext()) {
          String key = ite.next();// key
          Object value = urlParamMap.get(key);
          String param = key + "=" + value + "&";
          sb.append(param);
        }
        // 去掉最后 &
        String requestParam = sb.substring(0, sb.length() - 1);

        // 发送请求参数
        out.print(requestParam);
        // flush输出流的缓冲
        out.flush();
        // 定义BufferedReader输入流来读取URL的响应
        in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        String line;
        while ((line = in.readLine()) != null) {
          result += line;
        }
      }

      // 打印结果
      System.out.println(result);

    } catch (Exception e) {
      System.err.println("发送 POST 请求出现异常！" + e);
      e.printStackTrace();
    } finally { // 使用finally块来关闭输出流、输入流
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
   *
   * @param data 待签名数据
   * @param appSecret API密钥
   * @return 签名
   */
  public static String generateSignature(Map<String, String> data, String appSecret) {

    return generateSignature(new TreeMap<>(data), appSecret);
  }

  /**
   * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
   *
   * @param data 待签名数据 TreeMap 可以按照Key的UNICODE码排序
   * @param appSecret API密钥
   * @return 签名
   */
  public static String generateSignature(final TreeMap<String, String> data, String appSecret) {
    StringBuilder sb = new StringBuilder();

    Set<String> keySet = data.keySet();
    Iterator<String> iter = keySet.iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      if ("sign".equals(key)) {
        continue;
      }
      if (key.trim().length() > 0 && data.get(key).trim().length() > 0) { // 参数值为空，则不参与签名
        sb.append(key).append("=").append(data.get(key).trim()).append("&");
      }
    }
    sb.append("key=").append(appSecret);
    System.out.println("generateSignature ==> " + sb.toString());

    return HMACSHA256(sb.toString(), appSecret);
  }

  /**
   * 生成 HMACSHA256
   *
   * @param data 待处理数据
   * @param appSecret 密钥
   * @return 加密结果
   */
  public static String HMACSHA256(String data, String appSecret) {

    Mac sha256_HMAC = null;
    StringBuilder sb = new StringBuilder();

    try {
      sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secret_key = new SecretKeySpec(appSecret.getBytes("UTF-8"), "HmacSHA256");
      sha256_HMAC.init(secret_key);
      byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

      for (byte item : array) {
        sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return sb.toString().toUpperCase();
  }

  public static void main(String[] args) throws Exception {

  }

}
