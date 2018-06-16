package com.config.util;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;


import javax.validation.groups.ConvertGroup;

import com.config.dao.ChannelDAO;


public class LocalDateTimeConverter {
	
   
    public static Timestamp convertToDatabaseColumn(LocalDateTime ldt) {
        return Timestamp.valueOf(ldt);
    }

   
    public static LocalDateTime convertToEntityAttribute(Timestamp ts) {
    	if(ts!=null){
    		   return ts.toLocalDateTime();
    	}
    	return null;
    }
}
