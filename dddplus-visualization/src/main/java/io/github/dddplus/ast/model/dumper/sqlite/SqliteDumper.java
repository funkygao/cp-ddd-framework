/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model.dumper.sqlite;

import io.github.dddplus.ast.model.*;
import io.github.dddplus.ast.model.dumper.ModelDumper;
import io.github.dddplus.dsl.KeyElement;
import lombok.AllArgsConstructor;
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
    private SqliteHelper db;

    private static final String CallgraphInsert = "insert into callgraph(callerClazz,callerMethod,calleeClazz,calleeMethod) values('%s','%s','%s','%s');";
    private static final String EntityInsert = "insert into entity(aggregate, entity, kind, name, args, javadoc) values ('%s','%s',%d,'%s','%s','%s');";

    @Override
    public void dump(ReverseEngineeringModel model) throws Exception {
        this.model = model;
        this.db = new SqliteHelper(sqliteDb);

        prepareTables().dumpKeyElements();
    }

    private SqliteDumper prepareTables() throws SQLException, ClassNotFoundException, IOException {
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

    private SqliteDumper dumpKeyElements() {
        model.sortedAggregates().forEach(a -> {
            try {
                addAggregate(a);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return this;
    }

    private void addAggregate(AggregateEntry aggregate) throws SQLException, ClassNotFoundException {
        for (KeyModelEntry clazz : aggregate.keyModels()) {
            writeClazzDefinition(aggregate, clazz);
        }
    }

    private void writeClazzDefinition(AggregateEntry aggregateEntry, KeyModelEntry keyModelEntry) throws SQLException, ClassNotFoundException {
        List<String> sqlList = new LinkedList<>();
        for (KeyElement.Type type : keyModelEntry.types()) {
            for (KeyPropertyEntry propertyEntry : keyModelEntry.keyPropertiesByType(type)) {
                sqlList.add(String.format(EntityInsert,
                        aggregateEntry.getName(),
                        keyModelEntry.getClassName(),
                        EntityKind.PROPERTY.value,
                        propertyEntry.getName(),
                        "",
                        propertyEntry.getJavadoc()));
            }
        }
        for (KeyBehaviorEntry entry : keyModelEntry.getKeyBehaviorEntries()) {
            sqlList.add(String.format(EntityInsert,
                    aggregateEntry.getName(),
                    keyModelEntry.getClassName(),
                    EntityKind.BEHAVIOR.value,
                    entry.getMethodName(),
                    entry.displayArgs(),
                    entry.getJavadoc()));

        }
        for (KeyRuleEntry entry : keyModelEntry.getKeyRuleEntries()) {
            sqlList.add(String.format(EntityInsert,
                    aggregateEntry.getName(),
                    keyModelEntry.getClassName(),
                    EntityKind.RULE.value,
                    entry.getMethodName(),
                    "",
                    entry.getJavadoc()));
        }
        for (KeyFlowEntry entry : keyModelEntry.getKeyFlowEntries()) {
            sqlList.add(String.format(EntityInsert,
                    aggregateEntry.getName(),
                    keyModelEntry.getClassName(),
                    EntityKind.FLOW.value,
                    entry.getMethodName(),
                    entry.displayEffectiveArgs(),
                    entry.getJavadoc()));
        }

        db.executeUpdate(sqlList);
    }

    @AllArgsConstructor
    enum EntityKind {
        PROPERTY(1),
        BEHAVIOR(2),
        RULE(3),
        FLOW(4);

        int value;
    }
}
