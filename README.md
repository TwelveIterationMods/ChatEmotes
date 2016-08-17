EiraMoticons
=======

This mod adds emoticons to Minecraft chat. By default, it adds a few of its own emote packs as well as all Twitch global emotes and most Twitch subscriber emotes, but can be configured to include or not include any of the packs. One can also add custom emotes by putting them into the "emoticons" folder in the Minecraft directory. The mod is client-side only - the server does not need it and will simply ignore it.

##Useful Links
* [Latest Builds](http://jenkins.blay09.net) on my Jenkins

##API

In your build.gradle, add the repository

```
repositories {
    maven {
        url "http://artifactory.blay09.net/artifactory/jenkins-maven/"
    }
}
```

Then, add a dependency to the build.gradle

```
dependencies {
    compile 'net.blay09.mods:eiramoticons:major.minor.build'
}
```

See the Jenkins or CurseForge to find out the latest version number.

Done! Run gradle to update your project and you'll be good to go.

The latest EiraMoticons API of the mod can also be downloaded from my [Jenkins](http://jenkins.blay09.net), if you're not into all that maven stuff.