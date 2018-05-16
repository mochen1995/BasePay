package com.eland.basepay.component.pays;

import com.eland.basepay.component.model.PayType;
import com.eland.basepay.component.pays.ali.AliPay;
import com.eland.basepay.component.pays.weixin.WeixinPay;

public class PaysFactory {

	private static IPayable alipay = null;
	private static IPayable wxpay = null;
	public static IPayable GetInstance(PayType payType){
		switch (payType) {
		case AliPay:
			if (alipay==null) {
				alipay=new AliPay();
			}
			return alipay;
		case WeixinPay:
			if (wxpay==null) {
				wxpay=new WeixinPay();
			}
			return wxpay;
		}
		return wxpay;  //默认返回wx支付的实现类
	}
}
