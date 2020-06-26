#### 入款
1.  首先向管理员获取 merchantId：商户编号，appId：应用ID和，appSecret：秘钥，domain：域名

2.  文档查看
    1.  查看入款接口文档 ：入款接口.txt(包含3个接口下单接口，回调请求参数，查询订单接口)
    2.  下单demo类：PHP代码 DepositDemo.php，Java代码 PayJavaDepositDemoUtil.java
    3.  回调demo类：PHP代码 DepositDemo.php，Java代码 PayJavaDepositDemoUtil.java

3.  下单对接 
    1.  接口地址：http://域名/pay/payChannel/recharge.json
    2.  秘钥需要参与签名
    3.  参数值为空，则不参与签名
    999.    如果有工具类直接使用签名工具类

4.  回调对接
    1.  回调参数查看文档
    2.  秘钥需要参与签名
    3.  参数值为空，则不参与签名
      
5.  订单查询接口
    1.  接口地址：http://域名/pay/payChannel/orderQuery.json
    2.  秘钥需要参与签名
    3.  参数值为空，则不参与签名
    999.    如果有工具类直接使用签名工具类

#### 出款
