package yourdev.noisealert.Class;

import android.content.Context;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;
import yourdev.noisealert.R;

public class CarouselItens {

    public CarouselPicker.CarouselViewAdapter createCarousel(Context context){


        int size = 14;


        List<CarouselPicker.PickerItem> textItems= new ArrayList<>();
        textItems.add(new CarouselPicker.TextItem("100",size)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_90)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_90)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_90)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_90)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_90)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.DrawableItem(R.mipmap.ic_90)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.TextItem("30",size)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.TextItem("20",size)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.TextItem("10",size)); // 20 is text size (sp)
        textItems.add(new CarouselPicker.TextItem("0",size)); // 20 is text size (sp)


        CarouselPicker.CarouselViewAdapter mixAdapter = new CarouselPicker.CarouselViewAdapter(context,textItems,0);


        return  mixAdapter;

    }
}
