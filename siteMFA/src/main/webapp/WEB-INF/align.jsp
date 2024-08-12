<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.octest.beans.Interval" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Phonetic forced-aligner</title>
	<link rel="stylesheet" href="style.css">
	<!-- Sites des polices de texte -->
	<link
		href="https://fonts.googleapis.com/css2?family=Roboto&display=swap"
		rel="stylesheet">
	<link rel="stylesheet"
		href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap">
	<!-- Site des icones -->
	<link rel="stylesheet"
		href="https://cdnjs.cloudflare.com/ajax/libs/fontisto/3.0.4/css/fontisto/fontisto.min.css">
	<link rel="stylesheet"
		href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
	<link rel="icon" href="./images/signal-alt-2.png">
	<!--  <script src="js/loading.js"></script>  -->
	
</head>
<body>
	<header class="header">
		<img src="./images/signal-alt-2.png" alt="logo" title="logo">
		<nav class="nav">
			<li><a href="index.html">Accueil</a></li>
			<li><a href="#data">Data</a></li>
			<li><a href="#model">Model</a></li>
			<li><a href="#result">Result</a></li>
		</nav>
	</header>

	<section class="top-page">
		<h1 class="big-title">Phonetic forced-aligner</h1>

	</section>

	
			
		<%-- 	<p>Modèle acoustique choisi : <%= request.getAttribute("model") %></p>
<p>Dictionnaire de prononciation choisi : <%= request.getAttribute("dict") %></p>


       <% if (request.getAttribute("resultMessage") != null) { %>
        <p style="color: green;"><%= request.getAttribute("resultMessage") %></p>
    <% } %>
    
    Afficher le message d'erreur
    <% if (request.getAttribute("errorMessage") != null) { %>
        <p style="color: red;"><%= request.getAttribute("errorMessage") %></p>
    <% } %> --%>
    	



	<!-- <form class="print-form" action="printText" method="get" enctype="multipart/form-data">
		<input class="print-button" type="submit">	
	</form> -->

	<section class="result" id="result">
		<h2 class="title-result">Result</h2>
		
		 <div class="audio-container"> <!-- Contains audio, text and buttons -->
        	<div class="container">  <!-- Contains audio and text -->
        	
        		
            	<div class="print-audio"></div>  <!--Will be add by js-->
    			
    			<!-- PRINT THE WORDS -->
    		
    			<div class="print-text"> 
      			 <%
			            ArrayList<Interval> wordIntervals = (ArrayList<Interval>) request.getAttribute("wordIntervals");      			 		
			            
			    		if(wordIntervals != null){
				            for (Interval interval : wordIntervals) {
				            	float xmin = interval.getXmin();
				            	float xmax = interval.getXmax();
				            	float longueur = xmax-xmin; /* Calculates the duration */
				            	float longueurTotale = (float) request.getAttribute("maxWidth");
					        %>
					            <!-- Adapts the width on how long is the text and print the text -->
					             <div style="width: <%= (longueur/longueurTotale)*100%>%; display: flex; justify-content: center; align-items: center; 
					             border-right: 2px solid black; height: 40px;"><%= interval.getText() %> </div>
					        <%
			                }
			    		}
			            
			       %>
			     </div>
			     
			     <!-- PRINT THE PHONEMES -->
			     <div class="print-text"> 
      			 <%
			            ArrayList<Interval> phonemeIntervals = (ArrayList<Interval>) request.getAttribute("phonemeIntervals");
      			 		       
			    		if(phonemeIntervals != null){
				            for (Interval phoneme : phonemeIntervals) {
				            	float xmin = phoneme.getXmin();
				            	float xmax = phoneme.getXmax();
				            	float longueur = xmax-xmin;
				            	float longueurTotale = (float) request.getAttribute("maxWidth");
					        %>
					            
					             <div style="width: <%= (longueur/longueurTotale)*100 %>%; display: flex; justify-content: center; align-items: center;
					              border-right: 2px solid black; height: 40px;"><%= phoneme.getText() %> </div>
					                 
					        <%
			                }
			    		}     
			        %>
        		</div>
        	</div>
        	
        	<!-- THE BUTTONS -->
            <div class="buttons">
                <span class="play-btn btn">
                    <i class="fa-solid fa-play"></i>
                    <i class="fa-solid fa-pause"></i> 
                </span>
    
                <span class="stop-btn btn">
                    <i class="fa-solid fa-stop"></i>
                </span>
    
                <span class="mute-btn btn">
                    <i class="fa-solid fa-volume-up"></i>
                    <i class="fa-solid fa-volume-mute"></i>
                </span>
    
                <input type="range" min="0" max="1" step="0.1" value="0.5" class="volume-slider">
            </div>
        </div>
    
        <script src="https://unpkg.com/wavesurfer.js@7"></script> 
        <script src="js/audio.js"></script>
		
		
    	<form action="Download" method="get">
        	<button type="submit" class="download-button">Download the result</button>
    	</form>
    	
	</section>

	<footer class="footer"> </footer>

</body>
</html>