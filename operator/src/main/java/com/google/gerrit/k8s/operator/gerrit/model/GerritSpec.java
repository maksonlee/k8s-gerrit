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

package com.google.gerrit.k8s.operator.gerrit.model;

import com.google.gerrit.k8s.operator.shared.model.ContainerImageConfig;
import com.google.gerrit.k8s.operator.shared.model.GerritStorageConfig;
import com.google.gerrit.k8s.operator.shared.model.GlobalRefDbConfig;
import com.google.gerrit.k8s.operator.shared.model.IngressConfig;

public class GerritSpec extends GerritTemplateSpec {
  private ContainerImageConfig containerImages = new ContainerImageConfig();
  private GerritStorageConfig storage = new GerritStorageConfig();
  private IngressConfig ingress = new IngressConfig();
  private GlobalRefDbConfig refdb = new GlobalRefDbConfig();
  private String serverId = "";

  public GerritSpec() {}

  public GerritSpec(GerritTemplateSpec templateSpec) {
    super(templateSpec);
  }

  public ContainerImageConfig getContainerImages() {
    return containerImages;
  }

  public void setContainerImages(ContainerImageConfig containerImages) {
    this.containerImages = containerImages;
  }

  public GerritStorageConfig getStorage() {
    return storage;
  }

  public void setStorage(GerritStorageConfig storage) {
    this.storage = storage;
  }

  public IngressConfig getIngress() {
    return ingress;
  }

  public void setIngress(IngressConfig ingress) {
    this.ingress = ingress;
  }

  public GlobalRefDbConfig getRefdb() {
    return refdb;
  }

  public void setRefdb(GlobalRefDbConfig refdb) {
    this.refdb = refdb;
  }

  public String getServerId() {
    return serverId;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }
}
