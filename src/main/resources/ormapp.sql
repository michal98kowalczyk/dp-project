-- Database: ormapp

-- DROP DATABASE ormapp;


CREATE USER orm WITH PASSWORD 'orm';

CREATE DATABASE ormapp
    WITH 
    OWNER = orm
    ENCODING = 'UTF8'
    LC_COLLATE = 'Polish_Poland.1250'
    LC_CTYPE = 'Polish_Poland.1250'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE ormapp
    IS 'include results of ORM Application';