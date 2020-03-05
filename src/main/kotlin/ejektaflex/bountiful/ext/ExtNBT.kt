package ejektaflex.bountiful.ext

import ejektaflex.bountiful.data.entry.BountyEntry
import ejektaflex.bountiful.data.entry.BountyType
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.common.util.INBTSerializable
import kotlin.reflect.full.createInstance

fun CompoundNBT.clear() {
    for (key in keySet()) {
        remove(key)
    }
}

fun CompoundNBT.setUnsortedList(key: String, items: Set<INBTSerializable<CompoundNBT>>) {
    val listTag = CompoundNBT().apply {
        items.forEachIndexed { index, item ->
            put(index.toString(), item.serializeNBT())
        }
    }
    put(key, listTag)
}

fun CompoundNBT.getUnsortedList(key: String): Set<CompoundNBT> {
    val listTag = get(key) as CompoundNBT
    return listTag.keySet().map { index ->
        listTag.get(index) as CompoundNBT
    }.toSet()
}

/*
fun <T : INBTSerializable<CompoundNBT>> CompoundNBT.getUnsortedList(key: String, itemGen: () -> T): Set<T> {
    val listTag = get(key) as CompoundNBT
    return listTag.keySet().map { index ->
        val itag = listTag.get(index) as CompoundNBT

    }.toSet()
}

 */

val CompoundNBT.toBountyEntry: BountyEntry
    get() {
        val bTypeName = getString("type")
        val bType = BountyType.values().find { bTypeName == it.id } ?: throw Exception("Deserialized bounty with type: $bTypeName")
        val newBounty = bType.klazz.createInstance()
        return newBounty.apply { deserializeNBT(this@toBountyEntry) }
    }
