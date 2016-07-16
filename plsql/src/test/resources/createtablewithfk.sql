
-- <ChangeSet name="create tables" delimiter=":" />
CREATE TABLE PTABLE1 
    ( 
     ID NUMBER (18)  NOT NULL ,      
     NAME VARCHAR2 (60 CHAR)  NOT NULL
    )  
;

ALTER TABLE PTABLE1 ADD CONSTRAINT PTABLE1_PK PRIMARY KEY (ID)
;

 
CREATE TABLE FTABLE2 
    ( 
      ID NUMBER (18)  NOT NULL ,      
     NAME VARCHAR2 (60 CHAR)  NOT NULL
    )  
;


ALTER TABLE FTABLE2 ADD CONSTRAINT FTABLE2_PTABLE1_FK1 FOREIGN KEY (ID)  REFERENCES PTABLE1 (ID) ENABLE
;

insert into ptable1 values(1,'aa')
;
insert into ptable1 values(2,'aa')
;
insert into ptable1 values(3,'aa')
;
insert into ftable2 values(1,'aa')
;
insert into ftable2 values(2,'aa')
;
insert into ftable2 values(3,'aa')
;


