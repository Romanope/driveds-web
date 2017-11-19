package com.driveds.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.driveds.hibernate.HibernateSession;
import com.driveds.model.Compartilhamento;

@Repository
public class CompartilhamentoDAO extends GenericDAO<Compartilhamento> {

	@Override
	protected void validarDados(Compartilhamento obj) {
		// TODO Auto-generated method stub
		
	}
	
	public Compartilhamento consultarCompartilhamento(Long usuarioDono, Long usuarioComp, String nomeArq) {
		
		Session session = HibernateSession.getSession();
		session.beginTransaction();
		try {
			
			Criteria criteria = session.createCriteria(Compartilhamento.class);
			criteria.add(Restrictions.eq("usuarioDono.chavePrimaria", usuarioDono));
			criteria.add(Restrictions.eq("usuarioCompartilhamento.chavePrimaria", usuarioComp));
			criteria.add(Restrictions.eq("nomeArquivo", nomeArq));
			
			return (Compartilhamento) criteria.uniqueResult();
		} finally {
			session.getTransaction().commit();
		}
	}
	
	public List<Compartilhamento> consultarCompartilhamentosArquivo(Long usuarioDono, String nomeArq) {
		
		Session session = HibernateSession.getSession();
		session.beginTransaction();
		try {
			
			Criteria criteria = session.createCriteria(Compartilhamento.class);
			criteria.add(Restrictions.eq("usuarioDono.chavePrimaria", usuarioDono));
			criteria.add(Restrictions.eq("nomeArquivo", nomeArq));
			
			return criteria.list();
		} finally {
			session.getTransaction().commit();
		}
	}

}
