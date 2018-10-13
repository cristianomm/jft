
drop database "StockData"

CREATE DATABASE "StockData"
  WITH ENCODING='UTF8'
       CONNECTION LIMIT=-1
       TABLESPACE=stockdata;

CREATE TABLESPACE stockdata
  LOCATION E'C:\\Disco\\DataBases\\PostgreSQL\\9.5\\data\\tablespaces\\stockdata';

CREATE ROLE stockdata LOGIN ENCRYPTED PASSWORD 'md5b31363e750d3f98c9d7d78d4f0e0db01'
  CREATEDB REPLICATION
   VALID UNTIL 'infinity';


CREATE SCHEMA financial
  AUTHORIZATION stockdata;

CREATE SCHEMA marketdata
  AUTHORIZATION stockdata;
  
CREATE SCHEMA security
  AUTHORIZATION stockdata;

CREATE SCHEMA trading
  AUTHORIZATION stockdata;

  