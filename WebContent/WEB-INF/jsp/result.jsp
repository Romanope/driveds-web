<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Cadastrado com sucesso!</title>
	</head>
	<body>
		<h2>Submitted file Information</h2>
      	<table>
	         <tr>
	            <td>Nome</td>
	            <td>${nome}</td>
	         </tr>
	         <tr>
	            <td>Data</td>
	            <td>${data}</td>
	         </tr>
	         <tr>
	            <td>Diretório</td>
	            <td>${diretorioCompleto}</td>
	         </tr>
      	</table>
	</body>
</html>