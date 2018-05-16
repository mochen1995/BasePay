package com.eland.basepay.component.PayManager;

/**
 * 项目名：TestModuleApp
 * 包名：com.eland.basepay.component.PayManager
 * 创建者：mmcc
 * 创建时间：2017/7/27 13:37
 * 描述：TODO
 */


public interface OnPayCallback {
    void onSuccess(int type);
    void onWarning(int type);
    void onError(int type);
}
