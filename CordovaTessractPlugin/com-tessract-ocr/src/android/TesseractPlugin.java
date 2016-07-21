package com.googlecode.tesseract.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.res.AssetManager;
import android.util.Log;
import android.os.Environment;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


import com.googlecode.tesseract.android.TessBaseAPI;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;


import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.content.Context ;
public class TesseractPlugin extends CordovaPlugin {

	public static final String ACTION_ADD_TESSERACT_ENTRY = "addTesseractPluginEntry";
	public static final String INIT_TRAINING_DATA = "initTrainingData";

	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString();
	private static final String lang = "eng";
	private static final String LOG_TAG = "OCR";
		

	public CallbackContext callbackContext;
	@Override
	public boolean execute(String action, JSONArray args,CallbackContext callbackContext) throws JSONException {
		
        Log.i(LOG_TAG, "Data path in plugin " +DATA_PATH);

		this.callbackContext = callbackContext;
		
			try {
				if (ACTION_ADD_TESSERACT_ENTRY.equals(action)) {
					if(args!=null)
						Log.i(LOG_TAG, "arg_object  NOT Null " +args.toString());

					JSONObject arg_object = args.getJSONObject(0);
					Log.i(LOG_TAG, "arg_object  " + arg_object.toString());

					String imagePath = arg_object.getString("imagePath");
					Log.i(LOG_TAG, "imagePath  " + imagePath);
					
					//file:// URI is not allowed by android 
					// Convert to /storage/... Tested on Nexus 5 need to be changed on other manufacturers
					int indexFile = imagePath.indexOf("file");  // to Remove "file://"
					String path = null;
					if(indexFile == 0 )
						path = imagePath.substring(7,imagePath.indexOf("jpg")+3);
					else 
						path = imagePath;
					Log.i(LOG_TAG, "updated file path  " + path);
					
					//PathTo tessdata/eng.traineddata must end with / -> DATA_PATH+"/"
					this.startOCR(path,DATA_PATH+"/",callbackContext);
					return true;
				}
				else if (INIT_TRAINING_DATA.equals(action))	{
					Log.i(LOG_TAG, "Action   " + action);
					// Training data is required to use OCR , ensure that we have training data
					// at correct Path 
					copyTrainingData(callbackContext);
				}
            }
            catch (IllegalArgumentException e)
            {

                callbackContext.error("Illegal Argument Exception");
                PluginResult r = new PluginResult(PluginResult.Status.ERROR);
                callbackContext.sendPluginResult(r);
                return true;
            }

            PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
            r.setKeepCallback(true);
            callbackContext.sendPluginResult(r);

            return true;
		
	}
	
	public void startOCR(String pathToPic, String pathToPath ,CallbackContext callbackContext)
	{
		String recognizedText = "";
		
		try {
			
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(pathToPic, options);
		
		try {
			ExifInterface exif = new ExifInterface(pathToPic);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

		
			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}

		
			if (rotate != 0) {

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}

			// Convert to ARGB_8888, required by tess
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
			
		} catch (IOException e) {
			callbackContext.error("Couldnt correct orientation");
		}
						
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		
		
		boolean result = baseApi.init(pathToPath, lang);

		System.err.println("after baseApi.init   result "+result);

		System.err.println("Before baseApi.setImage");
		baseApi.setImage(bitmap);

		if(baseApi.getUTF8Text() == null)
		{
			callbackContext.error("baseApi.getUTF8Text = null");
		}
		else {
			recognizedText = baseApi.getUTF8Text();
		}

		System.err.println("Before baseApi.end" + recognizedText);
		baseApi.end();
		
		}
		catch (Exception e){
			System.err.println("TesseractTwoExample: " + e);
			callbackContext.error("TesseractTwoExample: " + e);

		}

		// You now have the text in recognizedText var, you can do anything with it.
		// We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
		// so that garbage doesn't make it to the display.

		//Log.v(TAG, "OCRED TEXT: " + recognizedText);
		System.err.println("OCRED TEXT: " + recognizedText);
		callbackContext.success(recognizedText);

		
	}

	/*  
	this Plugin and copyTrainingData function assumes only eng language to support
	provide specific language training data in raw/tessdata folder 
	and update logic to handle training Data for other languages as well. 
	
	*/	
	
	public void copyTrainingData(CallbackContext callbackContext){
	
			Context context=this.cordova.getActivity().getApplicationContext(); 
			String lang = "eng";
			String trainedData = ".traineddata";
			String trainData = DATA_PATH + "/tessdata/" + lang + trainedData;
			Log.i(LOG_TAG, "Training Data " + trainData);
		    if (!(new File(trainData)).exists()) {  
				Log.i(LOG_TAG, "Training Data Not found at " + trainData);
		    	callbackContext.error("Unable to find training Data");
				
				try {  
					Log.i(LOG_TAG, "Creating Path  " + trainData);
		    	
					AssetManager assetManager = context.getAssets();  
					InputStream in = assetManager.open( lang + trainedData);  
					File theFile = new File(DATA_PATH+ "/tessdata/"); 
					theFile.mkdirs();
					OutputStream out = new FileOutputStream(DATA_PATH+ "/tessdata/" + lang + trainedData);  
					byte[] buf = new byte[1024];  
					int len;  
					while ((len = in.read(buf)) > 0) { 
					//Log.i(LOG_TAG,"wrting .......Training  data");
					out.write(buf, 0, len);  
				}  
				in.close();  
				out.close();  
				callbackContext.success("Copied training data " + trainData);
				Log.v(LOG_TAG, "Copied " + lang + " traineddata");  
				} catch (IOException e) {  
					Log.e(LOG_TAG, "Unable to copy " + lang + " traineddata " + e.toString());  
					callbackContext.error("Unable to copy");
				}  
			}
			else {
				Log.i(LOG_TAG,"tessdata .traineddat file already exists..");
				callbackContext.success("Training Data already exists");

			}
	}// End of copyTrainingData
}

