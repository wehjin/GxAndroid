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
import com.rubyhuntersky.gx.basics.Sizelet.*
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
        val pole = Pole((right - left).toFloat(), 0f, 0, FrameLayoutScreen(frameLayout, human, this))

        data class Product(val name: String, val price: Float) {
            val nameAndPrice = "$name $price"
        }

        data class Asset(val name: String, val shares: Float, val price: Float) {
            val value = shares * price

            fun getSharesToSellToCoverShortfall(fundingAmount: Float): Float = if (price == 0f) 0f else (fundingAmount / price)
        }

        data class Account(val name: String, val cash: Float, val assets: List<Asset>) {
            fun hasFundsForPurchase(purchaseAmount: Float): Boolean = cash >= purchaseAmount
            fun getPurchasableShares(product: Product): Float {
                return cash / product.price
            }

            fun getShortfallForPurchase(purchaseAmount: Float): Float = purchaseAmount - cash
        }

        data class Purchase(val amountToSpend: Float,
                            val products: List<Product>, val productIndex: Int,
                            val accounts: List<Account>, val accountIndex: Int,
                            val salableAssets: List<Asset> = if (accountIndex < accounts.size) {
                                accounts[accountIndex].assets
                            } else {
                                emptyList()
                            }, val salableAssetIndex: Int = 0) {
            val productToBuy = products[productIndex]
            val sharesToBuy = when (productToBuy.price) {
                0f -> 0f
                else -> amountToSpend / productToBuy.price
            }
            val account: Account? = if (accountIndex < accounts.size) {
                accounts[accountIndex]
            } else {
                null
            }
            val isFunded: Boolean = account?.hasFundsForPurchase(amountToSpend) ?: false
            val canSellToFund: Boolean = salableAssets.size > 0
            val salableAsset: Asset? = if (salableAssetIndex < salableAssets.size) salableAssets[salableAssetIndex] else null
            val shortfall: Float = account?.getShortfallForPurchase(amountToSpend) ?: 0f
            val sharesToSellToCoverShortfall: Float = salableAsset?.getSharesToSellToCoverShortfall(shortfall) ?: 0f

            fun withProductIndex(value: Int): Purchase {
                if (value == productIndex) {
                    return this
                }
                return Purchase(amountToSpend, products, value, accounts, accountIndex, salableAssets, salableAssetIndex)
            }

            fun withAccountIndex(value: Int): Purchase {
                if (value == accountIndex) {
                    return this
                }
                return Purchase(amountToSpend, products, productIndex, accounts, value)
            }

            fun withAssetIndex(value: Int): Purchase {
                if (value == salableAssetIndex) {
                    return this;
                }
                return Purchase(amountToSpend, products, productIndex, accounts, accountIndex, salableAssets, value)
            }
        }

        val products = listOf(Product("VT", 58.44f), Product("Q", 32.36f))
        val iraAccount = Account("IRA", 50f, listOf(Asset("IBM", 30f, 10f), Asset("MSFT", 300f, 20f)))
        val nonIraAccount = Account("Non-IRA", 5000f, listOf(Asset("GOOG", 9f, 11f)))
        val accounts = listOf(iraAccount, nonIraAccount)
        val startingPurchase = Purchase(1000f, products, 0, accounts, 0)
        val purchaseDiv = Div0.create(object : Div.OnPresent {
            override fun onPresent(presenter: Div.Presenter) {
                presenter.addPresentation(object : Div.PresenterPresentation(presenter) {
                    var purchase = startingPurchase
                    var presentation: Div.Presentation = Div.Presentation.EMPTY

                    val Product.menuItem: Div0
                        get() = textColumn("\u00f7 $nameAndPrice ", IMPORTANT_DARK).expandVertical(READABLE)

                    val Asset.menuItem: Div0
                        get() = textColumn("\u00f7 $name \$$price", IMPORTANT_DARK).expandVertical(READABLE)

                    fun Account.menuItem(purchaseTarget: Float, product: Product): Div0 {
                        val accountLine = textColumn("Account $name", IMPORTANT_DARK)
                                .expandDown(gapColumn(Sizelet.READABLE))
                        if (cash >= purchaseTarget) {
                            return accountLine.expandDown(textColumn("Sufficient funds \$$cash", READABLE_DARK))
                        } else {
                            return accountLine
                                    .expandDown(textColumn("Buy ${getPurchasableShares(product)} shares", READABLE_DARK))
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
                    val Purchase.assetMenu: Div0
                        get() {
                            return if (!isFunded && salableAssets.size > 0) {
                                dropDownMenuDiv(salableAssetIndex, salableAssets.map { it.menuItem }, "assetMenu")
                            } else {
                                Div0.EMPTY
                            }
                        }

                    init {
                        present()
                    }

                    val Reaction.isProductChoice: Boolean get() = this is ItemSelectionReaction<*> && source == "productMenu"
                    val Reaction.isAccountChoice: Boolean get() = this is ItemSelectionReaction<*> && source == "accountMenu"
                    val Reaction.isAssetChoice: Boolean get() = this is ItemSelectionReaction<*> && source == "assetMenu"
                    @Suppress("UNCHECKED_CAST")
                    val Reaction.integerChoice: Int get() = (this as ItemSelectionReaction<Int>).item!!

                    private fun present() {
                        Log.d(tag, "present")
                        val amountLine = textColumn("Buy \$${purchase.amountToSpend}", IMPORTANT_DARK)
                        val divisionDivider = colorColumn(QUARTER_READABLE, BLACK).padHorizontal(HALF_FINGER)
                        val purchaseUi = amountLine
                                .expandDown(purchase.productMenu)
                                .expandVertical(HALF_READABLE)
                                .expandDown(divisionDivider)
                                .expandDown(gapColumn(READABLE))
                                .expandDown(textColumn("${purchase.sharesToBuy} shares", IMPORTANT_DARK))
                                .expandDown(gapColumn(DOUBLE_IMPORTANT))
                                .expandDown(purchase.accountMenu)
                                .expandDown(if (purchase.shortfall > 0 && purchase.salableAssets.size > 0) {
                                    purchase.assetMenu
                                            .expandDown(divisionDivider)
                                            .expandDown(gapColumn(READABLE))
                                            .expandDown(textColumn("Sell ${purchase.sharesToSellToCoverShortfall} shares", IMPORTANT_DARK))
                                } else {
                                    Div0.EMPTY
                                })
                                .expandVertical(THIRD_FINGER)

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
                                } else if (reaction.isAssetChoice) {
                                    Log.d(tag, "Asset selected $reaction")
                                    purchase = purchase.withAssetIndex(reaction.integerChoice)
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
