# INFO
This log contains changes made to the project. Each entry contains changed made after the last version but before the number was changed. Any changes made after a number change are considered part of the next release. This is regardless if versions are still being released with that version number attached. 

If this is a problem, use exact build numbers to track changes. As each build logs the git-hash it was created from to better understand changes made.

It is also important to note these changes are for several repos. As this repository is primarily used for the core segement of the project. Other projects are merged in at build time to create the complete project. 

# Versions
## 1.9.10
### Runtime Changes
New textures by Morton
    Sheet metal
    Gems
    Rods
    Ingots
    Dust
    Dust Impure
    Stone tools
    and others not listed
    
Converted: Sheet metal to JSON

### Develoment Changes
Added: Creative tab selection for JSON items and sub items
Added: Json object creation event that fires each time an json gen object is generated
Added: Phase tracking for JSON content loader (will be improved on over time)

## 1.9.9
### Runtime Changes
Added: Reflection injection into Furnace recipes to prevent Null ItemStack entries
        Fixes: Problems with mods that generate items and create invalid recipes by mistake
        Fixes: NPE crash due to null entries with Mekanism cloning furnace recipes
     

Fixed: canBuild() check failing and printing errors due to running client side with no data

### Develoment Changes
Added: Furnace recipe display for Debug GUI
Added: Shaped crafting recipe display for Debug GUI

Added: isSolid handling for JSON blocks
Added: isNormalCube handling for JSON blocks
Added: Methods to get ItemStack name and mod

Changed: canBuild() to use IWorldAccess allowing it to be used with world handlers or wrappers
Changed: Debug GUI to load from Voltz Engine instead of JSON content loader
Improved: JSON debug GUI
          Implemented reusable data lists
          Simplified GUI code for reuse          

Renamed: JSON debug GUI to Debug GUI
Reworked: JSON debug GUI to be used for anything

## 1.9.8
Reworked: JSON debug GUI and system
Added: Loading phase events for JSON. 
       Can be used to register content via JSON generated objects at key moements.

## 1.9.7
Converted: Metal ore to JSON (block, item, recipes, world gen, render)
Converted: Unfinished gem ore to JSON block, item, recipes, world gen, render)
Converted: Gem items to a single item, may result in slight item loss for non-standard usage of gems

Improved: Json For loop handling

Added: Lambda support for code isde of JSON for loop system
Added: Boolean handling for JSON settings entries
Added: Json conditional handling
Added: IF-ALL check allowing for conditionals to check several values
Added: condition for if mod is loaded
Added: condition if dev mode is enabled
Added: condition for global settings
Added: condition to check if value is equal
Added: condition to check if value is not equal
Added: item drop support for JSON blocks

Removed: Ore-loading call method


## 1.9.6
Converted: Infinite inventory to JSON and node system

Added: listener support for changing block light level
Improved: JSON file loading

## 1.9.5
Added: For loop support for JSON textures
Added: Model rotation stacking (child rotation + parent rotation)
Added: Texture loading to BlockState - reduces work required to add textures
Added: framework for JSON settings system that will replace old config system
Added: support for nested JSON for loops
Added: support for JSON forEach loops
Added: improved for loop to RenderState handler

Improved: Debug for world change threads
Improved: JSON for loop handling by abstracting For Loops as it's own system

Fixed: inventory wrapper template using canInsert() in canExtract() method
Fixed: infinite inventory handling and render
Fixed: world edit queue not running if edits per tick was zero
Fixed: NPE due to no world when packets are sent during world load

## 1.9.4
Added: Helpers in TileNode for handling GUI packets
Added: IPlayerUsing interface to TileNode by default
Added: basic gui packet handling to TileNode by default
Added: ways to modify packet sent per player

## 1.9.3
Fixed: Json items not showing in creative tab

## 1.9.2
Added: NEI support for 4x4 recipes
Added: Expanded JSON debug menu
Added: fail catch to ASM tests

Fixed: NPE when Explosive icons are null (mainly an issue with NEI)
Fixed: subtypes for JSON items

Improved: creative tab handling

Changed: ASM disable run argument to turn off all ASM instead of just templates (which were removed)

## x.x.x - 1.9.1
Anything before this is not logged for sanity as this log was made 9/18/2017. This is long after many of these versions were created. If you wish to expand the log feel free to pull request.
