package com.rubyhuntersky.peregrine.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rubyhuntersky.columnui.Observer;
import com.rubyhuntersky.columnui.Reaction;
import com.rubyhuntersky.columnui.basics.Sizelet;
import com.rubyhuntersky.columnui.columns.ColumnUi;
import com.rubyhuntersky.columnui.columns.ColumnUi1;
import com.rubyhuntersky.columnui.columns.ColumnUi2;
import com.rubyhuntersky.columnui.columns.ColumnUi3;
import com.rubyhuntersky.columnui.columns.ColumnUi5;
import com.rubyhuntersky.columnui.columns.ColumnUiView;
import com.rubyhuntersky.columnui.presentations.EmptyPresentation;
import com.rubyhuntersky.columnui.presentations.Presentation;
import com.rubyhuntersky.columnui.reactions.HeightChangedReaction;
import com.rubyhuntersky.columnui.reactions.ItemSelectionReaction;
import com.rubyhuntersky.columnui.tiles.TileUi;
import com.rubyhuntersky.peregrine.AssetPrice;
import com.rubyhuntersky.peregrine.BuyProgram;
import com.rubyhuntersky.peregrine.FundingAccount;
import com.rubyhuntersky.peregrine.FundingOption;
import com.rubyhuntersky.peregrine.R;
import com.rubyhuntersky.peregrine.TradeDialogFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.rubyhuntersky.columnui.Creator.colorColumn;
import static com.rubyhuntersky.columnui.Creator.gapColumn;
import static com.rubyhuntersky.columnui.Creator.textColumn;
import static com.rubyhuntersky.columnui.Creator.textTile;
import static com.rubyhuntersky.columnui.basics.Coloret.BLACK;
import static com.rubyhuntersky.columnui.basics.Coloret.WHITE;
import static com.rubyhuntersky.columnui.basics.Sizelet.PREVIOUS;
import static com.rubyhuntersky.columnui.basics.Sizelet.Ruler.READABLE;
import static com.rubyhuntersky.columnui.basics.Sizelet.THIRD_FINGER;
import static com.rubyhuntersky.columnui.basics.Sizelet.TWO_THIRDS_FINGER;
import static com.rubyhuntersky.columnui.basics.Sizelet.ofPortion;
import static com.rubyhuntersky.columnui.basics.TextStylet.IMPORTANT_DARK;
import static com.rubyhuntersky.columnui.basics.TextStylet.READABLE_DARK;
import static com.rubyhuntersky.columnui.material.Android.spinnerTile;
import static com.rubyhuntersky.columnui.tiles.TileCreator.textTile1;
import static com.rubyhuntersky.peregrine.ui.UiHelper.getCurrencyDisplayString;

/**
 * @author wehjin
 * @since 1/19/16.
 */
public class BuyDialogFragment extends TradeDialogFragment {

    public static final String DIVISION_SIGN = "\u00f7";
    public static final ColumnUi SPACING = gapColumn(Sizelet.QUARTER_FINGER);
    public static final ColumnUi DIVIDER = colorColumn(ofPortion(.1f, READABLE), BLACK);
    public static final String TAG = BuyDialogFragment.class.getSimpleName();
    public static final String PROGRAM_KEY = "programKey";
    public static final TileUi DIVISION_SIGN_TILE = textTile(DIVISION_SIGN, IMPORTANT_DARK);
    public static final String BUY_PRICES_SPINNER = "buyPricesSpinner";
    public static final String FUNDING_ACCOUNT_SPINNER = "fundingAccountSpinner";
    public static final String FUNDING_OPTION_SPINNER = "fundingOptionSpinner";

    private ColumnUiView columnUiView;
    private Presentation presentation = new EmptyPresentation();
    private ColumnUi ui;
    private BuyProgram program;

