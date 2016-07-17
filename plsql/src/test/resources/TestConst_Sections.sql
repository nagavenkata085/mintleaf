
-- <ChangeSet id="package"  />

  function getTEST_ID return varchar2;

-- <ChangeSet id="packagebody"  />


  function getTEST_ID return varchar2 as
  begin
	 
	  return TEST_ID;
  end;
  
