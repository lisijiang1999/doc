<?php
  // 入款接口请求地址
  $rechargeUrlAddress = 'https://tiantianpay.co/pay/payChannel/recharge.json?';
  // 应用ID ：去商户系统申请
  $appId = 'd7024830f2294e92afd25a86149ed0e5';
  // 商户ID ：去商户系统查看
  $merchantId = '98000064';
  // 商户私钥 平台获取
  $appSecret = '23478dc22bdd6fdc718daa099f64d4a3';

  // 入款加密接口参数
  $urlParamMap = [
    "appId" => $appId,
    "merchantId" => $merchantId,
    // 商户订单号
    "outTradeNo" => "K8ZDG7JOOVEYLKODP46U",
    // 随机字符串 要求 4位数据
    "nonceStr" => "8249",
    // 回调地址
    "notifyUrl" => "http://119.9.84.144:2002/api/lmp/Third/tiantianNotify.html?t=5093",
    // 下单数量（交易币种）
    "unitNameAmount" => "200",
    // 要求支付类型
    "payType" => "1",
    // 订单备注信息
    "remark" => "会员ID"
  ];

  // 加密函数
  function generateSignature($array, $appSecret) {
    //排序 $array
    ksort($array);
    print_r($array);
    echo "<br>";
    // array 转 查询连接
    $queryStr = http_build_query($array);
    echo $queryStr;
    echo "<br>";
    // $appSecret 也要参与加密
    $queryStr1 = urldecode($queryStr)."&key=$appSecret";
    echo $queryStr1;
    echo "<br>";
    // 转换为大写
    $sign = strtoupper(hash_hmac('sha256', $queryStr1, $appSecret, false));
    print_r("sign=$sign");
    return $sign;
  };

  // 请求下单接口
  function httpPost($url, $urlParamMap, $appSecret) {
      $curl = curl_init(); //初始化
      curl_setopt($curl, CURLOPT_URL, $url); //设置抓取的url
      curl_setopt($curl, CURLOPT_HEADER, 0); //设置为0不返回请求头信息
      curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1); //设置获取的信息以文件流的形式返回，而不是直接输出。
      curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, FALSE); // 跳过https请求 不验证证书和hosts
      curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, FALSE);
      curl_setopt($curl, CURLOPT_POST, 1); //设置post方式提交

      // 签名
      $sign = generateSignature($urlParamMap, $appSecret);
      $temp = [
          "sign" => $sign
      ];
      $result = array_merge_recursive($urlParamMap, $temp);

      curl_setopt($curl, CURLOPT_POSTFIELDS, $result); //设置post数据，
      $data = curl_exec($curl); //执行命令
      curl_close($curl); //关闭URL请求
      return $data; //返回获得的数据
  };

  // 回调接参方法
  function callback($_POST, $appSecret) {
      // 回调参数
      $requestParamMap = [
        "appId" => $_POST['appId'],
        "merchantId" => $_POST['merchantId'],
        "outTradeNo" => $_POST['outTradeNo'], // 商户订单号
        "orderId" => $_POST['orderId'], // 平台订单号
        "unitAmt" => $_POST['unitAmt'], // 币种金额
        "payOpt" => $_POST['payOpt'], // 支付类型 1
        "createdTime" => $_POST['createdTime'], // 10时间戳
        "confirmTime" => $_POST['confirmTime'], // 10时间戳
        "nonceStr" => $_POST['nonceStr'], // 随机数
      ];

      // 签名
      $sign = generateSignature($requestParamMap, $appSecret);

      $requestSign = request.getParameter("sign"); // 签名

      if (strcmp($requestSign, $sign) == 0) {
        // 相等  处理订单
      }

  };

  echo httpPost($rechargeUrlAddress, $urlParamMap, $appSecret);

?>
