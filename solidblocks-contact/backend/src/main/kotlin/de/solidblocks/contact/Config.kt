package de.solidblocks.contact

import org.yaml.snakeyaml.Yaml

fun loadValidComponentNames(): Set<String> {
  val stream =
    Thread.currentThread().contextClassLoader.getResourceAsStream("config.yml") ?: return emptySet()
  val data = Yaml().load<Map<String, Any>>(stream)
  val names = mutableSetOf<String>()
  collectComponentNames(data["groups"] as? List<*> ?: emptyList<Any>(), names)
  return names
}

private fun collectComponentNames(groups: List<*>, names: MutableSet<String>) {
  for (group in groups) {
    val g = group as? Map<*, *> ?: continue
    (g["components"] as? List<*>)?.forEach { comp ->
      ((comp as? Map<*, *>)?.get("name") as? String)?.let(names::add)
    }
    collectComponentNames(g["groups"] as? List<*> ?: emptyList<Any>(), names)
  }
}
