### Warning: All save data, including claims from 1.20.1-1.20.4 is not compatible with this release. Please be aware that you'll lose this data if you're updating from a previous Minecraft version.

- Added the following Prometheus permissions:
    - `teleport` - controls if players can use guild/party teleport functions
    - `create_party` - controls if players can create parties
    - `create_guild` - controls if players can create guilds
- Commands that take guild/team members as arguments now only suggest the relevant players
- Colorized command outputs.
- Added tooltip descriptions for all settings and permissions.
- Removed the role name setting in the members screen as it wasn't being used for anything.
- Added `/guild tp` to teleport to a guild member.
- Added `/guild permissions` and `/party permissions` for managing player permissions
- Added `public`, `friendlyFire`, and `passiveTeleport` settings to guilds.
- Added `announceJoin` and `announceLeave` settings.
- Added the following permissions for guilds and parties:
    - `operator` - Grants the member all permissions.
    - `manageMembers` - Allows the member to invite, remove, and modify members.
    - `manageSettings` - Allows the member to change settings.
    - `managePermissions` - Allows the member to modify member permissions.
    - `teleport` - Allows the member to use teleport commands.
    - `teleportMembers` - Allows the member to teleport other members.
- Guild admin commands now display the name of the guild in the command suggestion.
- When creating a new guild, it'll now be a random color.
- Added game rules and prometheus options for the max members per guild and max members per party.
- Fixed not being able to remove allies.

## API Changes

### Warning: This update is completely incompatible with the original API, as it's been entirely rewritten.

- Removed the `GuildClientApi` and `PartyClientApi`. Use the `GuildApi` and `PartyApi` instead, which can now be used on the client and server.
- Added the `Member Permission API`. It allows you to register permissions for guilds and parties, which are applied to each member of the team.
- Added the `Team Settings API`. It allows you to register guild and party settings. You can also create custom setting types.
- Argonauts no longer use mod-loader-specific events.
- Added the following events:
    - `CreateGuildEvent`
    - `RemoveGuildEvent`
    - `GuildChangedEvent`
    - `ModifyGuildMemberEvent`
    - `RemoveGuildMemberEvent`
    - `CreatePartyEvent`
    - `RemovePartyEvent`
    - `PartyChangedEvent`
    - `ModifyPartyMemberEvent`
    - `RemovePartyMemberEvent`
