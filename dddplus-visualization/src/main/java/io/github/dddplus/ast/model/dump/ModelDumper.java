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
        StringBuilder sql = new StringBuilder();
        try (InputStream inputStream = ModelDumper.class.getClassLoader().getResourceAsStream("callgraph.sql")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }
        }

        db.executeUpdate(sql.toString());
    }
}
