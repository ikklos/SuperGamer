package ikklo.server;

import java.sql.Connection;
import java.util.Collection;

public interface CanSolve{
    ServerResult solve(Connection conn);
}