# ffishlogs

## build

Using GraalVM Native Image:
```
sbt core/nativeImage
```
If `JAVA_HOME` is already set to a distribution of GraalVM 21+:
```
GRAALVM_INSTALLED=true sbt core/nativeImage
```

## configuration

It is recommended to configure the application by passing a `-Dproperty.name=value` to the program for each
configuration value. Environment variables only exist as a convenience. If both a property and an environment variable
exist, the property will take priority.

| Property                                   | Environment Variable             | Default   | Description                                                                                                                                                      |
|--------------------------------------------|----------------------------------|-----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `java.util.logging.SimpleFormatter.format` | N/A                              | ???       | It is recommended to set this to `%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %5$s%6$s%n` as the default may break lines and not include the correct stack traces |
| `ffishlogs.discord.clientid`               | `FFISHLOGS_DISCORD_CLIENTID`     | none      | The Client ID the server will use for Discord OAuth                                                                                                              |
| `ffishlogs.discord.clientsecret`           | `FFISHLOGS_DISCORD_CLIENTSECRET` | none      | The Client Secret the server will use for Discord OAuth                                                                                                          |
| `ffishlogs.httpserver.host`                | `FFISHLOGS_HTTPSERVER_HOST`      | `0.0.0.0` | The `host` the HTTP server binds to                                                                                                                              |
| `ffishlogs.httpserver.port`                | `FFISHLOGS_HTTPSERVER_PORT`      | `8080`    | The `port` the HTTP server binds to                                                                                                                              |
| `ffishlogs.sqlite.database`                | `FFISHLOGS_SQLITE_DATABASE`      | none      | The path to the `.sqlite` file, which will be passed directly to JDBC                                                                                            |
