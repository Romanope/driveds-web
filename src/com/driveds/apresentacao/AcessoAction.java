package com.driveds.apresentacao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.driveds.controllers.ControladorArquivo;
import com.driveds.controllers.ControladorUsuario;
import com.driveds.model.Usuario;

@Controller
public class AcessoAction extends AbstractController {

	@Autowired
	private ControladorUsuario controladorUsuario;
	
	@Autowired
	private ControladorArquivo controladorArquivo;
	
	@RequestMapping(value = "/logar", method = RequestMethod.POST)
	public ModelAndView logar(HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		
		Usuario usuario = controladorUsuario.getUsuarioByLogin(login, true);
		if (usuario == null || !usuario.getSenha().equals(senha)) {
			map.addAttribute("msgErro", gerarMensagemErro("Login ou senha inv�lido!"));
			map.addAttribute("classErro", "center ui-alert");
			map.addAttribute("login", login);
			map.addAttribute("senha", senha);
			return new ModelAndView("index");
		}
		
		request.getSession().setAttribute("login", login);
		
		return  new ModelAndView("redirect:/arquivo");
	}
	
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		System.out.println("passo no handle request internal    #########################################################################################################");
		return null;
	}
	
	@RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
	public ModelAndView cadastrar (HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String email = request.getParameter("email");

		ModelAndView model = validarEIncluirUsuario(map, login, senha, email);
		if (model != null) {
			return model;
		}
		
		Usuario usuario  = controladorUsuario.criarUsuario(login, senha, email);
		
		controladorUsuario.salvarUsuario(usuario);
		
		controladorArquivo.criarDiretorioUsuario(login);
		
		request.getSession().setAttribute("login", login);
		
		return new ModelAndView("redirect:/arquivo");
	}

	private ModelAndView validarEIncluirUsuario(ModelMap map, String login, String senha, String email) {
		if (!dadosValidos(login, senha, email)) {
			map.addAttribute("msgErro", gerarMensagemErro("Login ou senha inv�lido!"));
			map.addAttribute("classErro", "center ui-alert");
			setDadosForm(map, login, senha, email);
			return new ModelAndView("index"); 
		}
		
		if (controladorUsuario.getUsuarioByLogin(login, true) != null) {
			map.addAttribute("msgErro", gerarMensagemErro("Usu�rio <b> " + login + " </b> j� existe. Favor, entre com outro usu�rio!" ));
			map.addAttribute("classErro", "center ui-alert");
			setDadosForm(map, login, senha, email);
			return new ModelAndView("index"); 
		}
		
		return null;
	}

	private void setDadosForm(ModelMap map, String login, String senha, String email) {
		map.addAttribute("login", login);
		map.addAttribute("senha", senha);
		map.addAttribute("email", email);
	}
	
	private String gerarMensagemErro(String msg) {
		return "<div class=\"ui-alert-message\" style=\"text-align: center;\"> " + msg + " </div>";
	}
	
	private boolean dadosValidos(String login, String senha, String email) {
		if (StringUtils.isEmpty(login) || StringUtils.isEmpty(senha) || StringUtils.isEmpty(email)) {
			return false;
		}
		return true;
	}
}
