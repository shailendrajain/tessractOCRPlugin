# tessractOCRPlugin
Tessract based OCR plugin supporting from Android API 14 to Android API 23 +

Cordova OCR Plugin & Demo  for Android 4.4+  
Description : 
	 This Cordova OCR plugin is based on Tesseract OCR project .  Tessract Project is open source and supports 60 languages.    
Platform Support : 
Plugin tested with Android 4.4+ devices .  However it should support Android 2.3 – 4.3 devices as well   . 
This plugin supports by default only English language and respective training data file is embedded in the plugin.
Support for other languages can be easily extended by adding training data file in plugin and code to initialize OCR with the  language.  


#Usage Adding Plugin in project 
1.	Extract com-tessract-ocr.zip on your system .

2.	Run below command to  add Cordova OCR Plugin in your project  
cordova plugin add < path to com-tessract-ocr\ plugin.xml>

3.	Initialize OCR training data 

a.	tessract.initTrainingData(successcb,failurecb);
#Note : on Android 6.0+ platforms you need to ensure that appropriate permissions are granted by user before calling above function. 
Refer index.html file for  using cordova-plugin-android-permissions APIs to prompt  user to grant  permissions. 

4.	OCRing the image 

tessract.getText(imageURI,successCB,failurCB);
Note : ImageURI is the image path. 

#Commong Issues :
 * Image quality should not be very less , keep it 100% for best result.
 * Permissions are not granted to read & write the path of Images & .trainneddata files. 

#Source code of tesseract library  
https://github.com/rmtheis/tess-two/tree/master/tess-two 

To update the OCR libraries with  latest feature and issues fixes , get the source code from above repository 
and build the libraries using  Android NDK . Copy the update libraries in plugins src/libs folder .

#Source of Training Data repository for other languages 

https://github.com/tesseract-ocr/tessdata 
Download the appropriate .traineddata file from the repository and add in plugin

#Resources & Credits  : 

https://github.com/rmtheis/tess-two/tree/master/tess-two
http://scn.sap.com/community/developer-center/front-end/blog/2015/05/15/create-an-ocr-android-app-with-cordova-and-tesseract
https://github.com/tesseract-ocr/tessdata 


