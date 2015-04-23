/*
 * Copyright {2011-2015} Senthil Maruthaiappan
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.qamatic.mintleaf.oracle;

import org.qamatic.mintleaf.core.BaseSqlObject;
import org.qamatic.mintleaf.core.SqlObjectDependsOn;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlTrigger;
import org.springframework.jdbc.core.JdbcTemplate;

@SqlObjectDependsOn(Using = {DbUtility.class})
public abstract class BaseSqlTrigger extends BaseSqlObject implements SqlTrigger {

    public BaseSqlTrigger(DbContext context) {
        super(context);
    }

    @Override
    public void drop() {
        DbUtility utils = new DbUtility(getDbContext());
        if (!utils.isUserObjectExists(this.getName(), "TRIGGER")) {
            return;
        }
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getDbContext().getDataSource());
        template.execute("drop trigger " + this.getName());
    }

}
