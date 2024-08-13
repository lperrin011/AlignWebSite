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


public class Download extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10 ko
	private static final int TAILLE_TAMPON = 4096; 
  
    public Download() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// String directoryPath = "/home/lucie/eclipse-workspace/siteMFA/src/main/webapp/output/";
		 String webAppPath = getServletContext().getRealPath("/");
		 String directoryPath = webAppPath + "output/";
		 String directoryPath = "/home/lucie/eclipse-workspace/siteMFA/src/main/webapp/output/";
		 File folder = new File(directoryPath);
		 File[] files = folder.listFiles();
		 File result = files[0];
		 
		 if ( !result.exists() ) {
			    response.sendError(HttpServletResponse.SC_NOT_FOUND);
			    return;
			}
		 
		 FileInputStream inStream = new FileInputStream(result);
		 
		 String filePath = directoryPath + result.getName();
			 
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
		 String headerValue = String.format("attachment; filename=\"%s\"", result.getName() );
		 response.setHeader(headerKey, headerValue);
		 
		 
		 try (BufferedInputStream entree = new BufferedInputStream( new FileInputStream( result ), TAILLE_TAMPON );
		     BufferedOutputStream sortie = new BufferedOutputStream( response.getOutputStream(), TAILLE_TAMPON ) ){
		     

		     /* Lit le fichier et écrit son contenu dans la réponse HTTP */
		     byte[] tampon = new byte[TAILLE_TAMPON];
		     int longueur;
		     while ( ( longueur= entree.read( tampon ) ) > 0 ) {
		         sortie.write( tampon, 0, longueur );
		     }
		 }  catch (Exception e) {
	        	e.printStackTrace();
	            String errorMessage = "Erreur lors de l'alignement : " + e.getMessage();
	            request.setAttribute("errorMessage", errorMessage);
	        }

		 
		 
		    request.setAttribute("fileName", result.getName());
		    request.setAttribute("fileSize", result.length());
		    request.setAttribute("fileType", type);
		    request.setAttribute("filePath", filePath);
		    
		    this.getServletContext().getRequestDispatcher("/WEB-INF/align.jsp").forward(request, response);
		 
		     
//		 OutputStream outStream = response.getOutputStream();
//		 
//		 byte[] buffer = new byte[4096];
//		 int bytesRead = -1;
//		 
//		 // Read the file and put it in the output stream
//		 while ((bytesRead = inStream.read(buffer)) != -1) {
//			 outStream.write(buffer, 0, bytesRead);
//		 }	
//	
//		 inStream.close();
//		 outStream.close();
//		     
//		 return;
		     
	}	


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	     
	}

}
