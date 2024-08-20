package com.octest.servlets;

import jakarta.servlet.ServletException;




import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.octest.beans.Interval;

public class Align extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final int TAILLE_TAMPON = 10240;
	public static final String INPUT_PATH = "input/";
	public static final String OUTPUT_PATH = "output/";
	public static final String MODEL_PATH = "/home/lucie/Documents/MFA/pretrained_models/acoustic/";
	public static final String DICT_PATH = "/home/lucie/Documents/MFA/pretrained_models/dictionary/";


	public Align() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
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
	
	// Same as function executeCommandsSequentially but adapted for model validation
	private boolean executeCommandsValidate(String[] commands) throws IOException, InterruptedException {
		String command = String.join(" && ", commands);
		boolean isValid = true;

		ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
		processBuilder.redirectErrorStream(true);

		Process process = processBuilder.start();
		System.out.println("in execute command");
		StringBuilder output = new StringBuilder();
		StringBuilder errorOutput = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				output.append(line).append("\n");
				// if the output contains False for some of the required elements, it is not valid
				 if (line.contains("False")) {
		                isValid = false;
		         }
			}

			while ((line = errorReader.readLine()) != null) {
				errorOutput.append(line).append("\n");
			}

			int exitCode = process.waitFor();
			if (exitCode != 0) {
				isValid = false;
			}
		} finally {
			process.getOutputStream().close();
			process.destroy();
		}

		System.out.println("Commande exécutée avec succès !");
		return isValid;	
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		 String webAppPath = getServletContext().getRealPath("/");
		
		 ///////// MODELS AND DICTIONARY LISTS //////////
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
        
        
        //////// COLLECT THE MODEL AND DICTIONARY CHOSEN ///////////////
		
		String model = "";
		String dict = "";
		boolean errorModel = false;
		boolean errorDict = false;
		
		//ATTENTION : get all selected files !
		 Part modelPart = request.getPart("own-model"); //only get the first part of all in own-model to verify it is not empty
		 String nomModel = getNomFichier(modelPart);
		if(!nomModel.isEmpty() && nomModel != null) {
			String modelName = "";
			Collection<Part> parts = request.getParts(); //get all the files added in order to know if there is just one or not
			List<Part> modelFiles = new ArrayList<>();
			for(Part part : parts) {
				if(part.getName().equals("own-model") && part.getSubmittedFileName() != null) {
					modelFiles.add(part);
				}
			}
			
			System.out.println("size : ");
			System.out.println(modelFiles.size());
			if(modelFiles.size() == 1) { //in case there is just the zip file
				//verify this is really a zip file and get its name
				Part zipPart = modelFiles.get(0); //get the only part
				String zipName = zipPart.getSubmittedFileName(); //get its name and extension
				if(zipName.split("\\.")[1].equals("zip")) {
					ecrireFichier(zipPart, zipName, MODEL_PATH);
					modelName = zipName.split("\\.")[0]; 
				}
				else {
					// If there is an error, display the hiddenData page
					System.out.println("RETURN");
					request.setAttribute("errorZip", "yes");
					this.getServletContext().getRequestDispatcher("/WEB-INF/hiddenData.jsp").forward(request, response);
					return;
				}		
			}
			
			else {
				//in case there are all the files of the model, we need the name of the model to create the zip file
				 modelName = request.getParameter("modelName");
				 if(modelName.equals("")) {
					System.out.println("RETURN");
					request.setAttribute("errorName", "yes");
					this.getServletContext().getRequestDispatcher("/WEB-INF/hiddenData.jsp").forward(request, response);
					return;
				 }
				 modelName = modelName.split("\\.")[0]; //in case an extension is given, we don't want it
				 
				 File uploadDir = new File(MODEL_PATH, modelName);
			        if (!uploadDir.exists()) {
			            uploadDir.mkdirs(); // Create directory if it does not exist already
			        }
			        
			        // Process and store uploaded files
			        for (Part part : request.getParts()) {
			            String fileName = part.getSubmittedFileName();
			            if (fileName != null && !fileName.trim().isEmpty()) {
			                File file = new File(uploadDir, fileName);
			                try (InputStream inputStream = part.getInputStream();
			                     FileOutputStream fos = new FileOutputStream(file)) {
			                    byte[] buffer = new byte[1024];
			                    int len;
			                    while ((len = inputStream.read(buffer)) > 0) {
			                        fos.write(buffer, 0, len);
			                    }
			                }
			            }
			        }
	
			        String zipFileName = modelName + ".zip";
			        File zipFile = new File(MODEL_PATH, zipFileName);
	
			        // Create a ZipOutputStream to write the ZIP
			        try (FileOutputStream fos = new FileOutputStream(zipFile);
			             ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos))) {
			            // Create the ZIP file of the provided directory
			        	zipDirectory(uploadDir, uploadDir.getName(), zos);
			        	
			        } catch (Exception e) {
			            e.printStackTrace();
			            response.getWriter().println("Une erreur s'est produite lors de la création du fichier ZIP.");
			            response.getWriter().println("Message d'erreur : " + e.getMessage());
			        }
		        }
			
				//// MODEL VALIDATION
		        boolean success;
		        String commands[] = { "source /home/lucie/miniconda3/etc/profile.d/conda.sh", "conda activate aligner",
						 "mfa model inspect acoustic " + modelName,
						 "conda deactivate"};
		        try {
					 success = executeCommandsValidate(commands);
					 if(success == false) { // If the model is not valid, we delete it
						 Path path = Paths.get(MODEL_PATH + modelName + ".zip");
					     try {
					            Files.delete(path);
					        } catch (IOException e) {
					            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting file: " + e.getMessage());
					        }
						 errorModel = true; // to send the error message later
						 request.setAttribute("errorModel", "yes");
					 }
					 else { //in case the model is valid
						 model = modelName;
					 }
				 } catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}       
		}
		
		Part dictPart = request.getPart("own-dict");
		 String nomDict = getNomFichier(dictPart);
		if(!nomDict.isEmpty() && nomDict != null) {
			boolean success;
			ecrireFichier(dictPart, nomDict, DICT_PATH);
			
			// DICT VALIDATION
			 String commands[] = { "source /home/lucie/miniconda3/etc/profile.d/conda.sh", "conda activate aligner",
					 "mfa model inspect dictionary " + DICT_PATH + nomDict,
					 "conda deactivate"};
			 try {
				 success = executeCommandsValidate(commands);		 
				 if(success == false) { // If the dictionary is not valid, we delete it
					 Path path = Paths.get(DICT_PATH + nomDict);
					 Files.deleteIfExists(path);
					 System.out.println("Delete dict");
					 errorDict = true;
					 request.setAttribute("errorDict", "yes");
				 }
				 else {
					 dict = nomDict.split("\\.")[0].trim(); // Remove the extension
				 }
			 } catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
		}
		
		//If there is no own model or dictionary, model and dict variables have not been modified
		if(model.equals("")) {
			model = request.getParameter("model");
		}
		if(dict.equals("")) {
			dict = request.getParameter("dict");
		}

		request.setAttribute("model", model);
		request.setAttribute("dict", dict);
		

		///////// ALIGNMENT WITH MFA ////////////
		
		if(errorDict || errorModel) { //If the user wanted to add its own model or dictionary but it failed
			System.out.println("RETURN");
			this.getServletContext().getRequestDispatcher("/WEB-INF/hiddenData.jsp").forward(request, response);
			return;
		}
		// If no error, launch the alignment
		try {
			System.out.println("IN ALIGNMENT !");
			File modelFile = new File(MODEL_PATH + model + ".zip");
			File dictFile = new File(DICT_PATH + dict + ".dict");


			if (!modelFile.isFile() && !dictFile.isFile()) { // check if it is not a directory and if it exists at the same time
				// if doesn't already in MFA folder, download them
				System.out.println("commande nn");
				String[] commands = { "source /home/lucie/miniconda3/etc/profile.d/conda.sh", "conda activate aligner",
						"mfa model download acoustic " + " " + model, "mfa model download dictionary " + " " + dict,
						"mfa align --clean " + webAppPath + INPUT_PATH + " " + dict + " " + model + " " + webAppPath + OUTPUT_PATH,
						"conda deactivate" };
				try {
					executeCommandsSequentially(commands);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}

			} else if (!modelFile.isFile() && dictFile.isFile()) { // if only the dictionary is present, download the model
				System.out.println("commande yn");
				String[] commands = { "source /home/lucie/miniconda3/etc/profile.d/conda.sh", "conda activate aligner",
						"mfa model download acoustic " + " " + model,
						"mfa align --clean " + webAppPath + INPUT_PATH + " " + dict + " " + model + " " + webAppPath + OUTPUT_PATH,
						"conda deactivate" };
				try {
					executeCommandsSequentially(commands);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}

			} else if (modelFile.isFile() && !dictFile.isFile()) { // if only the model is present, download the dictionary
				System.out.println("commande ny");
				String[] commands = { "source /home/lucie/miniconda3/etc/profile.d/conda.sh", "conda activate aligner",
						"mfa model download dictionary " + " " + dict,
						"mfa align --clean " + webAppPath + INPUT_PATH + " " + dict + " " + model + " " + webAppPath + OUTPUT_PATH,
						"conda deactivate" };

				try {
					executeCommandsSequentially(commands);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}

			} else { // if both the model and the dictionary are already download
				System.out.println("commande yy");
				String[] commands = { "source /home/lucie/miniconda3/etc/profile.d/conda.sh", "conda activate aligner",
						"mfa align --clean " + webAppPath + INPUT_PATH + " " + dict + " " + model + " " + webAppPath + OUTPUT_PATH,
						"conda deactivate" };
				try {
					System.out.println("try start");
					executeCommandsSequentially(commands);
					System.out.println("try end");
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}

			}

			String resultMessage = "Alignement terminé avec succès !";
			request.setAttribute("resultMessage", resultMessage);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Erreur lors de l'alignement : " + e.getMessage();
			request.setAttribute("errorMessage", errorMessage);
		}

		////////////// PRINT THE TEXT ////////////

		// Arrays that will help print the text with the appropriate width
		ArrayList<Interval> wordIntervals = new ArrayList<>();
		ArrayList<Interval> phonemeIntervals = new ArrayList<>();

		// find the result file in the output folder
		String directoryPath = webAppPath + OUTPUT_PATH;
		File folder = new File(directoryPath);
		File[] files = folder.listFiles();
		File result = files[0];
		try (BufferedReader reader1 = new BufferedReader(new FileReader(directoryPath + result.getName()))) {
			String line;
			boolean words = false;
			boolean phones = false;
			boolean intervals = false;
			float maxWidth = 0;
			float min = 0;
			float max = 0;

			// Read the result file
			while ((line = reader1.readLine()) != null) {

				if (line.contains("name = \"words\"")) { // from there, text, xmin and xmax will be added to the words array
					words = true;
					phones = false;
				}

				if (line.contains("name = \"phones\"")) { // from there, text, xmin and xmax will be added to the phonemes array
					words = false;
					phones = true;
				}

				if (line.contains("intervals")) { // this avoid the xmin and xmax at the beginning of a TextGrid file, the total length
					intervals = true;
				}

				if (line.contains("item")) { // avoid the total length of each item (words and phonemes in this case)
					intervals = false;
				}

				if (line.contains("xmin") && words == false && phones == false) {
					float xmin = Float.parseFloat(line.split("=")[1].trim());
					maxWidth -= xmin;
				}

				if (line.contains("xmax") && words == false && phones == false) {
					float xmax = Float.parseFloat(line.split("=")[1].trim());
					maxWidth += xmax;
					request.setAttribute("maxWidth", maxWidth);
				}

				if (line.contains("xmin") && intervals == true) {
					min = Float.parseFloat(line.split("=")[1].trim());
				}

				if (line.contains("xmax") && intervals == true) {
					max = Float.parseFloat(line.split("=")[1].trim());
				}

				if (line.contains("text") && intervals == true) {
					int begin = line.indexOf('\"');
					int end = line.indexOf('\"', begin + 1);
					if (begin != -1 && end != -1) {
						String text = line.substring(begin + 1, end).trim();

						// Here we have all information (xmin, xmax and the text) to complete an interval
						Interval inter = new Interval(text, min, max);
						// And then add it to the good array
						if (words == true) {
							wordIntervals.add(inter);
						}
						if (phones == true) {
							phonemeIntervals.add(inter);
						}
					}

				}
			}
			// Pass the completed arrays to the JSP in order to print it
			request.setAttribute("wordIntervals", wordIntervals);
			request.setAttribute("phonemeIntervals", phonemeIntervals);

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.getServletContext().getRequestDispatcher("/WEB-INF/align.jsp").forward(request, response);

	}
	
	
	
	 private static String getNomFichier( Part part ) {
	        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
	            if ( contentDisposition.trim().startsWith( "filename" ) ) {
	                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
	            }
	        }
	        return null;
	    }   
	
	 
	 private void ecrireFichier( Part part, String nomFichier, String chemin ) throws IOException {
		 	System.out.println("In ecrire fichier");
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
	 
	 private void zipDirectory(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
	        for (File file : folder.listFiles()) {
	            if (file.isDirectory()) {
	                // Recursively zip directories
	                zipDirectory(file, parentFolder + "/" + file.getName(), zos);
	            } else {
	                // Add files to ZIP
	                try (FileInputStream fis = new FileInputStream(file)) {
	                    ZipEntry zipEntry = new ZipEntry(parentFolder + "/" + file.getName());
	                    zos.putNextEntry(zipEntry);
	                    
	                    byte[] buffer = new byte[1024];
	                    int len;
	                    while ((len = fis.read(buffer)) > 0) {
	                        zos.write(buffer, 0, len);
	                    }
	                    zos.closeEntry();
	                }
	            }
	        }
	    }

}
