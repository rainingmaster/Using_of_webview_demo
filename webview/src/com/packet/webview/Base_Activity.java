package com.packet.webview;

import com.example.webview.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Base_Activity extends Activity {

    private static final String LOG_TAG = "WebViewDemo";
    private WebView mWebView;  
    private TextView mReusultText ;  
    private Handler mHandler = new Handler();  
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //�����������  
        //WebView����һ���򵥵������  
        //android�����Դ�������LINUX\android\package\apps\Browser��  
        //��������в�������Χ��WebView��չ����  
        mWebView = (WebView) findViewById(R.id.webview); 
        mReusultText = (TextView) findViewById(R.id.resultText);
        mWebView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {       
                view.loadUrl(url);       
                return true;       
            }       
        });
        WebSettings webSettings = mWebView.getSettings();  
        webSettings.setSavePassword(false);  
        webSettings.setSaveFormData(false);  
        webSettings.setJavaScriptEnabled(true);  
        webSettings.setSupportZoom(false);
        mWebView.setWebChromeClient(new MyWebChromeClient()); 
        
        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(),"demo");
        mWebView.loadUrl("file:///android_asset/index.html");
        
        Button bt = (Button) findViewById(R.id.button); 
        bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
		        //��ҳ����
		        String webcontent = "<p>1111׳���Ҵ�Զ�磬������˶�����</p><br><input type=\"button\" value=\"���Ҵ����ݣ�Ү\" onclick=\"out(te)\" /><input id=\"te\" />";
		        
                //����js�е�onJsAndroid����  
                mWebView.loadUrl("javascript:onJsAndroid('"+webcontent+"')");
			}
		});
	} 
  
    final class DemoJavaScriptInterface {  
        DemoJavaScriptInterface() {}  
  
        /** 
         * �÷�����������˵��� 
         */  
        public void clickOnAndroid(final String order) {
            mHandler.post(new Runnable() {  
                public void run() {
                	TextView mText = (TextView) findViewById(R.id.resultText);
                	mText.setText(order); 
                }  
            });  
        }  
    }  
  
    /** 
     * �̳�WebChromeClient�� 
     * ��js������ʱ����д��� 
     *  
     */  
    final class MyWebChromeClient extends WebChromeClient {
  
        /** 
         * ����alert������ 
         */  
        @Override  
        public boolean onJsAlert(WebView view,String url,
                                 String message,JsResult result) {
            Log.d(LOG_TAG,"onJsAlert:"+message);
            mReusultText.setText("Alert:"+message);
            //��alert�ļ򵥷�װ  
            new AlertDialog.Builder(Base_Activity.this).
                setTitle("Alert").setMessage(message).setPositiveButton("OK",  
                new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface arg0, int arg1) {  
                       //TODO  
                   }  
            }).create().show();  
            result.confirm();  
            return true;  
        }  
  
        /** 
         * ����confirm������ 
         */  
        @Override  
        public boolean onJsConfirm(WebView view, String url, String message,  
                JsResult result) {  
            Log.d(LOG_TAG, "onJsConfirm:"+message);  
            mReusultText.setText("Confirm:"+message);  
            result.confirm();  
            return super.onJsConfirm(view, url, message, result);  
        }  
  
        /** 
         * ����prompt������ 
         */  
        @Override  
        public boolean onJsPrompt(WebView view, String url, String message,  
                String defaultValue, JsPromptResult result) {
            Log.d(LOG_TAG,"onJsPrompt:"+message);  
            mReusultText.setText("Prompt input is :"+message);  
            result.confirm();  
            return super.onJsPrompt(view, url, message, message, result);  
        }
    }  
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {       
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {       
            mWebView.goBack();       
                   return true;       
        }       
        return super.onKeyDown(keyCode, event);       
    }
	
}
