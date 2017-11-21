<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="resources/css/style.css">
<link rel="stylesheet" type="text/css" href="resources/css/jquery-ui.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="resources/js/dropzone.js"></script>
<script src="resources/js/jquery-3.2.1.min.js"></script>
<script src="resources/js/jquery-ui.min.js"></script>
<script src="resources/js/bootstrap.min.js"></script>

<title>DriveDS</title>
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

.ui-success-message {
	color: #09F23C;
	font-weight: bold;
	font-size: 20px;
}

.dropzone {
	background: #DCDCDC;
	text-align: center;
	margin-top: 50px;
	margin-bottom: 30px;
	height: 80px;
}
</style>
<script type="text/javascript">
	var usuarioSelecionados = [];
	function removerArquivo( nome ) {
		document.getElementById('nomeArquivo').value = nome;
		var form = document.getElementById('formNameFile');
		form.action = "/driveds-web/apagarArquivo";
		form.method = "POST";
		form.submit();
	}
	
	function download (nomeArquivo, usuario) {
		document.getElementById('nomeArquivo').value = nomeArquivo;
		document.getElementById('usuario').value = usuario;
		var form = document.getElementById('formNameFile');
		form.action = "/driveds-web/download";
		form.method = "GET";
		form.submit();
	}
	function filtrar (event) {
		if (document.getElementById('filtro').value.length > 0 && isPressEnter(event)) {
			var form = document.getElementById('formNameFile');
			form.action = "/driveds-web/consultar";
			form.method = "GET";
			form.submit();
		}
	}
	
	function isPressEnter(event) {
		if (event.keyCode == 13) {
			return true;
		}
		return false;
	}
	
	function openModal (chaveArquivo) {
		$("#myModal").dialog({
			autoOpen: false,
			modal: true,
			width: 500
		});

		setFileName(chaveArquivo);

		$("#myModal").dialog("open");
		
	}
	
	function addUser() {
		
		if ($("#users option:selected").val() > 0) {
			var usuario = {
				id: $("#users option:selected").val(),
				login: $("#users option:selected").text()
			}
			
			usuarioSelecionados.push(usuario);
			montarTableUsers();
		}
	}
	
	function montarTableUsers() {
		
		var rows = '';
		for (var i = 0; i < usuarioSelecionados.length; i++) {
			var usuario = usuarioSelecionados[i];
			rows += '<tr><td>' + usuario.id + '</td><td> ' + usuario.login + ' </td></tr>'
		}
		
		setBodyTable(rows);
		document.getElemenById('divUsers').style.display = 'block';
	}
	
	function compartilhar() {
		
		var ids = '';
		for (var i = 0; i < usuarioSelecionados.length; i++) {
			ids += usuarioSelecionados[i].id + ';';
		}

		$.get(
			'/driveds-web/compartilhar?usuarios=' + ids + '&chaveArquivo=' + getFileName(), 
			function( data ) {
			   $('#myModal').dialog('close');
			   usuarioSelecionados = [];
			   setBodyTable('');
			}
		);
	}
	
	function setFileName(chaveArquivo) {
		$('#chaveArquivo').val(chaveArquivo);
	}
	
	function getFileName() {
		return $('#chaveArquivo').val();
	}
	
	function setBodyTable(rows) {
		$('#bodyTable').html(rows);
	}
</script>
</head>
<body style="background-color: #F0F8FF;">
		<div>${ mensagem }</div>
		<input type="hidden" id="chaveArquivo" />
		<div class="center" style="width: 70%">
			
			<form method="POST" action="/driveds-web/addArquivo" enctype = "multipart/form-data" class="dropzone">
			  <div class="fallback">
			    <input name="file" type="file" multiple />
			  </div>
			</form>
		</div>
		<div class="center" style="width: 70%; margin-bottom: 30px">
			<form id="formNameFile">
				<input type="hidden" id="usuario" name="usuario"/>
				<input type="hidden" id="nomeArquivo" name="nomeArquivo" value="" />
				<div class="row">
					<input class="form-control .col-10 .col-sm-10 .col-lg-10" type="text" id="filtro" name="filtro" placeholder="Faça sua busca aqui" onkeypress="filtrar(event)" value'${filtro}'/>
				</div>
			</form>
		</div>
		<div class="center" style="width: 70%;">
			<table class="table table-striped">
				<thead>
					<tr>
						<th>Nome</th>
						<th>Última Modificação</th>
						<th>Tamanho</th>
						<th></th>
						<th></th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${ listaArquivos }" var="arquivo">
						<tr title="${ arquivo.title }">
							<td style="height: 40px"><div>${ arquivo.nome }</div></td>
							<td style="height: 40px">
								<div>${ arquivo.dataModificacao }</div>
							</td>
							<td style="height: 40px"><div>${ arquivo.tamanho }</div></td>
							<td style="height: 40px"><div style="width: 40px; height: 40px" >
									<c:if test="${ !arquivo.deTerceiro }">
										<button class="btn btn-link img-responsive" onclick="openModal('${ arquivo.chavePrimariaArquivo }')">
											<img src="resources/images/share.png" class="img-responsive">
										</button>
									</c:if>
								</div>
							</td>
							<td style="height: 40px"><div style="width: 40px; height: 40px">
									<c:if test="${ !arquivo.deTerceiro }">
										<button class="btn btn-link img-responsive" onclick="removerArquivo('${ arquivo.nome }')">
											<img src="resources/images/trash.ico" class="img-responsive">
										</button>
									</c:if>
								</div>
							</td>
							<td style="height: 40px">
								<div style="width: 40px; height: 40px">
									<button class="btn btn-link img-responsive" onclick="download('${ arquivo.nome }', '${ arquivo.usuario }')">
										<img src="resources/images/download.png" class="img-responsive">
									</button>
								</div>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div id="myModal" title="Usuários" style="display: none">
			<div class="form-group col-sm-10">
				<label for="users">Usuários</label>
				<select class="form-control" id="users">
					<option value="0">Selecione...</option>     
					<c:forEach var="usuario" items="${usuarios}">
						<option value="${usuario.chavePrimaria}">${usuario.login}</option>
					</c:forEach> 
				</select>
			</div>
			<div class="form-group col-sm-2" style="margin-top: 25px">
				<button class="btn btn-success" onClick="addUser()">Add</button>
			</div>
			<div class="form-group" id="divUsers" >
				<table class="table">
					<thead>
						<tr>
							<th>Identify</th>
							<th>Login</th>
						</tr>
					</thead>
					<tbody id="bodyTable"></tbody>
				</table>
			</div>
			<div class="form-group" >
				<button class="btn btn-success" onClick="compartilhar()">Share</button>
			</div>
		</div>
	</body>
</html>