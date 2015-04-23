
-- <sqlpart name="create person table" delimiter="/" />

CREATE TABLE PERSON 
    ( 
     
     id NUMBER (18)  NOT NULL ,      
     first_name VARCHAR2 (60 CHAR)  NOT NULL,
     last_name VARCHAR2 (60 CHAR)  NOT NULL,
     CONSTRAINT person_pk PRIMARY KEY (id) 
    )  
/

begin
insert into PERSON values(10,'EMP1','EMP1');
insert into PERSON values(20,'EMP2','EMP2');
end;
/


-- <sqlpart name="drop person table" delimiter="/" />

begin
execute immediate 'drop table PERSON purge';
exception when others then null;
end;
/	 
