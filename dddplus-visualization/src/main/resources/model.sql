drop table if exists callgraph;
create table if not exists callgraph (
    callerClazz varchar(100),
    callerMethod varchar(200),
    calleeClazz varchar(100),
    calleeMethod varchar(200)
);
CREATE INDEX idx_cg_caller ON callgraph (callerClazz, callerMethod);
