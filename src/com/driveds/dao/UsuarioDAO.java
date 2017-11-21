package com.driveds.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.driveds.hibernate.HibernateSession;
import com.driveds.model.Compartilhamento;
import com.driveds.model.Usuario;

@Repository
public class UsuarioDAO extends GenericDAO<Usuario> {

	
	@Override
	protected void validarDados(Usuario obj) {
		
	}
	
	public Usuario getUsuarioByLogin(String login, boolean lazy) {
		
		Session session = HibernateSession.getSession();
		
		session.beginTransaction();
		
		try {
			Criteria criteria = session.createCriteria(Usuario.class);
			criteria.add(Restrictions.eq("login", login));
			if (lazy) {
				criteria.setFetchMode("arquivosCompartilhados", FetchMode.JOIN);
			}
			return (Usuario) criteria.uniqueResult();
		} finally {
			session.getTransaction().commit();
		}
	
	}
	
	public List<Usuario> listAll(String login) {
		
		Session session = HibernateSession.getSession();
		
		session.beginTransaction();
		
		return session.createCriteria(Usuario.class).add(Restrictions.ne("login", login)).list();
	}
	
	public List<Usuario> getUserById(Long[] ids) {
		
		Session session = HibernateSession.getSession();
		
		session.beginTransaction();
		
		return session.createCriteria(Usuario.class).add(Restrictions.in("chavePrimaria", ids)).list();
	}
}
