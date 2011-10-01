/* Copyright 2011 Mathieu Perreault and Simon Pouliot */

String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g,"");
}

String.prototype.startsWith = function(str){
    return (this.indexOf(str) === 0);
}

function queryAllUsers() {
  $.getJSON('/queryall', function(data){
    if (data.success) {
      for (var i = 0; i < data.profiles.length; i++) {
        var item = data.profiles[i];
        createPlacemark(parseFloat(item.lat), parseFloat(item.lng), item.name, "red");
      }
    }
  });
}

function isDefaultKeyword(text) {
    if (text == "What are you looking for?") {
        return true;
    } else {
        return false;
    }
}

// This function is executed when the page is loaded.
$(document).ready(function(event){
  // Create the Google Earth instance
  google.earth.createInstance('map3d', initCallback, failureCallback);
  
  $(".vanish").click(function(event) {
        var saveIt = $(event.target).val();
        if (isDefaultKeyword(saveIt)){
            $(event.target).css("color", "black");
            $(event.target).val("");
            $(event.target).blur(function(ev) {
                if ($(ev.target).val() == "") {
                    $(ev.target).css("color", "grey");
                    $(ev.target).val(saveIt);
                }
				if (mailingList.length > 0 && $("#confirm").hasAttr("disabled")) {
				  $("#confirm").removeAttr("disabled");
				  $("#confirm").click(query);
				}
            });
        } else {
            $(event.target).css("color", "black");
        }
    });

  // If HTML5 location exists, get it and update the fields.
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(fillOutLocation, errorLocation); 
  }
  else {
    errorLocation();
  }
  
  queryAllUsers();
});

function query() {
	var answer = confirm("Here's the deal: if you want to ask an expert, you have to be an expert yourself. You've allowed us to see that you are at\n\n\tLatitude: " + globLat + '\n\tLongitude: ' + globLng + "\n\nIs it cool with you? (if not, you'll be redirected)");
	if (answer) {
		var query = $("#queryInput").val();
		var baseURL = "/query?ulat=" + globLat + "&ulon=" + globLng + "&q="+ query + "&emails=";
		for (var i = 0; i < mailingList.length; i++) {
			var item = mailingList[i];
			baseURL += item;
			if (i != mailingList.length -1) {
				baseURL += ',';
			}
		}
		$.getJSON(baseURL, function(data){
			if (data.success) {
				window.location.href = "http://localnose.appspot.com/thanks";
			}
			else {
				window.location.href = "http://localnose.appspot.com/sorry";
			}
		})
		console.debug(baseURL);
	}
	else {
		window.location.href = "http://www.google.com";
	}
	
}

function failureCallback(errorCode) {
}