package com.spychcio.awesome;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;


public class MainActivity extends FragmentActivity implements AsyncResponse {	
	
	public final static String EXTRA_MESSAGE = "com.spychcio.awesome.MESSAGE";	
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;	
	RequestTask asyncTask = new RequestTask(this);
	Intent intent = null;
	private Request.GraphUserCallback requestGraphUserCallback;
	static int logged=0;
	static String username=null;
	static String userid=null;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);	   
	    asyncTask.delegate = this;
	    requestGraphUserCallback = new RequestGraphUserCallback();  
	    intent = new Intent(this, DisplayMessageActivity.class); 
	    uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);  
    }
    
    public void sendMessage(View view) {     
    	new RequestTask(this).execute("http://mooduplabs.com/test/info.php");    	
    }
    
    
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(MainActivity.TAG, "Logged in...");
	        logged=1;
	    } else if (state.isClosed()) {
	        Log.i(MainActivity.TAG, "Logged out...");
	        logged=0;
	    }
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	        if (session.isOpened()) {
	        	Request.newMeRequest(session, requestGraphUserCallback).executeAsync();
	        	}	        
	    }
	};
	
	private class RequestGraphUserCallback implements Request.GraphUserCallback{

		   // callback after Graph API response with user object
		   @Override
		   public void onCompleted(GraphUser user, Response response) {
		       if (user != null) {
		           username = user.getName();
		           userid = user.getId();		    	  
		       }
		     }
		   }
	
	
	@Override	
	public void onResume() {
	    super.onResume();
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void processFinish(String output) {
		Log.i("main",output);   
    	intent.putExtra(EXTRA_MESSAGE, output);    	
    	startActivity(intent);
	}	

}
