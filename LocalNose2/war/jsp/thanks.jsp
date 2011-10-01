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
<link type="text/css" rel="stylesheet" href="/css/jquery.confirm.css" />

<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.3/jquery.min.js" type="text/javascript"></script> 
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
<script type="text/javascript" src="/js/main.js"></script>
<script type="text/javascript" src="/js/jquery.lightboxLib.js"></script>
<script src="http://www.mitya.co.uk/scripts/source/99.js"></script>
<body>
<div class="container">
<h1>Thanks! We've contacted some users and you should hear from them really soon!</h1>
</div>
</body>
</html>