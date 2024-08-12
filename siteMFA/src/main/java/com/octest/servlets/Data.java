package com.octest.servlets;

import jakarta.servlet.ServletException;


//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
//import java.io.IOException;
//import java.io.InputStream;


public class Data extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public static final int TAILLE_TAMPON = 10240;
    public static final String CHEMIN_FICHIERS = "/home/lucie/eclipse-workspace/siteMFA/src/main/webapp/input/";
    public static final String CHEMIN_SORTIE = "/home/lucie/eclipse-workspace/siteMFA/src/main/webapp/output/";
    
       
 
    public Data() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/////// ENREGISTREMENT DES FICHIERS /////////
		
		//Delete existing files in input and output directories
		File directory = new File(CHEMIN_FICHIERS);
        if (directory.exists() && directory.isDirectory()) {
            deleteDirectoryContents(directory);
        } 

        directory = new File(CHEMIN_SORTIE);
        if (directory.exists() && directory.isDirectory()) {
            deleteDirectoryContents(directory);
        } 
        
        // AUDIO
        Part part = request.getPart("audio");
        
        String nomFichier = getNomFichier(part);
        request.setAttribute("name", nomFichier);
        
        String type = part.getContentType();
        
        // TEXT
        Part part2 = request.getPart("text");
        
        String type2 = part2.getContentType();
        
        String nomFichier2 = getNomFichier(part2);
        request.setAttribute("name2", nomFichier2);
       
        
        //Verify if data as been provided
        if (nomFichier == null || nomFichier.isEmpty() || nomFichier2 == null || nomFichier2.isEmpty()) {
        	//Figure out which files are missing
        	
        	if(nomFichier == null || nomFichier.isEmpty()){
        		request.setAttribute("isAudio", "no");
        	}
        	if(nomFichier2 == null || nomFichier2.isEmpty()) {
        		request.setAttribute("isText", "no");
        	}
        }
        
        //Verify audio and transcription files have the same name -> necessary for MFA !
        else if(!nomFichier.split("\\.")[0].equals(nomFichier2.split("\\.")[0])) {
        	request.setAttribute("fileNames", "no");
        }
        
        else { //In case data files have been provided
        	//Verifying types
        	boolean audioType = false;
        	boolean textType = false;
        	
        	//AUDIO
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
        	
        	//TEXT
        	String textExtension = nomFichier2.split("\\.")[1].trim(); 
        	//Only verify the extension because few possibilities
        	if(textExtension.equals("TextGrid") || textExtension.equals("txt") || textExtension.equals("lab")) {
        		textType = true;
        	}
        	
        	
        	if(audioType == true && textType == true) { //In case both types are supported, we can write them
        		String nomChamp = part.getName();
            	String nomChamp2 = part2.getName();

                // Function that write the file
                ecrireFichier(part, nomFichier, CHEMIN_FICHIERS);
                ecrireFichier(part, nomFichier, "/home/lucie/eclipse-workspace/siteMFA/src/main/webapp/");
                ecrireFichier(part2, nomFichier2, CHEMIN_FICHIERS);

                // Allows to print the name of the file given by the user on the page and to display the model part of the page
                request.setAttribute(nomChamp, nomFichier);
                request.setAttribute(nomChamp2, nomFichier2);
        	}
        	
        	//Print error message
        	if(audioType == false && textType == false) {
        		request.setAttribute("pbAudioText", "y");
        	}
        	else if(audioType == false) {
        		request.setAttribute("pbAudioType", "y");
        	}
        	else if(textType == false) {
        		request.setAttribute("pbTextType", "y");
        	}
        	//Print success mssage
        	else {
        		request.setAttribute("end", "ok");
        	}
        	
        }
        
        
        
        
      
      
        ///////// LISTE DES MODELES ET DICTIONNAIRES //////////
         
        /* Models */
        ArrayList<String> models = new ArrayList<>();
		String filePath = "/home/lucie/eclipse-workspace/siteMFA/src/main/webapp/models.txt";  
        BufferedReader reader = null;
		String debug ="";


        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;

           
            while ((line = reader.readLine()) != null) {
            	// Get the name of the model
            	if (line.contains(":")) {
	            	int comma1 = line.indexOf('\'');
	            	int comma2 = line.indexOf('\'', comma1 + 1);
	            	debug = debug + line + " " + comma1 +" " + comma2 + " ";
	            	if(comma1 != -1 && comma2 != -1) {
	            		String model = line.substring(comma1 + 1, comma2).trim();
	            		debug+=model;
	            		// Write the model in the tab
	            		models.add(model);
	            	}
            	}
            }

            request.setAttribute("models", models);
            request.setAttribute("debug", debug);
            
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
		String filePathDict = "/home/lucie/eclipse-workspace/siteMFA/src/main/webapp/dictionaries.txt";  
        BufferedReader readerDict = null;
        debug="";

        try {
            readerDict = new BufferedReader(new FileReader(filePathDict));
            String line;
            
            while ((line = readerDict.readLine()) != null) {
            	if (line.contains(":")) {
	            	int comma1 = line.indexOf('\'');
	            	int comma2 = line.indexOf('\'', comma1 + 1);
	            	debug = debug + line + " " + comma1 +" " + comma2 + " ";
	            	if(comma1 != -1 && comma2 != -1) {
	            		String dict = line.substring(comma1 + 1, comma2).trim();
	            		debug+=dict;
	            		dicts.add(dict);
	            	}
            	}
            }

            request.setAttribute("dicts", dicts);
            request.setAttribute("debug", debug);
            
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
		
		this.getServletContext().getRequestDispatcher("/WEB-INF/data.jsp").forward(request, response);
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

}

