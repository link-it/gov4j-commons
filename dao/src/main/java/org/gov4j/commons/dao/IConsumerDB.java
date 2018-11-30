package org.gov4j.commons.dao;


@FunctionalInterface
public interface IConsumerDB {

    void accept(Connection dbConn) throws Exception;
}



