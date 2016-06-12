package com.rubyhuntersky.tour

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import butterknife.BindView
import butterknife.ButterKnife
import com.rubyhuntersky.coloret.Coloret.*
import com.rubyhuntersky.gx.Gx.colorColumn
import com.rubyhuntersky.gx.Gx.dropDownMenuDiv
import com.rubyhuntersky.gx.Gx.gapColumn
import com.rubyhuntersky.gx.Gx.textColumn
import com.rubyhuntersky.gx.android.AndroidHuman
import com.rubyhuntersky.gx.android.FrameLayoutScreen
import com.rubyhuntersky.gx.basics.Sizelet
import com.rubyhuntersky.gx.basics.Sizelet.FINGER
import com.rubyhuntersky.gx.basics.Sizelet.READABLE
import com.rubyhuntersky.gx.basics.TextStylet.IMPORTANT_DARK
import com.rubyhuntersky.gx.basics.TextStylet.READABLE_DARK
import com.rubyhuntersky.gx.devices.poles.Pole
import com.rubyhuntersky.gx.reactions.ItemSelectionReaction
import com.rubyhuntersky.gx.reactions.Reaction
import com.rubyhuntersky.gx.uis.divs.Div
import com.rubyhuntersky.gx.uis.divs.Div0

open class MainActivity : AppCompatActivity() {
    companion object {
        val tag = MainActivity::class.java.simpleName
    }

    @BindView(R.id.main_frame)
    lateinit var mainFrame: FrameLayout

    val human by lazy { AndroidHuman(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        mainFrame.addView(object : FrameLayout(this) {
            override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
                super.onLayout(changed, left, top, right, bottom)
                if (changed) {
                    onWidth(this, left, right)
                }
            }
        }, MATCH_PARENT, MATCH_PARENT)
    }

