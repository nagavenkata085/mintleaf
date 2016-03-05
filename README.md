[![Build Status](https://travis-ci.org/senips/mintleaf.svg?branch=master)](https://travis-ci.org/senips/mintleaf)

# MintLeaf

Cloudify your database development on Continuous Delivery Model

- Database Migration
- Acceptance Testing
- Unit Testing Foundation Framework
- Unit Testing support for PLSQL
- Support for mocking : data, schema and database objects

## JavaDoc

~[Java Doc.](http://senips.github.io/mintleaf/javadoc/)

## Architecture
![Architecture](https://github.com/senips/mintleaf/blob/master/img/mintleafarch.jpg)

## Build
mvn clean build

For Sonar:

mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Pcoverage-per-test sonar:sonar

## License

The MIT License (MIT)

Copyright (c) 2010-2015 Senthil Maruthaiappan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

