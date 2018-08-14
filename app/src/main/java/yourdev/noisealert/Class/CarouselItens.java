package yourdev.noisealert.Class;

import android.content.Context;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;
import yourdev.noisealert.R;

public class CarouselItens {

    public  List<CarouselPicker.PickerItem> createCarousel(Context context, List<CarouselPicker.PickerItem> textItems){


        int size = 14;


        textItems.clear();

        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_100)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_90)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_80)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_70)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_60)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_50));
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_40));
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_30));
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_20));
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_10));


     //   CarouselPicker.CarouselViewAdapter mixAdapter = new CarouselPicker.CarouselViewAdapter(context,textItems,0);


        return  textItems;

    }

    public  List<CarouselPicker.PickerItem> createCarouselCinza(Context context, List<CarouselPicker.PickerItem> textItems){

        textItems.clear();

        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_100_cinza)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_90_cinza)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_80_cinza)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_70_cinza)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_60_cinza)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_50_cinza));
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_40_cinza));
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_30_cinza));
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_20_cinza));
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_10_cinza));


       // CarouselPicker.CarouselViewAdapter mixAdapter = new CarouselPicker.CarouselViewAdapter(context,textItems,0);


        return  textItems;

    }
}
