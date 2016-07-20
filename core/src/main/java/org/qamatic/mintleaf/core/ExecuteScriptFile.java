package org.qamatic.mintleaf.core;

import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.SqlReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by senips on 7/18/16.
 */
public class ExecuteScriptFile extends AbstractSqlScript {

    private final DbContext context;
    private final String filename;
    private final String delimiter;

    public ExecuteScriptFile(DbContext context, String filename, String delimiter) {
        super(context);
        this.context = context;
        this.filename = filename;
        this.delimiter = delimiter;
    }

    @Override
    protected SqlReader getSourceReader() {
        InputStream stream = null;
        logger.info("reading file: " + this.filename);
        if (this.filename.startsWith("res:")) {
            String resFile = this.filename.substring(4);
            stream = this.getClass().getResourceAsStream(resFile);
        } else {
            try {
                stream = new FileInputStream(this.filename);
            } catch (FileNotFoundException e) {
                logger.error("file not found " + this.filename, e);
            }
        }

        SqlReader reader = new SqlStreamReader(stream);
        reader.setDelimiter(this.delimiter);
        return reader;
    }
}
