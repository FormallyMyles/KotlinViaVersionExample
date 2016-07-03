package us.myles.viaexample


import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import us.myles.ViaVersion.api.ViaVersion
import us.myles.ViaVersion.api.ViaVersionAPI
import us.myles.ViaVersion.api.protocol.ProtocolVersion

class ExamplePlugin : JavaPlugin() {
    override fun onEnable() {
        saveDefaultConfig()
        config.options().copyDefaults(true)

        logger.info("Now enabling the example plugin!")
        server.pluginManager.registerEvents(ExampleListener(this), this)
    }
}

class ExampleListener(examplePlugin: ExamplePlugin) : Listener {
    val plugin = examplePlugin
    val api: ViaVersionAPI = ViaVersion.getInstance()

    @EventHandler
    fun onJoin(joinEvent: PlayerJoinEvent) {
        val version = api.getPlayerVersion(joinEvent.player)
        val protoVersion = ProtocolVersion.getProtocol(version)

        val loginMessages = plugin.config.getConfigurationSection("protocol").getValues(true)
        if (loginMessages.containsKey(protoVersion.id.toString())) {
            val messages = loginMessages[protoVersion.id.toString()]
            if (messages is List<*>) {
                messages.forEach { msg -> joinEvent.player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.toString())) }
            } else {
                joinEvent.player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.toString()))
            }
        }
    }
}
