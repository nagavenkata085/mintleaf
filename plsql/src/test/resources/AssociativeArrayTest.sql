create or replace type STRINGLIST as table of varchar2(255);

/

create or replace package AssociativeArray
as  
  procedure tokenize( pstr IN varchar2, presult OUT NOCOPY STRINGLIST,ptoken IN varchar2 default ' ');
  
end AssociativeArray;
/
show err

create or replace package body AssociativeArray
as

 
  procedure tokenize( pstr IN varchar2, presult OUT NOCOPY STRINGLIST,ptoken IN varchar2 default ' ')
  as
    p binary_integer;
    vstr varchar2(255) := trim(pstr);
    i integer:=0;
  begin
    presult := STRINGLIST();
    if pstr is null or length(pstr)=0 then
      return;
    end if;
    p := instr(vstr,ptoken);
    while ( p > 0 ) loop
      presult.extend;
      presult(presult.last) := trim(substr(vstr,1,p-1));
      vstr := trim(substr(vstr,p+1,length(vstr)));
      p := instr(vstr,ptoken);
      i := i + 1 ;
      if i > 100 then
        exit;
      end if;
    end loop;
    presult.extend;
    presult(presult.last) := trim(vstr);
  end;
 
end AssociativeArray;
/
show err

