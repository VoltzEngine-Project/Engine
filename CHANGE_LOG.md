# INFO
This log contains changes made to the project. Each entry contains changed made after the last version but before the number was changed. Any changes made after a number change are considered part of the next release. This is regardless if versions are still being released with that version number attached. 

If this is a problem, use exact build numbers to track changes. As each build logs the git-hash it was created from to better understand changes made.

It is also important to note these changes are for several repos. As this repository is primarily used for the core segement of the project. Other projects are merged in at build time to create the complete project. 

# Versions
## 1.11.0 - 6/30/2018
### Runtime changes
* Added: Shadowing to disabled buttons
* Fixed: Rendering issues with Access Gui
* Fixed: permission issues with Access Gui
* Fixed: Issues with computer support

### Development Changes
* Reworked: Packet system to no longer expose ByteBuf for writing. All data needs to be provided as an object or as a Consumer<ByteBuf>
* Changed: few interfaces in old ICBM Launcher interfaces
* Changed: TileModuleMachine to return IEnergyBuffer instead of EnergyBuffer
* Cleanup: Moved NBT keys to static final fields


## 1.10.7-1.10.8 - 6/20/2018
### Runtime changes
* Added: shared data system method getEnergy() 
* Added: shared data system method getEnergyCapacity() 
* Added: shared data system method for to/from energy conversions (supports RF and IC2)

### Development Changes
* Replaced: annotation data system with lambda system. Old system will still work for a few versions. However, after toying with the annotation system it was found to not work effectivly. This had nothing to do with implementation but design. As methods would need to be created in order to use annotations in some cases. Instead a lambda expression is now being used to generate logic as needed. Without having to edit the target class for the exposed data/logic methods. As well this opens the possiblity of using VE to add CC and OC support to other mods indirectly. Including the possibility of a JSON driven system.


## 1.10.6
### Runtime Changes
* Added: Computer Craft support

### Development Changes
* Added: Early version of computer data system (designed for -> CC, OC, and eventually AL automation controllers)
* Added: annotation system for getting data methods
* Added: registry to add data systems or generate ones from annotations
* Changed: Machine prefab to return energy buffer interface instead of object

## 1.10.5 - 6/9/2018
### Runtime Changes
* Reworked: screwdriver into a multiTool
* Added: item and power tool mods
* Added: checks for invalid metadata values on BlockBase (fixes several errors/crashes)
* Changed: Smokey to Smoky for english translations
* Fixed: Inf loop while loading resources from folder
* Fixed: Smoky quartz lang key
* Fixed: NPE for multi-block layouts
* Fixed: creative tab entries having access to full creative tab list
* Fixed: GUI opening on node tiles regardless of tool interaction
* Fixed: rendering bounding box failing when using negative y values
* Fixed: multi-block offset for MultiBlockListener
* Fixed: world not being null when setting item bounds causing odd logic for legacy Tiles

### Development Changes
* Updated: CoFH energy API
* Added: CoFH tool API
* Added: helper to get multi-block position
* Added: helper to get redstone for multi-block
* Added: cube model system
* Added: ISBR json render system
* Added: multi-block json rendering
* Added: IBlockAccess for fake block rendering
* Added: callback for multi-block using a node host
* Added: callbacks for getting multi-block data for itemstack rendering
* Added: packet for mouse scroll event (includes ctrl & shift key detection)
* Added: interface for mouse scroll event for items
* Added: interface to receive multiTool interaction and change connection modes
* Added: connection color enum for use wtih connection mode changing
* Added: JSON converter for cube models
* Added: content ID listener for ItemBlockBase
* Added: content ID listener for Multi-Block
* Added: handling for blocks and listeners content IDs to TileRenderHandler
* Moved: Tool mode registry to ToolMode static method
* Depricated: IModeItem and its sub interfaces (being replaced by new scroll mode event)

## 1.10.4 - 3/4/2018
### Runtime Changes
* Fixed: Modules not loading NBT after being created (Fixes issues with ICBM and Basic Industries)

### Development Changes
* Added: load() call to AbstractModule#new(ItemStack)

## 1.10.3 - 3/3/2018
### Runtime Changes
* Fixed: Client side Infinite loop crash caused by onNeighborBlockChange() and onNeighborChange()

### Development Changes
* Fixed: ListenerIterator returning wrong value for getWorld()
* Added: blockExists checks to BlockBase's onNeighborBlockChange() and onNeighborChange()
* Deprecated: ModuleBuilder, will be replaced with a Registry system
* Disabled: Client side listener calls/events for onNeighborBlockChange() and onNeighborChange()
* Reworked: GuiLabel to no longer work as a disabled TextBox with no background
* Reworked: ModuleBuilder to use Class<? extends C> instead of Class<C>

