EiraMoticons
=======

Minecraft Mod. adds emoticons to Minecraft chat. By default, it adds all Twitch global emotes and most Twitch subscriber emotes, but can be configured to limit those further or add custom emotes by putting them into the "emoticons" folder in the Minecraft directory. The mod is client-side only - the server does not need it and will currently crash if put on there.

##Useful Links
* [Latest Builds](http://jenkins.blay09.net) on my Jenkins
* [Minecraft Forum Topic]() for discussion, support and feature requests 
* [EiraIRC - bring Twitch and IRC chat to Minecraft!](http://minecraft.curseforge.com/mc-mods/68420-eirairc)

##API
The easiest way to add EiraMoticons to your development environment is to do some additions to your build.gradle file. First, register EiraMoticons's maven repository by adding the following lines:

```
repositories {
    maven {
        name = "eiramods"
        url ="http://repo.blay09.net"
    }
}
```

Then, add a dependency to either just the EiraMoticons API (api) or, if you want EiraMoticons to be available while testing as well, the deobfuscated version (dev):

```
dependencies {
    compile 'net.blay09.mods:eiramoticons:not.yet.available:dev' // or just api instead of dev
}
```

*Important*: If you do use the dev version like that, make sure that you still only use code within the API packages! Rikka will get mad at you and give you a dose of Schwarz Sechs if you mess with any of EiraMoticons's internal classes.

Make sure you enter the correct version number for the Minecraft version you're developing for. The major version is the important part here; it is increased for every Minecraft update.

Done! Run gradle to update your project and you'll be good to go.

The latest EiraMoticons API and an unobfuscated version of the mod can also be downloaded from my [Jenkins](http://jenkins.blay09.net), if you're not into all that maven stuff.