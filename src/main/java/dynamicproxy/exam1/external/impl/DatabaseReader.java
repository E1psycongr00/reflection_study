package dynamicproxy.exam1.external.impl;

public interface DatabaseReader {

    int countRowsInTable(String tableName) throws InterruptedException;

    String[] readRow(String sqlQuery) throws InterruptedException;
}
