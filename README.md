# Trevor (n.) \tɹˈɛvə\
[![Build Status](https://ci.schemati.co/buildStatus/icon?job=trevor)](https://ci.schemati.co/job/trevor/)
![Java CI with Gradle](https://github.com/schematico/trevor/workflows/Java%20CI%20with%20Gradle/badge.svg)
[![Discord](https://img.shields.io/discord/548983491157819413.svg?logo=discord&label=)](https://discord.gg/7AEgrbE)

Trevor is an open-source platform independent solution that unionizes Redis and [Notchian-compliant](https://www.spigotmc.org/threads/what-is-notchian.156265/#post-1659509) proxies for cross-instance proxy communication.

## Goals
As a community, to unionize our thinking (and as well as our proxies!) setting goals is an important part of working together as a group.

#### Main Goal
Trevor as an idea was created to build upon the ideas set forth by [RedisBungee](https://github.com/minecrafter/RedisBungee) by creating a solution that has the ability to work with multiple proxy softwares in order to satisfy the need for cross-instance proxy communication. We developed Trevor because there are platforms where such cross-instance proxy communication simply does not exist.

#### Future goals
As the project progresses, it is our goal to provide high quality, featureful cross-instance proxy communication. We have future plans to expand in two main areas: storage and support. 

* Storage expansion means eventually, more ways to synchronize the servers will become avaliable as a replacement to just Redis at the moment. 
* Support expansion means that more platforms than just BungeeCord and Velocity will be supported. We also hope to perhaps also support Minecraft: Bedrock Edition as well in the future.

## Building Trevor
```
git clone https://github.com/schematico/trevor.git
cd trevor/
./gradlew build
```

## Project Layout
The project is laid out in multiple modules which interact with eachother in the ways described:

* **API** An open, public API used by other plugins to rely, integrate, or interact with Trevor.
* **Common** The code that is common between the independant platform modules is kept here.
* **Bungee, Velocity** These modules utilize the common module to integrate with Trevor on their respective platforms.
