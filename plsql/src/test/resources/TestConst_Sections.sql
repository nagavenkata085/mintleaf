
-- <sqlpart name="package"  />

  function getTEST_ID return varchar2;

-- <sqlpart name="packagebody"  />


  function getTEST_ID return varchar2 as
  begin
	 
	  return TEST_ID;
  end;
  
