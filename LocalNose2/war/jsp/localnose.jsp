<% /* Copyright 2011 Mathieu Perreault and Simon Pouliot */ %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="javax.jdo.JDOObjectNotFoundException" %>
<%@ page import="ca.mcgill.localnose.PMF" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="ca.mcgill.localnose.SocialProfile" %>
<%@ page import="java.util.logging.Logger" %>

<%
	Logger log = Logger.getLogger("JSPPAGE");
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
%>

<html>
<link href='http://fonts.googleapis.com/css?family=Molengo&subset=latin' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="css/blueprint/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="css/blueprint/print.css" type="text/css" media="print">	
<!--[if lt IE 8]><link rel="stylesheet" href="css/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
<link type="text/css" rel="stylesheet" href="/css/main.css" />

<script src="http://www.google.com/jsapi?key=ABQIAAAAHoYPalJ5tc6D0_W9snXhdRT5l-Fnm4B6LmbVkBvkjEctDkFAtxQmzNy-1Z6i7mexloy9ctAFoa76PA" type="text/javascript"></script> 
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.3/jquery.min.js" type="text/javascript"></script> 
<script type="text/javascript">
var ge;

var globLat;
var globLng;

var mailingList = [];

google.load("earth", "1");
</script>
<script src="js/earth.js" type="text/javascript"></script>
<script src="js/main.js" type="text/javascript"></script>

<body>
<div class="container">
<% if (user != null) { // User is logged in. %>
	<div class="span-24 last" id="headernose">
		<img src="/img/logo.png" />
		<!--<span id="noseh1">Local Nose</h1>
		<h3>Select your experts, and ask a question!</h3> -->
	</div>
	<div class="span-24 last">
	<div id="map3d" style="width: 900px; height: 500px;"></div>
		<div id="nosequery">
			<div class="secondaryInputs vanish">
				<input id="queryInput" type="text" name="queryText" value="What are you looking for?" size="120"/>
			</div>
			<input type="button" id="confirm" value="Ask the people I selected" disabled="disabled" />
		</div>
	</div>
	<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Logout</a>
<% } else {
 %>
 <div class="span-24 last" id="yfmheadershow">
                <div id="noseh1">Localnose</div>
                <h3>Please sign-in to your <a href='<%=userService.createLoginURL(request.getRequestURI())%>'>Google Account</a> to use Localnose</h3>
</div>
 <%
  } 
  %>

</div>
</body>
</html>