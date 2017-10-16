package com.ef.db.dao;

import com.ef.db.HibernateUtil;
import com.ef.db.entity.LogAccessIpEntity;
import com.ef.db.entity.LogBlockedIpEntity;
import com.ef.model.RequestLine;
import org.hibernate.Session;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mohamedrefaat on 10/15/17.
 */
public class LogAccessDao {

    private static final Logger LOGGER = Logger.getLogger(LogAccessDao.class.getSimpleName());


    public static void loadRequestLogLineToDatabase (RequestLine requestLine) {
        LogAccessIpEntity logAccessIpEntity = new LogAccessIpEntity();

        logAccessIpEntity.setDate(requestLine.getDate());
        logAccessIpEntity.setIp(requestLine.getIp());
        logAccessIpEntity.setRequest(requestLine.getRequest());
        logAccessIpEntity.setStatus(requestLine.getStatus());
        logAccessIpEntity.setUserAgent(requestLine.getUserAgent());

        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(logAccessIpEntity);
        LOGGER.log(Level.INFO, "Save IP to Access Table: " + logAccessIpEntity);


        session.getTransaction().commit();
    }

    public static void loadBlockedRequestLineToDatabase (RequestLine requestLine,String comment) {
        LogBlockedIpEntity blockedIpEntity = new LogBlockedIpEntity();

        blockedIpEntity.setDate(requestLine.getDate());
        blockedIpEntity.setIp(requestLine.getIp());
        blockedIpEntity.setComment(comment);

        Session session = HibernateUtil.getSession();
        session.beginTransaction();


        session.persist(blockedIpEntity);
        LOGGER.log(Level.INFO,"Save blocked IP to blocked Table: "+blockedIpEntity);

        session.getTransaction().commit();
    }

}
