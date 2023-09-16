package Driver;

import java.sql.SQLException;

public interface CrudOperations {
    public abstract boolean add() throws SQLException;
    public abstract boolean modify() throws SQLException;
    public abstract boolean delete() throws SQLException;
}