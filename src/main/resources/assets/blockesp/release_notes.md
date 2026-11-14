# BlockESP 1.0.0 release manifest

BlockESP is a client-side highlighting utility for Fabric 1.21.4. The release
is deliberately self-contained: it draws an independent depth-test-free line
layer and does not alter the server's block data, packets, or resource-pack
state.

## Target groups

The default set includes every vanilla overworld ore and its deepslate variant:
coal, iron, copper, gold, redstone, emerald, lapis lazuli, and diamond. Nether
gold ore, nether quartz ore, and ancient debris are also included. The target
list additionally contains barrels, chests, and deepslate tiles. Every target
has its own saved boolean in `config/blockesp.json`, so selections survive game
restarts and can be changed from Mod Menu when that optional mod is installed.

## Rendering details

The scanner maintains a bounded nearby block cache and updates it when the
player moves, settings change, or the scan interval elapses. The renderer then
uses the cache to submit colored outline boxes in a late world-render pass.
Depth testing is disabled only for the dedicated BlockESP layer; render state
is restored immediately after that layer is drawn. This makes the outlines
visible through terrain while avoiding changes to normal world rendering.

## Controls and compatibility

Run `/Besp` (or `/besp`) in the client chat to toggle all rendering. Fabric API
is required. Mod Menu is optional but provides the in-game per-target settings
screen. The mod is client-only; a server does not need to install it for a
single-player world or a normal Fabric-compatible multiplayer connection.
