-- <ChangeSet name="part1" delimiter="/" />

-- empty package
create or replace package EmptyPackage
as
    
end EmptyPackage;

/

-- <ChangeSet name="part2" delimiter="/" />

create or replace
package body EmptyPackage
as

end EmptyPackage;

/

-- <ChangeSet name="part3" delimiter="/" />




CREATE TABLE TABLE1 
    ( 
     
     ID NUMBER (18)  NOT NULL ,      
     NAME VARCHAR2 (60 CHAR)  NOT NULL
    
    )  
;

 



CREATE TABLE TABLE2 
    ( 
      ID NUMBER (18)  NOT NULL ,      
     NAME VARCHAR2 (60 CHAR)  NOT NULL
    )  
;

