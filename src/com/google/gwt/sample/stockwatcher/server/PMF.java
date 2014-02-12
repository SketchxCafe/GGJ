package com.google.gwt.sample.stockwatcher.server;

//From https://developers.google.com/appengine/docs/java/datastore/jdo/overview-dn2
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * The Persistence Manager Factory is kind of heavy so this is a singleton class.
 *
 */
public final class PMF {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
