package com.eland.basepay.component.PayManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.eland.basepay.component.model.KeyLibs;
import com.eland.basepay.component.model.OrderInfo;
import com.eland.basepay.component.model.PayType;
import com.eland.basepay.component.model.ali.PayResult;
import com.eland.basepay.component.pays.IPayable;
import com.eland.basepay.component.pays.PaysFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名：TestModuleApp
 * 包名：com.eland.basepay.component.PayManager
 * 创建者：mmcc
 * 创建时间：2017/7/27 11:54
 * 描述：TODO
 */


public class PayManager {
    private static PayManager instance;
    private static final int PAY_FLAG = 1;

    public static final int ALIPAY = 0x11;
    public static final int WXPAY = 0x12;

    private static OnPayCallback mOnPayCallback;

    private PayManager(){

    }
    public static PayManager getInstance(OnPayCallback onPayCallback) {
        if (instance == null) {
            instance = new PayManager();
        }
        mOnPayCallback = onPayCallback;
        return instance;
    }

    private IPayable payable;
    // 支付宝支付完成后，多线程回调主线程handler
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PAY_FLAG:
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();

                    if (TextUtils.equals(resultStatus, "9000")) {
                        // ----------调用重写方法
                        //支付成功
                        if (mOnPayCallback!=null) {
                            mOnPayCallback.onSuccess(ALIPAY);
                        }
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认。
                        if (TextUtils.equals(resultStatus, "8000")) {
                            // ---------调用重写方法
                            //支付结果确认中
                            if (mOnPayCallback!=null) {
                                mOnPayCallback.onWarning(ALIPAY);
                            }
                        } else {
                            Log.e("tag", "status = "+resultStatus+" info = "+resultInfo);
                            // -------调用重写方法
                            //支付失败
                            if (mOnPayCallback!=null) {
                                mOnPayCallback.onError(ALIPAY);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void aliPay(String json) {
        payable = PaysFactory.GetInstance(PayType.AliPay);
        /*final OrderInfo orderInfo = payable.BuildOrderInfo(body, invalidTime, notifyUrl, tradeNo,
                subject, totalFee, "终端ip。APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP");*/
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.optInt("code")==1) {
                final String orderInfo = jsonObject.optString("data");
                final OrderInfo order = new OrderInfo(orderInfo);
                // 2.调用支付方法
                Thread payThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 构造支付对象，调用支付接口，获取支付结果
                        if (mOnPayCallback instanceof Activity) {
                            String result = payable.Pay(((Activity) mOnPayCallback), order,
                                    null);
                            Log.e("tag", "result = "+result);
                            // 回调，通知主线程
                            Message msg = new Message();
                            msg.what = PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }else{
                            Log.e("tag", "回调接口出现异常！必须为Activity类型");
                        }

                    }
                });
                payThread.start();
            }else{

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param prepayJson  统一下单之后返回的统已json ，包含 appId，prepayId等json数据
     * {
     *    appid:xxx,    appid
     *    partnerid:xxx,   商户号
     *    prepayid:xxx,    预支付id
     *    sign:xxx    签名
     * }
     */
    public void wxPay(String prepayJson) {
        payable = PaysFactory.GetInstance(PayType.WeixinPay);

        if (mOnPayCallback instanceof Activity)
        {
            payable.RegisterApp((Context) mOnPayCallback, KeyLibs.weixin_appId);
            String pay = payable.Pay(((Activity) mOnPayCallback), null, prepayJson);
            if ("true".equals(pay))
            {
                mOnPayCallback.onWarning(WXPAY);
            }else if ("false".equals(pay)){
                mOnPayCallback.onError(WXPAY);
            }
        }else {
            Log.e("tag", "回调接口出现异常！必须为Activity类型");
        }

    }

}
