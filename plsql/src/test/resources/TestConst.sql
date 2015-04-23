create or replace package TestConst
as

--@testoverride appname
  APP_NAME CONSTANT VARCHAR2(8) := 'DBWORKS';
  --@end

  NAMESPACE CONSTANT VARCHAR2(7) := 'TEST_NS';

end TestConst;

/
 
create or replace
package body TestConst
as

  TEST_ID CONSTANT VARCHAR2(9) := '3433';

end TestConst;

/
 

