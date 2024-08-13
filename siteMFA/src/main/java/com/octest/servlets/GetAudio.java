package com.octest.servlets;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class GetAudio extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetAudio() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");

		 String webAppPath = getServletContext().getRealPath("/");
		String directoryPath = webAppPath + "input/";
		String directoryPath = "/home/lucie/eclipse-workspace/siteMFA/src/main/webapp/input/";
		 File folder = new File(directoryPath);
		 File[] files = folder.listFiles();
		 File inputFile1 = files[0];
		 Path filePath1 = Paths.get(directoryPath, inputFile1.getName());
		 String type1 = Files.probeContentType(filePath1);
		 File inputFile2 = files[1];
		 Path filePath2 = Paths.get(directoryPath, inputFile2.getName());
		 String type2 = Files.probeContentType(filePath2);
		 
		 if( inputFile1.exists() && inputFile2.exists()) {
			 if (type1.contains("audio")){
				 response.getWriter().write(inputFile1.getName());
			 }
			 else if(type2.contains("audio")) {
				 response.getWriter().write(inputFile2.getName());
			 }
			  
		
		 }
//		
//		response.getWriter().write("short.wav");
		 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
