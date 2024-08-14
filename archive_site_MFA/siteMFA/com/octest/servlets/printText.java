package com.octest.servlets;

import jakarta.servlet.ServletException;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;

import com.octest.beans.Interval;

@WebServlet("/api/textgrid")
public class printText extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public printText() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain; charset=UTF-8");
        
        
        ArrayList<Interval> wordIntervals = new ArrayList<>();
        ArrayList<Interval> phonemeIntervals = new ArrayList<>();
        
        
        try (BufferedReader reader = new BufferedReader(new FileReader("output/short.TextGrid"))) {
            StringBuilder sb = new StringBuilder();
            
//            ArrayList<StringBuilder> text = new ArrayList<>();
            String line;
            boolean words = false;
            boolean phones = false;
            boolean intervals = false;
            float maxWidth = 0;
            float min = 0;
            float max = 0;
            
            
            while ((line = reader.readLine()) != null) {
            	
            	if(line.contains("name = \"words\"")) {
            		words = true;
            		phones = false;
            	}
   
            	
            	if(line.contains("name = \"phones\"")) {
            		words = false;
            		phones = true;
            		sb.append("\n");
            	}
        	
            	
            	if(line.contains("intervals")) {
            		intervals = true;
            	}
            	
            	if(line.contains("item")) {
            		intervals = false;
            	}
            	
            	
//            	if(line.contains("intervals") && words == 1) {
//            		float min = 0;
//            		float max = 0;
//            		while(!line.contains("text")) {
//            			if(line.contains("xmin")) {
//            				min = Float.parseFloat(line.split("=")[1].trim());
//            				
//            			}
//            			if(line.contains("xmax")) {
//            				max = Float.parseFloat(line.split("=")[1].trim());
//            			}	
//
//            			
//            			line = reader.readLine();
//            			
//            		}
//            		
//            		if(line.contains("text")) {
//        				StringBuilder tmp = new StringBuilder();
//        				int begin = line.indexOf('\"');
//    	            	int end = line.indexOf('\"', begin + 1);
//    	            	String word = line.substring(begin + 1, end).trim();
//        				
//        				text.add(tmp);
//        			}
//            	}
            	
            	
            	if(line.contains("xmin") && words == false && phones == false) {
            		float xmin = Float.parseFloat(line.split("=")[1].trim());
            		maxWidth -= xmin;
            	}
            	
            	
            	if(line.contains("xmax") && words == false && phones == false) {
            		float xmax = Float.parseFloat(line.split("=")[1].trim());
            		maxWidth += xmax;
            		request.setAttribute("maxWidth", maxWidth);
            	}
            	
            	
            	if(line.contains("xmin") && intervals == true) {
            		min = Float.parseFloat(line.split("=")[1].trim());
            	}
            	
            	if(line.contains("xmax") && intervals == true) {
            		max = Float.parseFloat(line.split("=")[1].trim());
            	}

            	
            	if(line.contains("text") && intervals == true) {
            		int begin = line.indexOf('\"');
	            	int end = line.indexOf('\"', begin + 1);
	            	if(begin != -1 && end != -1) {
	            		String text = line.substring(begin + 1, end).trim();
	            		
	            		Interval inter = new Interval(text, min, max);
	            		
	            		if(words == true) {
	            			wordIntervals.add(inter);
	            		}
	            		
	            		if(phones == true) {
	            			phonemeIntervals.add(inter);
	            		}
	            		
	            		
//	            		if(!sentence.equals("")) {
//	            			if(words == true) {
//	            				sb.append("       ").append(sentence).append("       |");
//	            			}
//	            			else {
//	            				sb.append(" ").append(sentence).append(" |");
//	            			}
//	            		}
	            	}
	            	
            	}
            }
            
//            sb.append("\n");
//            response.getWriter().write(sb.toString());
//            response.getWriter().write(text.toString());
//        for(StringBuilder w : text) {
//        	response.getWriter().write(w.toString());
//
//        }
            
            request.setAttribute("wordIntervals", wordIntervals);
            request.setAttribute("phonemeIntervals", phonemeIntervals);

            
        } catch (IOException e) {
            e.printStackTrace();
        }

        request.getServletContext().getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
	}

}
