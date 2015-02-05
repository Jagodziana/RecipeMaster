package com.spychcio.awesome;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;










import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayMessageActivity extends FragmentActivity{
	
	ArrayList<String> ingredients = new ArrayList<String>();
	ArrayList<String> preparing = new ArrayList<String>();
	ArrayList<String> imgs = new ArrayList<String>();
	String title=null;
	String description=null;
	ImageView[] img= new ImageView[3];
	Bitmap[] bitmap=new Bitmap[3];
	int imgid=0;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
       
        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String name="NO_NAME";
        String userid="NO_USERID";
        if(MainActivity.logged==1)
        {
        	name=MainActivity.username;
        	userid=MainActivity.userid;
        }
        
        img[0] = (ImageView)findViewById(R.id.img1);
        img[1] = (ImageView)findViewById(R.id.img2);
        img[2] = (ImageView)findViewById(R.id.img3);
      
        for(int i=0;i<3;i++)
        {
        	 img[i].setClickable(true);
        	 final int j=i;
             img[i].setOnClickListener(new View.OnClickListener()  {
                 @Override
                 public void onClick(final View v) {
                     AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                     dialog.setIcon(R.drawable.ic_launcher);
                     dialog.setTitle("Download image?");
                     dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int id) {
                        	 img[j].buildDrawingCache();
                        	 Bitmap bm=img[j].getDrawingCache();                        	 
                        	 OutputStream fOut = null;
                        	 Uri outputFileUri;                        	    
                        	 try {                        	    
                        		 File root = Environment.getExternalStorageDirectory();
                        		 File sdImageMainDirectory = new File(root.getAbsolutePath() + "/DCIM/Pizzaimg"+Integer.toString(j+1)+".jpg");                        	    
                        		 outputFileUri = Uri.fromFile(sdImageMainDirectory);                        	    
                        		 fOut = new FileOutputStream(sdImageMainDirectory);    
                        		 Toast.makeText(v.getContext(), "Image downloaded.", Toast.LENGTH_SHORT).show(); 
                        	 } 
                        	 catch (Exception e) {                        	    
                        		 Toast.makeText(v.getContext(), "Error. Please try again later.", Toast.LENGTH_SHORT).show();                        		 
                        	 }                        	   
                        	 try {                        	   
                        		 bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);                        	   
                        		 fOut.flush();                        	   
                        		 fOut.close();                        		 
                        	 } 
                        	 catch (Exception e) {                        	   
                        	 }
                         }
                        });
                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 dialog.dismiss();
                             }
                       });
                       dialog.show();
                 }
             });
        }
        
        JSONObject json=null;
        try {
			json = new JSONObject(message);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
        
        
     JSONArray jArray = null;  
     try {
		title=json.getString("title");
	} catch (JSONException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
     try {
		description=json.getString("description");
	} catch (JSONException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}     
		
     
     try {    	
    	 jArray = json.getJSONArray("ingredients");		
     } 
     catch (JSONException e1) {			
    	 e1.printStackTrace();		
     }			
     for(int j=0;j<jArray.length();j++)	    	
     {	    		 
    	 try {	    			 
    		 ingredients.add(jArray.getString(j));	    		
    	 } 	    		 
    	 catch (JSONException e) {						    			 
    		 e.printStackTrace();	    		
    	 }	    	
     }    
     
     try {    	
    	 jArray = json.getJSONArray("preparing");		
     } 
     catch (JSONException e1) {			
    	 e1.printStackTrace();		
     }			
     for(int j=0;j<jArray.length();j++)	    	
     {	    		 
    	 try {	    			 
    		 preparing.add(jArray.getString(j));	    		
    	 } 	    		 
    	 catch (JSONException e) {						    			 
    		 e.printStackTrace();	    		
    	 }	    	
     }    
     
     try {    	
    	 jArray = json.getJSONArray("imgs");		
     } 
     catch (JSONException e1) {			
    	 e1.printStackTrace();		
     }			
     for(int j=0;j<jArray.length();j++)	    	
     {	    		 
    	 try {	    			 
    		 imgs.add(jArray.getString(j));	    		
    	 } 	    		 
    	 catch (JSONException e) {						    			 
    		 e.printStackTrace();	    		
    	 }	    	
     }    
     
     String result=null;
     result ="- " + ingredients.get(ingredients.size()-1);
     for(int i=ingredients.size()-2;i>=0;i--)
    	 result="- " +  ingredients.get(i)+"\n"+result;
     TextView msg = (TextView)findViewById(R.id.ingredients);
     msg.setText(result);
     
     result = Integer.toString(preparing.size()) + ". " + preparing.get(preparing.size()-1);
     for(int i=preparing.size()-2;i>=0;i--)
    	 result= Integer.toString(i+1) + ". " +  preparing.get(i)+"\n\n"+result;
     msg = (TextView)findViewById(R.id.preparing);
     msg.setText(result);
     
     
     msg = (TextView)findViewById(R.id.description);
     msg.setText(description);
     
     msg = (TextView)findViewById(R.id.title);
     msg.setText(title);
     
     msg = (TextView)findViewById(R.id.intro_ingredients);
     msg.setText("Ingredients");
     
     msg = (TextView)findViewById(R.id.intro_preparing);
     msg.setText("Preparing");
     
     msg = (TextView)findViewById(R.id.intro_imgs);
     msg.setText("Images");
     

     msg = (TextView)findViewById(R.id.username);
     if(MainActivity.logged==1)msg.setText("Logged as " + name);
     else msg.setText("You're not logged with facebook");
     
     new DownloadImageTask((ImageView) findViewById(R.id.img1)).execute(imgs.get(0));
     new DownloadImageTask((ImageView) findViewById(R.id.img2)).execute(imgs.get(1));
     new DownloadImageTask((ImageView) findViewById(R.id.img3)).execute(imgs.get(2));
     if(MainActivity.logged==1) new DownloadImageTask((ImageView) findViewById(R.id.user_photo)).execute("https://graph.facebook.com/"+userid+"/picture");
    
    }
      
    
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    	ImageView bmImage;

    	public DownloadImageTask(ImageView bmImage) {
    	    this.bmImage = bmImage;
    	}

    	protected Bitmap doInBackground(String... urls) {
    	    String urldisplay = urls[0];
    	    Bitmap mIcon11 = null;
    	    try {
    	        InputStream in = new java.net.URL(urldisplay).openStream();
    	        mIcon11 = BitmapFactory.decodeStream(in);
    	    } catch (Exception e) {
    	        Log.e("Error", e.getMessage());
    	        e.printStackTrace();
    	    }
    	    return mIcon11;
    	}

    	 protected void onPostExecute(Bitmap result) {
    	    bmImage.setImageBitmap(result);
    	    }
    	   }
    
  /*  public void img1click(View v)
    {
      Log.i(SystemSettings.APP_TAG + " : " + HomeActivity.class.getName(), "Entered onClick method");
      Toast.makeText(v.getContext(),
              "The favorite list would appear on clicking this icon",
              Toast.LENGTH_LONG).show();
    }*/

  
  

}
