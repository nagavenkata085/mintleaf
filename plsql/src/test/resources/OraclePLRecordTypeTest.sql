create or replace package OraclePLRecordTypeTest
as

TYPE plemployee_record IS RECORD (
     id   NUMBER,
     name VARCHAR2(60));
    
function getEmployee return   plemployee_record;  
end OraclePLRecordTypeTest;

/
 
create or replace
package body OraclePLRecordTypeTest
as

function getEmployee return plemployee_record
as
aEmp plemployee_record;
begin
  aEmp.id := 22;
  aEmp.name := 'senthil maruthaiappan';
  
  return aEmp;
end;

end OraclePLRecordTypeTest;

/
 

