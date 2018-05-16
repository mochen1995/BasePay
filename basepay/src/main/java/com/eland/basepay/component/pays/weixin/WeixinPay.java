package com.eland.basepay.component.pays.weixin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.eland.basepay.component.model.KeyLibs;
import com.eland.basepay.component.model.OrderInfo;
import com.eland.basepay.component.pays.IPayable;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class WeixinPay implements IPayable {

	//微信sdk对象
	private IWXAPI msgApi;

	@Override
	public String Pay(Activity activity, OrderInfo orderInfo,String prepayJson) {
		// 支付调用\
		boolean isSuccess=msgApi.sendReq(BuildCallAppParams(prepayJson));
		return String.valueOf(isSuccess);
	}

	//生成订单信息
	@Override
	public OrderInfo BuildOrderInfo(String body, String invalidTime,
			String notifyUrl, String tradeNo,
			String subject, String totalFee,String spbillCreateIp) {

		try {
			/*String	nonceStr = GetNonceStr();

			xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", KeyLibs.weixin_appId));
			packageParams.add(new BasicNameValuePair("body", subject));//和支付宝的subject类似
			packageParams.add(new BasicNameValuePair("mch_id", KeyLibs.weixin_mchId));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", notifyUrl));
			packageParams.add(new BasicNameValuePair("out_trade_no",tradeNo));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",spbillCreateIp));
			packageParams.add(new BasicNameValuePair("total_fee", totalFee));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
			
			paramsForPrepay=packageParams;//将参数保存一份，待调用支付时使用


			String sign = Sign(packageParams); //签名
			packageParams.add(new BasicNameValuePair("sign", sign));*/

		   return new OrderInfo("wxorder = 1546846465&monkey = 132");

		} catch (Exception e) {
			return null;
		}
	}

	
	public void RegisterApp(Context context,String appId){
		msgApi=WXAPIFactory.createWXAPI(context,null);
		boolean b = msgApi.registerApp(appId);
	}

	//访问服务器获取统一下单的 预支付交易会话标识
	public String GetPrepayId(OrderInfo orderInfo){
		Log.e("tag", "微信支付的订单："+orderInfo.GetContent());
		//TODO 访问服务器 获取统一下单的后获取的带有 预支付交易会话标识的json


		return "";
	}

	//根据预支付json生成支付请求
	private PayReq BuildCallAppParams(String prepayJson) {
		PayReq req=new PayReq();

		try {
			JSONObject jsonObject = new JSONObject(prepayJson);
			if (jsonObject.optInt("code")==1) {
				JSONObject data = jsonObject.getJSONObject("data");
				String prepayId = data.optString("prepayid");
				String sign = data.optString("sign");
				String nonceStr = data.optString("noncestr");
				String timestamp = data.optString("timestamp");
				String mchId = data.optString("mchId");
				req.appId = KeyLibs.weixin_appId;  //appid

				req.partnerId = mchId;  //// 商户号

				req.nonceStr= nonceStr;  //随机字符串

				req.timeStamp= timestamp;  //时间戳

				req.packageValue = "Sign=WXPay";  //固定值

				req.prepayId= prepayId; //预支付订单

				req.sign= sign;

				return req;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return req;
	}
}
