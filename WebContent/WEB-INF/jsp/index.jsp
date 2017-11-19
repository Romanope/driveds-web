<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="resources/css/style.css">
		<script src="resources/js/dropzone.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Insert title here</title>
		
		<script type="text/javascript">
			
			function logar () {
				if (dadosValidos(false)) {
					document.forms[0].method = 'POST';
					document.forms[0].action = '/driveds-web/logar';
					document.forms[0].submit();					
				}
				document.forms[0].method = '';
				document.forms[0].action = '';
			}
			
			function cadastrar () {
				if (dadosValidos(true)) {
					document.forms[0].method = 'POST';
					document.forms[0].action = '/driveds-web/cadastrar';
					document.forms[0].submit();					
				}
				document.forms[0].method = '';
				document.forms[0].action = '';
			}
			
			function dadosValidos(validarEmail) {
				var login = document.getElementById('login').value;
				var senha = document.getElementById('senha').value;
				var email = document.getElementById('email').value;
				
				if (login != undefined && login != '' && senha != undefined && senha != '') {
					if (validarEmail) {
						if (email != undefined && email != '') {
							return true;
						} 
						return false;
					}
					return true;
				}
				return false;
			}
			
			function exibirCampoCadastro () {
				document.getElementById('divDadosCadastro').style.display = 'block';
				var btnCadastrar = "<button id='btnEntrar' style='width: 150px;' class='btn btn-primary' onClick='cadastrar()'>Cadastrar</button>"
				document.getElementById('divBtnCadastro').innerHTML = btnCadastrar;
				document.getElementById('divBody').style.height = '280px';
			}
			
			setTimeout(function () {
				document.getElementById('divErro').style.display = 'none';
			}, 150000);
			
		</script>
		<style>
			.center {
				margin-right: auto;
				margin-left: auto;
				width: 450px;
			}
			.ui-alert {
				background-color: #F2DEDE;
				padding: 20px;
				border-radius: 10px;
				margin-top: 20px;
				margin-bottom: 20px;
			}
			
			.ui-alert-message {
				color: #AF4341;
				font-weight: bold;
				font-size: 20px;
			}
		</style>
	</head>
	<body style="background-color: #F0F8FF">
		<div id="divErro" class='${ classErro }'>
			${msgErro}
		</div>
		<div id="divBody" class="jumbotron">
			<form>
				<h2>DriveDS</h2>
				<div  class="center">
					<input type="text" class="form-control" id="login" name="login" placeholder="Usuário" value="${ login }"/>
				</div>
				<div class="center">
					<input type="password" class="form-control" id="senha" name="senha" placeholder="Senha" value="${ senha }"/>
				</div>
				<div id="divDadosCadastro" style="display: none">
					<div class="center">
						<input type="text" class="form-control" id="email" name="email" placeholder="Email" value="${ email }"/>
					</div>
				</div>
			</form>
			<div>
				<div class="center" style="margin-top: 20px; width: 50%; float: left;">
					<button id="btnEntrar" style="width: 150px;" class="btn btn-primary" onClick="logar()">Entrar</button>
				</div>
				<div class="center" id="divBtnCadastro" style="margin-top: 20px; width: 50%; float: right;">
					<input type="button" class="btn btn-link" onclick="exibirCampoCadastro()" value="Cadastre-se agora!" />
				</div>
			</div>
		</div>
	</body>
</html>