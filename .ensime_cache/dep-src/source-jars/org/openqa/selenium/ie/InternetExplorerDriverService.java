/*
Copyright 2011-2012 Selenium committers
Copyright 2011-2012 Software Freedom Conservancy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.openqa.selenium.ie;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.Beta;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Manages the life and death of an IEDriverServer.
 */
public class InternetExplorerDriverService extends DriverService {

  /**
   * System property that defines the location of the IEDriverServer executable
   * that will be used by the {@link #createDefaultService() default service}.
   */
  public static final String IE_DRIVER_EXE_PROPERTY = "webdriver.ie.driver";

  /**
   * System property that defines the location of the file where IEDriverServer
   * should write log messages to.
   */
  public static final String IE_DRIVER_LOGFILE_PROPERTY = "webdriver.ie.driver.logfile";

  /**
   * System property that defines the detalization level the IEDriverServer logs.
   */
  public static final String IE_DRIVER_LOGLEVEL_PROPERTY = "webdriver.ie.driver.loglevel";

  /**
   * System property that defines the implementation of the driver engine to use.
   */
  public static final String IE_DRIVER_ENGINE_PROPERTY = "webdriver.ie.driver.engine";

  /**
   * System property that defines host to which will be bound IEDriverServer.
   */
  public final static String IE_DRIVER_HOST_PROPERTY = "webdriver.ie.driver.host";

  /**
   * System property that defines path to which will be extracted IEDriverServer library.
   */
  public final static String IE_DRIVER_EXTRACT_PATH_PROPERTY = "webdriver.ie.driver.extractpath";

  /**
   * System property that defines logging to stdout for IEDriverServer.
   */
  public final static String IE_DRIVER_SILENT_PROPERTY = "webdriver.ie.driver.silent";

  /**
   *
   * @param executable The IEDriverServer executable.
   * @param port Which port to start the IEDriverServer on.
   * @param args The arguments to the launched server.
   * @param environment The environment for the launched server.
   * @throws IOException If an I/O error occurs.
   */
  private InternetExplorerDriverService(File executable, int port, ImmutableList<String> args,
                                        ImmutableMap<String, String> environment) throws IOException {
    super(executable, port, args, environment);
  }

  /**
   * Configures and returns a new {@link InternetExplorerDriverService} using the default configuration. In
   * this configuration, the service will use the IEDriverServer executable identified by the
   * {@link #IE_DRIVER_EXE_PROPERTY} system property. Each service created by this method will
   * be configured to use a free port on the current system.
   *
   * @return A new InternetExplorerDriverService using the default configuration.
   */
  public static InternetExplorerDriverService createDefaultService() {
    return new Builder().usingAnyFreePort().build();
  }

  /**
   * Builder used to configure new {@link InternetExplorerDriverService} instances.
   */
  public static class Builder {

    private int port = 0;
    private File exe = null;
    private ImmutableMap<String, String> environment = ImmutableMap.of();
    private File logFile;
    private InternetExplorerDriverLogLevel logLevel;
	private InternetExplorerDriverEngine engineImplementation;
    private String host = null;
    private File extractPath = null;
    private Boolean silent = null;
    private Boolean forceCreateProcess = null;
    private String ieSwitches = null;

    /**
     * Sets which driver executable the builder will use.
     *
     * @param file The executable to use.
     * @return A self reference.
     */
    public Builder usingDriverExecutable(File file) {
      checkNotNull(file);
      checkExecutable(file);
      this.exe = file;
      return this;
    }

    /**
     * Sets which port the driver server should be started on. A value of 0 indicates that any
     * free port may be used.
     *
     * @param port The port to use; must be non-negative.
     * @return A self reference.
     */
    public Builder usingPort(int port) {
      checkArgument(port >= 0, "Invalid port number: %d", port);
      this.port = port;
      return this;
    }

    /**
     * Configures the driver server to start on any available port.
     *
     * @return A self reference.
     */
    public Builder usingAnyFreePort() {
      this.port = 0;
      return this;
    }

    /**
     * Defines the environment for the launched driver server. These
     * settings will be inherited by every browser session launched by the
     * server.
     *
     * @param environment A map of the environment variables to launch the
     *     server with.
     * @return A self reference.
     */
    @Beta
    public Builder withEnvironment(Map<String, String> environment) {
      this.environment = ImmutableMap.copyOf(environment);
      return this;
    }

