package ejektaflex.bountiful.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import ejektaflex.bountiful.BountifulMod
import ejektaflex.bountiful.SetupLifecycle
import ejektaflex.bountiful.api.ext.sendMessage
import ejektaflex.bountiful.data.BountifulResourceType
import ejektaflex.bountiful.registry.DecreeRegistry
import ejektaflex.bountiful.registry.PoolRegistry
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands.literal
import net.minecraftforge.fml.loading.FMLClientLaunchProvider
import net.minecraftforge.resource.ReloadRequirements
import net.minecraftforge.resource.SelectiveReloadStateHandler


object BountifulCommand {

    fun generateCommand(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
                literal("bo")
                        .then(
                                literal("dump").executes(dump())
                        )
                        .then(
                                literal("test").executes(dump(true))
                        )
                        .then(
                                literal("reload").executes(reload())
                        ).apply {
                            if (BountifulMod.config.debugMode) {
                                /*
                                then(
                                        literal("debug_reinitDefaultContent").executes(reinitDefaultContent())
                                )

                                 */
                            }
                        }
        )
    }


    /*
    private fun reinitDefaultContent() = Command<CommandSource> {

        try {
            BountifulMod.logger.info("Bountiful reloading resources on server with command!")
            BountifulResourceType.values().forEach { type ->
                BountifulMod.tryFillDefaultData(it.source.server, it.source.server.resourceManager, type, true)
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
            println(e.message)
            println(e.stackTrace)
            BountifulMod.logger.error(e.message)
        }

        1
    }

     */

    private fun reload() = Command<CommandSource> {

        //SetupLifecycle.loadContentFromFiles(it.source)

        1
    }

    // TODO If test is true, warn on invalid pool entries
    private fun dump(test: Boolean = false) = Command<CommandSource> {

        it.source.sendMessage("Dumping Decrees to console...")
        for (decree in DecreeRegistry.content) {
            BountifulMod.logger.info("* $decree")
        }
        it.source.sendMessage("Decrees dumped.")

        it.source.sendMessage("Dumping Pools to console...")
        for (pool in PoolRegistry.content) {

            SetupLifecycle.validatePool(pool, it.source, test)

        }
        it.source.sendMessage("Pools dumped.")

        1
    }


}