package Util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Created by pwwpche on 2014/5/1.
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        Configuration cf = new Configuration();
        SessionFactory sf;
        cf.configure();
        ServiceRegistry sr = new ServiceRegistryBuilder().applySettings(cf.getProperties()).buildServiceRegistry();
        sf = cf.buildSessionFactory(sr);
        return sf;
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

}
