# EvientsCore
An extremly simple-to-use plugin for managing Minecraft events without hassle.
## Installation
In the "Releases", download the latest version and simply add it to your plguins folder.  
Make sure to have LuckPerms and WorldGuard installed!
## Permissions
EvientsCore has two base permissions:
- `evients.host.*`: All commands that only hosts should run
- `evients.player.*`: All commands that anyone should have access to
It's recommended to give your `default` group the "evients.player.*" permission.
## Suggestions / Issues
If you ever find any issues with EvientsCore, either open an issue on GitHub, or friend me on Discord: "hypnobella".
## Customisation
You can change how EvientsCore looks like in the chat, by editing the colors in the config.yml.
## Usage
You can always check the `/help evientscore` command, but here is a list of basic commands within this plugin.
### Selectors
In EvientsCore, there are "selectors": `all`, `dead`, `alive`, `random`, `randomalive`, `randomdead`.  
Commands can have all of these or some of them:

- `/tpall` `/tpdead` `/tpalive` `/tprandomalive`, etc. This will tp the players that the selector has matched.
- `/reviveall` `/reviverandom`

When these types of commands are used, they will say in chat who was selected.  
You can also use a player's username instead, like: `/revive <username>`
Or, if you want to run the command on yourself: `/revive`
### Time Periods
Some commands also accept time periods, for example: `5s`, `5m`, `10m` etc.
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
### Rejoins
You can disable rejoins, this means when an alive person leaves and rejoins, they will either be sent to spawn or killed, depending on config.  
> `/rejoins`
### Hiding
All players can use the `/hide` command, this hides players from the player that used the command.
> `/hide all` Hides *all* players  
> `/hide off` Turns off the hide  
> `/hide staff` Hides everyone except those that have `evients.host` luck perms permission  
### Giving
> `/give<selector> <item> <amount>` Gives all players in the selector amount of item

Example: `/giveall dirt 64`
### Wins
EvientsCore has a wins database which you can easily add too.
> `/addwin <user>` Adds a win for a user
> `/wins` Get the amount of wins you have
### Warps
EvientsCore has its own warps, so it can manage who can use them. This means that alive players cannot `/warp`, only dead, or staff!
> `/setwarp name` Creates a new warp called "name" at your current position  
> `/setwarp name 0 0 0` Creates a new warp called "name" at 0, 0, 0  
> `/deletewarp name` Deletes the warp called "name"  
> `/warp name` Teleports you to name  
> `/warplist` Shows you all available warps  
### Region Flags
You can toggle different region flag permissions where you are standing.
> ℹ️ You need to be standing within a region, not a \_\_global\_\_ one!

The following region flag commands exist:
> `/pvp` `/falldamage` `/break` `/build`
### Kits
EvientsCore has its own kit system which can be used in events.  
> `/createkit <name>` Creates a kit using the items in your inventory
> `/kits` Get a list of kits

The `/kit` command is used to give players kits, it works similarly as the selectors.
`/kit<all|alive|dead|random|randomalive|randomdead> <name>`
### Undoing
You can undo `/tp` and `/revive` commands by simply typing `/undocommand`.  
It will undo what the last command did within the last 10 seconds.  
### Messaging
> `/msg <user> <message>` Message another user a message
> `/togglemssages` Toggle receiving messages