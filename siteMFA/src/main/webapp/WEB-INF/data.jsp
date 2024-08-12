<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
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
	<script src="js/app.js"></script>
	
	
	
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
		<div class="data" id="data">
			<h2 title="Load your data" class="title">Data</h2>

			<form class="data-form" action="data" method="post" enctype="multipart/form-data">
				<div class="data-items">
					<div class="data-item">
						<img src="./images/volume.png" alt="volume icon"> <label
							for="audio" class="data-details">Load audio file</label><br>
						<input class="data-input" type="file" name="audio" id="audio" title="Add the audio file"/>
					</div>

					<div class="data-item">
						<img src="./images/edit.png" alt="text icon"> <label
							for="text" class="data-details">Load transcription file</label><br>
						<input class="data-input" type="file" name="text" id="text" title="Add the transcription file">
					</div>
				</div>
				<br>
				<br> <input class="data-submit" type="submit" value="OK" title="Validate input files">
			</form>
			
			<!-- Print an error or success message depending on if files are filled -->
			<p>
			<span class="error-message">
			${ !empty isAudio && !empty isText ? "Please load data files" : ""} 
			${ empty isAudio && !empty isText ? "Transcription file is missing" : ""} 
			${ !empty isAudio && empty isText ? "Audio file is missing" : ""} 
			${ !empty fileNames ? "Audio and transcription files need to have the same name !" : ""}
			
			
			${ !empty pbAudioType ? "The format of the audio file is not supported by the MFA. All audio formats are supported by the MFA but WAV format gives better precision." : ""} 
			${ !empty pbTextType ? "The format of the transcription file is not supported by the MFA. Supported transcription formats are txt, lab and TextGrid." : ""}
			${ !empty pbAudioText ? "The formats of the transcription and the audio files are not supported by the MFA. " : ""} </span>
			<span class="success-message">${ !empty end ? "Files successfully saved" : ""} </span>
			</p>

		</div>

	</section>
	
	

	<% String audio = (String) request.getAttribute("audio");
	String text = (String) request.getAttribute("text"); %>
	<% if (audio != null && !audio.isEmpty() && text != null && !text.isEmpty()) { %>
	<section class="model" id="model">
		<div class="model-item">
			
			<form action="align" method="post" class="model-form" enctype="multipart/form-data">
				<label for="model" class="model-details">Choose an acoustic model</label><br> 
				<select id="model" class="model-select" name="model">
			        <% 
			        	ArrayList<String> models = (ArrayList<String>) request.getAttribute("models");
			            for (String model : models) {
			        %>
			            <option value="<%= model %>"><%= model %></option>
			        <% 
			            } 
			        %>
	       		</select> 

				<div class="model-choice">
					<div class="check-box">
						<input type="checkbox" id="import-text"> 
						<label for="import-text" class="import-text">Import your own
						model</label>
					</div>
					<div class="hidden-content">
						<div class="instructions">
							<p> Before importing your model, make sure it follows the <strong>Kaldi format</strong>.
								For that, you need to have at least the following files :
								</p>
								<ul>
									<li><code class="file-name">meta.json</code> or <span class="file-name">meta.yaml</span> that gives information about the model such as the version, the language, the type of features, the sample rate etc.</li>
									<li><span class="file-name">final.mdl</span> which is a binary file containing the final model and that can be read by tools like Kaldi or MFA.</li>
									<li><span class="file-name">tree</span> is the file that contains the decision tree in order to choose which acoustic state to put to each phoneme.</li>
									<li><span class="file-name">HCLG.fst</span> that contains the decoding graph.</li>
									<li><span class="file-name">graphemes.txt</span> that contains the list of the graphemes (most of the time letters) of the language.</li>
									<li><span class="file-name">phonemes.txt</span> that is the list of all the phonemes of the language.</li>
								</ul>
								<p>Your model can also contain other files such as the following ones for instance :</p>
								<ul>
									<li><span class="file-name">lda.mat</span> that contains the LDA transformation matrice in case it is used in the training of the model.</li>
									<li><span class="file-name">phone_lm.fst</span> contains the probabilities of phoneme associations.</li>
								</ul>
								<p>If you need more details on the Kaldi format, please refer to the <a href="http://kaldi-asr.org/doc/" class="doc-link">Kaldi documentation</a> 
								or follow this complete <a href="https://eleanorchodroff.com/tutorial/kaldi/index.html" class="doc-link">tutorial on Kaldi</a>. <br> <br>
								You can add the model either by adding the <strong>required files</strong> or by adding directly the <strong>zip file</strong> returned by Kaldi. 
								<strong>IMPORTANT :</strong> If you don't provide directly the zip file, please <strong>fill the name of your model.</strong> </p>
						</div>
						<div class="own-model">
							<input class="import-button" type="file" name="own-model" multiple>
							<label for="modelName">Model's name :</label>
							<input type="text" name="modelName" id="modelName">
							<!-- <button class="check-button" type="submit">Check your
								model</button> -->

						</div>
					</div>
					<script src="js/hidden.js"></script>
				</div>
				
				
				<br>
				<label for="dict" class="model-details">Choose a pronunciation dictionary</label><br> 
				<select id="dict" class="model-form" name="dict">
			        <% 
			        	ArrayList<String> dicts = (ArrayList<String>) request.getAttribute("dicts");
			            for (String dict : dicts) {
			        %>
			            <option value="<%= dict %>"><%= dict %></option>
			        <% 
			            } 
			        %>
	       		</select> 

				<div class="model-choice">
					<div class="checkbox-part">	
						<input type="checkbox" id="import-text2"> 
						<label for="import-text2" class="import-text">Import your own dictionary</label>
					</div>
					<div class="hidden-content2">
						<div class="instructions">
						
							<p>Before importing your own dictionary, make sure it has the good format. 
							A pronunciation dictionary is a <strong>.dict file</strong> that lists the different pronunciations of a language. It looks like the following example.<br> </p>
							
							<div class="example-container">
								<p class="dict-example"><span style="margin-right: 60px;">acceleration</span>  æ k s ɛ l ə ɹ ej ʃ ə n <br>
								<span style="margin-right: 60px;">acceleration</span>  ə k s ɛ l ə ɹ ej ʃ ə n <br>
								<span style="margin-right: 113px;"> these</span>	ð iː z <br>
								<span style="margin-right: 113px;"> these</span>	z iː z <br>
								<span style="margin-right: 113px;"> these</span>	d̪ iː z <br>
								</p>
							</div>
							
							<p>It has to contain <strong>2 columns</strong> separated by a tab : the left column is the <strong>list of words</strong> and the right column is the <strong>
							list of phonetic pronunciations</strong>. 
							If the dictionary has pronunciation probabilities, you can add a third column in the center.</p><br> <br>
							<p>
							For more details and examples, please check the <a href="https://montreal-forced-aligner.readthedocs.io/en/latest/user_guide/dictionary.html" class="doc-link">MFA documentation</a>.
							</p>
						
							
								<input class="import-button" type="file" name="own-dict">
							
						</div>
					</div>
					
					<script src="js/hidden.js"></script>
				</div>
				<p class="error-message">
			${ !empty errorModel ? "The model chosen is not valid" : ""} 
			${ !empty errorDict ? "The dictionary chosen is not valid" : ""} </p>
				

				<button class="align-button" type="submit" title="Launch the alignment">Align</button>

			</form>
			
			<form class="update-form" action="update" method="post">
					<label for="update-button" class="update-text">Click here if you want to update the models and dictionaries lists </label>
					<input class="update-button" type="submit" value="Update" title="Update">	
				</form>

		</div>
	</section>

<% } %>
    

	<footer class="footer"> </footer>

</body>
</html>