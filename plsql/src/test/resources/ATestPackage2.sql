create or replace package ATestPackage2
as

TEST_CONSTANT_VALUE CONSTANT VARCHAR2(16) := 'PACKAGE CONSTANT';

function FINDMAX (value1 NUMBER, value2 NUMBER) return NUMBER;

  
end ATestPackage2;

/
 
create or replace
package body ATestPackage2
as
function FINDMAX (value1 NUMBER, value2 NUMBER) return NUMBER is 
begin  if (value1>value2) then return value1; end if; return value2; end;
   

end ATestPackage2;

/
 

