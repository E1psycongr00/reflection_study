package dynamicproxy.exam1.external.impl;

public class DatabaseReaderImpl implements DatabaseReader {
    @Override
    public int countRowsInTable(String tableName) throws InterruptedException {
        System.out.println(String.format("DatabaseReaderImpl - counting rows in table %s", tableName));

        Thread.sleep(1000);
        return 50;
    }

    @Override
    public String[] readRow(String sqlQuery) throws InterruptedException {
        return new String[0];
    }
}
