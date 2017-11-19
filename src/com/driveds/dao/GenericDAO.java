package com.driveds.dao;

import java.lang.reflect.ParameterizedType;

import org.hibernate.Session;

import com.driveds.hibernate.HibernateSession;

public abstract class GenericDAO<T> {

	
	private Class<T> persistenceClass;
	
	protected abstract void validarDados(T obj);
	
	protected GenericDAO() {
		persistenceClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public T save(T obj) {
		
		Session session = HibernateSession.getSession();
		
		//validar dados
		validarDados(obj);
		
		session.beginTransaction();
		
		session.saveOrUpdate(obj);
		
		session.getTransaction().commit();
		
		return obj;
	}
	
	
	public T get(long id) {
		Session session = HibernateSession.getSession();
		session.beginTransaction();
		try {
			return session.get(persistenceClass, id);
		} finally {
			
		}
	}
}
