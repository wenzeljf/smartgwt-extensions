package com.smartgwt.extensions.client.gwtrpcds.example;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Aleksandras Novikovas
 * @author System Tier
 * @version 1.0
 */
public class SimpleGwtRPCDSRecord
    implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

    private String name;

    private Date date;

    /**
     * @return the id
     */
    public Integer getId () {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId (Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName () {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName (String name) {
        this.name = name;
    }

    /**
     * @return the date
     */
    public Date getDate () {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate (Date date) {
        this.date = date;
    }

}
