package io.github.dddplus.ast.model.dumper.sqlite;

import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.model.dumper.ModelDumper;
import lombok.AllArgsConstructor;

import java.io.*;
import java.sql.SQLException;

@AllArgsConstructor
public class SqliteDumper implements ModelDumper {
    private final String sqliteDb;

    @Override
    public void dump(ReverseEngineeringModel model) throws Exception {
        SqliteHelper db = new SqliteHelper(sqliteDb);
        prepareTables(db)
                .dumpKeyElements(db)
                .dumpCallGraph(db);
    }

    private SqliteDumper prepareTables(SqliteHelper db) throws SQLException, ClassNotFoundException, IOException {
        StringBuilder sql = new StringBuilder();
        try (InputStream inputStream = SqliteDumper.class.getClassLoader().getResourceAsStream("model.sql")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }
        }

        db.executeUpdate(sql.toString());
        return this;
    }

    private SqliteDumper dumpCallGraph(SqliteHelper db) {
        return this;
    }

    private SqliteDumper dumpKeyElements(SqliteHelper db) {
        return this;
    }
}