## 1.10.2 - 2/27/2018
### Runtime Changes
* Added: Quick Access Gui (contains access to GuideBook, Access Profiles, and Settings)
* Added: Navigation for guide book (Still WIP)
* Changed: access GUI opening (ctrl + `) to open quick access GUI

### Development Changes
* Added: Methods for working with Guide Book
* Moved: Access and Guide Book open calls to respective handlers

## 1.10.1 - 2/17/2018
### Development Changes
* Implemented early data and methods for guide book

## 1.10.0 - 2/15/2018
### Runtime Changes
* Added: gift box blast
* Added: button to copy access profile ID
* Added: * support in profiles for groups and users
* Added: simple friends list for global profiles
* Improved: EMP's tile discovery system
* Fixed: Several issues with access system
* Fixed: Save manager failing to build objects
* Fixed: access profiles using username over UUID or instance
* Fixed: perm checks for profiles, mainly on users
* Fixed: refresh profile button crashing client
* Fixed: NPE crash when group id is null in GUI
* Fixed: Crash with underground biomes due to localization names
* Fixed: Missing TileInfinite when running in dev mode
* Fixed: issues dropping NBT driven items
* Fixed: issues with protection systems due to block break event
* Fixed: placement listeners running client side causing odd render issues
* Fixed: placement listener pathfinder inf looping
* Fixed: global access profiles staying after sever/world switch
* Fixed: tooltip drawing in GUIs rendering behind NEI

### Development Changes
* Added: Delayed spawn object - allows spawning entities after a timer
* Added: Question button for use as a help button on GUIs
* Added: can edit check to profile, groups, and users - allows for locking settings
* Added: method to load profile GUI with a profile to load by default
* Added: rotation listener for 0-3 meta values
* Added: way to override render pass
* Changed: groups to use lowercase names
* Reworked: processors to handle annotations by default
* Reworked: Global access system seperating it from profile system
* Reworked: multiblocks to no longer handle dropping of items
* Reworked: item dropping, system can handle NBT in normal MC methods
* Removed: Tile map - chunks store tile locations already

## 1.9.18 - 12/24/2017
### Runtime Changes
* Fixed: crash while parsing JSON tags marked as client or server only
* Fixed: entity effects not decaying due to missing timer
* Added: position dev debug tool

### Development Changes
* Changed: JsonConvert to use an interface

## 1.9.17 - 12/18/2017
### Runtime Changes
* Added: JSON override commands
        /ve json override gen [type] [id]                       - generate override file in /.minecraft/configs/bbm/json
        /ve json override get [type] [id] [fieldName]           - allows checking  fields that support override system
        /ve json override set [type] [id] [fieldName] [value]   - allows modifying fields that support override system
* Finished: JSON override system
* Fixed: server trying to send a client side packet to itself causing a crash
* Fixed: infinite loop where multiblock thought it was the host block
* Fixed: items not showing in NEI
* Fixed: some minor NEI problems, not all problems are solve yet


### Development Changes
* Added: helper to render text similiar to XP bar
* Added: lambda support for looping over JSON generated content
* Added: ability to convert objects to json using json converts (reverse mode basicly)
* Added: Silk harvest support for JSON blocks
* Added: Listener to handle silk harvest properties
* Added: way to get AABB from cube without running validation checks
* Fixed: missing isNormalCube() method for JSON blocks causing redstone to break

## 1.9.16 - 11/25/2017
### Runtime Changes
* Fixed: issues with JSON loader using the wrong path on linux
* Fixed: block localization containing ${meta} causing some mods to fail
* Fixed: issues with block localization not working in some cases
* Fixed: VEP effects being run as JSON effects
* Fixed: issues with blocks returning light levels over 15
* Fixed: icon issues with ItemBlocks using JSON for rendering
* Fixed: inventory helpers not respecting max slot capacity
* Added: sub type for dev tool to test metadata only

### Development Changes
* Added: new Rect constructor that takes the corner and size from the corner
* Added: add method for direction and a scale of that direction. Allows for incremental movements ex. (North, 0.1)
* Added: nbt saving to BlockPos
* Added: rotation listener that uses 0-3 instead of 2-5 as used by MC
* Added: code to bypass a block only rendering in a normal or alpha pass
* Added: json to note to render a block in several passes
* Added: json and code to set texture sheet ID to use for getting icons for ItemBlocks
* Added: host field to GuiContainerBase, uses generics to allow easier implementation
* Added: a few more rotation helpers to convert rotation to direction and back
* Added: early framework for JSON rendering static blocks
* Changed: basic inventory to no longer fire inventory change events while loading from save
* Renamed: RotationListener to RotationListnerMC to reduce confusion between two different classes


## 1.9.15 - 11/10/2017
### Runtime Changes
* Fixed: issues with file paths crashing due to special characters [] {} %20

### Development Changes
* Added: Apple Core to mods enum

## 1.9.14 - 11/3/2017
### Runtime Changes
* Added: Improved help command page count (page # -> page # of #)
* Added: gems to materials tab (sorta a fix)
* Added: gem hand crafting recipes
* Added: Explosive bat (supports any explosive type, works same as ex creeper)
* Added: spawn egg for explosive bat and creeper (note not the same as vanilla, supports ICBM explosive types)
* Added: Waila support for MultiBlock tiles
* Fixed: NPE with delay event system
* Fixed: pick block for multiblock tiles
* Fixed: blocks droping on corner instead of center (only effected newer JSON blocks)
* Fixed: missing ore names for JSON blocks
* Fixed: unknown entry errors
* Fixed: ore names loading on wrong phase
* Fixed: BlockTile not dropping inventory
* Fixed: Blocks using old Tile system not dropping correctly (effected Armory and ICBM-Classic)
* Fixed: Projectiles colliding with riders (Fixes ICBM & ICBM-Classic missile riding)
* Fixed: Minor issues iterative over player inventory
* Implemented: EMP inventory handling (unfinished)
* Changed: EMP multiplier from 10 to 1
* Changed: Ore textures out for improved versions by Morton00000
* Updated: ore names for flint and gunpowder, converted to JSON system


### Development Changes
* Added: non-VE tile scanning to map tile entities (will eventually be phased out)
* Added: Horizontal version of render with repeat 2D
* Added: AT for accessing player food stats
* Added: AT for accessing AI data
* Added: Support for several data fields in JSON for loops
* Added: Color support for JSON rendering
* Added: Improved support for simple tools in recipes
* Added: JSON support for block harvest tool and level
* Added: Redstone checks for TileNodeHost - allows entity versions to override world checks
* Fixed: JSON for loop not handling arrays correctly
* Fixed: Sphere get entity method not working
* Fixed: Sphere using an int for radius (now uses double)
* Fixed: BlockPos not using IPos3D for distance checks
* Updated: json processors to use for loops (excludes item and block)
* Updated: EnergyBuffer to have an abstract version without 'final int maxBuffer' field (designed for extending to make custom buffers)

## 1.9.13 - 10/4/2017
### Runtime Changes
* Added: Entity Effect system (basicly a potion system with more flexibility)
* Added: Bleeding effect - tick based loss of HP

* Fixed: GUI helpers in TileNode not working with ContainerBase 
        This caused older tiles to stop sending GUI packets to users

* Fixed: Container Base not storing host
        This caused several issues including packets, gui actions, etc
        
* Fixed: IPlayerUsing storing duplicates of the same player instance
        Not really an issue but caused some players to get spammed by GUI packets
        
* Fixed: TileNode not using getMod()
        This caused issues with renders and listeners not working
        
* Fixed: GUI's opening client side for JSON based blocks
        Not a major issue but caused problems with interaction on tiles
        
* Fixed: Multi-block interactions not passing to host client side
        Caused a series of interaction problems

* Fixed: Fluid tank multi-block tiles not support TileNode framework
        Caused problems with bucket interaction from other mods
  

### Development Changes
* Added: JSON converter for Array Lists
* Added: Hook to allow converter calls to use JSON processors
* Added: check for halloween season
* Added: isOwner(player) check to TileNode 
* Added: Early version of map trigger
        This will be used to create event and location based triggers for a world.
        The idea usage will be for AOE effects in ICBM and traps for map builders
* Added: Block render method that allows for overriding all 6 sided textures used
* Added: Tile listener for block harvest
* Added: Tile listener for block hardness (location and player based)
* Added: Tile listener for harvest tool and level
* Added: IMapArea and implemented in on 2d & 3d shapes
        Will be used to abstract the shape of an area on the map.
        The goal is to use it with any current map implementation and trigger system.

* Reworked: IJsonGenObject to extend IModObject
        This fixed a few issues with overlap and confusion on method usage
* Reworked: Block breaking on BlockBase to implement custom harvest checks
      
* Fixed: NPE when registering delay actions

## 1.9.12
### Runtime Changes
* Added: Item Render baker
    Can be used to bake item renders to file for uses in websites or external programs
    Can be activated in game by pressing 'ctrl' and 'end' keys at the same time
    Will print in chat when it starts and ends
    
* Fixed: left ctrl being forced for developer keys, right ctrl should now work as well
* Fixed: JSON debug window not opening when pressing 'ctrl' and 'home' keys

### Development Changes
* Added: JSON to hash map converter
* Added: JSON to primitive (int, double, float, string, etc) converters
* Added: block entry for storing json data until blocks finish loading
* Added: JSON converter for blocks
* Added: JSON converter for materials

* Changed: JSON converters to allow several registry keys per instance

# Versions
## 1.9.11
### Runtime Changes
* Add: translations for entity type sets (used by armory mod)
* Fixed: splitByLine method not working for single lines (fixes armory mod sentry gun tooltip GUIs)

### Development Changes
* Added: getUnquidID() method to all JSON generated content to seperate out name from contentID and modID
        This is only implemented on some objects as not all objects use content IDs or IDs in general.

# Versions
## 1.9.10
### Runtime Changes
* New textures by Morton
    Sheet metal
    Gems
    Rods
    Ingots
    Dust
    Dust Impure
    Stone tools
    and others not listed
    
* Converted: Sheet metal to JSON

### Development Changes
Added: Creative tab selection for JSON items and sub items
* Added: Json object creation event that fires each time an json gen object is generated
* Added: Phase tracking for JSON content loader (will be improved on over time)

## 1.9.9
### Runtime Changes
* Added: Reflection injection into Furnace recipes to prevent Null ItemStack entries
        Fixes: Problems with mods that generate items and create invalid recipes by mistake
        Fixes: NPE crash due to null entries with Mekanism cloning furnace recipes
     

* Fixed: canBuild() check failing and printing errors due to running client side with no data

### Development Changes
* Added: Furnace recipe display for Debug GUI
* Added: Shaped crafting recipe display for Debug GUI

* Added: isSolid handling for JSON blocks
* Added: isNormalCube handling for JSON blocks
* Added: Methods to get ItemStack name and mod

* Changed: canBuild() to use IWorldAccess allowing it to be used with world handlers or wrappers
* Changed: Debug GUI to load from Voltz Engine instead of JSON content loader
* Improved: JSON debug GUI
          Implemented reusable data lists
          Simplified GUI code for reuse          

* Renamed: JSON debug GUI to Debug GUI
* Reworked: JSON debug GUI to be used for anything

## 1.9.8
* Reworked: JSON debug GUI and system
* Added: Loading phase events for JSON. 
       Can be used to register content via JSON generated objects at key moements.

## 1.9.7
* Converted: Metal ore to JSON (block, item, recipes, world gen, render)
* Converted: Unfinished gem ore to JSON block, item, recipes, world gen, render)
* Converted: Gem items to a single item, may result in slight item loss for non-standard usage of gems

* Improved: Json For loop handling

* Added: Lambda support for code isde of JSON for loop system
* Added: Boolean handling for JSON settings entries
* Added: Json conditional handling
* Added: IF-ALL check allowing for conditionals to check several values
* Added: condition for if mod is loaded
* Added: condition if dev mode is enabled
* Added: condition for global settings
* Added: condition to check if value is equal
* Added: condition to check if value is not equal
* Added: item drop support for JSON blocks

* Removed: Ore-loading call method


## 1.9.6
* Converted: Infinite inventory to JSON and node system

* Added: listener support for changing block light level
* Improved: JSON file loading

## 1.9.5
* Added: For loop support for JSON textures
* Added: Model rotation stacking (child rotation + parent rotation)
* Added: Texture loading to BlockState - reduces work required to add textures
* Added: framework for JSON settings system that will replace old config system
* Added: support for nested JSON for loops
* Added: support for JSON forEach loops
* Added: improved for loop to RenderState handler

* Improved: Debug for world change threads
* Improved: JSON for loop handling by abstracting For Loops as it's own system

* Fixed: inventory wrapper template using canInsert() in canExtract() method
* Fixed: infinite inventory handling and render
* Fixed: world edit queue not running if edits per tick was zero
* Fixed: NPE due to no world when packets are sent during world load

## 1.9.4
* Added: Helpers in TileNode for handling GUI packets
* Added: IPlayerUsing interface to TileNode by default
* Added: basic gui packet handling to TileNode by default
* Added: ways to modify packet sent per player

## 1.9.3
* Fixed: Json items not showing in creative tab

## 1.9.2
* Added: NEI support for 4x4 recipes
* Added: Expanded JSON debug menu
* Added: fail catch to ASM tests

* Fixed: NPE when Explosive icons are null (mainly an issue with NEI)
* Fixed: subtypes for JSON items

* Improved: creative tab handling

* Changed: ASM disable run argument to turn off all ASM instead of just templates (which were removed)

## x.x.x - 1.9.1
Anything before this is not logged for sanity as this log was made 9/18/2017. This is long after many of these versions were created. If you wish to expand the log feel free to pull request.
