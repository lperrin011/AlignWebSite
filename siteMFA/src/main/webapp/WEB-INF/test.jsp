<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Test JSP</title>
</head>
<body>

	<p>${ empty nom ? "" : nom }</p>

	<p>
		<%@ include file="menu.jsp"%>
	</p>
	<p>
		<%
			String variable = (String) request.getAttribute("variable"); 
			out.println(variable);
		%>
	</p>
	<p>Bonjour ${ empty name ? "" : name }</p>
	<p>${ auteur.nom } ${ auteur.prenom } ${ auteur.actif }</p>

	<p>${ empty form ? "" : form.resultat }</p>

	<form action="bonjour" method="post">
		<label for="nom">Nom:</label> <input type="text" id="nom" name="nom" />
		<input type="submit" />
	</form>


	<!-- 	<form action="bonjour" method="post">
		<label for="login">Enter your login</label>
		<input type="text" id="login" name="login">
		
		<label for="pass">Enter your password</label>
		<input type="password" id="pass" name="pass">
		
		<input type="submit"/>
	</form> -->

	${ !empty fichier ? "" : "bien upload" }
	<form method="post" action="bonjour" enctype="multipart/form-data">
		<p>
			<label for="description">Description du fichier : </label> <input
				type="text" name="description" id="description" />
		</p>
		<p>
			<label for="fichier">Fichier à envoyer : </label> <input type="file"
				name="fichier" id="fichier" />
		</p>

		<input type="submit" />
	</form>

</body>
</html>