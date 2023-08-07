package io.github.dddplus.ast.model.dumper.sqlite;

import io.github.dddplus.ast.model.CallGraphEntry;
import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.model.dumper.ModelDumper;
import io.github.dddplus.ast.report.CallGraphReport;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class SqliteDumper implements ModelDumper {
    private final String sqliteDb;
    private ReverseEngineeringModel model;

    @Override
    public void dump(ReverseEngineeringModel model) throws Exception {
        this.model = model;
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

    private SqliteDumper dumpCallGraph(SqliteHelper db) throws SQLException, ClassNotFoundException {
        CallGraphReport callGraphReport = model.getCallGraphReport();
        List<String> sqlList = new LinkedList<>();
        for (CallGraphEntry entry : callGraphReport.sortedEntries()) {
            String sql = String.format("insert into callgraph(callerClazz,callerMethod,calleeClazz,calleeMethod) values('%s','%s','%s','%s');",
                    entry.getCallerClazz(), entry.getCallerMethod(),
                    entry.getCalleeClazz(), entry.getCalleeMethod());
            sqlList.add(sql);
        }
        db.executeUpdate(sqlList);

        return this;
    }

    private SqliteDumper dumpKeyElements(SqliteHelper db) {
        return this;
    }
}
