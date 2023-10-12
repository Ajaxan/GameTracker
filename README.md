# GameTracker
This plugin provides a simplified structure for creating "games" among players with predefined rules and results.


### Kotlin
This plugin is written in Kotlin. Why? Because I like Kotlin, it's better than Java, fight me.

Anyway, the most important thing to note about this difference is that this plugin includes the koltin std-lib

I honestly recommend cloning the repo for the that first link located here: https://git.mcdevs.us/MCDEVS/Libraries/Spigot/Kotlin
It is also a kotlin gradle project so if you figure out how this works you should be able to build that 
jar and deploy it as well

### Gradle
This plugin uses gradle kotlin (kotlin has a special flavor of gradle). This means it differs 
from building everything locally significantly but only slightly from maven.


## Build and Run

### Setup
Currently, to set up the project you must properly configure your java environment. 
Next You need to copy/paste the `sample.gradle.properties` file to `gradle.properties` and configure 
the jar copy location to be where your server is. 

### Commands
`./gradlew build` - This builds the project and creates the jar file and copies it to the 
location in the gradle file


## Debugging

### Common Issues
None so far

### Hints
None so far