    public static BuyDialogFragment create(BigDecimal amount, List<AssetPrice> prices, int selectedPrice, List<FundingAccount> fundingAccounts) {
        final BuyProgram buyProgram = new BuyProgram(amount, prices, selectedPrice);
        if (fundingAccounts != null) {
            buyProgram.setFundingAccounts(fundingAccounts, 0, 0);
        }

        final Bundle arguments = new Bundle();
        arguments.putParcelable(PROGRAM_KEY, buyProgram);

        final BuyDialogFragment fragment = new BuyDialogFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PROGRAM_KEY, program);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);

        program = getArguments().getParcelable(PROGRAM_KEY);
        if (program == null) {
            return;
        }

        ui = getBuyUi(program.buyAmount, getBuyPrices(program.buyOptions))
              .expandBottom(gapColumn(Sizelet.FINGER))
              .expandBottom(getFundingUi())
              .expandVertical(TWO_THIRDS_FINGER)
              .padHorizontal(THIRD_FINGER)
              .placeBefore(colorColumn(PREVIOUS, WHITE), 0)
              .printReadEval(new ColumnUi5.Repl<Integer, String, Integer, String, ColumnUi>() {

                  private int fundingAccountSelection = program.getSelectedFundingAccount();
                  private int buyPriceSelection = program.getSelectedBuyOption();
                  private int fundingPriceSelection = program.getSelectedFundingOption();

                  @Override
                  public ColumnUi print(ColumnUi5<Integer, String, Integer, String, ColumnUi> div4) {
                      return div4.bind(program.getSelectedBuyOption())
                            .bind(getSharesString(program.getSharesToBuy()))
                            .bind(program.getSelectedFundingAccount())
                            .bind(program.fundingAccountHasSufficientFundsToBuy()
                                        ? "Sufficient funds " + getCurrencyDisplayString(program.getFundingAccount()
                                                                                               .getCashAvailable())
                                        : "Add funds " + getCurrencyDisplayString(program.getAdditionalFundsNeededToBuy()))
                            .bind(program.fundingAccountHasSufficientFundsToBuy()
                                        || program.getFundingOptions().size() == 0
                                        ? ColumnUi.EMPTY
                                        : SPACING.expandBottom(getSellUi(getFundingPrices(program.getFundingOptions()),
                                                                         program.getSelectedFundingOption(),
                                                                         program.getSharesToSellForFunding(),
                                                                         program.getAdditionalFundsNeededAfterSale())));
                  }

                  @Override
                  public void read(Reaction reaction) {
                      Log.d(TAG, "Repl reaction: " + reaction);
                      if (BUY_PRICES_SPINNER.equals(reaction.getSource())) {

                          buyPriceSelection = (int) ((ItemSelectionReaction) reaction).getItem();
                      }
                      if (FUNDING_ACCOUNT_SPINNER.equals(reaction.getSource())) {

                          fundingAccountSelection = (int) ((ItemSelectionReaction) reaction).getItem();
                      }
                      if (FUNDING_OPTION_SPINNER.equals(reaction.getSource())) {
                          fundingPriceSelection = (int) ((ItemSelectionReaction) reaction).getItem();
                      }
                  }

                  @Override
                  public boolean eval() {
                      if (buyPriceSelection == program.getSelectedBuyOption()
                            && fundingAccountSelection == program.getSelectedFundingAccount()
                            && fundingPriceSelection == program.getSelectedFundingOption())
                          return false;

                      program.setSelectedBuyOption(buyPriceSelection);
                      program.setSelectedFundingAccount(fundingAccountSelection);
                      program.setSelectedFundingOption(fundingPriceSelection);
                      return true;
                  }
              });

    }

    private ColumnUi2<Integer, String> getBuyUi(BigDecimal buyAmount, List<String> buyPrices) {
        return textColumn("Buy " + getCurrencyDisplayString(buyAmount), IMPORTANT_DARK)
              .expandBottom(SPACING)
              .expandBottom(spinnerTile(buyPrices)
                                  .name(BUY_PRICES_SPINNER)
                                  .expandLeft(DIVISION_SIGN_TILE)
                                  .toColumn())
              .expandBottom(SPACING)
              .expandBottom(DIVIDER)
              .expandBottom(SPACING)
              .expandBottom(textTile1(IMPORTANT_DARK).toColumn());
    }

    @NonNull
    private List<String> getBuyPrices(List<AssetPrice> buyOptions) {
        final List<String> buyPrices = new ArrayList<>();
        for (AssetPrice price : buyOptions) {
            buyPrices.add(price.name + " " + getCurrencyDisplayString(price.price));
        }
        return buyPrices;
    }

    private ColumnUi3<Integer, String, ColumnUi> getFundingUi() {
        List<String> fundingAccountNames = new ArrayList<>();
        for (FundingAccount fundingAccount : program.getFundingAccounts()) {
            fundingAccountNames.add("Account " + fundingAccount.getAccountName());
        }

        final ColumnUi1<Integer> fundingAccountDiv = spinnerTile(fundingAccountNames).name(FUNDING_ACCOUNT_SPINNER)
              .toColumn();
        final ColumnUi1<String> fundingAccountStatusDiv = textTile1(READABLE_DARK).toColumn();
        return fundingAccountDiv.expandBottom(fundingAccountStatusDiv).expandBottom();
    }

    @NonNull
    private List<String> getFundingPrices(List<FundingOption> fundingOptions) {
        List<String> fundingOptionPrices = new ArrayList<>();
        for (FundingOption fundingOption : fundingOptions) {
            fundingOptionPrices.add(fundingOption.getAssetName() + " " + getCurrencyDisplayString(fundingOption.getSellPrice()));
        }
        return fundingOptionPrices;
    }

    private ColumnUi getSellUi(List<String> sellOptions, int selectedSellOption, BigDecimal sharesToSell, BigDecimal shortfall) {
        ColumnUi shortfallExpansion = shortfall.compareTo(BigDecimal.ZERO) <= 0
              ? ColumnUi.EMPTY
              : SPACING.expandBottom(textColumn("Shortfall " + getCurrencyDisplayString(shortfall), READABLE_DARK));

        return spinnerTile(sellOptions, selectedSellOption).name(FUNDING_OPTION_SPINNER)
              .expandLeft(DIVISION_SIGN_TILE)
              .toColumn()
              .expandBottom(SPACING)
              .expandBottom(DIVIDER)
              .expandBottom(SPACING)
              .expandBottom(textColumn("Sell " + sharesToSell
                    .setScale(0, BigDecimal.ROUND_CEILING) + " shares", IMPORTANT_DARK))
              .expandBottom(shortfallExpansion);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.fragment_buy, container, false);
        columnUiView = (ColumnUiView) inflate.findViewById(R.id.ui);
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        present();
    }

    private void present() {
        presentation.cancel();
        presentation = columnUiView.present(ui, new Observer() {
            @Override
            public void onReaction(Reaction reaction) {
                Log.d(TAG, "onReaction: " + reaction);
                if (reaction instanceof HeightChangedReaction) {
                    columnUiView.clearVariableDimensions();
                    columnUiView.requestLayout();
                }
            }

            @Override
            public void onEnd() {
                Log.d(TAG, "onEnd");
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "onError", throwable);
            }
        });
    }

    @Override
    public void onPause() {
        presentation.cancel();
        super.onPause();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}
