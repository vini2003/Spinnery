# Spinnery, ahoy!

[ ![Download](https://api.bintray.com/packages/spinnery/Spinnery/spinnery/images/download.svg) ](https://bintray.com/spinnery/Spinnery/spinnery/_latestVersion)

![Spinnery](https://i.imgur.com/Mu1EqaK.png)

Spinnery is a modern, feature-complete GUI library for Minecraft 1.15 and 1.16, with a focus on making the GUI design and user experience significantly smoother and less nonsensic.

# Setup

Firstly, add Spinnery's Bintray to your `build.gradle`'s `repositories`:

```gradle
	maven {
		name = "Spinnery"
		url  "https://dl.bintray.com/spinnery/Spinnery"
	}
```

Afterwards, add it to your project:

```gradle
	// Spinnery
	implementation "com.github.vini2003:spinnery:${project.spinnery_version}"
```
  
 As you may realize, that's using a variable defined in `gradle.properties` for the version:
 
 ```gradle
 # Mod Properties
  spinnery_version = VERSION
```

Once that's done, you're set!