    /**
     * Configures the driver server to write log to the given file.
     *
     * @param logFile A file to write log to.
     * @return A self reference.
     */
    public Builder withLogFile(File logFile) {
      this.logFile = logFile;
      return this;
    }

    /**
     * Configures the logging level for the driver server.
     *
     * @param logLevel A level of the log verbosity.
     * @return A self reference.
     */
    public Builder withLogLevel(InternetExplorerDriverLogLevel logLevel) {
      this.logLevel = logLevel;
      return this;
    }

    /**
     * Configures the driver engine implementation for the driver server.
     *
     * @param engineImplementation The engine implementation to be used.
     * @return A self reference.
     */
    public Builder withEngineImplementation(InternetExplorerDriverEngine engineImplementation) {
      this.engineImplementation = engineImplementation;
      return this;
    }

    /**
     * Configures the host to which the driver server bound.
     *
     * @param host A host name.
     * @return A self reference.
     */
    public Builder withHost(String host) {
      this.host = host;
      return this;
    }

    /**
     * Configures path to which the driver server library will be extracted.
     *
     * @param extractPath A path.
     * @return A self reference.
     */
    public Builder withExtractPath(File extractPath) {
      this.extractPath = extractPath;
      return this;
    }

    /**
     * Configures silence in stdout of the driver server by unlogged messages.
     *
     * @param silent To be silent in stdout ir not.
     * @return A self reference.
     */
    public Builder withSilent(Boolean silent) {
      this.silent = silent;
      return this;
    }

    /**
     * Creates a new service to manage the driver server. Before creating a new service, the
     * builder will find a port for the server to listen to.
     *
     * @return The new service object.
     */
    public InternetExplorerDriverService build() {
      if (port == 0) {
        port = PortProber.findFreePort();
      }
      if (exe == null) {
        exe = findExecutable("IEDriverServer", IE_DRIVER_EXE_PROPERTY,
                             "http://code.google.com/p/selenium/wiki/InternetExplorerDriver",
                             "http://selenium-release.storage.googleapis.com/index.html");
      }
      if (logFile == null) {
        String logFilePath = System.getProperty(IE_DRIVER_LOGFILE_PROPERTY);
        if (logFilePath != null) {
          logFile = new File(logFilePath);
        }
      }
      if (logLevel == null) {
        String level = System.getProperty(IE_DRIVER_LOGLEVEL_PROPERTY);
        if (level != null) {
          logLevel = InternetExplorerDriverLogLevel.valueOf(level);
        }
      }
      if (engineImplementation == null) {
        String engineToUse = System.getProperty(IE_DRIVER_ENGINE_PROPERTY);
        if (engineToUse != null) {
          engineImplementation = InternetExplorerDriverEngine.valueOf(engineToUse);
        }
      }
      if (host == null) {
        String hostProperty = System.getProperty(IE_DRIVER_HOST_PROPERTY);
        if (hostProperty != null) {
          host = hostProperty;
        }
      }
      if (extractPath == null) {
        String extractPathProperty = System.getProperty(IE_DRIVER_EXTRACT_PATH_PROPERTY);
        if (extractPathProperty != null) {
          extractPath = new File(extractPathProperty);
        }
      }
      if (silent == null) {
        String silentProperty = System.getProperty(IE_DRIVER_SILENT_PROPERTY);
        if (silentProperty != null) {
          silent = Boolean.valueOf(silentProperty);
        }
      }

      try {
        ImmutableList.Builder<String> argsBuilder = ImmutableList.builder();
        argsBuilder.add(String.format("--port=%d", port));
        if (logFile != null) {
          argsBuilder.add(String.format("--log-file=\"%s\"", logFile.getAbsolutePath()));
        }
        if (logLevel != null) {
          argsBuilder.add(String.format("--log-level=%s", logLevel.toString()));
        }
        if (engineImplementation != null) {
          argsBuilder.add(String.format("--implementation=%s", engineImplementation.toString()));
        }
        if (host != null) {
          argsBuilder.add(String.format("--host=%s", host));
        }
        if (extractPath != null) {
          argsBuilder.add(String.format("--extract-path=\"%s\"", extractPath.getAbsolutePath()));
        }
        if (silent != null && silent.equals(Boolean.TRUE)) {
          argsBuilder.add("--silent");
        }

        return new InternetExplorerDriverService(exe, port, argsBuilder.build(), environment);

      } catch (IOException e) {
        throw new WebDriverException(e);
      }
    }
  }
}