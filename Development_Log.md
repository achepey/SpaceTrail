Development Log
=======
Team 19: Andrew Chepey, Robert Christian, Evan Kirkland, Matthan Myers

##### 11/04/2014
- Lab group met in and decided to use astronomical data for project
- 20 minutes

##### 11/06/2014
- Lab group met and decided on game idea to use for project
- Split up design document requirements between group members
- 50 minutes

##### 11/09/2014
- Met and combined our separate parts of the design document
- Discussed specifics of game mechanics and implementation 
- 2 hours

##### 11/10/2014
- Met to finalize design document and prepare submission
- 30 minutes

##### 11/11/2014
- Divided team in two groups: Robert and Andrew work on UI and Evan and Matthan work on game engine
- Practice using Android Studio
- 50 minutes

##### 11/13/2014
- Robert and Andrew worked together in lab to create basic game menu screen (50 minutes)
- Andrew created GitHub repository and MainActivity, which is the menu screen that pops up when you begin app (4 hours)

##### 11/14/2014
- Andrew: 7 hours
  - created activities for starting a new game (giving user information and purchasing supplies)
  - created activity SpaceActivity (later incorporated into GameScreenActivity)
  - researched how to switch between activities
- Robert: 3 hours
  - created ActivityLoadGame, GameScreenActivity, ActivityGameInfo
  - found images to use in game

##### 11/15/2014
- Andrew: 4 hours
  - researched switching between activities
  - implemented touch listeners to GameScreenActivity
  - researched and implemented AlertDialogs
- Robert: 2 hours
  - Added functionality to move spaceship across screen in GameScreenActivity
  - formatted planet pictures to be used in game

##### 11/16/2014
- Andrew and Robert worked together: 7 hours
  - Combined SpaceActivity and GameScreenActivity
  - Allowed for user to select planet to travel to 
  - Andrew created PlanetActivity which allows player to purhcase supplies and select a new planet to travel to
  - Robert implemented animation on GameScreenActivity which causes spaceship to shrink as it approaches the planet
  - Created AlertDialog for when spaceship crashes
  - Fixed errors with AlertDialog
  - Implemented tapping and holding on GameScreenActivity to move ship and pull up information page
  - Robert created InstructionScreen

##### 11/17/2014
- Robert: 1 hour
  - Fixing bugs with spaceship getting smaller as it approached planet
- Andrew: 2 hours
  - Created exit activity
- Andrew and Robert worked together: 2 hours
  - Writing development log, adding comments, finishing test driven development, and updating design document illustration

##### 11/19/2014
- Evan: 1.5 hours
  - Building basic classes for game (Game, Ship, Person, etc)
  
##### 11/20/2014
- Evan: 1 hour
  - Developing Race class and Part class
- Andrew: 2 hours
  - Started developing the Asteroid Mining Activity, including animation of asteroids

##### 11/23/2014
- Evan: 2 hours
  - Building up Game class, along with constructor
- Andrew: 1 hour
  - Began updating the in game menu

##### 11/24/2014
- Evan: 3 hours
  - Moving I/O functionality from Game to JavaGame (help from Robert)
- Andrew: 2 hours
  - Updated in game menu and on screen functionality
  - Updating animation for asteroid mining activity

##### 11/25/2014
- Robert: 4 hours
  - Added game functionality to MainActivity
- Evan: 1.5 hours
  - Assigning randomly generated values to Race age
  - Working on I/O

##### 11/26/2014
- Robert: 3 hours
  - Began to integrate GameScreenActivity
  - Added getter and setter methods to game classes
- Evan: 2 hours
  - Handling cases for incorrect input
  - Calculating distance for traveling amongst planets
  - Added attrition to crew and resources
  - Modifying makeMove

##### 11/28/2014
- Robert: 4 hours
  - Researched XML loading and saving
  - Wrote loading functionality for game (GameFileLoader class)
- Evan: 2 hours
  - Redoing distance by using trigonometry and distances from sun
  - Using planet compounds to heal or harm crew, depending on Race

##### 11/29/2014
- Robert: 2 hours
  - Wrote saving functionality for game to XML (GameFileSaver class)

##### 11/30/2014
- Robert: 4 hours
  - Wrote XML planetData
  - Created functionality for Planet class to load data from XML
  - Tested loading and saving
- Robert and Evan: 3 hours
  - Evan finished up game functionality and wrote selling methods
  - Robert began writing getIssue() method in Game class
- Andrew: 4.5 hours
  - Implemented animation to asteroid activity, correctly implemented view animation style

##### 12/1/2014
- Robert: 6.75 hours
  - Writing getIssue() method
  - Updated GameFileSaver and GameFileSaver for changes made to Game class 
- Robert and Evan: 1.5 hours
  - Testing Game class with main class JavaGame
  - Had file path issue
- Evan, Robert and Andrew: 3 hours
- Andrew worked on AsteroidActivity
- Robert and Evan completed game testing

##### 12/2/2014
- Worked in lab to finish submission and assigned jobs for final submission (50 minutes)

##### 12/3/2014
- Andrew: 3 hours
  - Implemented different asteroid animation style
  - Minor glitch with implementing post animation dialog

##### 12/4/2014
- Worked in lab on individual parts for submission (50 minutes)
- Robert: 5 hours
  - Integrated MainActivity and began to integrate GameScreenActivity
- Andrew: 2 hours
  - Fixed issue with asteroid animation, and completed asteroid activity

##### 12/5/2014
- Robert: 5 hours
  - Finished integrating game into GameScreenActivity and completely integrated PlanetActivity
- Andrew: 6 hours
  - Began integrating AsteroidActivity and GameInfoActivity into final product
  - Added functionality to fix minor bugs

#### 12/6/2014
- Andrew: > 4 hours
  - Completely integrated AsteroidActivity and updating values
  - Finihsed integrating GameInfoActivity
  - Added repairing of hull functionality
  - Began searching for and fixing issues within integration
