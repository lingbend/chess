# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

# Note

**This project is currently in progress and is on track to complete at the end of Winter semester, 2025 (April).**

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)]([https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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-FNr7vCYeupybAXFJgMkq9VEsm3pqfe-JpgMG5vzaS8lrQ2kluqTOdVtKo5rqrZ6tANaGHnzTmyxtyHN0MXzsaodnbu1Wolf221WHMYwFHUqlVU7COnp1TAP187DViEw6aowKALUoFw2WwdA8iMka3q65j7rVX4a9RR31c676LuXbyzsvjQ2+i3RG85bMNpZJ+bcv5AI40pxeWLUFHzr3KahapmFj7VbPs1jp1a9wlMhJU0bfmsLzYIqtoESEtoHbQhgAAcVHKybFHts3SW1XJDzpLFL2FHKW2DkH+kv0qNW5lLlENMOhF5si2doTcr7vy1RMAhUirY7BjjHauNOtI5OyLwnEu6pcXfOjmX20CvKMgWIKW1CwhlTXB1xGStdtZBOku3np2UfBAAWWAP4CAbjPOjnjEGqDDM3PNe83JyNfRo1rFG9gR5Llc2JqHFMMLZFxiVH6AdlAABJaQR2Am9nKnBJ47tx40JmDvPoOgECgAbAaOOfQvinYAHKjgmBJGAjRjNFI4qUDNvR9veaOxUE7o4LtXZu3dqYD2tHwJWM9pIr33siq+5037o4AejCB3sEHWa-2afXMF3Bp2IuCfLRQ4NBE4u1rJunZL3m0uxAywxjt5Rcs9r9H28DBHOOiK64q8dZHysS5E9V9xnj6OzeiyWBbYAWuqGW6VZJu6cpgdqbE+LhTqfdQvW8sFenKmG+-abSB81n2vvMxLa3NVP2pCN-UqngWachdaPTsDUqouVrZwh7VoMmua+51HvnRysvhnKH6bwyRWThCqQQUYMBKAwSfnNmLGutc676Bc9m+mDa2elSblN+FKii0t7p991mDOV-+Q71Ni1gWDhdxk6WFe7l2YOHCi2iLrbYB8FAbA3B4C6kyFNrPvncU+9ry00L4Wg-qt3P9zpvTmf5-pbBt52-hmm3Z5Mz0eoWuwjQCgZILW48roT8XIX+XGeFYFcV6XC7Zd+j2hBirEeacNGNWD8Kuj+9W2WmwTYV+LWNQOgAAVsKv5BRnKsRpAHsvPoYHAYgeAINgrv6rVt4iztsrfvfuGitmtrMKdhdltqbr7gRLtv1NQZdhMNdrdvbszuDmmlDgjqMEjqUGwXBN7o0rmjgmvqMAzmLugJmiHicjBozkmtXk0qDBfpkFfjfnfqOA-pJqugMhoWQTmPJmVDGmXiEsJKJBiMeiyrmkRA3hZk3jVOYehkxLSlwYtDwe8vYTrGYXRB5H5MIfimbpUGIQHuvmWlvsTjvhwbNJQtBtWhEaMCTggqfuHnWqoSgOoaQVoc4QsvHhAYnjll2nlr2nhlIVXigZ1txt-mVr-j-EJvLpVtRqJkrkumATocQfodkYYStnrqYe7rblEnUrQTXoAuepej3uCjbgeoMT+u3kCs7mko3t4f0dMUepTiIUEavqERIRvnOAkSgEkU9HvmruuPEUwZESfupieo0ekZkZoaMNoW2lhknqkCnmnhnkgFnjntAHnicQqFkQ8eQQpiYVZlci3gPlXkLJsfXm+ssWmDZhCW3pwY7hDl3rCRUmCf3n8vUsPo5kigEJYMxhnMkDAAAFIQA8iYGBBvYfb+beybFVCUh05hGwa7hT7ACElQBwA3zQA3TMErCCHREqz76nGwZ7BsnvacnckZxQB8mI4sF9CClXEsqNHwEUkzJqk8iPGJhP6IyFE4YlHsbtaf5jrVECZbx1EaoAF1rAEGqgF1bPELrYxX7MHGmS5VF9bMF4GNG2kBptFPGMZ+CaIunymzAgA8lQCzCoyDaoF0QcCsjsmcnemAHuS+mEGq6+KaloAGHbprbDHPLriJo9Bg4oncEgrFm-obH0HBFMniEoCSFSoyEVpyFlrim9DKkJYpnlBZmwhZnal0windnqk5lGFET65Ci+EWFYT5mUZyS2GLFeEYlCSTk5FCl0HzHlmeGu4OHLn0T+HrGBHVnVC1nbH1m7HSH9SJmUDSm8l-T8msEo5rlGSh5ikSkcnXkRlyl8EKlKlKFbI9l9k5Gto6n5HP5FHC7ipGkxmVHdZmnTh-40rWkc6+nib+kFSDkwBZkjk9EXrjke5e4zk2EW4LnblwkG6rEPquGlmd4LFbm94foDFrGVmHkr6MnMk7HhGXmSkfkylfnnY-mPnGYYXyFlFtn9BXlcmfl3nykCmCV-mqnqm9nqn9kqIFEv6GkFZumoEek-4Wn-4NFdlNGK5+kOmMZuLOmjiwiunQUjqwWenynJk2nNEmX84NapTYDBmWXMFhkRlRmExaWdZyCZDDDyysiQC1Q2iHzcVQCOUc7J4QCp6gUsgQQAAeKgEJ3QpUm040O0k0tqRBGFWFXR26PU45OVdEeVzYhFmxPUjQJF9FIS5Vu0+084JZHe6sPU6J2V20FVe0U0B0zFOaDJJ5LQgenF4l0VN5sp0l35sl7BQlfxsWr5XF75klvFM1-Fc1QhHZrEClWcgFLaplAuEQHxXxTMvxmZw5xVRhpeoJ8J4J2J1VR59e9VkxmJNyreT5T6qJZmr1bu91WJRmZs8Kls+JXgHJXYTqtU2AU+hA8QiQKQOKnsLFoxeaxKgGikxgshr0FZZ+VGbqlqyo+sFqxsKl-cx1BNrGRNAVdclNrIKAJQlgfoPI9gsVkyaZ9pBVi1BNpNQJq2iET1K+RZbVQKPBB5Q1R5AGharQWNzZONO1RkdalNCgRNPNiQZNupvC7AYQqg0AzAE2MAPIMIwQCA2e51XNviatbCfNt1S5esD1MKgtqNL1dFb1ANH1iJX1JmP1m5VuO59tgNg+wNmAQAA))

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
