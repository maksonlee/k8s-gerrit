// Copyright (C) 2023 The Android Open Source Project
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

package com.google.gerrit.k8s.operator.gerrit.config;

import com.google.common.annotations.VisibleForTesting;
import com.google.gerrit.k8s.operator.gerrit.model.Gerrit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.eclipse.jgit.errors.ConfigInvalidException;
import org.eclipse.jgit.lib.Config;

@SuppressWarnings("rawtypes")
public abstract class ConfigBuilder {
  private final String configFileName;

  private List<RequiredOption> requiredOptions = new ArrayList<>();
  private Config config = new Config();

  public ConfigBuilder(String configFileName) {
    this.configFileName = configFileName;
  }

  abstract void addRequiredOptions(Gerrit gerrit);

  public ConfigBuilder forGerrit(Gerrit gerrit) {
    String configText = gerrit.getSpec().getConfigFiles().getOrDefault(configFileName, "");
    this.config = parseConfig(configText);

    addRequiredOptions(gerrit);

    return this;
  }

  @VisibleForTesting
  ConfigBuilder withConfig(String configText) {
    this.config = parseConfig(configText);
    return this;
  }

  void addRequiredOption(RequiredOption opt) {
    requiredOptions.add(opt);
  }

  private Config parseConfig(String text) {
    Config cfg = new Config();
    try {
      cfg.fromText(text);
    } catch (ConfigInvalidException e) {
      throw new IllegalStateException("Invalid configuration: " + text, e);
    }
    return cfg;
  }

  public Config build() {
    ConfigValidator configValidator = new ConfigValidator(requiredOptions);
    try {
      configValidator.check(config);
    } catch (InvalidGerritConfigException e) {
      throw new IllegalStateException(e);
    }
    setRequiredOptions();
    return config;
  }

  public void validate() throws InvalidGerritConfigException {
    new ConfigValidator(requiredOptions).check(config);
  }

  @SuppressWarnings("unchecked")
  private void setRequiredOptions() {
    for (RequiredOption<?> opt : requiredOptions) {
      if (opt.getExpected() instanceof String) {
        config.setString(
            opt.getSection(), opt.getSubSection(), opt.getKey(), (String) opt.getExpected());
      } else if (opt.getExpected() instanceof Boolean) {
        config.setBoolean(
            opt.getSection(), opt.getSubSection(), opt.getKey(), (Boolean) opt.getExpected());
      } else if (opt.getExpected() instanceof Set) {
        List<String> values =
            new ArrayList<String>(
                Arrays.asList(
                    config.getStringList(opt.getSection(), opt.getSubSection(), opt.getKey())));
        List<String> expectedSet = new ArrayList<String>();
        expectedSet.addAll((Set<String>) opt.getExpected());
        expectedSet.removeAll(values);
        values.addAll(expectedSet);
        config.setStringList(opt.getSection(), opt.getSubSection(), opt.getKey(), values);
      }
    }
  }

  public List<RequiredOption> getRequiredOptions() {
    return requiredOptions;
  }
}
