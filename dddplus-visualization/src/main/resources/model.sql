drop table if exists callgraph;
create table if not exists callgraph (
    callerClazz varchar(100),
    callerMethod varchar(200),
    calleeClazz varchar(100),
    calleeMethod varchar(200)
);
CREATE INDEX idx_cg_caller ON callgraph (callerClazz, callerMethod);

drop table if exists entity;
create table if not exists entity (
    aggregate varchar(50),
    entity varchar(100),
    kind SMALLINT,
    name varchar(100),
    args varchar(200),
    javadoc varchar(200)
);