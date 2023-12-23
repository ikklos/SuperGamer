package ikklo.server;

import ikklo.pojo.ReqResult;

import java.sql.Connection;

public interface CanSolve {
    ReqResult solve(Connection conn) throws Exception;
}