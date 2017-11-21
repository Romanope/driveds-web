package com.driveds.apresentacao;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.driveds.controllers.ControladorArquivo;
import com.driveds.controllers.ControladorCompartilhamento;
import com.driveds.controllers.ControladorUsuario;
import com.driveds.model.Arquivo;
import com.driveds.model.Compartilhamento;
import com.driveds.model.Usuario;

@Controller
public class ArquivoAction extends BaseAction {

	@Autowired
	private ControladorArquivo controladorArquivo;
	
	@Autowired
	private ControladorUsuario controladorUsuario;
	
	@Autowired
	private ControladorCompartilhamento controladorCompartilhamento;
	
	
	@RequestMapping(value = "/arquivo", method = RequestMethod.GET)
	public ModelAndView getArquivos(HttpServletRequest request, ModelMap map) {
		
		ModelAndView model = new ModelAndView("arquivo", "command", new ArquivoVO());
		
		String login = (String) request.getSession().getAttribute("login");
		
		Usuario usuarioLogado = controladorUsuario.getUsuarioByLogin(login, true);

		List<ArquivoVO> arquivos = controladorArquivo.obterArquivosPorUsusario(usuarioLogado, null);

		setNomeUsuariosCompartilhamentos(arquivos, usuarioLogado.getChavePrimaria());
		
		List<Compartilhamento> compartilhamentos = controladorCompartilhamento.consultarCompartilhamentosRecebidos(usuarioLogado.getChavePrimaria(), null);
		
		arquivos.addAll(controladorCompartilhamento.adicionarArqCompartilhamentos(compartilhamentos));		

		List<Usuario> usuarios = controladorUsuario.listAll(login);
		
		request.getSession().setAttribute("listaArquivos", arquivos);
		
		request.getSession().setAttribute("usuarios", usuarios);
		
		map.addAttribute("filtro", null);
		
		return model;
	}

	@RequestMapping(value = "/apagarArquivo", method = RequestMethod.POST)
	public ModelAndView apagarArquivo(HttpServletRequest request, ModelMap map) {

		String login = (String) request.getSession().getAttribute("login");
		String nomeArquivo = request.getParameter("nomeArquivo");
		
		controladorArquivo.apagarArquivo(login, nomeArquivo);
		
		Arquivo arqRemovido = controladorArquivo.removerArquivo(controladorUsuario.getUsuarioByLogin(login, true), nomeArquivo);
		
		controladorCompartilhamento.invativarCompartilhamentos(arqRemovido.getChavePrimaria());
		
		return getArquivos(request, map);
	}
	
	@RequestMapping(value = "/addArquivo", method = RequestMethod.POST)
	public ModelAndView addArquivo(HttpServletRequest request, ModelMap map) {
		
		String login = (String) request.getSession().getAttribute("login");
		
		if (login == null) {
			map.addAttribute("msgErro", gerarMensagemErro("Fa�a login para continuar!"));
			map.addAttribute("classErro", "center ui-alert");
			return new ModelAndView("index");
		}
		
		try {
			controladorArquivo.criarDiretorioUsuario(login);
			controladorArquivo.salvarArquivo(request, login);
		} catch (Exception e) {
			map.addAttribute("msgErro", gerarMensagemErro("Erro ao salvar arquivo!"));
			map.addAttribute("classErro", "center ui-alert");
			return new ModelAndView("arquivo");
		}
		
		return null;
	}
	
	private String gerarMensagemErro(String msg) {
		return "<div class=\"ui-alert-message\" style=\"text-align: center;\"> " + msg + " </div>";
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam("nomeArquivo") String nomeArquivo, @RequestParam("usuario") String usuario, ModelMap map) {

		String login = (String) request.getSession().getAttribute("login");
		if (usuario == null || usuario.length() == 0) {
			usuario = login;
		}
		
		prepararResponse(response, nomeArquivo);

		try {
			controladorArquivo.downloadFile(usuario, nomeArquivo, response.getOutputStream());
		} catch (IOException e) {
			map.addAttribute("msgErro", gerarMensagemErro("N�o foi poss�vel realizar o download. Tente novamente."));
			map.addAttribute("classErro", "center ui-alert");
			e.printStackTrace();
		}
		
		return null;
	}

	private void prepararResponse(HttpServletResponse response, String nomeArquivo) {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="+nomeArquivo);
	}
	
	@RequestMapping(value = "/consultar", method = RequestMethod.GET)
	public ModelAndView consultar(HttpServletRequest request, @RequestParam("filtro") String filtro,
			ModelMap map) {
		
		ModelAndView model = new ModelAndView("arquivo");
		
		String login = (String) request.getSession().getAttribute("login");
		
		Usuario usuarioLogado = controladorUsuario.getUsuarioByLogin(login, true);

		List<ArquivoVO> arquivos = controladorArquivo.obterArquivosPorUsusario(usuarioLogado, filtro);

		setNomeUsuariosCompartilhamentos(arquivos, usuarioLogado.getChavePrimaria());
		
		List<Compartilhamento> compartilhamentos = controladorCompartilhamento.consultarCompartilhamentosRecebidos(usuarioLogado.getChavePrimaria(), filtro);
		
		arquivos.addAll(controladorCompartilhamento.adicionarArqCompartilhamentos(compartilhamentos));		

		request.getSession().setAttribute("listaArquivos", arquivos);
		
		map.addAttribute("filtro", filtro);
		
		return model;
	}

	@RequestMapping(value = "/compartilhar", method = RequestMethod.GET)
	public ModelAndView compartilhar(HttpServletRequest request,
			ModelMap map) {
		
		String [] idsStr = request.getParameter("usuarios").split(";");
		
		Long[] ids = new Long[idsStr.length];
		for (int i = 0; i < idsStr.length; i++) {
			ids[i] = Long.valueOf(idsStr[i]);
		}
		
		Long chaveArquivo = null;
		if (request.getParameter("chaveArquivo") != null && request.getParameter("chaveArquivo").length() > 0) {
			chaveArquivo = Long.valueOf(request.getParameter("chaveArquivo"));
		}
		
		List<Usuario> usuariosCompartilhamento = controladorUsuario.getUserById(ids);		
		
		Usuario usuarioLogado = controladorUsuario.getUsuarioByLogin((String)request.getSession().getAttribute("login"), true);
		
		String erros = controladorCompartilhamento.salvarCompartilhamento(chaveArquivo, usuarioLogado, usuariosCompartilhamento);
		
//		mensagens(erros, map, fileName);
		
		return getArquivos(request, map);
	}
	
	private void mensagens (String erros, ModelMap map, String fileName) {
		String msgError = "O arquivo " + fileName + " j� foi compartilhado com o(s) usu�rio(s) " + erros;
		setMsg(map, msgError, null);
	}
	
	private void setNomeUsuariosCompartilhamentos (List<ArquivoVO> arquivos, Long usuarioLogado) {
		
		StringBuilder usuarios = new StringBuilder();
		for (ArquivoVO arquivo: arquivos) {
			List <Compartilhamento> comps = controladorCompartilhamento.consultarCompartilhamentoArquivo(usuarioLogado, arquivo.getChavePrimariaArquivo());
			for (Compartilhamento compartilhamento: comps) {
				usuarios = usuarios.length() == 0 ? usuarios.append(compartilhamento.getUsuarioCompartilhamento().getLogin()) : usuarios.append(", ").append(compartilhamento.getUsuarioCompartilhamento().getLogin());
			}
			if (usuarios.length() > 0) {
				arquivo.setTitle("Compartilhado com ".concat(usuarios.toString()));
				usuarios = new StringBuilder();
			}
		}
		
	}
}
