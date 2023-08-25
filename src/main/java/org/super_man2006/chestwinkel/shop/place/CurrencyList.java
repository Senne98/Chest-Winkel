package org.super_man2006.chestwinkel.shop.place;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.geldapi.events.CurrencyCreateEvent;
import org.super_man2006.geldapi.events.CurrencyRemoveEvent;

public class CurrencyList implements Listener {

    @EventHandler
    public void onCreate(CurrencyCreateEvent e) {
        ChestWinkel.currencys.add(e.getKey());
    }

    @EventHandler
    public void onRemove(CurrencyRemoveEvent e) {
        ChestWinkel.currencys.remove(e.getKey());
    }
}
