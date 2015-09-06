EiraMoticons
=======

This mod adds emoticons to Minecraft chat. By default, it adds a few of it's own emote packs as well as all Twitch global emotes and most Twitch subscriber emotes, but can be configured to include or not include any of the packs. One can also add custom emotes by putting them into the "emoticons" folder in the Minecraft directory. The mod is client-side only - the server does not need it and will simply ignore it.

##Useful Links
* [Latest Builds](http://jenkins.blay09.net) on my Jenkins
* [Help Page and Emoticon List](http://blay09.net/?page_id=347)
* [Minecraft Forum Topic](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2433229-eiramoticons-emoticons-in-minecraft-supports) for discussion, support and feature requests 
* [EiraIRC - bring Twitch and IRC chat to Minecraft!](http://minecraft.curseforge.com/mc-mods/68420-eirairc)

##API
The easiest way to add EiraMoticons to your development environment is to do some additions to your build.gradle file. First, register EiraMoticons's maven repository by adding the following lines:

```
repositories {
    maven {
        name = "eiranet"
        url ="http://repo.blay09.net"
    }
}
```

Then, add a dependency to either just the EiraMoticons API (api) or, if you want EiraMoticons to be available while testing as well, the deobfuscated version (dev):

```
dependencies {
    deobfCompile 'net.blay09.mods:eiramoticons:major.minor.build:sources' // replace major.minor.build with version number
    // OR
    // compile 'net.blay09.mods:eiramoticons:major.minor.build:api' // replace major.minor.build with version number
}
```

*Important*: If you do use the dev version like that, make sure that you still only use code within the API packages! Rikka will get mad at you and give you a dose of Schwarz Sechs if you mess with any of EiraMoticons's internal classes.

Make sure you enter the correct version number for the Minecraft version you're developing for. The major version is the important part here; it is increased for every Minecraft update.

Done! Run gradle to update your project and you'll be good to go.

The latest EiraMoticons API and an unobfuscated version of the mod can also be downloaded from my [Jenkins](http://jenkins.blay09.net), if you're not into all that maven stuff.