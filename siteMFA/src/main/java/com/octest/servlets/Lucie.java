package com.octest.servlets;

import jakarta.servlet.ServletException;



import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;


@WebServlet("/Lucie")
public class Lucie extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10 ko
	private static final int TAILLE_TAMPON = 4096; 
  
    public Lucie() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    this.getServletContext().getRequestDispatcher("/WEB-INF/lucie.jsp").forward(request, response);
		     
	}	


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		String directoryPath = "/home/lucie/mica/MFA/Chodroff/output/";
//		 File folder = new File(directoryPath);
//		 File[] files = folder.listFiles();
//		 File result = files[0];
		 
		String filePath = "/home/lucie/mica/MFA/Chodroff/output/ALL_049_F_ENG_ENG_HT1.TextGrid";
		File result = new File(filePath);
		
		 if ( !result.exists() ) {
			    response.sendError(HttpServletResponse.SC_NOT_FOUND);
			    return;
			}
		 
		 FileInputStream inStream = new FileInputStream(result);
		 
//		 String filePath = directoryPath + result.getName(); //   A DECOMMENTER !!!!!!!!!!!!!!!!
			 
		 // Define the type of the result in order to adapt the download
		 String type = getServletContext().getMimeType(filePath);
		 if ( type == null ) {
			    type = "application/octet-stream"; //give the default type in case of failure
			}
		 
		 //HTTP answer
		 response.reset();
		 response.setBufferSize( DEFAULT_BUFFER_SIZE );
		 response.setContentType(type);
		 response.setContentLength((int) result.length());
		     
		 //In order to say to the web browser that it has to download the file
		 String headerKey = "Content-Disposition";
		 String headerValue = String.format("attachment; filename=\"", result.getName() + "\"" );
		 response.setHeader(headerKey, headerValue);
		 
		 
		 
		 BufferedInputStream entree = null;
		 BufferedOutputStream sortie = null;
		 try {
		     /* Ouvre les flux */
		     entree = new BufferedInputStream( new FileInputStream( result ), DEFAULT_BUFFER_SIZE );
		     sortie = new BufferedOutputStream( response.getOutputStream(), DEFAULT_BUFFER_SIZE );

		     /* Lit le fichier et écrit son contenu dans la réponse HTTP */
		     byte[] tampon = new byte[TAILLE_TAMPON];
		     int longueur;
		     while ( ( longueur= entree.read( tampon ) ) > 0 ) {
		         sortie.write( tampon, 0, longueur );
			     request.setAttribute("longueur", longueur);
		     }

	         request.getRequestDispatcher("lucie.jsp").forward(request, response);
		 } catch (Exception e) {
	        	e.printStackTrace();
	            String errorMessage = "Erreur lors du téléchargement : " + e.getMessage();
	            request.setAttribute("errorMessage", errorMessage);
	            request.getRequestDispatcher("lucie.jsp").forward(request, response);
	        } finally {
			 try {
				 sortie.close();
			 } catch ( IOException e ) {
		            e.printStackTrace();
			 }
			 try {
				 entree.close();
			 } catch ( IOException e ) {
		            e.printStackTrace();
			 }
		 } 
		 
		    request.setAttribute("fileName", result.getName());
		    request.setAttribute("fileSize", result.length());
		    request.setAttribute("fileType", type);
		    request.setAttribute("filePath", filePath);
		 
	}

}
