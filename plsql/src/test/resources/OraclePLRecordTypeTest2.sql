create or replace package OracleRecordTypeTest
as
    TYPE t_ColumnRecTest IS RECORD (
      column_name varchar2(30),
      data_type varchar2(30),
      precision   smallint,
      scale smallint,
      nullable boolean default true,
      char_semantics varchar2(4),
      comments varchar2(255));
      
  TYPE t_ColumnsTableTest IS TABLE OF t_ColumnRecTest;
  
PROCEDURE clear_columns;  
function getColumnName(idx IN NUMBER) return VARCHAR2;  
 procedure add_columnrec(pcolumnrec IN t_ColumnRecTest);  
 PROCEDURE add_column(pcolumn_name IN varchar2
                     , pdata_type IN varchar2
                     , pprec IN smallint default null
                     , pscale IN smallint default null
                     , pnullable IN boolean default true
                     , pchar_semantics IN varchar2 default 'CHAR'
                     , pcomments IN varchar2 default null); 
  
end OracleRecordTypeTest;

/
 
create or replace
package body OracleRecordTypeTest
as
  vcols t_ColumnsTableTest := t_ColumnsTableTest();

procedure add_columnrec(pcolumnrec IN t_ColumnRecTest)
AS
begin
  vcols.extend;
  vcols(vcols.LAST) := pcolumnrec;  
end;


function getColumnName(idx IN NUMBER) return VARCHAR2
AS
Begin
  return vcols(idx).column_name;
end;

PROCEDURE clear_columns
  as
  begin
    vcols := t_ColumnsTableTest();
  end;



 PROCEDURE add_column(pcolumn_name IN varchar2
                     , pdata_type IN varchar2
                     , pprec IN smallint default null
                     , pscale IN smallint default null
                     , pnullable IN boolean default true
                     , pchar_semantics IN varchar2 default 'CHAR'
                     , pcomments IN varchar2 default null)
  AS
  begin
    vcols.extend;
    vcols(vcols.LAST).column_name := upper(pcolumn_name);
    vcols(vcols.LAST).data_type := upper(pdata_type);
    vcols(vcols.LAST).precision := pprec;
    vcols(vcols.LAST).scale := pscale;
    vcols(vcols.LAST).nullable := pnullable;
    vcols(vcols.LAST).char_semantics := pchar_semantics;
    vcols(vcols.LAST).comments := pcomments;

  end;

end OracleRecordTypeTest;

/