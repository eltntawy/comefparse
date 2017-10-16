package com.ef.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by mohamedrefaat on 10/14/17.
 */
public class HibernateUtil {


    private static  Session currentSession;

    private static Configuration configure = new Configuration().configure();
    private static SessionFactory sessionFactory = configure.buildSessionFactory();

    public static Session getSession() {

        if(currentSession == null ) {
            synchronized (HibernateUtil.class) {
                if(currentSession == null) {
                    currentSession = sessionFactory.openSession();
                }
            }
        }

        if(!currentSession.isConnected()) {
            currentSession = sessionFactory.getCurrentSession();
        }

        return currentSession;
    }


}
