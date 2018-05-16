package com.eland.basepay.component.pays.ali;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.eland.basepay.component.model.OrderInfo;
import com.eland.basepay.component.pays.IPayable;

public class AliPay implements IPayable {
	
	/**
	 * 支付
	 * @param activity 支付页面activity
	 * @param orderInfo 规范的订单信息
	 * @param prepayId 预付单号（微信）
	 * @return
	 */
	public String Pay(Activity activity,OrderInfo orderInfo,String prepayId){
		PayTask payTask = new PayTask(activity);
		Log.e("tag", "阿里支付的订单："+orderInfo.GetContent());
		String result=payTask.pay(orderInfo.GetContent(),true);
		return result;
	}
	
	/**
	 * 生成订单参数
	 * @param body 商品详情。对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body
	 * @param invalidTime 未付款交易的超时时间。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。参数不接受小数点，如1.5h，可转换为90m。
	 * @param notifyUrl 服务器异步通知页面路径
	 * @param tradeNo 商户唯一订单号
	 * @param subject 商品的标题/交易标题/订单标题/订单关键字等。该参数最长为128个汉字。
	 * @param totalFee 该笔订单的资金总额，单位为RMB-Yuan。取值范围为[0.01，100000000.00]，精确到小数点后两位。
	 * @param spbillCreateIp 终端ip。APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
	 * @return
	 */
	public OrderInfo BuildOrderInfo(
			String body,
			String invalidTime,
			String notifyUrl,
			String tradeNo,
			String subject,
			String totalFee,
			String spbillCreateIp
			){

		String orderInfo = "";
		/*Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(KeyLibs.ali_appid,body,invalidTime,notifyUrl,tradeNo,subject,totalFee,false);
		String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
		String sign = OrderInfoUtil2_0.getSign(params, KeyLibs.ali_privateKey, false);
		final String orderInfo = orderParam + "&" + sign;*/
		return new OrderInfo(orderInfo);
	}
	
	public void RegisterApp(Context context,String appId){
		return;
	}
	public String GetPrepayId(OrderInfo orderInfo){
		return null;
	}
	
	
}
