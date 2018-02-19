package com.efpro.bengkelmotor_01.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.efpro.bengkelmotor_01.Model.Foto;
import com.efpro.bengkelmotor_01.R;

import java.util.List;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Foto> mfotoDetailBengkels;
    private Integer [] images = {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3,
            R.drawable.slide4,R.drawable.slide5,R.drawable.slide6};
    int flag;


    public ViewPagerAdapter(Context context, List<Foto> mfotoDetailBengkels) {
        this.context = context;
        this.mfotoDetailBengkels = mfotoDetailBengkels;
        flag = 0;
    }

    public ViewPagerAdapter(Context context) {
        this.context = context;
        flag = 1;
    }

    @Override
    public int getCount() {
        if(flag==0){
            return mfotoDetailBengkels.size();
        } else {
            return images.length;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(flag==0){
            View view = layoutInflater.inflate(R.layout.foto_bengkel, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imgFotoBengkel);

            Foto fotoDetailBengkel = mfotoDetailBengkels.get(position);
            byte[] bytes = fotoDetailBengkel.getFoto();
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bmp);
            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;
        } else{
            View view = layoutInflater.inflate(R.layout.foto_tips, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imgFotoTips);
            imageView.setImageResource(images[position]);
            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

}
