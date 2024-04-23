package com.main.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component("biProcessor")
public class BookItemProcessor implements ItemProcessor<String, String> 
{
   
	@Override
	public String process(String item) throws Exception {
		  
		System.out.println("BookItemProcessor.process()");
		String itemWithTitle=null;
		itemWithTitle = item.equalsIgnoreCase("AAJ")?item+" by AAJ":item.equalsIgnoreCase("BBP")?item+" by BBP":item.equalsIgnoreCase("KL")?item+" by KL":
			item.equalsIgnoreCase("UI")?item+" by UI":item.equalsIgnoreCase("OP")?item+" by OP":item.equalsIgnoreCase("ER")?item+" by ER":item.equalsIgnoreCase("TY")?item+" by TY"
					:item.equalsIgnoreCase("QE")?item+" by QE":null;
		return itemWithTitle;
	}

}
