package com.main.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component("biReader")
public class BookDetailsReader implements ItemReader<String>
{
       String[] books = new String[]{"AAJ","BBP","KL","UI","OP","ER","TY","QE"};
       int count = 0;
       public BookDetailsReader() 
       {
		    System.out.println("BookDetailsReader() :: 0-param constructor executed");
	   }
       
       @Override
       public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException
       {
    	    System.out.println("BookDetailsReader.read()"); 
    	    if(count<books.length)
    	    {
    	    	return books[count++];
    	    }
    	    else
    	   return null;
       }
}
