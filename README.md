# EvientsCore
An extremly simple-to-use plugin for managing Minecraft events without hassle.
## Installation
In the "Releases", download the latest version and simply add it to your plguins folder
## Usage
You can always check the `/help evientscore` command, but here is a list of basic commands within this plugin.

In EvientsCore, there are "selectors": `all`, `dead`, `alive`, `random`, `randomalive`, `randomdead`.  
Commands can have all of these or some of them:

- `/tpall` `/tpdead` `/tpalive` `/tprandomalive`, etc. This will tp the players that the selector has matched.
- `/reviveall` `/reviverandom`

When these types of commands are used, they will say in chat who was selected.

Soem commands also accept time periods, for example: `5s`, `5m`, `10m` etc.
## All Commands
### Timers
Timers aren't annoying! They don't ping you every single second!
> `/timer 5s` Starts a timer for 5s  
> `/timer 10m` Starts a timer for 10m  
> `/timer cancel` Cancels the timer  
### Teleporting
These are used to teleport players to you
> `/tpall` `/tpdead` `/tpalive` `/tprandomalive` `/tprandomdead` `/tprandom`  
### Reviving
These are used to revive players, and then teleport them to you
> `/revive @user` Revive a specific user  
> `/reviveall` `/reviverandomdead`  
> `/revivepast 5s` Revives players from the last x seconds  
### Killing
You can kill people using the same selectors.
> `/killall` `/killdead` `/killalive` `/killrandom` `/killrandomalive` `/killrandomdead`  
### Marking as dead
You can mark players as dead.
> `/markdead @user` Marks @user as dead (does not kill them though!)  
### Hiding
All players can use the `/hide` command, this hides players from the player that used the command.
> `/hide all` Hides *all* players  
> `/hide off` Turns off the hide  
> `/hide staff` Hides everyone except those that have `evients.host` luck perms permission  
### Warps
EvientsCore has it's own warps, so it can manage who can use them. This means that alive players cannot `/warp`, only dead, or staff!
> `/setwarp name` Creates a new warp called "name" at your current position  
> `/setwarp name 0 0 0` Creates a new warp called "name" at 0, 0, 0  
> `/deletewarp name` Deletes the warp called "name"  
> `/warp name` Teleports you to name  
> `/warplist` Shows you all available warps  