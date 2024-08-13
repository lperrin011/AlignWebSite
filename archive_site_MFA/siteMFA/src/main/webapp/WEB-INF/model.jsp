<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Lists</title>
</head>
<body>

	<form action="align" method="post" class="model-form">
		<select id="model" class="model-form" name="model">
	        <% 
	        	ArrayList<String> models = (ArrayList<String>) request.getAttribute("models");
	            for (String model : models) {
	        %>
	            <option value="<%= model %>"><%= model %></option>
	        <% 
	            } 
	        %>
	       </select> 
        </form>
        
        <% String debug = (String) request.getAttribute("debug");
        %>
        <%= debug %>
	
	

	<form action="align" method="post" class="model-form">
				<label for="model" class="model-details">Choose an acoustic model</label><br> 
				<select id="model" class="model-form" name="model">
					<option value="english_mfa">english_mfa</option>
					<option value="french">french</option>
					<option value="vietnamese">vietnamese</option>
				</select>

				<div class="model-choice">
					<input type="checkbox" id="import-text"> <label
						for="import-text" class="import-text">Import your own
						model</label>
					<div class="hidden-content">
						<div class="own-model">
							<input type="file">
							<button class="check-button" type="submit">Check your
								model</button>

						</div>
					</div>
				</div>

				<br> <label for="dict" class="model-details">Choose a pronunciation dictonary</label><br> <select id="dict"
					class="model-form" name="dict">
					<option value="english_us_mfa">english_us_mfa</option>
					<option value="french">french</option>
					<option value="vietnamese">vietnamese</option>
				</select>

				<div class="model-choice">
					<input type="checkbox" id="import-text"> <label for="import-text" class="import-text">Import your own dictionary</label>
					<div class="hidden-content">
						<div class="own-model">
							<input type="file">
							<button class="check-button" type="submit">Check your dictionary</button>
						</div>
					</div>
				</div>


				<button class="align-button" type="submit">Align</button>

			</form>
</body>
</html>