package com.framgia.fdms.utils.binding;

import android.app.Activity;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.databinding.NavHeaderMainBinding;
import com.framgia.fdms.screen.ViewPagerScroll;
import com.framgia.fdms.screen.dashboard.DashboardViewModel;
import com.framgia.fdms.screen.devicedetail.DeviceDetailViewModel;
import com.framgia.fdms.screen.deviceselection.BottomSheetCallback;
import com.framgia.fdms.screen.main.MainViewModel;
import com.framgia.fdms.screen.requestdetail.information.RequestInformationViewModel;
import com.framgia.fdms.utils.Utils;
import com.framgia.fdms.widget.CustomSpinner;
import com.framgia.fdms.widget.FDMSShowcaseSequence;
import com.framgia.fdms.widget.OnSearchMenuItemClickListener;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static com.framgia.fdms.data.anotation.Permission.ACCOUNTANT;
import static com.framgia.fdms.data.anotation.Permission.ADMIN;
import static com.framgia.fdms.data.anotation.Permission.BO_MANAGER;
import static com.framgia.fdms.data.anotation.Permission.BO_STAFF;
import static com.framgia.fdms.data.anotation.Permission.DIVISION_MANAGER;
import static com.framgia.fdms.data.anotation.Permission.GROUP_LEADER;
import static com.framgia.fdms.data.anotation.Permission.SECTION_MANAGER;
import static com.framgia.fdms.data.anotation.RequestStatus.APPROVED;
import static com.framgia.fdms.data.anotation.RequestStatus.CANCELLED;
import static com.framgia.fdms.data.anotation.RequestStatus.DONE;
import static com.framgia.fdms.data.anotation.RequestStatus.WAITING_APPROVE;
import static com.framgia.fdms.data.anotation.RequestStatus.WAITING_DONE;
import static com.framgia.fdms.data.anotation.RequestStatus.WAITING_FORWARD;
import static com.framgia.fdms.screen.dashboard.DashboardViewModel.Tab.TAB_DEVIVE_DASH_BOARD;
import static com.framgia.fdms.screen.dashboard.DashboardViewModel.Tab.TAB_REQUEST_DASH_BOARD;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_CLOSE;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_OPEN;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Created by Age on 4/3/2017.
 */
public final class BindingUtils {
    private static final float TEXT_LABLE_SIZE = 11f;
    private static final float TEXT_CENTER_SIZE = 15f;
    private static final int ROTATION_ANGLE = 0;
    private static final int ANIMATE_DURATION = 2000;
    private static final int CIRCLE_ALPHA = 110;
    private static final float DECELERATION_FRICTION_COEF = 0.95f;

    private BindingUtils() {
        // No-op
    }

