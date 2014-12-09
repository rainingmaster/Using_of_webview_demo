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
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //获得浏览器组件  
        //WebView就是一个简单的浏览器  
        //android浏览器源码存在于LINUX\android\package\apps\Browser中  
        //里面的所有操作都是围绕WebView来展开的  
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
				// TODO 自动生成的方法存根
		        //网页内容
		        String webcontent = "<p>1111壮哉我大远哥，你今天运动了吗？</p><br><input type=\"button\" value=\"点我传内容，耶\" onclick=\"out(te)\" /><input id=\"te\" />";
		        
                //调用js中的onJsAndroid方法  
                mWebView.loadUrl("javascript:onJsAndroid('"+webcontent+"')");
			}
		});
	} 
  
    final class DemoJavaScriptInterface {  
        DemoJavaScriptInterface() {}  
  
        /** 
         * 该方法被浏览器端调用 
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
     * 继承WebChromeClient类 
     * 对js弹出框时间进行处理 
     *  
     */  
    final class MyWebChromeClient extends WebChromeClient {
  
        /** 
         * 处理alert弹出框 
         */  
        @Override  
        public boolean onJsAlert(WebView view,String url,
                                 String message,JsResult result) {
            Log.d(LOG_TAG,"onJsAlert:"+message);
            mReusultText.setText("Alert:"+message);
            //对alert的简单封装  
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
         * 处理confirm弹出框 
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
         * 处理prompt弹出框 
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
