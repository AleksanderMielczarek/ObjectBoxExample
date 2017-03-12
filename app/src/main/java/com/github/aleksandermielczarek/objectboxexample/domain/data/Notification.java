package com.github.aleksandermielczarek.objectboxexample.domain.data;

import com.github.aleksandermielczarek.objectboxexample.domain.converter.LocalDateTimeConverter;

import org.threeten.bp.LocalDateTime;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Generated;

/**
 * Created by Aleksander Mielczarek on 10.03.2017.
 */
@Entity
public class Notification {

    @Id
    private long id;
    private String title;
    private String body;
    @Convert(converter = LocalDateTimeConverter.class, dbType = String.class)
    private LocalDateTime date;
    private boolean read;

    public Notification(String title, String body, LocalDateTime date, boolean read) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.read = read;
    }

    @Generated(hash = 2075985222)
    public Notification(long id, String title, String body, LocalDateTime date, boolean read) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.date = date;
        this.read = read;
    }

    @Generated(hash = 1855225820)
    public Notification() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean getRead() {
        return read;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (id != that.id) return false;
        return read == that.read;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (read ? 1 : 0);
        return result;
    }
}
