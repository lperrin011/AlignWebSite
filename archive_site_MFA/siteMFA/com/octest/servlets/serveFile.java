package com.octest.servlets;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.File;

public class serveFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public serveFile() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter("fileName");
        String filePath = getServletContext().getRealPath("/input/") + File.separator + fileName;
        File file = new File(filePath);

        if (file.exists()) {
            response.setContentType("audio/wav");
            response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                 BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
