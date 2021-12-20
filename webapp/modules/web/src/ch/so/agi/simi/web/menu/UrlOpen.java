package ch.so.agi.simi.web.menu;

import com.haulmont.cuba.gui.WebBrowserTools;
import com.haulmont.cuba.gui.config.MenuItem;
import com.haulmont.cuba.gui.config.MenuItemRunnable;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.web.AppUI;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class UrlOpen implements MenuItemRunnable {

    private WebBrowserTools tools;

    @Override
    public void run(FrameOwner origin, MenuItem menuItem) {
        tools = AppUI.getCurrent().getWebBrowserTools();

        Optional<String> desc = Optional.ofNullable(menuItem.getDescription());

        URL link = null;
        try{
            link = new URL(desc.orElse("ERROR - Menu item description is no URL"));
        }
        catch(MalformedURLException me){
            throw new RuntimeException(me);
        }

        tools.showWebPage(link.toString(), null);
    }
}
