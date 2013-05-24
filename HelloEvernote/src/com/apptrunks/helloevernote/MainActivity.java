package com.apptrunks.helloevernote;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.InvalidAuthenticationException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final String CONSUMER_KEY = "Change to your consumer key";
    private static final String CONSUMER_SECRET = "Change to your counsumer secret";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

    protected EvernoteSession mEvernoteSession;
    
    private Button mBtnLogin;
    private TextView mTxtToken;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mEvernoteSession = 
				EvernoteSession.getInstance(this, CONSUMER_KEY, CONSUMER_SECRET, EVERNOTE_SERVICE);
		
		mBtnLogin = (Button)findViewById(R.id.btn_login);
		mTxtToken = (TextView)findViewById(R.id.txt_my_token);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		//在這邊執行畫面要呈現登入或登出的狀態
		//若是我們進到mEvernoteSession.authenticate()這個授權函式去看的話
		//我們可以看到，這個函式在正確登入時是採用startActivityForResult的方式
		//所以，其實我們也可以使用onActivityResult來接回正確登入後要做的動作
		//不過因為本範例的狀況是在一開始進來以及正確登入時都要確認授權狀況
		//所以就集中在onResume這個狀態裡撰寫了
		setLogin();
	}


	/**
	 * 設定登入或是登出要呈現的畫面
	 * */
	private void setLogin(){
		if(mEvernoteSession.isLoggedIn()){
			//已登入，將按鈕文字改為「登出」
			mBtnLogin.setText(getString(R.string.logout));
			//將偵聽式設定為執行登出的偵聽式
			mBtnLogin.setOnClickListener(lisLogout);
			mTxtToken.setText(mEvernoteSession.getAuthToken());
		} else {
			//未登入，將按鈕文字改為「登入」
			mBtnLogin.setText(getString(R.string.login));
			//將偵聽式設定為執行登入的偵聽式
			mBtnLogin.setOnClickListener(lisLogin);
			mTxtToken.setText(R.string.hello_world);
		}
	}
	
	/**
	 * 執行登入的偵聽式
	 * */
	private OnClickListener lisLogin = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mEvernoteSession.authenticate(MainActivity.this);
		}
	};
	
	/**
	 * 執行登出的偵聽式
	 * */
	private OnClickListener lisLogout = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				mEvernoteSession.logOut(MainActivity.this);
				setLogin();
			} catch (InvalidAuthenticationException e) {
				e.printStackTrace();
			}
		}
	};
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
