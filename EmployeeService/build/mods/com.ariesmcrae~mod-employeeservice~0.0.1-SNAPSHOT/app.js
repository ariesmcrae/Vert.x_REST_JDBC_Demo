/** 
	The MIT License (MIT)
	
	Copyright (c) 2014 Aries McRae
	
	Permission is hereby granted, free of charge, to any person obtaining a copy of
	this software and associated documentation files (the "Software"), to deal in
	the Software without restriction, including without limitation the rights to
	use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
	the Software, and to permit persons to whom the Software is furnished to do so,
	subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
	FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
	COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
	IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
	CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */


var container = require('vertx/container')
var appConfig = container.config;

container.deployVerticle("com.ariesmcrae.employeeservice.Server", appConfig.webserver_config, 8); //Create 8 Server.java instances - 1 for each core, so that it saturates 8 cores. Remaining 8 cores are saturated by Mapper.java (assuming it's a 16 core server).
container.deployVerticle("com.ariesmcrae.employeeservice.Mapper", 8);
container.deployModule("com.bloidonia~mod-jdbc-persistor~2.1.2", appConfig.jdbc_persistor_config, 30); //Make 30 worker threads


