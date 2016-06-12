package com.rubyhuntersky.gx.uis.divs

import com.rubyhuntersky.gx.Human
import com.rubyhuntersky.gx.cancelable.BooleanCancelable
import com.rubyhuntersky.gx.cancelable.Cancelable
import com.rubyhuntersky.gx.devices.poles.Pole
import com.rubyhuntersky.gx.reactions.Reaction

/**
 * @author Jeffrey Yu
 * @since 6/10/16.
 */

interface Div {

    fun present(human: Human, pole: Pole, observer: Observer): Presentation

    interface Observer : com.rubyhuntersky.gx.observers.Observer {
        fun onHeight(height: Float)
    }

    interface Presentation : Cancelable {
        object EMPTY : Presentation, Cancelable {
            override val isCancelled: Boolean = true
            override fun cancel() {
                // Do nothing
            }
        }
    }

    interface Presenter : Observer, Presentation {
        val human: Human
        val pole: Pole
        fun addPresentation(presentation: Presentation)
    }

    interface OnPresent {
        fun onPresent(presenter: Presenter)
    }

    interface PrintReadEvaluater<UnboundDiv> {
        fun print(unbound: UnboundDiv): Div0
        fun read(reaction: Reaction)
        fun eval(): Boolean
    }

    object EmptyObserver : Observer {
        override fun onHeight(height: Float) {
            // Do nothing
        }

        override fun onReaction(reaction: Reaction) {
            // Do nothing
        }

        override fun onError(throwable: Throwable) {
            // Do nothing
        }
    }

    open class ForwardingObserver(val observer: Observer) : Observer {
        override fun onHeight(height: Float) {
            observer.onHeight(height)
        }

        override fun onReaction(reaction: Reaction) {
            observer.onReaction(reaction)
        }

        override fun onError(throwable: Throwable) {
            observer.onError(throwable)
        }
    }

    abstract class BooleanPresentation : Presentation, BooleanCancelable() {
        init {
            onPresent()
        }

        abstract protected fun onPresent()
    }

    abstract class PresenterPresentation(val presenter: Presenter) : BooleanPresentation() {

        val human = presenter.human
        val pole = presenter.pole

        final override fun onPresent() {
            // Do nothing.  Block further overrides because pole and human as not ready at the time this is called.
        }
    }


    class BasePresenter(override val human: Human, override val pole: Pole, private val observer: Observer) : Presenter, BooleanPresentation() {

        val presentations = mutableSetOf<Cancelable>()

        override fun onPresent() {
            // Do nothing
        }

        override fun addPresentation(presentation: Div.Presentation) {
            if (isCancelled) {
                presentation.cancel()
            } else {
                presentations.add(presentation)
            }
        }

        override fun onHeight(height: Float) {
            if (isCancelled) {
                // Do nothing
            } else {
                observer.onHeight(height)
            }
        }

        override fun onReaction(reaction: Reaction) {
            if (isCancelled) {
                // Do nothing
            } else {
                observer.onReaction(reaction)
            }
        }

        override fun onError(throwable: Throwable) {
            if (isCancelled) {
                // Do nothing
            } else {
                cancel()
                observer.onError(throwable)
            }
        }

        override fun onCancel() {
            presentations.forEach { it.cancel() }
            presentations.clear()
        }
    }
}

