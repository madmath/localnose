/* Copyright 2011 Mathieu Perreault and Simon Pouliot */

// Error in location.
function errorLocation() {
  $("#messagearea").html("Could not load your location");
}

// When HTML5 location gets in, update the fields.
function fillOutLocation(position) {
	globLat= position.coords.latitude;
	globLng= position.coords.longitude;			 
}

function initCallback(pluginInstance) {
  ge = pluginInstance;
  ge.getWindow().setVisibility(true);

  // add a navigation control
  ge.getNavigationControl().setVisibility(ge.VISIBILITY_AUTO);

  // add some layers
  ge.getLayerRoot().enableLayerById(ge.LAYER_BORDERS, true);
  ge.getLayerRoot().enableLayerById(ge.LAYER_ROADS, true);
  
  //For the rotateEarth function
  ge.getOptions().setFlyToSpeed(4); 
  google.earth.addEventListener(ge, "frameend", rotateEarth); 
  
  // Click event
  // listen to the click event on the globe and window
  google.earth.addEventListener(ge.getGlobe(), 'click', function eventHandler(event) {
	//alert('remove me!');
	google.earth.removeEventListener(ge, "frameend", rotateEarth);
    // Prevent default balloon from popping up for marker placemarks
    event.preventDefault();
  });

  rotateEarth();
}


function flyLocation() {
  var lookAt = ge.createLookAt('');
  lookAt.set(globLat,globLng, 10, ge.ALTITUDE_RELATIVE_TO_GROUND,0, 10, 2000000);
  ge.getView().setAbstractView(lookAt);
  
}

function rotateEarth(){ 
     google.earth.addEventListener(ge, "frameend", rotateEarth); 
     var lookAt = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND); 
     var myLon = lookAt.getLongitude(); 
       
	   if (myLon<350) { myLon = myLon + 10; } else { myLon=0; } 
       
	   lookAt.setLongitude(myLon); 
	   lookAt.setRange(15000000);
       //lookAt.setHeading(0);   // Workaround for heading bug, issue  
       ge.getView().setAbstractView(lookAt); 
}

function createPlacemark(_lat,_lng,_userName,_color) {
  var placemark = ge.createPlacemark('');
  placemark.setName(_userName);
  ge.getFeatures().appendChild(placemark);

  // Create style map for placemark
  var icon = ge.createIcon('');
  if(_color=="red"){
  	icon.setHref('http://maps.google.com/mapfiles/kml/paddle/red-circle.png');
  }
  else{
	icon.setHref('http://gd.bbbsfoundation.org/gd.donations/images/icon_marker_blue.png');
  }
  var style = ge.createStyle('');
  style.getIconStyle().setIcon(icon);
  style.getIconStyle().setScale(5.0);
  placemark.setStyleSelector(style);

  // Create point
  var la = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND);
  var point = ge.createPoint('');
  point.setLatitude(_lat);
  point.setLongitude(_lng);
  placemark.setGeometry(point);
    
  //EVENT
  google.earth.addEventListener(placemark, 'click', myEventHandler);
}

// Click sur un placemark
function myEventHandler(event) {
  // wrap alerts in API callbacks and event handlers
  // in a setTimeout to prevent deadlock in some browsers
  setTimeout(function() {
	var placemark = event.getTarget();
	
	var _lat = placemark.getGeometry().getLatitude();
	var _lng = placemark.getGeometry().getLongitude();
	var _name = placemark.getName();
	
	placemark.setVisibility(false);
	
	createPlacemark(_lat,
					_lng,
					_name,
					"Blue");
					
	mailingList.push(_name)
	if (mailingList.length >= 1 && $("#queryInput").val() != "What are you looking for?") {
	  $("#confirm").removeAttr("disabled");
	  $("#confirm").click(query);
	}
  }, 0);
}
 
function failureCallback(errorCode) {
}