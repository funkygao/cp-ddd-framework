package io.github.dddplus.ast.model.dump;

import io.github.dddplus.ast.model.ReverseEngineeringModel;
import lombok.AllArgsConstructor;

import java.io.*;
import java.sql.SQLException;

@AllArgsConstructor
public class ModelDumper {
    private final String sqliteDb;
    private final ReverseEngineeringModel model;

    public void dump() throws SQLException, ClassNotFoundException, IOException {
        SqliteHelper db = new SqliteHelper(sqliteDb);
        prepareTables(db)
                .dumpKeyElements(db)
                .dumpCallGraph(db);
    }

    private ModelDumper prepareTables(SqliteHelper db) throws SQLException, ClassNotFoundException, IOException {
        StringBuilder sql = new StringBuilder();
        try (InputStream inputStream = ModelDumper.class.getClassLoader().getResourceAsStream("model.sql")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }
        }

        db.executeUpdate(sql.toString());
        return this;
    }

    private ModelDumper dumpCallGraph(SqliteHelper db) {
        return this;
    }

    private ModelDumper dumpKeyElements(SqliteHelper db) {
        return this;
    }
}
