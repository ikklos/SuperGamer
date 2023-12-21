package ikklo.server;

import java.sql.Connection;

public class Requirement implements CanSolve{
    private String username;
    @Override
    public ServerResult solve(Connection conn) {
        return null;
    }
}