    fun onWidth(frameLayout: FrameLayout, left: Int, right: Int) {
        Log.d(tag, "onWidth left $left right $right")
        val pole = Pole((right - left).toFloat(), 0f, 0, FrameLayoutScreen(frameLayout, human))
        val menuItem1 = textColumn("Account 1234", IMPORTANT_DARK)
                .padBottom(READABLE)
                .expandDown(textColumn("Buy 20 shares", READABLE_DARK))
                .padBottom(READABLE)
                .expandDown(textColumn("and", READABLE_DARK))
                .padBottom(Sizelet.readables(3f))
                .expandDown(textColumn("Add funds $3398.29", IMPORTANT_DARK))
                .padVertical(READABLE)
        val menuItem2 = textColumn("Account ABCD", IMPORTANT_DARK)
                .padBottom(READABLE)
                .expandDown(textColumn("Sufficient funds $8972.33", READABLE_DARK))
                .padVertical(READABLE)

        val menuItems = listOf(menuItem1, menuItem2)
        val dropDownMenu: Div0 = dropDownMenuDiv(0, menuItems)

        data class Product(val name: String, val price: Float) {
            val nameAndPrice = "$name $price"
        }

        data class Account(val name: String, val cash: Float) {
            fun getBuyableShares(product: Product): Float {
                return cash / product.price
            }
        }

        data class Purchase(val amountToSpend: Float,
                            val products: List<Product>, val productIndex: Int,
                            val accounts: List<Account>, val accountIndex: Int) {
            val productToBuy = products[productIndex]
            val sharesToBuy = when (productToBuy.price) {
                0f -> 0f
                else -> amountToSpend / productToBuy.price
            }

            fun withProductIndex(value: Int) = Purchase(amountToSpend, products, value, accounts, accountIndex)
            fun withAccountIndex(value: Int) = Purchase(amountToSpend, products, productIndex, accounts, value)
        }

        val products = listOf(Product("VT", 58.44f), Product("Q", 32.36f))
        val accounts = listOf(Account("IRA", 50f), Account("Non-IRA", 5000f))
        val startingPurchase = Purchase(1000f, products, 0, accounts, 0)
        val purchaseDiv = Div0.create(object : Div.OnPresent {
            override fun onPresent(presenter: Div.Presenter) {
                presenter.addPresentation(object : Div.PresenterPresentation(presenter) {
                    var purchase = startingPurchase
                    var presentation: Div.Presentation = Div.Presentation.EMPTY

                    val Product.menuItem: Div0
                        get() = textColumn("\u00f7 $nameAndPrice ", IMPORTANT_DARK).expandVertical(READABLE)

                    fun Account.menuItem(purchaseTarget: Float, product: Product): Div0 {
                        val accountLine = textColumn("Account $name", IMPORTANT_DARK)
                                .expandDown(gapColumn(Sizelet.READABLE))
                        if (cash >= purchaseTarget) {
                            return accountLine.expandDown(textColumn("Sufficient funds \$$cash", READABLE_DARK))
                        } else {
                            return accountLine
                                    .expandDown(textColumn("Buy ${getBuyableShares(product)} shares", READABLE_DARK))
                                    .expandDown(gapColumn(READABLE))
                                    .expandDown(textColumn("or", READABLE_DARK))
                                    .expandDown(gapColumn(READABLE))
                                    .expandDown(textColumn("Add funds \$${purchaseTarget - cash}", IMPORTANT_DARK))
                        }
                    }

                    val Purchase.productMenu: Div0
                        get() = dropDownMenuDiv(productIndex, products.map { it.menuItem }, "productMenu")
                    val Purchase.accountMenu: Div0
                        get() = if (accounts.size > 0) {
                            dropDownMenuDiv(accountIndex, accounts.map { it.menuItem(amountToSpend, productToBuy) }, "accountMenu")
                        } else {
                            Div0.EMPTY
                        }

                    init {
                        present()
                    }

                    val Reaction.isProductChoice: Boolean get() = this is ItemSelectionReaction<*> && source == "productMenu"
                    val Reaction.isAccountChoice: Boolean get() = this is ItemSelectionReaction<*> && source == "accountMenu"
                    @Suppress("UNCHECKED_CAST")
                    val Reaction.integerChoice: Int get() = (this as ItemSelectionReaction<Int>).item!!

                    private fun present() {
                        Log.d(tag, "present")
                        val amountLine = textColumn("Buy \$${purchase.amountToSpend}", IMPORTANT_DARK)
                        val divisionDivider = colorColumn(Sizelet.QUARTER_READABLE, BLACK)
                        val purchaseUi = amountLine
                                .expandDown(purchase.productMenu)
                                .expandVertical(READABLE)
                                .expandDown(divisionDivider)
                                .expandDown(gapColumn(READABLE))
                                .expandDown(textColumn("${purchase.sharesToBuy} shares", IMPORTANT_DARK))
                                .expandDown(gapColumn(Sizelet.TRIPLE_IMPORTANT))
                                .expandDown(purchase.accountMenu)

                        presentation.cancel()
                        presentation = purchaseUi.present(presenter.human, presenter.pole, object : Div.ForwardingObserver(presenter) {
                            override fun onReaction(reaction: Reaction) {
                                if (reaction.isProductChoice) {
                                    Log.d(tag, "Product selected $reaction")
                                    purchase = purchase.withProductIndex(reaction.integerChoice)
                                    present()
                                } else if (reaction.isAccountChoice) {
                                    Log.d(tag, "Account selected $reaction")
                                    purchase = purchase.withAccountIndex(reaction.integerChoice)
                                    present()
                                } else {
                                    super.onReaction(reaction)
                                }
                            }
                        })
                    }

                    override fun onCancel() {
                        presentation.cancel()
                    }
                })
            }
        })

        val div = colorColumn(FINGER, GREEN)
                .expandDown(colorColumn(FINGER, BLUE))
                .expandDown(purchaseDiv)
                .expandDown(colorColumn(FINGER, RED))

        div.present(human, pole, LogObserver())
    }

    override fun onResume() {
        super.onResume()
        val width = mainFrame.width
        Log.d(tag, "onResume mainFrame width $width")
    }

    open class LogObserver : Div.Observer {

        val tag = "${LogObserver::class.java.simpleName}${this.hashCode()}"

        override fun onHeight(height: Float) {
            Log.d(tag, "onHeight $height")
        }

        override fun onReaction(reaction: Reaction) {
            Log.d(tag, "onReaction $reaction")
        }

        override fun onError(throwable: Throwable) {
            Log.e(tag, "onError", throwable)
        }
    }
}
