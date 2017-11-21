package com.driveds.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.driveds.hibernate.HibernateSession;
import com.driveds.model.Arquivo;

@Repository
public class ArquivoDAO extends GenericDAO<Arquivo> {

	@Override
	protected void validarDados(Arquivo obj) {
		
	}
	
	public Arquivo consultarArquivo (Long usuario, String nomeArquivo) {
		
		Session session = HibernateSession.getSession();
		session.beginTransaction();
		try {
			
			Criteria criteria = session.createCriteria(Arquivo.class);
			criteria.add(Restrictions.eq("usuario.chavePrimaria", usuario));
			criteria.add(Restrictions.eq("nome", nomeArquivo));
			
			return (Arquivo) criteria.uniqueResult();
		} finally {
			session.getTransaction().commit();
		}
	}
	
	public List<Arquivo> consultarArquivos (Long chavePrimariaUsuario, String nomeArquivo) {
		
		Session session = HibernateSession.getSession();
		session.beginTransaction();
		try {
			
			Criteria criteria = session.createCriteria(Arquivo.class);
			criteria.add(Restrictions.eq("usuario.chavePrimaria", chavePrimariaUsuario));
			criteria.add(Restrictions.eq("removido", false));
			if (nomeArquivo != null && nomeArquivo.length() > 0) {
				criteria.add(Restrictions.like("nome", nomeArquivo));
			}
			
			return criteria.list();
		} finally {
			session.getTransaction().commit();
		}
		
	}
}
