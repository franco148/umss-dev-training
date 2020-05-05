### CREATE THE CLIENT APP INTO SPRING PROJECT

1. Adding react: Here we are going to use `npx` (included in the latest versions of `npm`).

```bash
# Command
npx create-react-app <app-name>

# Example
npx create-react-app client
```

2. Once completed previous step, go to the client folder `cd client`, and execute `npm start`. 
This will open a web browser on your desktop. You will see:

```bash
Compiled successfully!

You can now view client in the browser.

  Local:            http://localhost:3000
  On Your Network:  http://192.168.174.128:3000

Note that the development build is not optimized.
To create a production build, use npm run build.

```

Previous example means that you can access to you client app in `localhost:3000`

3. So far, we can see `backend` project running at `http://localhost:8585` and the `client` 
running at `http://localhost:3000`. So we would like to able to call services in the backend
and display the results in the frontend, but currently it is not possible due to `CORS` 
configuration, and for this we have many solutions, however the easiest one would be
running both `backend & frontend` in the same `HOST` and in the same `PORT`. 

So, in that sense, we will need to continue with the following steps.

4. Install a library `(Into your react project)` that is going to help us redirect the requests from `frontend` to `backend`.

```bash
npm i --save http-proxy-middleware
```

5. Create a proxy (according this [documentation](https://create-react-app.dev/docs/proxying-api-requests-in-development/#configuring-the-proxy-manually)) 
in the `react app` in order to redirect the requests from the `client` 
to the `server`. This change will enable us to call the `backend` without running into 
any `CORS` issues.

Create the file in: `client/src/` call it `setupProxy.js`

```js
const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        '/api',
        createProxyMiddleware({
            target: 'http://localhost:8585',
            changeOrigin: true,
        })
    );
};
```

The previous configuration is taking into account that our `backend service` has the following 
configuration:

```bash
SERVER PORT: 8585
SERVER BASE_PATH: /api/users
    

# Once the server is on, we can access it in the following path:
http://localhost:8585
```

6. For this example open `client/src/App.js` and add the following code in it.

```typescript jsx
import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';

class App extends Component {

    state = {};

    componentDidMount() {
        setInterval(this.hello, 1000);
    }

    hello = () => {
        fetch('/api/users/test-react')
            .then(response => response.text())
            .then(message => {
                this.setState({message: message});
            });
    };

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">{this.state.message}</h1>
                </header>
                <p className="App-intro">
                    To get started, edit <code>src/App.js</code> and save to reload.
                </p>
            </div>
        );
    }
}

export default App;
```

7. Let's execute `backend` and `frontend` project with the following commands:

```bash
# backend 
java -jar target/<application.name>.jar

or just use your IDE for starting the project.

# frontend
Go to client folder in the project, and execute: npm start
```

Once start your projects, you should see the `react app` at `http://localhost:3000` calling the service endpoint.


So far, we have configured our `development` environment, so we still need to do some other configurations
for `production` environment.


### PACKAGING THE REACT APP WITH SPRING BOOT

1. We'd like to be able to publish one jar file to production, and that jar file should contain 
both the backend and the frontend. Spring Boot applications can serve static content if you 
put it into the `classes/public` directory of the application jar file. Create React App can 
build a static bundle for production by running `npm build` in the client directory.

To accomplish this, we have to do the following.

- Create a production build of the frontend
- copy the production build into `${target/classes/public}`

2. We will use and configure `frontend-maven-plugin` and `maven-antrun-plugin` in our `pom.xml`.

Add the following under `/build` tag.
```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>

		<plugin>
			<groupId>com.github.eirslett</groupId>
			<artifactId>frontend-maven-plugin</artifactId>
			<version>1.10.0</version>
			<configuration>
				<workingDirectory>client</workingDirectory>
				<installDirectory>target</installDirectory>
			</configuration>
			<executions>
				<execution>
					<id>install node and npm</id>
					<goals>
						<goal>install-node-and-npm</goal>
					</goals>
					<configuration>
						<nodeVersion>v12.16.3</nodeVersion>
						<npmVersion>6.14.4</npmVersion>
					</configuration>
				</execution>
				<execution>
					<id>npm install</id>
					<goals>
						<goal>npm</goal>
					</goals>
					<configuration>
						<arguments>install</arguments>
					</configuration>
				</execution>
				<execution>
					<id>npm run build</id>
					<goals>
						<goal>npm</goal>
					</goals>
					<configuration>
						<arguments>run build</arguments>
					</configuration>
				</execution>
			</executions>
		</plugin>

		<plugin>
			<artifactId>maven-antrun-plugin</artifactId>
			<executions>
				<execution>
					<phase>generate-resources</phase>
					<configuration>
						<target>
							<copy todir="${project.build.directory}/classes/public">
								<fileset dir="${project.basedir}/client/build"/>
							</copy>
						</target>
					</configuration>
					<goals>
						<goal>run</goal>
					</goals>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```

3. When all previous steps are done, we can just type: `mvn clean install`. Once the command
completes, we can see the following structure of our project. Execute `tree target/classes`

```bash
linosx@elementary-vm:~/Code/umss-dev-training/java-project-template$ tree target/classes
target/classes
├── application.properties
├── com
│   └── umss
│       └── dev
│           └── training
│               └── jtemplate
│                   ├── common
│                   │   ├── config
│                   │   │   ├── BeanUtilsConfig.class
│                   │   │   └── SecurityConfig.class
│                   │   └── dto
│                   │       ├── request
│                   │       │   ├── RoleRegistrationDto.class
│                   │       │   └── UserRegistrationDto.class
│                   │       └── response
│                   │           └── UserResponseDto.class
│                   ├── controller
│                   │   └── UserRestController.class
│                   ├── event
│                   │   └── handler
│                   │       ├── auth
│                   │       │   └── LoginSuccessHandler.class
│                   │       ├── filter
│                   │       │   ├── JwtAuthenticationFilter.class
│                   │       │   └── JwtAuthorizationFilter.class
│                   │       └── SimpleGrantedAuthorityMixin.class
│                   ├── exception
│                   │   ├── ExceptionResponse.class
│                   │   └── UserNotFoundException.class
│                   ├── JTemplateServiceApplication.class
│                   ├── persistence
│                   │   ├── domain
│                   │   │   ├── Role.class
│                   │   │   ├── RoleEnum.class
│                   │   │   └── User.class
│                   │   └── repository
│                   │       └── UserRepository.class
│                   └── service
│                       ├── JwtService.class
│                       ├── JwtServiceImpl.class
│                       └── UserService.class
└── public
    ├── asset-manifest.json
    ├── favicon.ico
    ├── index.html
    ├── logo192.png
    ├── logo512.png
    ├── manifest.json
    ├── precache-manifest.df88e9ea56a593ebadd13784033714bc.js
    ├── robots.txt
    ├── service-worker.js
    └── static
        ├── css
        │   ├── main.82a252d1.chunk.css
        │   └── main.82a252d1.chunk.css.map
        ├── js
        │   ├── 2.51d0bc62.chunk.js
        │   ├── 2.51d0bc62.chunk.js.LICENSE.txt
        │   ├── 2.51d0bc62.chunk.js.map
        │   ├── main.362ee673.chunk.js
        │   ├── main.362ee673.chunk.js.map
        │   ├── runtime-main.98a2b520.js
        │   └── runtime-main.98a2b520.js.map
        └── media
            └── logo.5d5d9eef.svg

25 directories, 40 files
```

4. We can also check that the files are present in the resulting `jar` file. Execute
`jar tvf target/<project-name>.jar | grep public`

```bash
linosx@elementary-vm:~/Code/umss-dev-training/java-project-template$ jar tvf target/java-project-template-0.0.1-SNAPSHOT.jar | grep public
     0 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/
     0 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/
     0 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/media/
     0 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/css/
     0 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/js/
    67 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/robots.txt
  2213 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/index.html
  2671 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/media/logo.5d5d9eef.svg
  1652 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/css/main.82a252d1.chunk.css.map
  1069 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/css/main.82a252d1.chunk.css
  7948 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/js/main.362ee673.chunk.js.map
131301 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/js/2.51d0bc62.chunk.js
  8272 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/js/runtime-main.98a2b520.js.map
  5347 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/logo192.png
  9664 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/logo512.png
  1181 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/service-worker.js
   492 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/manifest.json
  1099 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/asset-manifest.json
   763 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/precache-manifest.df88e9ea56a593ebadd13784033714bc.js
  3150 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/favicon.ico
  1553 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/js/runtime-main.98a2b520.js
  1698 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/js/main.362ee673.chunk.js
   790 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/js/2.51d0bc62.chunk.js.LICENSE.txt
319768 Mon May 04 19:30:54 BOT 2020 BOOT-INF/classes/public/static/js/2.51d0bc62.chunk.js.map

```

5. Once all previous steps are completed, we will need to start the project (`jar` file that contains
frontend and backend projects).

```bash
java -jar target/java-project-template-0.0.1-SNAPSHOT.jar
```

After that you will be able to access your project just through one path only. In this case: 
`http://localhost:8585`


##### CONGRATULATIONS!!!
Now we have a simple/single jar file which can be deployed in our applications servers, and it
weill be ready to be used for our customers!
