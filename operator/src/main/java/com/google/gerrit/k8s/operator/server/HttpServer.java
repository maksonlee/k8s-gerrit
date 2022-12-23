// Copyright (C) 2022 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gerrit.k8s.operator.server;

import com.google.inject.Singleton;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

@Singleton
public class HttpServer {
  private final Server server = new Server();

  public void start() throws Exception {
    ServerConnector connector = new ServerConnector(server);
    connector.setPort(8080);
    server.setConnectors(new Connector[] {connector});

    ServletHandler servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(HealthcheckServlet.class, "/health");
    server.setHandler(servletHandler);

    server.start();
  }
}