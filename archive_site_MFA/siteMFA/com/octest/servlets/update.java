package com.octest.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            
            String line2;
            StringBuilder output = new StringBuilder();
            while ((line2 = reader2.readLine()) != null) {
                output.append(line2).append("\n");
            }
            StringBuilder errorOutput = new StringBuilder();
            while ((line2 = errorReader.readLine()) != null) {
                errorOutput.append(line2).append("\n");
            }
            
            // Wait until the script is done
            int exitCode = process.waitFor();
            
            // Print the output
            System.out.println("Output:");
            System.out.println(output);
            System.out.println("Errors:");
            System.out.println(errorOutput);
            System.out.println("Exited with code: " + exitCode);
            
            
            ///////// LISTE DES MODELES ET DICTIONNAIRES //////////
            // Useful here in case there is an error so we need to print the data page
    		
            /* Models */
            ArrayList<String> models = new ArrayList<>();
    		String filePath = webAppPath + "models.txt";  
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
    		String filePathDict = webAppPath + "dictionaries.txt";  
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
            
            this.getServletContext().getRequestDispatcher("/WEB-INF/hiddenData.jsp").forward(request, response);
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