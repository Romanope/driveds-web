package com.driveds.hibernate;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSession {	

	private static SessionFactory buildSessionFactory() {
		
		try {
			StandardServiceRegistry standardRegistry = 
				       new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
					Metadata metaData = 
				        new MetadataSources(standardRegistry).getMetadataBuilder().build();
					return metaData.getSessionFactoryBuilder().build();
		} catch (Throwable ex) {
			System.out.println(ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static Session getSession() {
		return buildSessionFactory().getCurrentSession();
	}
}
