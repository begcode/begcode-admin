# monolithMybatis

本应用程序由BegCode8.1.0-beta.0生成, 你可以在 [https://www.begcode.com/documentation-archive/v8.1.0-beta.0](https://www.begcode.com/documentation-archive/v8.1.0-beta.0) 找到文档和帮助。

## Project Structure

生成时需要 Node，并建议在开发过程中使用它。package.json 文件始终会生成，以提供更好的开发体验，包括 prettier、commit hooks、脚本等等。

在项目的根目录中，JHipster会生成用于配置诸如git、prettier、eslint、husky等众多常见工具的配置文件。你可以在网络上找到有关这些工具的参考文档。

`/src/*` 目录结构遵循默认的Java结构。

- `.yo-rc.json` - Yeoman配置文件（BegCode/JHipster配置文件）
  BegCode的配置存储在key为generator-begcode的属性中，这里定义了BegCode生成器的全局配置。此外，你可能会在项目根目录下的.yo-rc.json文件中找到类似generator-begcode-\*的蓝图配置，它包含了项目特定的配置选项。
- `.yo-resolve` (可选) - Yeoman 冲突解决器
  允许在发现冲突时使用特定操作，跳过匹配模式的文件的提示。每一行应该匹配 [模式] [操作]，其中模式是一个 Minimatch 模式，操作是 skip（如果省略则为默认操作）或者 force 中的一个。以 # 开头的行被视为注释，将被忽略。
- `.jhipster/*.json` - JHipster实体配置文件

- `npmw` - 用于本地安装的npm的包装器
  JHipster默认使用构建工具在本地安装Node和npm。此包装器确保本地安装npm并使用它，避免了不同版本可能引起的一些差异。通过使用./npmw而不是传统的npm，您可以配置一个无需Node的环境来开发或测试您的应用程序。
- `/src/main/docker` - 应用程序及其依赖的服务的Docker配置

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
pnpm install
```

We use pnpm scripts and [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
pnpm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `pnpm update` and `pnpm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `pnpm help update`.

The `pnpm run` command will list all of the scripts available to run for this project.

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is commented out by default. To enable it, uncomment the following code in `front/src/index.html`:

```html
<script>
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('./service-worker.js').then(function () {
      console.log('Service Worker Registered');
    });
  }
</script>
```

Note: [Workbox](https://developers.google.com/web/tools/workbox/) powers JHipster's service worker. It dynamically generates the `service-worker.js` file.

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
pnpm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
pnpm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

## Building for production

### Packaging as jar

To build the final jar and optimize the monolithMybatis application for production, run:

```
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar
```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

### JHipster Control Center

JHipster Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker compose -f src/main/docker/jhipster-control-center.yml up
```

## Testing

### Spring Boot tests

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][]. They're located in [front/src/test/javascript/](front/src/test/javascript/) and can be run with:

```
pnpm test
```

## Others

### Code quality using Sonar

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off forced authentication redirect for UI in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

Additionally, Instead of passing `sonar.password` and `sonar.login` as CLI arguments, these parameters can be configured from [sonar-project.properties](sonar-project.properties) as shown below:

```
sonar.login=admin
sonar.password=admin
```

For more information, refer to the [Code quality page][].

### Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mysql database in a docker container, run:

```
docker compose -f src/main/docker/mysql.yml up -d
```

To stop it and remove the container, run:

```
docker compose -f src/main/docker/mysql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
npm run java:docker
```

Or build a arm64 docker image when using an arm64 processor os like MacOS with M1 processor family running:

```
npm run java:docker:arm64
```

Then run:

```
docker compose -f src/main/docker/app.yml up -d
```

When running Docker Desktop on MacOS Big Sur or later, consider enabling experimental `Use the new Virtualization framework` for better processing performance ([disk access performance is worse](https://github.com/docker/roadmap/issues/7)).

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[JHipster Homepage and latest documentation]: https://www.begcode.com
[JHipster 8.1.0-beta.0 archive]: https://www.begcode.com/documentation-archive/v8.1.0-beta.0
[Using JHipster in development]: https://www.begcode.com/documentation-archive/v8.1.0-beta.0/development/
[Using Docker and Docker-Compose]: https://www.begcode.com/documentation-archive/v8.1.0-beta.0/docker-compose
[Using JHipster in production]: https://www.begcode.com/documentation-archive/v8.1.0-beta.0/production/
[Running tests page]: https://www.begcode.com/documentation-archive/v8.1.0-beta.0/running-tests/
[Code quality page]: https://www.begcode.com/documentation-archive/v8.1.0-beta.0/code-quality/
[Setting up Continuous Integration]: https://www.begcode.com/documentation-archive/v8.1.0-beta.0/setting-up-ci/
[Node.js]: https://nodejs.org/
[NPM]: https://www.npmjs.com/
[Webpack]: https://webpack.github.io/
[BrowserSync]: https://www.browsersync.io/
[Jest]: https://facebook.github.io/jest/
[Leaflet]: https://leafletjs.com/
[DefinitelyTyped]: https://definitelytyped.org/
