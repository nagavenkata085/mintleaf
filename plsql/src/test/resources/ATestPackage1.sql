create or replace function FINDMAX (value1 NUMBER, value2 NUMBER) return NUMBER is 
begin  if (value1>value2) then return value1; end if; return value2; end;

/