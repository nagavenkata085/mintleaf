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

package org.qamatic.mintleaf.interfaces;/*
 * Copyright {2015} {Senthil Maruthaiappan}
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

@XmlRootElement
@XmlType(propOrder = {"name", "delimiter"})
public class SqlPart {

    private static final Logger logger = LoggerFactory.getLogger(SqlPart.class);
    private String name = "";
    private String delimiter = "";
    private String sqlPartSource = "";

    public SqlPart() {

    }


    public SqlPart(String partName, String delimiter, String source) {
        this.name = partName;
        this.delimiter = delimiter;
        sqlPartSource = source;
    }

    public static SqlPart xmlToSqlPart(String xmlStr) {
        String cleanXml = xmlStr.replace("--", "");
        try {
            JAXBContext jc = JAXBContext.newInstance(SqlPart.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(cleanXml));
            JAXBElement<SqlPart> je = unmarshaller.unmarshal(streamSource,
                    SqlPart.class);

            return (SqlPart) je.getValue();


        } catch (JAXBException e) {
            System.out.println(cleanXml);
            logger.error("sqlpart xml parsing error: ", e);
        }
        return null;
    }

    public String getSqlPartSource() {
        return sqlPartSource;
    }

    @XmlTransient
    public void setSqlPartSource(String sqlPartSource) {
        this.sqlPartSource = sqlPartSource;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    public String getDelimiter() {
        return delimiter;
    }

    @XmlAttribute
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public String toString() {
        try {
            JAXBContext jc = JAXBContext.newInstance(SqlPart.class);
            Marshaller marshaller = jc.createMarshaller();
            StringWriter sw = new StringWriter();
            marshaller.marshal(this, sw);
            return sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
