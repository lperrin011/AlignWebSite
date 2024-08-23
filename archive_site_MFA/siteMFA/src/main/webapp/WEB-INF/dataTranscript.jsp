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
		<!-- Ask to give the wav and the text files -->
		<div class="dataTranscript" id="data">
			<h2 title="Load your data" class="title">Data</h2>

			<form class="data-form" action="dataTranscript" method="post" enctype="multipart/form-data">
				<div class="data-items">
					<div class="data-item">
						<img src="./images/volume.png" alt="volume icon"> <label
							for="audio" class="data-details">Load audio file</label><br>
						<input class="data-input" type="file" name="audio" id="audio" title="Add the audio file"/>
					</div>
				</div>
				<div class="instructions">
					<p>By default, the Kaldi model used for generating the transcription file is the <a href="http://kaldi-asr.org/models/m13" class="doc-link">English model Librispeech</a>. </p>

					<p>If you want to use another Kaldi model for transcription, please check the box below.</p>
				</div>
				<div class="checkbox-part2">	
						<input type="checkbox" id="kaldi-text"> 
						<label for="kaldi-text" class="import-text">Add your own Kaldi model</label>
				</div>
					<div class="hidden-content3">
						<div class="instructions">
							<p>To do the transcription with Kaldi, you need to provide the following files :</p>
							<ul>
								<li>a <strong>chain model</strong> : the model used to create the decoding graph and the lexicon</li>
								<li>a <strong>ivector extractor</strong> : the model used to identify the different speakers</li>
								<li>a <strong>language model</strong> : the model used to statically predict the sequence of words.</li>
							</ul>
							<p>So please provide the url addresses where each file of your model can be found.</p>
							<div class="transcript-items">
								<div class="transcript-item">
									<label for="chain" class="kaldi-model">Chain model : </label>
									<input class="kaldi-url" type="url" name="chain" id="chain" title="URL of the chain model" placeholder="http://kaldi-asr.org/models/13/0013_librispeech_v1_main.tar.gz"/> <br>
								</div>
								<div class="transcript-item">
									<label for="extractor" class="kaldi-model">Ivector extractor : </label>
									<input class="kaldi-url" type="url" name="extractor" id="extractor" title="URL of the ivector extractor" placeholder="http://kaldi-asr.org/models/13/0013_librispeech_v1_extractor.tar.gz"/> <br>
								</div>
								<div class="transcript-item">
									<label for="lm" class="kaldi-model">Language model : </label>
									<input class="kaldi-url" type="url" name="lm" id="lm" title="URL of the lm model" placeholder="http://kaldi-asr.org/models/13/0013_librispeech_v1_lm.tar.gz"/> <br>
								</div>
							</div>
						</div>
						
					</div>
				
				 <input class="data-submit" type="submit" value="OK" title="Validate input files">
				<!-- <script src="js/hidden.js"></script> -->
				
			</form>
			
			<!-- Print an error or success message depending on if files are filled and the types are good -->
			<p>
			<span class="error-message">
			${ !empty isAudio ? "Audio file is missing" : ""} 
			${ !empty pbAudioType ? "The format of the audio file is not supported by the MFA. All audio formats are supported by the MFA but WAV format gives better precision." : ""} 
			${ !empty pbURL ? "One or more of the model urls are missing" : "" }</span>
			
			<span class="success-message">${ !empty end ? "Files successfully saved" : ""} </span>
			</p>

		</div>

	</section>
	
	

	<!-- In case provided files are good, display the model part -->
	<% String audio = (String) request.getAttribute("audio");%>
	<% if (audio != null && !audio.isEmpty()) { %>
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
					<!-- <script src="js/hidden.js"></script> -->
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
							If the dictionary has pronunciation probabilities, you can add a third column in the center.</p>
							<p>
							For more details and examples, please check the <a href="https://montreal-forced-aligner.readthedocs.io/en/latest/user_guide/dictionary.html" class="doc-link">MFA documentation</a>.
							</p>
						
							
								<input class="import-button" type="file" name="own-dict">
							
						</div>
					</div>
					<!-- 
					<script src="js/hidden.js"></script> -->
				</div>
				<p class="error-message">
				

				<button class="align-button" type="submit" title="Launch the alignment">Align</button>

			</form>
			
			<form class="update-form" action="update" method="post">
					<label for="update-button" class="update-text">Click here if you want to update the models and dictionaries lists </label>
					<input class="update-button" type="submit" value="Update" title="Update">	
				</form>

		</div>
	</section>

<% } %>
    
	<script src="js/hidden.js"></script>
	<footer class="footer"> </footer>

</body>
</html>