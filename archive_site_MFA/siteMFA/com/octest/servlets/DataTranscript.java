package com.octest.servlets;

import jakarta.servlet.ServletException;



import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.*;


public class DataTranscript extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public static final int TAILLE_TAMPON = 10240;
    public static final String INPUT_PATH = "input/";
    public static final String OUTPUT_PATH = "output/";
    
       
 
    public DataTranscript() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/WEB-INF/dataTranscript.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	    String webAppPath = getServletContext().getRealPath("/");
	    System.out.println("webAppPath : ");
	    System.out.println(webAppPath);
		
		//Delete existing files in input and output directories
		File directory = new File(webAppPath + INPUT_PATH);
        if (directory.exists() && directory.isDirectory()) {
            deleteDirectoryContents(directory);
        } 

        directory = new File(webAppPath + OUTPUT_PATH);
        if (directory.exists() && directory.isDirectory()) {
            deleteDirectoryContents(directory);
        } 
        
        // Get audio file information
        Part part = request.getPart("audio");
        String nomFichier = getNomFichier(part);
        String type = part.getContentType();
       
        
        //////// VERIFICATIONS ///////////
        
        //Verify if data as been provided
        if (nomFichier == null || nomFichier.isEmpty()) {
        	//Figure out which files are missing
        	if(nomFichier == null || nomFichier.isEmpty()){
        		request.setAttribute("isAudio", "no");
        	}
        }
        
      
        else { //In case audio file have been provided
        	boolean audioType = false;
        	
        	//Verifying types
        	//Audio
        	if(type.contains("audio")) { //verify the MIME type
        		audioType = true;
        	}
        	else { //In case the MIME type is not recognized by the server
        		String extension = nomFichier.split("\\.")[1].trim(); //take the extension of the file
        		String[] audioExtensions = {"wav", "mp3", "aac", "flac", "ogg", "wma", "alac", "aiff", "pcm", "amr", "opus", "mid", "midi", "ar"}; //supported extensions
        		if(Arrays.asList(audioExtensions).contains(extension)) { //verifying the extension is supported
        			audioType = true;
        		}
        	}
        	
        	
    		/////// SAVING AUDIO /////////
        	
        	if(audioType == true) { //In case the type is supported, we can write it
        		String nomChamp = part.getName();

                // Function that write the file
                ecrireFichier(part, nomFichier, webAppPath + INPUT_PATH);

                // Allows to print the name of the file given by the user on the page and to display the model part of the page
                request.setAttribute(nomChamp, nomFichier);
        	}
        	
        	//Print error message
        	else if(audioType == false) {
        		request.setAttribute("pbAudioType", "y");
        	}
        	//Print success message
        	else {
        		request.setAttribute("end", "ok");
        	}
        	
        	
        	/////// TRANSCRIPTION //////
        	
        	String chain = request.getParameter("chain");
        	String extractor = request.getParameter("extractor");
        	String lm = request.getParameter("lm");
        	if(chain != null || extractor != null || lm != null) {
        		if(chain != null && extractor != null && lm != null) {
        			// launch the command with the urls in parameter
        			String[] commands = { "cd /home/lucie/kaldi/egs/kaldi-asr-tutorial/s5",
    						"python3 main.py " + webAppPath + INPUT_PATH + " " + chain + " " + extractor + " " + lm }; 
    				try {
    					executeCommandsSequentially(commands);
    				} catch (IOException | InterruptedException e) {
    					e.printStackTrace();
    				}    
        		}
        		else { // not all needed urls have been given
        			request.setAttribute("pbURL", "y");
        			this.getServletContext().getRequestDispatcher("/WEB-INF/dataTranscript.jsp").forward(request, response);
        			return;
        		}
        	}
        	else {
	        	String[] commands = { "cd /home/lucie/kaldi/egs/kaldi-asr-tutorial/s5",
						"python3 main.py " + webAppPath + INPUT_PATH }; 
				try {
					executeCommandsSequentially(commands);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}    
        	}
			
			//Need to remove the name of the file from the transcription file
			String transcriptPath = webAppPath + INPUT_PATH + nomFichier.split("\\.")[0].trim() + ".txt";  

	        try (BufferedReader transcriptReader = new BufferedReader(new FileReader(transcriptPath))) {
	            String line2; 
	            while ((line2 = transcriptReader.readLine()) != null) {
	            	// Need to parse after the first space after the last /
	            	 int lastSlashIndex = line2.lastIndexOf('/');
	            	 if (lastSlashIndex != -1) {
	            		 String firstParse = line2.substring(lastSlashIndex + 1).trim(); //only the name of file remains

	            		 int firstSpaceIndex = firstParse.indexOf(' ');
	            	     if (firstSpaceIndex != -1) {
	            	    	 String sentence = firstParse.substring(firstSpaceIndex + 1).trim(); //Only the sentence remains
	            	    	 Files.write(Paths.get(transcriptPath), sentence.getBytes());
	            	      }
	            	 }	            	  
	            }	          
	        }
        }
             
      
      
        ///////// LISTE OF MODELS ET DICTIONARIES //////////
         
        /* Models */
        ArrayList<String> models = new ArrayList<>();
		String filePath = webAppPath + "models.txt";  
        BufferedReader reader = null;


        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;

           
            while ((line = reader.readLine()) != null) {
            	// Get the name of the model
            	if (line.contains(":")) {
	            	int comma1 = line.indexOf('\'');
	            	int comma2 = line.indexOf('\'', comma1 + 1);
	            	if(comma1 != -1 && comma2 != -1) {
	            		String model = line.substring(comma1 + 1, comma2).trim();
	            		// Write the model in the tab
	            		models.add(model);
	            	}
            	}
            }

            request.setAttribute("models", models);
            
        } catch (IOException e) {
            e.printStackTrace();
            response.getWriter().write("Error reading file: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }  
  
        
        
        /* Dictionary */
        ArrayList<String> dicts = new ArrayList<>();
		String filePathDict = webAppPath + "dictionaries.txt";  
        BufferedReader readerDict = null;

        try {
            readerDict = new BufferedReader(new FileReader(filePathDict));
            String line;
            
            while ((line = readerDict.readLine()) != null) {
            	if (line.contains(":")) {
	            	int comma1 = line.indexOf('\'');
	            	int comma2 = line.indexOf('\'', comma1 + 1);
	            	if(comma1 != -1 && comma2 != -1) {
	            		String dict = line.substring(comma1 + 1, comma2).trim();
	            		dicts.add(dict);
	            	}
            	}
            }

            request.setAttribute("dicts", dicts);
            
        } catch (IOException e) {
            e.printStackTrace();
            response.getWriter().write("Error reading file: " + e.getMessage());
        } finally {
            if (readerDict != null) {
                try {
                    readerDict.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }  
		
		this.getServletContext().getRequestDispatcher("/WEB-INF/dataTranscript.jsp").forward(request, response);
	}
	
	
	private void deleteDirectoryContents(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectoryContents(file);
                }
                file.delete();
            }
        }
    }

	
	// Parse the file in order to extract the name of it
	 private static String getNomFichier( Part part ) {
	        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
	            if ( contentDisposition.trim().startsWith( "filename" ) ) {
	                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
	            }
	        }
	        return null;
	    }   
	
	 
	 private void ecrireFichier( Part part, String nomFichier, String chemin ) throws IOException {
	        BufferedInputStream entree = null;
	        BufferedOutputStream sortie = null;
	        try {
	            entree = new BufferedInputStream(part.getInputStream(), TAILLE_TAMPON);
	            sortie = new BufferedOutputStream(new FileOutputStream(new File(chemin + nomFichier)), TAILLE_TAMPON);

	            byte[] tampon = new byte[TAILLE_TAMPON];
	            int longueur;
	            while ((longueur = entree.read(tampon)) > 0) {
	                sortie.write(tampon, 0, longueur);
	            }
	        } finally {
	            try {
	                sortie.close();
	            } catch (IOException ignore) {
	            }
	            try {
	                entree.close();
	            } catch (IOException ignore) {
	            }
	        }
	    }
	 
	// Execute the commands and return true if the execution has succeeded and false otherwise
		private boolean executeCommandsSequentially(String[] commands) throws IOException, InterruptedException {
			// Concatenate the several commands
			String command = String.join(" && ", commands);
			boolean success = true;

			ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command); //it is shell commands
			processBuilder.redirectErrorStream(true);

			// launch the command with processBuilder
			Process process = processBuilder.start();
			System.out.println("in execute command"); //debugging
			StringBuilder output = new StringBuilder();
			StringBuilder errorOutput = new StringBuilder();

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
				String line;
				//get the output
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
					output.append(line).append("\n");
				}

				while ((line = errorReader.readLine()) != null) {
					errorOutput.append(line).append("\n");
				}

				int exitCode = process.waitFor();
				if (exitCode != 0) {
					success = false;
				}
			} finally {
				// Ensure that the output stream is closed and the process is destroyed
				process.getOutputStream().close();
				process.destroy();
			}

			System.out.println("Commande exécutée avec succès !");
			return success;
			

		}

}

