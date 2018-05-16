package net.sourceforge.simcpux.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.eland.basepay.component.model.KeyLibs;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
    private IWXAPI api;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.wxpay_result);
		Log.e("tag", "WXPayEntryActivity onCreate");
    	api = WXAPIFactory.createWXAPI(this, KeyLibs.weixin_appId);

        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("tag", "WXPayEntryActivity onDestroy");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	/**'
	 errCode
	 0	成功	展示成功页面
	 -1	错误	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
	 -2	用户取消	无需处理。发生场景：用户不支付了，点击取消，返回APP。
	 * @param resp
	 */
	@Override
	public void onResp(BaseResp resp) {
		Log.e("tag", "onResp");
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示");
			builder.setMessage("支付结果"+resp.errStr +";code=" + String.valueOf(resp.errCode));
			builder.show();
		}

	}

}
