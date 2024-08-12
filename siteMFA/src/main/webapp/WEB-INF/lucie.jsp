<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Test Download</title>
	<link rel="stylesheet" href="style.css">
	<!-- Sites des polices de texte -->
	<link
		href="https://fonts.googleapis.com/css2?family=Roboto&display=swap"
		rel="stylesheet">
	<link rel="stylesheet"
		href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap">
	
	<script src="js/app.js"></script>
</head>
<body>

	<% 
	    String errorMessage = (String) request.getAttribute("errorMessage");
	    if (errorMessage != null) {
	%>
	    <p><%= errorMessage %></p>
	<% 
	    }
	%>

   <% 
Integer longueurObj = (Integer) request.getAttribute("longueur");
int longueur = (longueurObj != null) ? longueurObj.intValue() : 0; // Défaut à 0 si null
%>
<p>Longueur : <%= longueur %></p>


<h1>Informations sur le fichier téléchargé</h1>
    <p>Nom du fichier : <%= request.getAttribute("fileName") %></p>
    <p>Taille du fichier : <%= request.getAttribute("fileSize") %> octets</p>
    <p>Type MIME du fichier : <%= request.getAttribute("fileType") %></p>
    <p>Chemin du fichier : <%= request.getAttribute("filePath") %></p>
	
	
	<p>Nom du fichier : ${fileName}</p>
	
	
	<h1>LUCIE</h1>
	
	<form action="Lucie" method="post">
        	<button type="submit">Download the result</button>
    </form>
    
      <c:if test="${not empty errorMessage}">
        <p style="color: red;">${errorMessage}</p>
    </c:if>
    <c:if test="${empty errorMessage}">
        <p>Nom du fichier : ${fileName}</p>
        <p>Taille du fichier : ${fileSize} octets</p>
        <p>Type du fichier : ${fileType}</p>
        <p><a href="${pageContext.request.contextPath}/lucie">Télécharger le fichier</a></p>
    </c:if>
</body>
</html>
