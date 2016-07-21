alert("asjdkakdkd");
		var pictureSource;   // picture source
        var destinationType; // sets the format of returned value
			
			function onPhotoURISuccess(imageURI) {
               console.log("success photo");
			   console.log(" imageURI " + imageURI);
                callNativePlugin({url_imagen: imageURI});
            }
            
            // A button will call this function
            //
            function capturePhoto() {
                // Take picture using device camera and retrieve image as file uri string
                navigator.camera.getPicture(onPhotoURISuccess, onFail, { quality: 100,
                                            destinationType: destinationType.FILE_URI });
            }
            
            // A button will call this function
            //
            function capturePhotoEdit() {
                // Take picture using device camera, allow edit, and retrieve image as base64-encoded string  
                navigator.camera.getPicture(onPhotoURISuccess, onFail, { quality: 50, allowEdit: true,
                                            destinationType: destinationType.FILE_URI });
            }
            

            
            // Called if something bad happens.
            // 
            function onFail(message) {
                alert('Failed because: ' + message);
            }
            
            function callNativePlugin( returnSuccess ) { 
			 var imgname = document.getElementById("imgname");
                //alert(resultado.innerHTML);
                imgname.innerHTML = imgname;
                //tesseractPlugin.createEvent( imageURI,nativePluginResultHandler); 
				navigator.connection.getText(imageURI,nativePluginResultHandler,nativePluginResultHandler);
            } 
            
            function nativePluginResultHandler (callback) { 
                alert("ok: "+callback);
                
            } 
            
            function nativePluginErrorHandler (error) { 
                alert("error: "+error);
            }
			
var app = {
    // Application Constructor
	count:0,
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
		if(app.count == 0) {
        app.receivedEvent('deviceready');
		console.log ("device ready");
		}
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
		
		

		
		pictureSource=navigator.camera.PictureSourceType;
        destinationType=navigator.camera.DestinationType;		
		/*
		function onSuccess(imageData) {
		var image = document.getElementById('myImage');
			image.src = "data:image/jpeg;base64," + imageData;
		}

		function onFail(message) {
			alert('Failed because: ' + message);
		}
		

		navigator.connection.getText();
	

		
		
		navigator.camera.getPicture(onSuccess, onFail, { quality: 25,
			destinationType: Camera.DestinationType.DATA_URL
		});

			*/
			
	
		
		//capturePhotoEdit();
    }
	
			
			
};

app.initialize();