    @BindingAdapter({"recyclerAdapter"})
    public static void setAdapterForRecyclerView(RecyclerView recyclerView,
                                                 RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter({"recyclerLayoutManager"})
    public static void setLayoutManager(RecyclerView recyclerView,
                                        RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    @BindingAdapter({"bottomSheetState", "bottomSheetCallback"})
    public static void setBottomSheetState(final View view, int state,
                                           final BottomSheetCallback callback) {
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setState(state);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                callback.onStateChanged(bottomSheet, newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @BindingAdapter(value = {"app:imageUrl", "app:error"}, requireAll = false)
    public static void loadImage(ImageView view, String imageUrl, Drawable error) {
        if (error == null) {
            Glide.with(view.getContext())
                    .load(imageUrl)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.ic_no_image)
                    .into(view);
        } else {
            Glide.with(view.getContext())
                    .load(imageUrl)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(error)
                    .into(view);
        }
    }

    @BindingAdapter("errorText")
    public static void setErrorText(final TextInputLayout layout, String text) {
        layout.setError(text);
        EditText editText = layout.getEditText();
        if (editText == null) {
            return;
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //No-Op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //No-Op
            }

            @Override
            public void afterTextChanged(Editable s) {
                layout.setError("");
            }
        });
    }

    @BindingAdapter("errorText")
    public static void setErrorText(TextView layout, String text) {
        layout.setError(text);
    }

    @BindingAdapter({"spinnerAdapter"})
    public static void setAdapterForSpinner(AppCompatSpinner spinner,
                                            ArrayAdapter adapter) {
        spinner.setAdapter(adapter);
    }

    @BindingAdapter({"spinnerAdapter"})
    public static void setAdapterForSpinner(CustomSpinner spinner, ArrayAdapter adapter) {
        spinner.setAdapter(adapter);
    }

    @BindingAdapter({"spinnerListener"})
    public static void setListenerForSpinner(CustomSpinner spinner,
                                             AdapterView.OnItemSelectedListener listener) {
        spinner.setListener(listener);
    }

    @BindingAdapter({"spinnerListener"})
    public static void setListenerForSpinner(Spinner spinner,
                                             AdapterView.OnItemSelectedListener listener) {
        spinner.setOnItemSelectedListener(listener);
    }

    @BindingAdapter({"bind:font"})
    public static void setFont(TextView textView, String fontName) {
        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), fontName));
    }

    @BindingAdapter({"scrollListenner"})
    public static void setScrollListenner(RecyclerView recyclerView,
                                          RecyclerView.OnScrollListener listener) {
        recyclerView.addOnScrollListener(listener);
    }

    @BindingAdapter(value = {"bind:adapter", "model", "linearDot"}, requireAll = false)
    public static void setupViewPager(final ViewPager viewPager, FragmentPagerAdapter adapter,
                                      final ViewPagerScroll viewModel, final LinearLayout layout) {
        viewPager.setAdapter(adapter);
        if (viewModel == null) return;
        if (adapter != null) viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewModel.onCurrentPosition(position);
                if (layout != null) {
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        View view = layout.getChildAt(i);
                        if (view == null) {
                            continue;
                        }
                        view.setBackgroundResource(position == i ? R.drawable.ic_circle_white
                                : R.drawable.ic_circle_border_white);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private static void changeImageColor(ImageView image, int colorRes) {
        image.getDrawable()
                .setColorFilter(image.getContext().getResources().getColor(colorRes),
                        PorterDuff.Mode.SRC_IN);
    }

    @BindingAdapter({"bind:activity"})
    public static void setupViewPager(Toolbar view, AppCompatActivity activity) {
        activity.setSupportActionBar(view);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @BindingAdapter(value = {
            "bind:searchListenner", "bind:clearListenner", "bind:menuItemClick", "bind:searchText",
            "bind:searchHint"
    }, requireAll = false)
    public static void setOnQueryChangeListenner(final FloatingSearchView searchView,
                                                 FloatingSearchView.OnSearchListener onSearchListener,
                                                 FloatingSearchView.OnClearSearchActionListener clearSearchActionListener,
                                                 final OnSearchMenuItemClickListener onSearchMenuItemClickListener, String searchText,
                                                 String searchHint) {
        searchView.setOnSearchListener(onSearchListener);
        searchView.setOnClearSearchActionListener(clearSearchActionListener);
        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                onSearchMenuItemClickListener.onActionMenuItemSelected(searchView, item);
            }
        });
        if (searchText != null) {
            searchView.setSearchText(searchText);
        }
        if(searchHint != null){
            searchView.setSearchHint(searchHint);
        }
    }

    @BindingAdapter({"pieData", "totalValue", "description"})
    public static void setData(final PieChart pieChart, PieData pieData, final int total,
                               String description) {
        final Resources resources = pieChart.getContext().getResources();
        if (pieData.getDataSetCount() > 0) {
            pieChart.setUsePercentValues(true);
            pieChart.setDrawEntryLabels(false);
            pieChart.getDescription().setEnabled(true);
            pieChart.setDragDecelerationFrictionCoef(DECELERATION_FRICTION_COEF);
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(Color.WHITE);
            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(CIRCLE_ALPHA);
            pieChart.setDrawCenterText(true);
            pieChart.setRotationAngle(ROTATION_ANGLE);
            pieChart.setRotationEnabled(true);
            pieChart.setHighlightPerTapEnabled(true);
            pieChart.setCenterText(resources.getString(R.string.title_total) + total);
            pieChart.setCenterTextColor(Color.BLUE);
            pieChart.setCenterTextSize(TEXT_CENTER_SIZE);
            pieChart.animateY(ANIMATE_DURATION, Easing.EasingOption.EaseInOutQuad);
            pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
            pieChart.getLegend().setDrawInside(false);
            pieChart.setEntryLabelColor(Color.WHITE);
            pieChart.setEntryLabelTextSize(TEXT_LABLE_SIZE);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(TEXT_LABLE_SIZE);
            pieData.setValueTextColor(Color.WHITE);
            pieChart.setData(pieData);
            pieChart.invalidate();
            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry entry, Highlight highlight) {
                    PieEntry pieEntry = (PieEntry) entry;
                    pieChart.setCenterText(pieEntry.getLabel() + " " + ((int) pieEntry.getY()));
                }

                @Override
                public void onNothingSelected() {
                    pieChart.setCenterText(resources.getString(R.string.title_total) + total);
                }
            });
        }
    }

    /**
     * format money currency
     */
    @InverseBindingAdapter(attribute = "currency", event = "textAttrChangedAM")
    public static String captureTextValue(EditText view) {
        String value = view.getText().toString();
        if (value.isEmpty()) {
            return value;
        }
        String numberValue = value.replaceAll("\\,", "");
        double number = Double.parseDouble(numberValue);
        DecimalFormat decimalFormat = new DecimalFormat();
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setGroupingSeparator(',');
        decimalFormat.setDecimalFormatSymbols(formatSymbols);
        return decimalFormat.format(number);
    }

    @BindingAdapter(value = {"currency", "textAttrChangedAM"}, requireAll = false)
    public static void setChange(final EditText view, final String currency,
                                 final InverseBindingListener textAttrChanged) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textAttrChanged.onChange();
                try {
                    String value = view.getText().toString();
                    view.setSelection(value.length());
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /*
    * set Toolbar Activity device return
    * */

    @BindingAdapter({"view", "titleToolbar"})
    public static void bindToolbar(Toolbar view, AppCompatActivity activity, String resTitle) {
        if (activity == null) {
            return;
        }
        activity.setSupportActionBar(view);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        activity.setTitle(resTitle);
    }

    @BindingAdapter({"model"})
    public static void setupViewPagerDashBorad(final ViewPager viewPager,
                                               final DashboardViewModel viewModel) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewModel.setTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @BindingAdapter("tabDashBoard")
    public static void onChangeImage(TextView image, int tab) {
        image.setBackgroundResource(R.drawable.bg_circle_grey);
        switch (tab) {
            case TAB_DEVIVE_DASH_BOARD:
                if (image.getId() == R.id.image_device_dashboard) {
                    image.setBackgroundResource(R.drawable.bg_circle_red);
                }
                break;
            case TAB_REQUEST_DASH_BOARD:
                if (image.getId() == R.id.image_request_dashboard) {
                    image.setBackgroundResource(R.drawable.bg_circle_red);
                }
                break;
            default:
                break;
        }
    }

    @BindingAdapter("bind:textForHtml")
    public static void setText(TextView view, String title) {
        view.setText(Html.fromHtml(title));
    }

    @BindingAdapter("bind:dateCreate")
    public static void setDate(TextView view, Date dateTime) {
        if (dateTime == null) {
            view.setText(view.getContext().getString(R.string.title_unknown));
            return;
        }
        String niceDateStr = String.valueOf(DateUtils.getRelativeTimeSpanString(dateTime.getTime(),
                Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS));
        view.setText(niceDateStr);
    }

    @BindingAdapter("bind:deviceStatus")
    public static void setTextColor(TextView view, int status) {
        switch (status) {
            case CANCELLED:
                view.setTextColor(view.getResources().getColor(R.color.color_red_500));
                break;
            case WAITING_APPROVE:
                view.setTextColor(view.getResources().getColor(R.color.color_blue_600));
                break;
            case APPROVED:
                view.setTextColor(view.getResources().getColor(R.color.color_green));
                break;
            case WAITING_DONE:
                view.setTextColor(view.getResources().getColor(R.color.color_teal_700));
                break;
            case DONE:
                view.setTextColor(view.getResources().getColor(R.color.color_orange_800));
                break;
            case WAITING_FORWARD:
                view.setTextColor(view.getResources().getColor(R.color.color_cyan_300));
                break;
            default:
                break;
        }
    }

    @BindingAdapter("bind:backgroundstatus")
    public static void setBackgroundText(TextView view, int status) {
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        switch (status) {
            case CANCELLED:
                drawable.setColor(view.getResources().getColor(R.color.color_red_500));
                break;
            case WAITING_APPROVE:
                drawable.setColor(view.getResources().getColor(R.color.color_blue_600));
                break;
            case APPROVED:
                drawable.setColor(view.getResources().getColor(R.color.color_green));
                break;
            case WAITING_DONE:
                drawable.setColor(view.getResources().getColor(R.color.color_teal_700));
                break;
            case DONE:
                drawable.setColor(view.getResources().getColor(R.color.color_orange_800));
                break;
            case WAITING_FORWARD:
                drawable.setColor(view.getResources().getColor(R.color.color_cyan_300));
                break;
            default:
                break;
        }
        view.setBackground(drawable);
    }

    @BindingAdapter(value = {"bind:queryTextListener", "bind:searchText"}, requireAll = false)
    public static void querySearchView(SearchView searchView,
                                       SearchView.OnQueryTextListener listener, String searchText) {
        searchView.setOnQueryTextListener(listener);
        if (searchText != null) {
            searchView.setQuery(searchText, false);
        }
    }

    @BindingAdapter({"resourceId"})
    public static void setImage(ImageView view, int resource) {
        view.setImageResource(resource);
    }

    @InverseBindingAdapter(attribute = "deviceCategoryId", event = "deviceCategoryIdAttrChanged")
    public static int captureDeviceCategoryId(AppCompatSpinner spinner) {
        Object selectedItem = spinner.getSelectedItem();
        return ((Status) selectedItem).getId();
    }

    @BindingAdapter(value = {
            "deviceCategoryId", "deviceCategoryIdAttrChanged"
    }, requireAll = false)
    public static void setCategoryId(AppCompatSpinner view, int newSelectedValue,
                                     final InverseBindingListener bindingListener) {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bindingListener.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bindingListener.onChange();
            }
        };
        view.setOnItemSelectedListener(listener);
    }

    /*
    * bind Spinner Adapter
    * in Activity Return Device
    * */
    @BindingAdapter("spinnerAdapter")
    public static void spinnerAdapter(AppCompatSpinner spinner, ArrayAdapter adapter) {
        spinner.setAdapter(adapter);
    }

    /*
    * bind ViewPager to Model
    * Device Detail ViewHolder
    * */

    @BindingAdapter("bindViewModel")
    public static void bindViewModel(ViewPager view, final DeviceDetailViewModel viewModel) {
        view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewModel.updateFloatingVisible(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @BindingAdapter("visibleAnim")
    public static void setVisibleFab(FloatingActionButton fab, boolean isVisible) {
        if (isVisible) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    @BindingAdapter("refreshAble")
    public static void setRefreshLayout(SwipeRefreshLayout view, boolean isRefresh) {
        view.setRefreshing(isRefresh);
    }

    @BindingAdapter("swipeRefreshListener")
    public static void setOnRefreshUserRequest(SwipeRefreshLayout view,
                                               SwipeRefreshLayout.OnRefreshListener listener) {
        view.setOnRefreshListener(listener);
    }

    @BindingAdapter("imageBitmap")
    public static void setImageBitmap(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter("setVisibility")
    public static void setVisibility(com.github.clans.fab.FloatingActionButton view,
                                     RequestInformationViewModel viewModel) {
        int visibility = viewModel.getStatusRequest().equals(DONE) || viewModel.getStatusRequest()
                .equals(APPROVED) ? View.GONE : View.VISIBLE;
        view.setVisibility(visibility);
    }

    @BindingAdapter("totalDot")
    public static void setDot(LinearLayout layout, int total) {
        Resources resources = layout.getContext().getResources();
        for (int i = 0; i <= total; i++) {
            ImageView imageView = new ImageView(layout.getContext());
            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins((int) resources.getDimension(R.dimen.dp_0),
                    (int) resources.getDimension(R.dimen.dp_0),
                    (int) resources.getDimension(R.dimen.dp_10),
                    (int) resources.getDimension(R.dimen.dp_0));
            imageView.setLayoutParams(lp);
            layout.addView(imageView);
            layout.getChildAt(i).setBackgroundResource(R.drawable.ic_circle_border_white);
        }
        layout.getChildAt(0).setBackgroundResource(R.drawable.ic_circle_white);
    }

    @BindingAdapter("scrollPosition")
    public static void scrollToPosition(RecyclerView recyclerView, int position) {
        if (position > OUT_OF_INDEX && position < (recyclerView.getChildCount() - 1)) {
            recyclerView.smoothScrollToPosition(position);
        }
    }

    @BindingAdapter("hideButton")
    public static void hideFloatButton(RecyclerView recyclerView,
                                       final FloatingActionButton button) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    button.hide();
                } else {
                    button.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @BindingAdapter("messageError")
    public static void showError(final TextInputLayout textInputLayout, final String message) {
        EditText editText = textInputLayout.getEditText();
        if (TextUtils.isEmpty(editText.getText())) {
            textInputLayout.setError(message);
        }
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    textInputLayout.setError(message);
                    return;
                }
                textInputLayout.setError("");
            }
        });
    }

    @BindingAdapter({"showcaseSequence", "contentShowCase", "dismissText"})
    public static void setTooltip(final View view, final FDMSShowcaseSequence sequence,
                                  String content, String dismissText) {
        sequence.addSequenceItem(view, content, dismissText);
    }

    @BindingAdapter("model")
    public static void setShowcase(final View view, final MainViewModel viewModel) {
        if (viewModel == null || viewModel.isShowCase()) {
            return;
        }
        Activity activity = viewModel.getActivity();
        final FDMSShowcaseSequence sequence = viewModel.getSequence();
        new MaterialShowcaseView.Builder(activity).setTarget(view)
                .withoutShape()
                .setMaskColour(R.color.color_black_transprarent)
                .setDismissText(R.string.title_ok)
                .setContentText(R.string.title_welcome)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                        sequence.start();
                    }
                })
                .show();
        sequence.setOnItemDismissedListener(
                new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
                    @Override
                    public void onDismiss(MaterialShowcaseView materialShowcaseView, int i) {
                        sequence.setCount(sequence.getCount() - 1);
                        if (sequence.getCount() == 0) {
                            viewModel.onShowCaseDashBoard();
                        }
                    }
                });
    }

    @BindingAdapter("hideMenuButton")
    public static void hideFloatMenuButton(RecyclerView recyclerView,
                                           final FloatingActionsMenu button) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    button.setVisibility(View.GONE);
                } else {
                    button.setVisibility(View.VISIBLE);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @BindingAdapter("tabNumber")
    public static void setTab(ViewPager view, int tab) {
        view.setCurrentItem(tab);
    }

    @BindingAdapter("expandableAdapter")
    public static void setExpandableAdapter(ExpandableListView expandableListView,
                                            BaseExpandableListAdapter baseExpandableListAdapter) {
        expandableListView.setAdapter(baseExpandableListAdapter);
    }

    @BindingAdapter("scrollListener")
    public static void setExpandableScrollListener(ExpandableListView expandableListView,
                                                   AbsListView.OnScrollListener listener) {
        expandableListView.setOnScrollListener(listener);
    }

    @BindingAdapter("scrollListener")
    public static void setOnScrollListener(ExpandableListView expandableListView,
                                           AbsListView.OnScrollListener listener) {
        expandableListView.setOnScrollListener(listener);
    }

    @BindingAdapter({"itemSelected", "currentItem", "model", "staffType"})
    public static void setNavigationItemSelected(NavigationView navigationView,
                                                 NavigationView.OnNavigationItemSelectedListener listen, int currentItem,
                                                 MainViewModel viewModel, int staffType) {
        navigationView.setNavigationItemSelectedListener(listen);
        navigationView.setCheckedItem(currentItem);
        if (navigationView.getHeaderCount() == 0) {
            NavHeaderMainBinding binding =
                    NavHeaderMainBinding.inflate(LayoutInflater.from(navigationView.getContext()));
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
            navigationView.addHeaderView(binding.getRoot());
        }
        MenuItem manageDevice = navigationView.getMenu().findItem(R.id.item_manage_device);
        MenuItem manageRequest = navigationView.getMenu().findItem(R.id.item_manage_request);
        MenuItem manageDeviceGroup =
                navigationView.getMenu().findItem(R.id.item_manage_device_group);
        MenuItem manageDeviceCategory =
                navigationView.getMenu().findItem(R.id.item_manage_device_category);
        MenuItem manageVendor = navigationView.getMenu().findItem(R.id.item_manage_vendor);
        MenuItem manageMaker = navigationView.getMenu().findItem(R.id.item_manage_maker);
        MenuItem manageMeetingRoom =
                navigationView.getMenu().findItem(R.id.item_manage_meeting_room);
        MenuItem deviceUsingHistory =
                navigationView.getMenu().findItem(R.id.item_device_using_history);
        MenuItem myDevice = navigationView.getMenu().findItem(R.id.item_my_device);
        MenuItem myRequest = navigationView.getMenu().findItem(R.id.item_my_request);
        manageDevice.setVisible(false);
        manageRequest.setVisible(false);
        manageVendor.setVisible(false);
        manageMaker.setVisible(false);
        manageMeetingRoom.setVisible(false);
        deviceUsingHistory.setVisible(false);
        manageDeviceGroup.setVisible(false);
        manageDeviceCategory.setVisible(false);
        myDevice.setVisible(false);
        myRequest.setVisible(false);

        // default enable request and device
        myRequest.setVisible(true);
        myDevice.setVisible(true);

        switch (staffType) {
            case BO_MANAGER:
            case BO_STAFF:
            case ADMIN:
                manageDevice.setVisible(true);
                manageRequest.setVisible(true);
                manageVendor.setVisible(true);
                manageMaker.setVisible(true);
                manageMeetingRoom.setVisible(true);
                deviceUsingHistory.setVisible(true);
                manageDeviceGroup.setVisible(true);
                manageDeviceCategory.setVisible(true);
                break;
            case DIVISION_MANAGER:
            case SECTION_MANAGER:
                deviceUsingHistory.setVisible(true);
                manageDevice.setVisible(true);
                manageRequest.setVisible(true);
                break;
            case GROUP_LEADER:
                deviceUsingHistory.setVisible(true);
                manageRequest.setVisible(true);
                break;
            case ACCOUNTANT:
                manageDevice.setVisible(true);
                break;
            default:
                break;
        }
    }

    @BindingAdapter({"statusDrawerLayout", "side"})
    public static void setStatusDrawerLayout(DrawerLayout drawerLayout, final String status,
                                             int side) {
        if (status != null) {
            if (status.equals(DRAWER_IS_CLOSE)) {
                drawerLayout.closeDrawer(side);
            }
            if (status.equals(DRAWER_IS_OPEN)) {
                drawerLayout.openDrawer(side);
            }
        }
    }

    @BindingAdapter("bind:adapter")
    public static void setAdapter(ExpandableListView expandableListView,
                                  BaseExpandableListAdapter adapter) {
        expandableListView.setAdapter(adapter);
    }

    @BindingAdapter({"drawerListener"})
    public static void setDrawerListener(DrawerLayout drawerLayout,
                                         DrawerLayout.DrawerListener listener) {
        drawerLayout.setDrawerListener(listener);
    }

    @BindingAdapter("activity")
    public static void setUpDrawerListener(final DrawerLayout drawlayout, final Activity activity) {
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(activity, drawlayout, R.string.msg_open_drawer,
                        R.string.msg_close_drawer);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @BindingAdapter("listener")
    public static void setToolbarListener(Toolbar toolbar, View.OnClickListener listener) {
        if (listener == null) {
            return;
        }
        toolbar.setNavigationOnClickListener(listener);
    }

    @BindingAdapter("lockMode")
    public static void setLockMode(DrawerLayout layout, int lockMode) {
        layout.setDrawerLockMode(lockMode);
    }

    @BindingAdapter("bind:pagerAdapter")
    public static void setAdapter(ViewPager viewPager, FragmentPagerAdapter pagerAdapter) {
        viewPager.setAdapter(pagerAdapter);
    }

    @BindingAdapter("bind:limitOffSet")
    public static void setViewPagerLimitOffset(ViewPager view, int limit) {
        view.setOffscreenPageLimit(limit);
    }

    @BindingAdapter("bind:viewPager")
    public static void setupWithViewPager(TabLayout tabLayout, ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);
    }

    @BindingAdapter("bind:hint")
    public static void setHint(TextView view, String text) {
        if (text == null) {
            return;
        }
        view.setText(null);
        view.setHint(text);
    }

    @BindingAdapter("activity")
    public static void setUpToolbar(Toolbar toolbar, AppCompatActivity activity) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @BindingAdapter("dateText")
    public static void setDateText(TextView view, Date date) {
        view.setText(Utils.getStringDate(date, view.getContext()));
    }

    @BindingAdapter({"borrowDateText", "returnDateText"})
    public static void setDatesText(TextView view, Date borrowDate, Date returnDate) {
        view.setText(
                (Utils.getStringDate(borrowDate, view.getContext())) + "->" + (Utils.getStringDate(
                        returnDate, view.getContext())));
    }

    @BindingAdapter("convertDateToString")
    public static void convertDateToString(TextView textView, Date date){
        textView.setText(Utils.dateToString(date));
    }
}
