create or replace package BooleanTest
as
    procedure isGreaterThan100Proc(result out boolean, value1 IN NUMBER, defvalue in boolean);
 	function isGreaterThan100(value1 IN NUMBER) return Boolean;
function isGreaterThan100ButConditional(value1 IN NUMBER, bcondition IN Boolean) return Boolean;

FUNCTION bool2int (b BOOLEAN)
      RETURN INTEGER;

   FUNCTION int2bool (i INTEGER)
      RETURN BOOLEAN;
   
end BooleanTest;

/
 
create or replace
package body BooleanTest
as

procedure isGreaterThan100Proc(result out boolean, value1 IN NUMBER, defvalue in boolean) as
begin
result := isGreaterThan100(value1);
end;

function isGreaterThan100(value1 IN NUMBER) return Boolean
as
begin
	if (value1 > 100) then
	return true;
	else return false;
	end if;
end;	
   
function isGreaterThan100ButConditional(value1 IN NUMBER, bcondition IN Boolean) return Boolean
as
begin
	return  bcondition AND isGreaterThan100(value1);
end;	

FUNCTION bool2int (b BOOLEAN)
      RETURN INTEGER
   IS
   BEGIN
      IF b IS NULL
      THEN
         RETURN NULL;
      ELSIF b
      THEN
         RETURN 1;
      ELSE
         RETURN 0;
      END IF;
   END bool2int;

   FUNCTION int2bool (i INTEGER)
      RETURN BOOLEAN
   IS
   BEGIN
      IF i IS NULL
      THEN
         RETURN NULL;
      ELSE
         RETURN i <> 0;
      END IF;
   END int2bool;
   
end BooleanTest;

/
 

