package org.qamatic.mintleaf.dbs.h2;

import org.qamatic.mintleaf.core.AbstractDbContext;
import org.qamatic.mintleaf.DbColumn;
import org.qamatic.mintleaf.RowListener;
import org.qamatic.mintleaf.DbMetaData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Created by senips on 7/12/16.
 */
public class H2DbContextImpl extends AbstractDbContext implements H2DbContext {
    public H2DbContextImpl(DataSource datasource) {
        super(datasource);
    }
    
    
    public void iterateTableByQuery(String tableName, String columns, String whereClause, RowListener listener) throws SQLException {
        DbMetaData metaData = getMetaData(tableName);
    }

    public void iterativeQuery(String sql, final RowListener listener){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.getDataSource());
        jdbcTemplate.query(sql, new RowMapper() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                listener.getMetaData(1);

                return null;
            }
        });

    }


    @Override
    public DbMetaData getMetaData(String objectName) throws SQLException {
        final DbMetaData metaData = new DbMetaData();
        if(objectName != null) {
            objectName = objectName.toUpperCase();
        }

        String[] splits = objectName.split(Pattern.quote("."));

        metaData.setObjectName(objectName);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.getDataSource());
        String sql = String.format("select * from information_schema.columns where TABLE_SCHEMA ='%s' and TABLE_NAME='%s'", splits[0], splits[1]);
        jdbcTemplate.query(sql, new RowMapper() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                DbColumn colMetaData = new DbColumn();
                colMetaData.setColumnName(rs.getString("COLUMN_NAME"));
                colMetaData.setNullable(rs.getString("IS_NULLABLE")!="NO");
                colMetaData.setColumnSize(1);
                colMetaData.setDatatype(rs.getInt("DATA_TYPE"));

                //colMetaData.setTypeName(rs.getString("TYPE_NAME"));
                colMetaData.setColumnSize(rs.getInt("CHARACTER_OCTET_LENGTH"));

                if(rs.getString("TYPE_NAME").equals("CHAR")) {
                  //  colMetaData.setColumnSize(rs.getInt("CHARACTER_OCTET_LENGTH"));
                } else if(rs.getString("TYPE_NAME").equals("DECIMAL")) {
                    colMetaData.setColumnSize(rs.getInt("NUMERIC_PRECISION"));
                }
                colMetaData.setDecimalDigits(rs.getInt("NUMERIC_SCALE"));

                metaData.add(colMetaData);
                return null;
            }
        });
        return metaData;
    }

    @Override
    public boolean isTableExists(String tableName) throws SQLException {
         return getMetaData(tableName).size()!=0;
    }
}
