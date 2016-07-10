package com.rubyhuntersky.gx.puddles

/**
 * @author Jeffrey Yu
 * @since 7/9/16.
 */

open class CompositePresentation : Puddle.Presentation {
    private val presentations = mutableMapOf<Long, Puddle.Presentation>()
    private var ending = false
    private var ended = false;

    fun add(presentation: Puddle.Presentation) = add(presentation, ID_GENERATOR.nextLong())
    fun add(presentation: Puddle.Presentation, id: Long) {
        if (ending || ended) {
            presentation.end()
            return
        }
        val previous = presentations[id]
        if (previous == presentation) {
            return
        }
        previous?.end()
        presentations[id] = presentation;
    }

    fun remove(id: Long) {
        if (ending || ended) return
        val previous = presentations[id];
        previous?.end()
        presentations.remove(id)
    }

    override fun end() {
        if (ending || ended) return

        ending = true
        for ((id, presentation) in presentations) {
            presentation.end()
        }
        presentations.clear()
        ended = true
    }
}