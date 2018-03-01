package bbt.com.iconiccardview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import bbt.com.iconiccardview.widgets.CircleImageView;

/**
 * Created by anish on 13-11-2017.
 */

public class IconicCardView extends LinearLayout {
    private int cardBgColor;
    private int cardElevation;
    private Drawable imgSrc;
    private int imgWidth;
    private int imgHeight;
    private Context context;
    private LinearLayout insideContainer;
    private CircleImageView imgHolder;
    private CardView cardView;

    public IconicCardView(Context context) {
        super(context);
        this.context = context;
    }

    public IconicCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconicCardView, 0, 0);
        try {
            imgWidth = (int) a.getDimension(R.styleable.IconicCardView_imgWidth, getResources().getDimension(R.dimen.img_dimens));
            imgHeight = (int) a.getDimension(R.styleable.IconicCardView_imgHeight, getResources().getDimension(R.dimen.img_dimens));
            cardElevation = (int) a.getDimension(R.styleable.IconicCardView_cardElevation, getResources().getDimension(R.dimen.card_elevation));
            imgSrc = a.getDrawable(R.styleable.IconicCardView_imgSrc);
            cardBgColor = a.getColor(R.styleable.IconicCardView_cardBgColor, 0);
        } finally {
            a.recycle();
        }

        initializeViews(context);
    }


    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.iconic_card, this);

        SetUpViews();
    }

    private void SetUpViews() {
        imgHolder = (CircleImageView) getRootView().findViewById(R.id.imgHolder);
        cardView = (CardView) getRootView().findViewById(R.id.cardHolder);
        insideContainer = (LinearLayout) getRootView().findViewById(R.id.insideContainer);


        imgHolder.getLayoutParams().width = imgWidth;
        imgHolder.getLayoutParams().height = imgHeight;
        imgHolder.setImageDrawable(imgSrc);


        cardView.setMinimumHeight(2 * imgHeight);//setting cardView minimum height


        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) cardView.getLayoutParams();//setting inside container
        marginLayoutParams.setMargins((imgWidth / 2) - cardElevation, 0, 0, 0);

        cardView.setCardElevation(cardElevation);
        ((FrameLayout.LayoutParams) insideContainer.getLayoutParams()).setMargins(imgWidth / 2 + cardElevation, 0, 0, 0);

        cardView.setCardBackgroundColor(cardBgColor);
//        insideContainer.setBackgroundColor(cardBgColor);

    }

    public void setIconicImage(String glidePath) {
        Glide.with(context).load(glidePath)
                .thumbnail(0.5f)
                .into(imgHolder);
    }
    /*public void setIconicImage(Drawable imgSrc) {
        Glide.with(context).load(imgSrc)
                .thumbnail(0.5f)
                .into(imgHolder);
    }*/

    public void addInsideView(View view) {
        insideContainer.removeAllViews();
        insideContainer.addView(view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //you can manipulate the view here also
    }

}
