package thingswithworth.org.transittimes.ui.menu;

import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

/**
 * Created by andrey on 8/28/15.
 */
public class AppDrawer extends Drawer {

    public AppDrawer(){
        addDrawerItems(new PrimaryDrawerItem().withName("Settings"));
        addDrawerItems(new DividerDrawerItem());
        addDrawerItems(new DividerDrawerItem());

        //TOADD

    }
}
