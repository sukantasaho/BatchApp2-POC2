package com.main.writer;

import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component("bdWriter")
public class BookDetailsWriter implements ItemWriter<String>
{
    

	@Override
	public void write(List<? extends String> chunk) throws Exception 
	{
		System.out.println("BookDetailsWriter.write()");
		 chunk.forEach(System.out::println);
	}

	 
}
