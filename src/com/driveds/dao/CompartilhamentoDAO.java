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
	
	public Compartilhamento consultarCompartilhamento (Long usuarioDono, Long usuarioComp, Long chaveArquivo) {
		
		Session session = HibernateSession.getSession();
		session.beginTransaction();
		try {
			
			Criteria criteria = session.createCriteria(Compartilhamento.class);
			criteria.add(Restrictions.eq("usuarioDono.chavePrimaria", usuarioDono));
			criteria.add(Restrictions.eq("usuarioCompartilhamento.chavePrimaria", usuarioComp));
			criteria.add(Restrictions.eq("arquivo.chavePrimaria", chaveArquivo));
			
			return (Compartilhamento) criteria.uniqueResult();
		} finally {
			session.getTransaction().commit();
		}
	}
	
	public List<Compartilhamento> consultarCompartilhamentosArquivo (Long usuarioDono, Long chavePrimariaArquivo) {
		
		Session session = HibernateSession.getSession();
		session.beginTransaction();
		try {
			
			Criteria criteria = session.createCriteria(Compartilhamento.class);
			criteria.add(Restrictions.eq("usuarioDono.chavePrimaria", usuarioDono));
			criteria.add(Restrictions.eq("arquivo.chavePrimaria", chavePrimariaArquivo));
			
			return criteria.list();
		} finally {
			session.getTransaction().commit();
		}
	}
	
	public List<Compartilhamento> consultarCompartilhamentosRecebidos(Long usuarioCompartilhamento, String filtro) {
		
		Session session = HibernateSession.getSession();
		session.beginTransaction();
		try {
			
			Criteria criteria = session.createCriteria(Compartilhamento.class);
			criteria.add(Restrictions.eq("usuarioCompartilhamento.chavePrimaria", usuarioCompartilhamento));
			criteria.add(Restrictions.eq("ativo", true));
			if (filtro != null && filtro.length() > 0) {
				criteria.add(Restrictions.like("arquivo.nome", filtro));
			}
			
			return criteria.list();
		} finally {
			session.getTransaction().commit();
		}
	}
	
	public List<Compartilhamento> consultarCompartilhamentosPorArquivo (Long chaveArquivo) {
		Session session = HibernateSession.getSession();
		session.beginTransaction();
		try {
			
			Criteria criteria = session.createCriteria(Compartilhamento.class);
			criteria.add(Restrictions.eq("arquivo.chavePrimaria", chaveArquivo));
			
			return criteria.list();
		} finally {
			session.getTransaction().commit();
		}
	}
}
