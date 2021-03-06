/**
 * IntellectualServer is a web server, written entirely in the Java language.
 * Copyright (C) 2015 IntellectualSites
 * <p>
 * This program is free software; you can redistribute it andor modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.plotsquared.iserver.api.events;

import com.plotsquared.iserver.api.util.Assert;

/**
 * This is an event, in other words:
 * <br/>
 * Something that happens, that can be captured
 * using code
 *
 * @author Citymonstret
 */
public abstract class Event
{

    private final String name;
    private final int accessor;

    /**
     * The name which will be used
     * to identity this event
     *
     * @param name Event Name
     */
    protected Event(final String name)
    {
        Assert.notEmpty( name );

        this.name = name;
        this.accessor = getClass().getName().hashCode();
    }

    @Override
    public final String toString()
    {
        return this.getClass().getName();
    }

    /**
     * Get the specified event name
     *
     * @return Event name
     */
    private String getName()
    {
        return this.name;
    }

    @Override
    public final int hashCode()
    {
        return this.accessor;
    }

    @Override
    public final boolean equals(final Object o)
    {
        return o instanceof Event && ( (Event) o ).getName().equals( getName() );
    }
}
