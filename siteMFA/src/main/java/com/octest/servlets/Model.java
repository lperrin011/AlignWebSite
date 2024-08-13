package com.octest.servlets;

import jakarta.servlet.ServletException;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;


public class Model extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Model() {
        super();
        // TODO Auto-generated constructor stub
    }

		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			ArrayList<String> models = new ArrayList<>();
			String debug ="";
			
			String webAppPath = getServletContext().getRealPath("/");
			String filePath = webAppPath + "models.txt";  
	        BufferedReader reader = null;

	        try {
	            reader = new BufferedReader(new FileReader(filePath));
	            String line;

	            while ((line = reader.readLine()) != null) {
	            	if (line.contains(":")) {
		            	int comma1 = line.indexOf('\'');
		            	int comma2 = line.indexOf('\'', comma1 + 1);
		            	debug = debug + line + " " + comma1 +" " + comma2 + "                         ";
		            	if(comma1 != -1 && comma2 != -1) {
		            		String model = line.substring(comma1 + 1, comma2).trim();
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
	  

		    this.getServletContext().getRequestDispatcher("/WEB-INF/model.jsp").forward(request, response);
		}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
