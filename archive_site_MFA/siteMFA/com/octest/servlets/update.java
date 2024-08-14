package com.octest.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class update extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    public update() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String[] commands = {
//                "source /home/lucie/miniconda3/etc/profile.d/conda.sh",
//                "conda activate aligner",
//                "mfa model download acoustic > models.txt",
//                "mfa model download dictionary > dictionaries.txt",
//                "conda deactivate"
//            };
//		
//        try {
//           executeCommandsSequentially(commands);
//        } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            
//        }
//		this.getServletContext().getRequestDispatcher("/WEB-INF/data.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//        String scriptPath = "./lists.sh";
//
//        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", scriptPath);
		
		String webAppPath = getServletContext().getRealPath("/");
		String scriptPath = webAppPath + "lists.sh";
		
		ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", scriptPath);
        
        try {
            Process process = processBuilder.start();
            
            // output answer of the shell script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
            
            // Wait until the script is done
            int exitCode = process.waitFor();
            
            // Print the output
            System.out.println("Output:");
            System.out.println(output);
            System.out.println("Errors:");
            System.out.println(errorOutput);
            System.out.println("Exited with code: " + exitCode);
            
            this.getServletContext().getRequestDispatcher("/WEB-INF/data.jsp").forward(request, response);
//            // Envoyez la réponse au client
//            response.setContentType("text/plain");
//            response.getWriter().write("Script executed with exit code: " + exitCode + "\nOutput:\n" + output + "\nErrors:\n" + errorOutput);
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error executing script: " + e.getMessage());
        }
	}

	
	private void executeCommandsSequentially(String[] commands) throws IOException, InterruptedException {
	    String command = String.join(" && ", commands);

	    ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);
	    processBuilder.redirectErrorStream(true);

	    try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
            
            int exitCode = process.waitFor();
            System.out.println("\nExited with code: " + exitCode);
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
	   

	    System.out.println("Commande exécutée avec succès : ");
	    
	}
	
}