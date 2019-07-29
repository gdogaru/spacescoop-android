package com.gdogaru.spacescoop.events;

/**
 * Created by Gabriel Dogaru (gdogaru@gmail.com)
 */
public class PageChangedEvent {
    private final long id;
    private final int position;

    public PageChangedEvent(long id, int position) {
        this.id = id;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }
}
