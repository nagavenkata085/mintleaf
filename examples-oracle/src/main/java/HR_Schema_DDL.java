import org.qamatic.mintleaf.core.SqlDDL;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;

/**
 * Created by senthil on 5/6/15.
 */
@SqlObjectInfo(name = "HR_SCHEMA", source = "/HR_Schema_DDL.java", sourceDelimiter = ";")
public class HR_Schema_DDL extends SqlDDL{
    public HR_Schema_DDL(DbContext context) {
        super(context);
    }


}
